package jp.spacee.app.android.spacee_app.activity;


import android.app.Activity;
import android.app.PendingIntent;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.os.Build;
import android.os.Message;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Calendar;

import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.BluetoothLeScanner;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v4.app.ActivityCompat;

import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FindIccBleDevice
{
	private  static			BluetoothManager			mBTManager				= null;
	private  static			BluetoothAdapter			mBTAdapter				= null;
	private  static			BluetoothLeScanner			mBTLeScanner			= null;
	private  static			BluetoothDevice				mBtDevice				= null;

	private  static  final	String						TIMERTICK				= "TimerTick";
	private						boolean						fRcvr					= false;

	private 					AlarmManager				alarmManager			= null;
	private 					PendingIntent				sender					= null;


	@android.annotation.TargetApi(android.os.Build.VERSION_CODES.LOLLIPOP)
	public  void  prepareService()
	{
		alarmManager = (AlarmManager) ReceiptTabApplication.AppContext.getSystemService(Context.ALARM_SERVICE);

		mBTManager = (BluetoothManager) ReceiptTabApplication.AppContext.getSystemService(Context.BLUETOOTH_SERVICE);
		mBTAdapter = mBTManager.getAdapter();
		if (mBTAdapter != null)
		{
			if (mBTAdapter.isEnabled() == false)
			{
				Intent intnt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				((Activity) ReceiptTabApplication.AppContext).startActivityForResult(intnt, SpaceeAppMain.REQ_ENABLE_BT);
			}
			else
			{
				mBTLeScanner = mBTAdapter.getBluetoothLeScanner();
				if (mBTLeScanner != null)
				{
					mBTLeScanner.startScan(scanCallback);
					sendTTick(20000);			//	20秒間待っても見つからない場合は「ＡＣＲ電源確認」ダイアログを表示する
				}
				else
				{
					//	OK
					//	BLE搭載かどうかは確認済み・・ここ22には来ないはず
				}
			}
		}
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private android.bluetooth.le.ScanCallback scanCallback = new android.bluetooth.le.ScanCallback()
	{
		@Override
		public void onScanResult(int callbackType, android.bluetooth.le.ScanResult result)
		{
			super.onScanResult(callbackType, result);

			BleScanCallback_onScanResult(callbackType, result);
		}
	};



	private  void  BleScanCallback_onScanResult(int callbackType, final android.bluetooth.le.ScanResult result)
	{
		android.bluetooth.BluetoothDevice mBtDev;
		String				mBtName;

		if ((result != null) && (result.getDevice() != null))
		{
			mBtDev  = result.getDevice();
			mBtName = mBtDev.getName();
			if (  (mBtName != null)
					&& (ReceiptTabApplication.targetBLEDeviceName.compareTo(mBtName.substring(0, ReceiptTabApplication.targetBLEDeviceName.length())) == 0))
			{
				if (mBtDevice == null)
				{
					mBtDevice = mBtDev;
					mBTLeScanner.stopScan(scanCallback);

					if (mBtDevice != null)
					{
						ReceiptTabApplication.AppContext.unregisterReceiver(TTickReceiver);

//						startICRService();
						Message msg = new Message();
						msg.what = SpaceeAppMain.MSG_START_ICR_SERVICE;
						msg.arg1 = 0;
						msg.obj  = mBtDevice;
						SpaceeAppMain.mMsgHandler.sendMessage(msg);
					}
				}
			}
			else
			{
/*
		ここでリセットしても同じデバイスしか捉えられないので、無視してタイムアウトを待つ
				mBtDevice = null;
				if (mBTLeScanner != null)
				{
					if (mBTLeScanner != null)
					{
						mBTLeScanner.stopScan(scanCallback);
						SystemClock.sleep(200);
					}

//					prepareService();
				}
*/
			}
		}
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  sendTTick(int wait)
	{
		java.util.Calendar cal;

		if (fRcvr == false)
		{
			IntentFilter filter = new IntentFilter();
			filter.addAction(TIMERTICK);
			ReceiptTabApplication.AppContext.registerReceiver(TTickReceiver, filter);
			fRcvr = true;
		}
		else
		{
			alarmManager.cancel(sender);
		}

		sender	= PendingIntent.getBroadcast(ReceiptTabApplication.AppContext, 0, new Intent(TIMERTICK), 0);
		cal		= Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(java.util.Calendar.MILLISECOND, wait);
		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
	}


	private  final BroadcastReceiver TTickReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			if (TIMERTICK.equals(intent.getAction()) == true)			//	30sec expired
			{
				ReceiptTabApplication.AppContext.unregisterReceiver(TTickReceiver);
				fRcvr = false;

				mBtDevice = null;
				if (mBTLeScanner != null)
				{
					if (mBTLeScanner != null)
					{
						mBTLeScanner.stopScan(scanCallback);
					}
				}

//				showDialogMessage("タイトル", "メッセージ", 1);
				Builder builder = new Builder(ReceiptTabApplication.AppContext);
				builder.setTitle(ReceiptTabApplication.AppContext.getString(R.string.error_title_icc));
				builder.setMessage(ReceiptTabApplication.AppContext.getString(R.string.error_msg_icc));
				builder.setPositiveButton(ReceiptTabApplication.AppContext.getString(R.string.relay_ok1), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						Message msg = new Message();
						msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
						SpaceeAppMain.mMsgHandler.sendMessage(msg);

						prepareService();
					}
				});
/*
				builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
					}
				});
*/
				builder.create().show();
			}
		}
	};


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  void  onActivityResult(int reqCode, int rsltCode, Intent data)
	{
		if (reqCode == SpaceeAppMain.REQ_ENABLE_BT)
		{
			if (rsltCode == Activity.RESULT_OK)
			{
				mBTLeScanner = mBTAdapter.getBluetoothLeScanner();
				mBTLeScanner.startScan(scanCallback);
			}
		}
	}
}