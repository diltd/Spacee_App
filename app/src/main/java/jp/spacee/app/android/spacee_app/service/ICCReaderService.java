package jp.spacee.app.android.spacee_app.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.Build;
import android.widget.Toast;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothAdapter;

import jp.spacee.app.android.spacee_app.IICCReaderService;
import jp.spacee.app.android.spacee_app.IICCReaderServiceCallback;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.util.PlayWaveFile;

import com.acs.bluetooth.BluetoothReader;
import com.acs.bluetooth.BluetoothReader.*;
import com.acs.bluetooth.BluetoothReaderManager;
import com.acs.bluetooth.Acr1255uj1Reader;
import com.acs.bluetooth.BluetoothReaderGattCallback;

import jp.spacee.app.android.spacee_app.R;


public  class  ICCReaderService  extends  Service
{
	private  static			ICCReaderService		mICCReaderService			= null;

	private  static			BluetoothDevice			mBLEDev						= null;
	private  static			BluetoothGatt			mBTGatt						= null;

	private						int						openStatus					= 0;


	private						boolean					ICCR_Caller				= false;

	public	  static  final	String					EXTRAS_DEVICE_NAME			= "DEVICE_NAME";
	public	  static  final	String					EXTRAS_DEVICE_ADDRESS			= "DEVICE_ADDRESS";

	private	  static  final	String					DEFAULT_1255_MASTER_KEY		= "ACR1255U-J1 Auth";
	private	  static  final	String					DEFAULT_1255_APDU_COMMAND	= "FF CA 00 00 00";
	private	  static  final	String					DEFAULT_1255_ESCAPE_COMMAND	= "E0 00 00 18 00";

	private	  static  final	byte[]					CMD_AUTO_POLLING_START		= { (byte) 0xE0, 0x00, 0x00, 0x40, 0x01 };
	private	  static  final	byte[]					CMD_AUTO_POLLING_STOP			= { (byte) 0xE0, 0x00, 0x00, 0x40, 0x00 };
	private	  static  final	byte[]					CMD_APDU						= { (byte) 0xFF, (byte)0xCA, 0x00, 0x00, 0x00};
	private	  static  final	byte[]					CMD_ESCAPE						= { (byte) 0xE0, 0x00, 0x00, 0x18, 0x00};
	private	  static  final	byte[]					CMD_AUTHENTICATE				= {  0x41, 0x43, 0x52, 0x31, 0x32, 0x35,
																							 0x35, 0x55, 0x2D, 0x4A, 0x31, 0x20,
																							 0x41, 0x75, 0x74, 0x68};



	/* Reader to be connected. */
	private						String					mDeviceName					= null;
	private						String					mDeviceAddress					= null;
	private						int						mConnectState					= BluetoothReader.STATE_DISCONNECTED;

	private  static  final	int						RC_OK							=  0;
//	private  static  final	int						RC_INITIALIZED					=  1;
//	private  static  final	int						RC_ACK_OK						=  0;
//	private  static  final	int						RC_COMMAND_QUEUED				=  0;
//	private  static  final	int						RC_FALSE_SP_NOT_CERTIFIED	= -1;


	public  static   final	RemoteCallbackList<IICCReaderServiceCallback> mCallbackList = new RemoteCallbackList<IICCReaderServiceCallback>();

	private						BluetoothReader					mBTReader			= null;
	private						BluetoothReaderManager			mBTReaderManager	= null;
	private						BluetoothReaderGattCallback		mGattCallback		= null;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  ICCReaderService()
	{
		mICCReaderService = this;
	}


	@Override
	public  void  onCreate()
	{
		super.onCreate();
	}


	@Override
	public IBinder onBind(Intent intent)
	{
		IBinder ret = null;
		if (ICCR_Caller == false)
		{
			ret = mIICCReaderService;
			ICCR_Caller = true;                                //	only one application can use
		}
		return  ret;
	}


	@Override
	public boolean onUnbind(Intent intent)
	{
		super.onUnbind(intent);
		ICCR_Caller = false;
		return true;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}


	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mICCReaderService = null;
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private final IICCReaderService.Stub mIICCReaderService = new IICCReaderService.Stub()
	{
		@Override
		public void registerCallback(IICCReaderServiceCallback callback) throws RemoteException
		{
			// Callbackリストに登録.
			if (callback != null) {
				mCallbackList.register(callback);
			}
			int nn = mCallbackList.beginBroadcast();
			mCallbackList.finishBroadcast();
		}

		@Override
		public void unregisterCallback(IICCReaderServiceCallback callback) throws RemoteException
		{
			// Callbackリストから登録解除.
			if (callback != null) {
				mCallbackList.unregister(callback);
			}
		}

		@Override
		public boolean ICCInitializeReader(android.bluetooth.BluetoothDevice dev) throws RemoteException
		{
			return InitializeReader(dev);
		}

		@Override
		public int ICCOpenReader() throws RemoteException
		{
			return OpenReader();
		}

		@Override
		public int ICCReadIDm() throws RemoteException
		{
			return ReadIDm();
		}

		@Override
		public int ICCCloseReader() throws RemoteException
		{
			return CloseReader();
		}

		@Override
		public int ICCCancelReader() throws RemoteException
		{
			return CancelReader();
		}
	};


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private boolean InitializeReader(BluetoothDevice dev)
	{
		mBLEDev = dev;

		mDeviceName	= mBLEDev.getName();
		mDeviceAddress	= mBLEDev.getAddress();


		mGattCallback		= new BluetoothReaderGattCallback();			//	Initialize BluetoothReaderGattCallback
		mBTReaderManager	= new BluetoothReaderManager();					//	Initialize mBluetoothReaderManager

		setGattCallbackListener();
		setBTReaderManagerListener();

		connectReader();													//	Connect the reader

		return  true;
	}


	private int OpenReader()
	{
		if (mBTReader != null)
		{
			openStatus = 0;						//	reset
			mBTReader.transmitApdu(CMD_APDU);
		}

		return RC_OK;
	}


	private int ReadIDm()
	{
		if (mBTReader != null)
		{
			openStatus = 1;						//	reset
			mBTReader.transmitEscapeCommand(CMD_AUTO_POLLING_START);
		}

		return RC_OK;
	}


	private int CloseReader()
	{

		return RC_OK;
	}


	private int CancelReader()
	{

		return RC_OK;
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  sendInitializeCompleted(int compCode)
	{
		int nn = mCallbackList.beginBroadcast();			//	直接 forループに書くと、エラーとなるので要注意
		for (int k=0; k<nn; k++)
		{
			try
			{
				mCallbackList.getBroadcastItem(k).ICCR_InitComp(compCode);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
		mCallbackList.finishBroadcast();
	}


	private  void  sendOpenCompleted(int compCode)
	{
		int nn = mCallbackList.beginBroadcast();			//	直接 forループに書くと、エラーとなるので要注意
		for (int k=0; k<nn; k++)
		{
			try
			{
				mCallbackList.getBroadcastItem(k).ICCR_OpenComp(compCode);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
		mCallbackList.finishBroadcast();
	}


	private  void  sendCardRead(String IDm)
	{
		int nn = mCallbackList.beginBroadcast();			//	直接 forループに書くと、エラーとなるので要注意
		for (int k=0; k<nn; k++)
		{
			try
			{
				mCallbackList.getBroadcastItem(k).ICCR_ReadData(IDm);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
		mCallbackList.finishBroadcast();
	}


	private  void  sendAttention(int attCode, String attnMsg)
	{
		int nn = mCallbackList.beginBroadcast();			//	直接 forループに書くと、エラーとなるので要注意
		for (int k=0; k<nn; k++)
		{
			try
			{
				mCallbackList.getBroadcastItem(k).ICCR_Attention(attCode, attnMsg);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}
		mCallbackList.finishBroadcast();
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  sendAuthentication()
    {
		if (mBTReader == null)
		{
			return;
		}

		// Retrieve master key from edit box. //	"ACR1255U-J1 Auth"
		mBTReader.authenticate(CMD_AUTHENTICATE);
	}


	private  void  startAutoDetect()
	{
		mBTReader.transmitEscapeCommand(CMD_APDU);
	}


	private  void  stopAutoDetect()
	{
//		status_AutoDetect = false;
/*
		mAuthentication.setEnabled(true);

		mStartAutoDetect.setEnabled(true);
		mStopAutoDetect.setEnabled(false);
*/
	}


	private  void  activateReader(BluetoothReader reader)
	{
		if (reader == null)
		{
			return;
		}

		if (mBTReader instanceof Acr1255uj1Reader) 			//	Enable notification.
		{
			mBTReader.enableNotification(true);
		}
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void setBTReaderListener(BluetoothReader reader)
	{
		mBTReader.setOnCardStatusChangeListener(new OnCardStatusChangeListener()
		{
			@Override
			public void onCardStatusChange(BluetoothReader bluetoothReader, final int sta)
			{
				if		(sta == 2)				//	Cardが検出された
				{
					mBTReader.powerOnCard();
					mBTReader.transmitEscapeCommand(CMD_APDU);
				}
				else if (sta == 255)
				{
					//	カードが一定時間かざされなかった
					sendAttention(-1, "カード未検出");
				}
				else
				{
				}
			}
		});


        /* Wait for authentication completed. */
		mBTReader.setOnAuthenticationCompleteListener(new OnAuthenticationCompleteListener()
		{
			@Override
			public void onAuthenticationComplete(BluetoothReader bluetoothReader, final int errorCode)
			{
				if		(errorCode == BluetoothReader.ERROR_SUCCESS)
				{
					//	初期設定完了通知
					sendInitializeCompleted(0);
				}
				else if (errorCode == BluetoothReader.ERROR_AUTHENTICATION_FAILED)
				{
					//	Authenticateを送信
					mBTReader.authenticate(CMD_AUTHENTICATE);
				}
				else if (errorCode == BluetoothReader.ERROR_AUTHENTICATION_REQUIRED)
				{
				}
			}
		});

        /* Wait for receiving ATR string. */
		mBTReader.setOnAtrAvailableListener(new OnAtrAvailableListener()
		{
			@Override
			public void onAtrAvailable(BluetoothReader bluetoothReader, final byte[] atr, final int errorCode)
			{
//				if ((atr != null) && (status_AutoDetect == true))
				if (atr != null)
				{
					if ((atr[13] == 0x00) && (atr[14] == 0x3B))         //  felica Card
					{
						mBTReader.transmitApdu(CMD_APDU);
					}
				}
			}

		});

        /* Wait for power off response. */
		mBTReader.setOnCardPowerOffCompleteListener(new OnCardPowerOffCompleteListener()
		{
			@Override
			public void onCardPowerOffComplete(BluetoothReader bluetoothReader, final int result)
			{
				return;
			}

		});

        /* Wait for response APDU. */
		mBTReader.setOnResponseApduAvailableListener(new OnResponseApduAvailableListener()
		{
			@Override
			public void onResponseApduAvailable(BluetoothReader bluetoothReader, final byte[] apdu, final int errorCode)
			{
				if (errorCode == BluetoothReader.ERROR_SUCCESS)
				{
					if ((apdu != null) && (apdu.length > 0))
					{
						if		(openStatus == 0)
						{
							sendOpenCompleted(0);
							openStatus = 1;				//	opened
						}
						else if (openStatus == 1)
						{
							if (apdu.length == 10)
							{
								String  wIDm = "";
								String  wStr = "";
								for (int k=0; k<8; k++)
								{
									wStr = "0" + Integer.toHexString(apdu[k] & 0xFF);
									wIDm += wStr.substring(wStr.length()-2, wStr.length());
								}
								sendCardRead(wIDm.toUpperCase());
								openStatus = 2;				//	send card data

								mBTReader.powerOffCard();
								mBTReader.transmitEscapeCommand(CMD_AUTO_POLLING_STOP);
							}
						}
					}
				}
			}

		});

        /* Wait for escape command response. */
		mBTReader.setOnEscapeResponseAvailableListener(new OnEscapeResponseAvailableListener()
		{
			@Override
			public void onEscapeResponseAvailable(BluetoothReader bluetoothReader, final byte[] response, final int errorCode)
			{
				if (openStatus == 0)
				{
					//	Authenticateを送信
					mBTReader.authenticate(CMD_AUTHENTICATE);
				}
				else
				{
					return;
				}
			}
		});

        /* Wait for device info available. */
		mBTReader.setOnDeviceInfoAvailableListener(new OnDeviceInfoAvailableListener()
		{
			@Override
			public void onDeviceInfoAvailable(BluetoothReader bluetoothReader, final int infoId, final Object o, final int status)
			{
				if (status != BluetoothGatt.GATT_SUCCESS)
				{
					Toast.makeText(ReceiptTabApplication.AppContext, "Failed to read device info!", Toast.LENGTH_SHORT).show();
					return;
				}
			}

		});


        /* Handle on slot status available. */
		mBTReader.setOnCardStatusAvailableListener(new OnCardStatusAvailableListener()
		{
			@Override
			public void onCardStatusAvailable(BluetoothReader bluetoothReader, final int cardStatus, final int errorCode)
			{
				return;
			}
		});

		mBTReader.setOnEnableNotificationCompleteListener(new OnEnableNotificationCompleteListener()
		{
			@Override
			public void onEnableNotificationComplete(BluetoothReader bluetoothReader, final int result)
			{
				if (result == BluetoothGatt.GATT_SUCCESS) 				//	Failed
				{
					//	Escapeを送信
					mBTReader.transmitEscapeCommand(CMD_ESCAPE);
				}
				else
				{

				}
			}
		});
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void setGattCallbackListener()
	{
		mGattCallback.setOnConnectionStateChangeListener(new BluetoothReaderGattCallback.OnConnectionStateChangeListener()
		{
			@Override
			public void onConnectionStateChange(final BluetoothGatt gatt, final int state, final int newState)
			{
				if (state != BluetoothGatt.GATT_SUCCESS)			//	Show the message on fail to connect/disconnect.
				{
					mConnectState = BluetoothReader.STATE_DISCONNECTED;
//					clearAllUi();
//					updateUi(null);
//					invalidateOptionsMenu();
					return;
				}

				mConnectState = newState;

				if (newState == android.bluetooth.BluetoothProfile.STATE_CONNECTED)		//	Detect the connected reader.
				{
					if (mBTReaderManager != null)
					{
						mBTReaderManager.detectReader(gatt, mGattCallback);
					}
				}
				else if (newState == android.bluetooth.BluetoothProfile.STATE_DISCONNECTED)
				{
					mBTReader = null;				//	Release resources occupied by Bluetooth GATT client.
					if (mBTGatt != null)
					{
						mBTGatt.close();
						mBTGatt = null;
					}
				}
			}
		});
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void setBTReaderManagerListener()
	{
		mBTReaderManager.setOnReaderDetectionListener(new BluetoothReaderManager.OnReaderDetectionListener()
		{
			@Override
			public void onReaderDetection(BluetoothReader reader)
			{
				if (reader instanceof Acr1255uj1Reader)			//	The connected reader is ACR1255U-J1 reader.
				{
					mBTReader = reader;
					setBTReaderListener(reader);
					activateReader(reader);
				}
				else
				{
///					disconnectReader();
///					mConnectState = BluetoothReader.STATE_DISCONNECTED;
				}
			}
		});
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//	Create a GATT connection with the reader. And detect the connected reader once service list is available.
	private boolean connectReader()
	{
		BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		if (bluetoothManager == null)
		{
			mConnectState = BluetoothReader.STATE_DISCONNECTED;
			return false;
		}

		BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
		if (bluetoothAdapter == null)
		{
			mConnectState = BluetoothReader.STATE_DISCONNECTED;
			return false;
		}

        //	Connect Device  Clear old GATT connection.
		if (mBTGatt != null)
		{
			mBTGatt.disconnect();
//			mBTGatt.close();
			mBTGatt = null;
		}

        //	Create a new connection.
		if (bluetoothAdapter != null)
		{
			final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mDeviceAddress);

			if (device == null)
			{
				return false;
			}

			//	Connect to GATT server.
			mConnectState = BluetoothReader.STATE_CONNECTING;
			mBTGatt = device.connectGatt(ReceiptTabApplication.AppContext, false, mGattCallback);
			return true;
		}
		else
		{
			return false;
		}
	}


	//	Disconnects an established connection.
	private void disconnectReader()
	{
		if (mBTGatt == null)
		{
			mConnectState = BluetoothReader.STATE_DISCONNECTED;
			return;
		}

		mConnectState = BluetoothReader.STATE_DISCONNECTING;
		mBTGatt.disconnect();
	}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  final  BroadcastReceiver  mBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			BluetoothAdapter	bluetoothAdapter = null;
			BluetoothManager	bluetoothManager = null;
			final String action = intent.getAction();

			if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
                /* Get bond (pairing) state */
				if (mBTReaderManager == null)
				{
					return;
				}

				bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
				if (bluetoothManager == null)
				{
					return;
				}

				bluetoothAdapter = bluetoothManager.getAdapter();
				if (bluetoothAdapter == null)
				{
					return;
				}

				final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(mDeviceAddress);

				if (device == null)
				{
					return;
				}

				final int bondState = device.getBondState();
			}
		}
	};
}