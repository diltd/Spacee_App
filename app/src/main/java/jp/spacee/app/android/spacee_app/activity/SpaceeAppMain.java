package jp.spacee.app.android.spacee_app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.IBinder;
import android.os.Build;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.widget.Toast;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.AudioFormat;
import android.media.AudioAttributes;
import android.graphics.Bitmap;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.annotation.NonNull;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import android.bluetooth.BluetoothDevice;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.fragment.*;
import jp.spacee.app.android.spacee_app.listener.*;
import jp.spacee.app.android.spacee_app.IICCReaderService;
import jp.spacee.app.android.spacee_app.IICCReaderServiceCallback;
import jp.spacee.app.android.spacee_app.comm.AsyncTaskDownloadImages;
import jp.spacee.app.android.spacee_app.comm.HttpCommGlueRoutines;
//import jp.spacee.app.android.spacee_app.util.MessageDialog;
import jp.spacee.app.android.spacee_app.util.PlayWaveFile;


public  class  SpaceeAppMain  extends  CustomBaseWindow
{
	private 					Object						mListener				= null;

	public   static 			Handler						mMsgHandler				= null;

	public   static			ActionBar					actionBar				= null;

	public   static			boolean						backReqPending			= false;
	public   static			float						scale					= 0;

	public   static			IICCReaderService			mIICCReaderService		= null;

	private						FragmentManager				fragmentManager		= null;
	private 					FindIccBleDevice			findIccBleDevice		= null;
	public   static			HttpCommGlueRoutines		httpCommGlueRoutines	= null;

	private  static			BluetoothDevice				mBtDevice				= null;

	public   static			Bitmap						FloorMap				= null;

	public   static			List<HashMap<String, String>>	 workAreaInfo		= null;
	public   static			List<HashMap<String, String>>	 meetingRoomInfo	= null;


	private						AudioAttributes				audioAttributes		= null;

	public  static  final		int							REQ_ENABLE_BT 			=  1;


	public  static  final		int							FRAGMENT_APP_MAIN			=  1;
	public  static  final		int							FRAGMENT_WORK_LIST		=  2;
	public  static  final		int							FRAGMENT_WORK_DETAIL		=  3;
	public  static  final		int							FRAGMENT_MEETING_LIST		=  4;
	public  static  final		int							FRAGMENT_MEETING_DETAIL	=  5;
	public  static  final		int							FRAGMENT_CHECK_BOOKING	=  6;
	public  static  final		int							FRAGMENT_LOGIN_IC			=  7;
	public  static  final		int							FRAGMENT_LOGIN_QR			=  8;
	public  static  final		int							FRAGMENT_LOGIN_PW			=  9;
	public  static  final		int							FRAGMENT_ENTRY_APP		= 10;
	public  static  final		int							FRAGMENT_ENTRY_CARD		= 11;
	public  static  final		int							FRAGMENT_ENTRY_INPUT		= 12;
	public  static  final		int							FRAGMENT_ENTRY_INVOICE	= 13;
	public  static  final		int							FRAGMENT_ENTRY_POLICY		= 14;
	public  static  final		int							FRAGMENT_BOOK_LIST		= 15;
	public  static  final		int							FRAGMENT_BOOK_DETAIL		= 16;
	public  static  final		int							FRAGMENT_ORDER_COMP		= 17;
	public  static  final		int							FRAGMENT_ORDER_CONFIRM	= 18;
	public  static  final		int							FRAGMENT_RULE_GUIDE		= 19;
	public  static  final		int							FRAGMENT_START_LINK		= 20;
	public  static  final		int							FRAGMENT_ACC_LINK_START	= 21;
	public  static  final		int							FRAGMENT_ACC_LINK_COMP	= 22;
	public  static  final		int							FRAGMENT_STATUS_BOOKING	= 23;
	public  static  final		int							FRAGMENT_PROVIDER_LOGIN	= 24;
	public  static  final		int							FRAGMENT_OFFICE_LIST		= 25;
	public  static  final		int							FRAGMENT_TABLET_PORTRAIT	= 26;

	public  static  final		int							MSG_APP_MAIN_COMP			=  1;
	public  static  final		int							MSG_WORK_LIST_COMP		=  2;
	public  static  final		int							MSG_WORK_DETAIL_COMP		=  3;
	public  static  final		int							MSG_MEETING_LIST_COMP		=  4;
	public  static  final		int							MSG_MEETING_DETAIL_COMP	=  5;
	public  static  final		int							MSG_CHECK_BOOKING_COMP	=  6;
	public  static  final		int							MSG_LOGIN_IC_COMP			=  7;
	public  static  final		int							MSG_LOGIN_QR_COMP			=  8;
	public  static  final		int							MSG_LOGIN_PW_COMP			=  9;
	public  static  final		int							MSG_ENTRY_APP_COMP		= 10;
	public  static  final		int							MSG_ENTRY_CARD_COMP		= 11;
	public  static  final		int							MSG_ENTRY_INPUT_COMP		= 12;
	public  static  final		int							MSG_ENTRY_INVOICE_COMP	= 13;
	public  static  final		int							MSG_ENTRY_POLICY_COMP		= 14;
	public  static  final		int							MSG_BOOK_LIST_COMP		= 15;
	public  static  final		int							MSG_BOOK_DETAIL_COMP		= 16;
	public  static  final		int							MSG_ORDER_COMP_COMP		= 17;
	public  static  final		int							MSG_ORDER_CONFIRM_COMP	= 18;
	public  static  final		int							MSG_RULE_GUIDE_COMP		= 19;
	public  static  final		int							MSG_START_LINK				= 20;
	public  static  final		int							MSG_ACC_LINK_START		= 21;
	public  static  final		int							MSG_ACC_LINK_COMP			= 22;
	public  static  final		int							MSG_STATUS_BOOKING_COMP	= 23;
	public  static  final		int							MSG_PROVIDER_LOGIN_COMP	= 24;
	public  static  final		int							MSG_OFFICE_LIST_COMP		= 25;
	public  static  final		int							MSG_TABLET_PORTRAIT_COMP	= 26;

	public  static  final		int							MSG_BACK_CLICKED			= 31;
	public  static  final		int							MSG_HOME_CLICKED			= 32;
	public  static  final		int							MSG_PLACE_CLICKED			= 33;
	public  static  final		int							MSG_START_PLACE			= 34;
	public  static  final		int							MSG_HIDE_ACTIONBAR		= 35;
	public  static  final		int							MSG_START_PROVIDER_LOGIN	= 36;

	public  static  final		int							MSG_START_ICR_SERVICE		= 41;


	public  static  final		String						TAG_APP_MAIN				= "APP_MAIN";
	public  static  final		String						TAG_WORK_LIST				= "WORK_LIST";
	public  static  final		String						TAG_WORK_DETAIL			= "WORK_DETAIL";
	public  static  final		String						TAG_MEETING_LIST			= "MEETING_LIST";
	public  static  final		String						TAG_MEETING_DETAIL		= "MEETING_DETAIL";
	public  static  final		String						TAG_CHECK_BOOKING			= "CHECK_BOOKING";
	public  static  final		String						TAG_LOGIN_IC				= "LOGIN_IC";
	public  static  final		String						TAG_LOGIN_QR				= "LOGIN_QR";;
	public  static  final		String						TAG_LOGIN_PW				= "LOGIN_PW";
	public  static  final		String						TAG_ENTRY_APP				= "ENTRY_APP";
	public  static  final		String						TAG_ENTRY_CARD				= "ENTRY_CARD";
	public  static  final		String						TAG_ENTRY_INPUT			= "ENTRY_INPUT";
	public  static  final		String						TAG_ENTRY_INVOICE			= "ENTRY_INVOICE";
	public  static  final		String						TAG_ENTRY_POLICY			= "ENTRY_POLICY";
	public  static  final		String						TAG_BOOK_LIST				= "BOOK_LIST";
	public  static  final		String						TAG_BOOK_DETAIL			= "BOOK_DETAIL";
	public  static  final		String						TAG_ORDER_COMP				= "ORDER_COMP";
	public  static  final		String						TAG_ORDER_CONFIRM			= "ORDER_CONFIRM";
	public  static  final		String						TAG_RULE_GUIDE				= "RULE_GUIDE";
	public  static  final		String						TAG_MESSAGE_DIALOG		= "MSG_DIALOG";
	public  static  final		String						TAG_START_LINK				= "START_LINK";
	public  static  final		String						TAG_ACC_LINK_START		= "ACC_LINK_ST";
	public  static  final		String						TAG_ACC_LINK_COMP			= "ACC_LINK_COMP";
	public  static  final		String						TAG_STATUS_BOOKING		= "STATUS_BOOKING";
	public  static  final		String						TAG_PROVIDER_LOGIN		= "PROVIDER_LOGIN";
	public  static  final		String						TAG_OFFICE_LIST			= "OFFICE_LIST";
	public  static  final		String						TAG_TABLET_PORTRAIT		= "TABLET_PORTRAIT";

	private static final		int							REQUEST_CAMERA_PERMISSION		= 1;
	private static final		int							REQUEST_BLUETOOTH_PERMISSION		= 2;
	private static final		int							REQUEST_LOCATION_PERMISSION		= 3;


	@Override
	public  void  onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_app_main);

		ReceiptTabApplication.AppContext = this;
/*
		//  画面サイズを確認し、必要に応じてレイアウト変更する
		Display		display = getWindowManager().getDefaultDisplay();
		Point		real    = new Point(0, 0);
		int	 scrHght = getResources().getDisplayMetrics().heightPixels;
		scale = getResources().getDisplayMetrics().density;

		display.getRealSize(real);

		if (scrHght == real.y)
		{


		}
*/
		scale = getResources().getDisplayMetrics().density;

		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
		{
			Toast.makeText(this, getString(R.string.appmain_ble_not_supported1), Toast.LENGTH_SHORT).show();
			terminateApp();
			return;
		}

		//	BLEが使えるAndroidバージョンかどうかをチェックし、4.3以下なら中止する
		if		(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
		{
			Toast.makeText(this, getString(R.string.appmain_ble_not_supported2), Toast.LENGTH_SHORT).show();
			terminateApp();
			return;
		}
		else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		{
			Toast.makeText(this, getString(R.string.appmain_lower_than_os5_0), Toast.LENGTH_SHORT).show();
			terminateApp();
			return;
		}


		ReceiptTabApplication.CallStack = new int[30];
		for (int i=0; i<30; i++)
		  {
			ReceiptTabApplication.CallStack[i] = -1;
		  }
		ReceiptTabApplication.stackPos = 0;


		workAreaInfo	= new ArrayList<HashMap<String, String>>();
		meetingRoomInfo	= new ArrayList<HashMap<String, String>>();


		LooperThread  looperThread = new LooperThread();
		looperThread.start();


		FragmentTop		fragmentTop		= new FragmentTop();
		if (savedInstanceState == null) {
			fragmentManager = getSupportFragmentManager();
			FragmentTransaction	transaction	= fragmentManager.beginTransaction();

			transaction.add(R.id.content_container, fragmentTop, TAG_APP_MAIN);
			transaction.commit();
		}

		fullScreenControl(findViewById(R.id.topPage));

		actionBar = getSupportActionBar();


		// Add permission for camera and let user grant the permission
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
			return;
		}

		if (Build.VERSION.SDK_INT >= 23)					//	>= Marshmallow(6.0)
		{
			if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
				requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
				return;
			}
		}

		// Add permission for BT and let user grant the permission
		if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
		{
			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH}, REQUEST_BLUETOOTH_PERMISSION);
			return;
		}

		//	Glueルーチンをロードしておく
		httpCommGlueRoutines = new HttpCommGlueRoutines();


		loadSharedPreferences();
		if (ReceiptTabApplication.providerId.equals("") == false)
		{
			//	掲載者(provider_user)認証
			String	result = httpCommGlueRoutines.providerUserSignin(ReceiptTabApplication.providerId, ReceiptTabApplication.providerPw);
			if (result != null)
			{
				try
				{
					org.json.JSONObject json = new org.json.JSONObject(result);
					if (json != null)
					{
						String	rc = json.getString("status");
						if (rc.equals("ok"))
						{
							ReceiptTabApplication.providerAuthToken = json.getString("auth_token");
						}
						else
						{
//							showErrorMsg("エラー", json);
							Toast.makeText(this, getString(R.string.error_msg_common1), Toast.LENGTH_LONG).show();
							return;
						}
					}
					else
					{
//						showErrorMsg("エラー", null);
						Toast.makeText(this, getString(R.string.error_msg_common1), Toast.LENGTH_LONG).show();
						return;
					}
				}
				catch (org.json.JSONException e)
				{
					e.printStackTrace();
					return;
				}
			}
			else
			{
//				showErrorMsg(getString(R.string.error_title1), null);
				Toast.makeText(this, getString(R.string.error_msg_common1), Toast.LENGTH_LONG).show();
				return;
			}

			result = httpCommGlueRoutines.retrieveProviderOffices();
			if (result != null)
			{
				try
				{
					org.json.JSONObject json = new org.json.JSONObject(result);
					if (json != null)
					{
//						String	rc = json.getString("status");
//						if (rc.equals("ok"))
//						{
							ReceiptTabApplication.Offices = new ArrayList<HashMap<String, String>>();
							org.json.JSONArray arr = json.getJSONArray("offices");
							if (arr != null)
							{
								for (int i=0; i<arr.length(); i++)
								{
									HashMap<String, String>  map = new HashMap<String, String>();
									org.json.JSONObject json2 = arr.getJSONObject(i);
									map.put("id",   json2.getString("id"));
									map.put("name", json2.getString("name"));
									ReceiptTabApplication.Offices.add(map);
								}

								HashMap<String, String>  map = new HashMap<String, String>();
								map = ReceiptTabApplication.Offices.get(0);
								ReceiptTabApplication.officeId	 = map.get("id");
								ReceiptTabApplication.officeName = map.get("name");
							}
							else
							{
//								showErrorMsg("エラー", null, "");
								Toast.makeText(this, getString(R.string.error_msg_common1), Toast.LENGTH_LONG).show();
								return;
							}
//						}
//						else
//						{
//							showErrorMsg("エラー", json, "");
//							return;
//						}
					}
					else
					{
//						showErrorMsg("エラー", null);
						Toast.makeText(this, getString(R.string.error_msg_common1), Toast.LENGTH_LONG).show();
						return;
					}
				}
				catch (org.json.JSONException e)
				{
					e.printStackTrace();
					return;
				}
			}
			else
			{
//				showErrorMsg("エラー", null);
				Toast.makeText(this, getString(R.string.error_msg_common1), Toast.LENGTH_LONG).show();
				return;
			}
		}

		//	ICCardリーダーServiceをスタートさせる
		prepareService();
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if		(requestCode == REQUEST_CAMERA_PERMISSION)
		{
			if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_DENIED)
			{
				// close the app
				terminateApp();
				return;
			}
			else
			{
				if (Build.VERSION.SDK_INT >= 23)					//	>= Marshmallow(6.0)
				{
					if (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
					{
						requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
						return;
					}
				}

				// Add permission for BT and let user grant the permission
				if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
				{
					ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH}, REQUEST_BLUETOOTH_PERMISSION);
					return;
				}
			}
		}
		else if (requestCode == REQUEST_BLUETOOTH_PERMISSION)
		{
			if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_DENIED)
			{
				// close the app
				terminateApp();
				return;
			}

			prepareService();
		}
		else if (requestCode == REQUEST_LOCATION_PERMISSION)
		{
			if (grantResults[0] == android.content.pm.PackageManager.PERMISSION_DENIED)
			{
				// close the app
				terminateApp();
			}
			else
			{
				// Add permission for BT and let user grant the permission
				if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
				{
					ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH}, REQUEST_BLUETOOTH_PERMISSION);
					return;
				}

				prepareService();
			}
		}
	}


	//	通常は終了しないが、BLE未対応端末の場合は終了させる
	//
	private  void  terminateApp()
	{
		mMsgHandler			= null;
		actionBar			= null;
		mIICCReaderService	= null;

		finish();
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  static  void  loadSharedPreferences()
	{
		android.content.SharedPreferences pref	= android.preference.PreferenceManager.getDefaultSharedPreferences(ReceiptTabApplication.AppContext);
		ReceiptTabApplication.officeId		= pref.getString("officeId", "");			//  default : ""
		ReceiptTabApplication.providerId	= pref.getString("providerId", "");		//  default : ""
		ReceiptTabApplication.providerPw	= pref.getString("password", "");			//	default ; ""
	}


	public  static  void  saveSharedPreferences()
	{
		android.content.SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(ReceiptTabApplication.AppContext);
		android.content.SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString("officeId", 	 ReceiptTabApplication.officeId);			//  default : ""
		prefEditor.putString("providerId",	 ReceiptTabApplication.providerId);			//  default : ""  設定にて入力させる
		prefEditor.putString("password",	 ReceiptTabApplication.providerPw);			//	default : ""
		prefEditor.commit();
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public  void  onBtnPlace1Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			Message  msg = new Message();
			msg.what = MSG_PLACE_CLICKED;
			msg.arg1 = 1;
			mMsgHandler.sendMessage(msg);
		}
	}

	@Override
	public  void  onBtnBack2Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			Message  msg = new Message();
			msg.what = MSG_BACK_CLICKED;
			msg.arg1 = 2;
			mMsgHandler.sendMessage(msg);
		}
	}


	@Override
	public  void  onBtnHome2Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			Message  msg = new Message();
			msg.what = MSG_HOME_CLICKED;
			msg.arg1 = 2;
			mMsgHandler.sendMessage(msg);
		}
	}


	@Override
	public  void  onBtnPlace2Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			Message  msg = new Message();
			msg.what = MSG_PLACE_CLICKED;
			msg.arg1 = 2;
			mMsgHandler.sendMessage(msg);
		}
	}


	@Override
	public  void  onBtnBack3Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			Message  msg = new Message();
			msg.what = MSG_BACK_CLICKED;
			msg.arg1 = 3;
			mMsgHandler.sendMessage(msg);
		}
	}


	@Override
	public  void  onBtnHome3Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			Message  msg = new Message();
			msg.what = MSG_HOME_CLICKED;
			msg.arg1 = 3;
			mMsgHandler.sendMessage(msg);
		}
	}


	@Override
	public  void  onBtnPlace3Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			Message  msg = new Message();
			msg.what = MSG_PLACE_CLICKED;
			msg.arg1 = 3;
			mMsgHandler.sendMessage(msg);
		}
	}


	@Override
	public  void  onBtnOpt3Clicked()
	{
		if (ReceiptTabApplication.isMsgShown == false)
		{
			ReceiptTabApplication.userRegData		= null;
			ReceiptTabApplication.bookingRoomData	= null;

			Message  msg = new Message();
			msg.what = MSG_HOME_CLICKED;
			msg.arg1 = 3;
			mMsgHandler.sendMessage(msg);
		}
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected  void  onActivityResult(int reqCode, int rsltCode, Intent data)
	{
		super.onActivityResult(reqCode, rsltCode, data);
		findIccBleDevice.onActivityResult(reqCode, rsltCode, data);
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	class  LooperThread  extends  Thread
	{
		public void run()
		{
			Looper.prepare();

			mMsgHandler = new Handler()
			{
				public  void  handleMessage(Message msg)
				{
					int		rc		= -1;

					switch (msg.what)
					{
						case MSG_APP_MAIN_COMP:
								if		(msg.arg1 == 1)		startFragmentWorkList();
								else if (msg.arg1 == 2)	startFragmentMeetingList();
								else if (msg.arg1 == 3)	startFragmentTabletPortrait();
								else if (msg.arg1 == 4)	startFragmentRuleGuide();
								else if (msg.arg1 == 5)	startDialogFloorGuide();
								else if (msg.arg1 == 6)	startFragmentProviderLogin();
								break;

						case MSG_WORK_LIST_COMP:
								ReceiptTabApplication.currentWorkId		 = msg.arg1;
								ReceiptTabApplication.currentWorkStatus	 = msg.arg2;
								ReceiptTabApplication.currentWorkName	 = (String)msg.obj;
								startFragmentWorkDetail();
								break;

						case MSG_WORK_DETAIL_COMP:
							if		(msg.arg1 == 1)		startFragmentCheckBooking();
							else if (msg.arg1 == 2)	startFragmentStatusBooking(msg.arg2, 1);
								break;

						case MSG_MEETING_LIST_COMP:
								ReceiptTabApplication.currentMeetingId		= msg.arg1;
								ReceiptTabApplication.currentMeetingStatus	= msg.arg2;
								ReceiptTabApplication.currentMeetingName	= (String)msg.obj;
								startFragmentMeetingDetail();
								break;

						case MSG_MEETING_DETAIL_COMP:
							if		(msg.arg1 == 1)		startFragmentCheckBooking();
							else if (msg.arg1 == 2)	startFragmentStatusBooking(msg.arg2, 2);
								break;

						case MSG_CHECK_BOOKING_COMP:
								if		(msg.arg1 == 1)		startFragmentLoginIC();
								else if (msg.arg1 == 2)	startFragmentLoginQR();
								else if (msg.arg1 == 3)	startFragmentLoginPW();
								else if (msg.arg1 == 4)	startFragmentEntryApp();
								else if (msg.arg1 == 5)	startFragmentEntryInput();
								break;

						case MSG_LOGIN_IC_COMP:
								if (backReqPending == false)
								{
									if ((msg.arg1 == 1) || (msg.arg1 == 4))
									{
										if		(msg.arg1 == 1)
										{
											int pos1 = ReceiptTabApplication.stackPos - 2;		//	caller
											if		(ReceiptTabApplication.CallStack[pos1] == FRAGMENT_TABLET_PORTRAIT)
											{
												startFragmentBookList();
											}
											else
											{
												startFragmentOrderConfirm();
											}
										}
										else if (msg.arg1 == 4)	startFragmentStartLink();
									}
									else
									{
										//	ICCをクローズする
										try
										{
											mIICCReaderService.ICCCloseReader();
										}
										catch (android.os.RemoteException e)
										{
											e.printStackTrace();
										}

										if		(msg.arg1 == 2)		startFragmentLoginQR();
										else if (msg.arg1 == 3)	startFragmentLoginPW();
									}
								}
								else
								{
									backReqPending = false;
									doBack();
								}
								break;

						case MSG_LOGIN_QR_COMP:
								if (backReqPending == false)
								{
									if (((String)msg.obj).compareTo("") != 0)
									{
										int pos1 = ReceiptTabApplication.stackPos - 2;		//	caller
										if		(ReceiptTabApplication.CallStack[pos1] == FRAGMENT_TABLET_PORTRAIT)
										{
											startFragmentBookList();
										}
										else if (ReceiptTabApplication.CallStack[pos1] == FRAGMENT_CHECK_BOOKING)
										{
											startFragmentOrderConfirm();
										}
										else if (ReceiptTabApplication.CallStack[pos1] == FRAGMENT_START_LINK)
										{
											startFragmentAccountLink();
										}
									}
								}
								 else
								{
									backReqPending = false;
									doBack();
								}
								break;

						case MSG_LOGIN_PW_COMP:
								int pos1 = ReceiptTabApplication.stackPos - 2;		//	caller
								if		(ReceiptTabApplication.CallStack[pos1] == FRAGMENT_TABLET_PORTRAIT)
								{
									startFragmentBookList();
								}
								else if (ReceiptTabApplication.CallStack[pos1] == FRAGMENT_CHECK_BOOKING)
								{
									startFragmentOrderConfirm();
								}
								else if (ReceiptTabApplication.CallStack[pos1] == FRAGMENT_START_LINK)
								{
									startFragmentAccountLink();
								}
								break;

						case MSG_ENTRY_APP_COMP:
								if		(msg.arg1 == 1)		startFragmentAppMain();
								else if (msg.arg1 == 2)	startFragmentLoginIC();
								else if (msg.arg1 == 3)	startFragmentLoginQR();
								break;

						case MSG_ENTRY_CARD_COMP:
								if		(msg.arg1 == 1)		startFragmentEntryInvoice();
								else if (msg.arg1 == 2)	startFragmentEntryPolicy();
								break;

						case MSG_ENTRY_INPUT_COMP:
								startFragmentEntryCard();
								break;

						case MSG_ENTRY_INVOICE_COMP:
								if		(msg.arg1 == 1)		startFragmentEntryCard();
								else if (msg.arg1 == 2)	startFragmentEntryPolicy();
								break;

						case MSG_ENTRY_POLICY_COMP:
								startFragmentAppMain();
								break;

						case MSG_BOOK_LIST_COMP:
								startFragmentBookDetail(msg.arg1, (String)msg.obj);
								break;

						case MSG_BOOK_DETAIL_COMP:
								if		(msg.arg1 == 1)		startDialogFloorGuide();
								else if (msg.arg1 == 2)	startFragmentAppMain();				//	違いは????
								else if (msg.arg1 == 3)	startFragmentAppMain();				//	違いは????
								break;

						case MSG_ORDER_CONFIRM_COMP:
								if		(msg.arg1 == 1)		startFragmentOrderComplete(msg.arg2);
								else if (msg.arg1 == 2)
								{
									ReceiptTabApplication.stackPos -= 2;
									doBack();
								}
								break;

						case MSG_ORDER_COMP_COMP:
								if		(msg.arg1 == 1)		startDialogFloorGuide();
								else if (msg.arg1 == 2)	startFragmentAppMain();
								break;

						case MSG_RULE_GUIDE_COMP:
								startFragmentAppMain();
								break;

						case MSG_START_LINK:
								if		(msg.arg1 == 1)		startFragmentLoginQR();
								else if (msg.arg1 == 2)	startFragmentLoginPW();
								else if (msg.arg1 == 3)	startFragmentEntryApp();
								else if (msg.arg1 == 4)	startFragmentEntryInput();
								break;

						case MSG_ACC_LINK_START:
								if		(msg.arg1 == 1)		startFragmentLinkComplete();
								else if (msg.arg1 == 2)	startFragmentAppMain();
								break;

						case MSG_ACC_LINK_COMP:
							int	  pos3 = ReceiptTabApplication.stackPos - 7;
							if		(ReceiptTabApplication.CallStack[pos3] == FRAGMENT_APP_MAIN)
							{
								startFragmentBookList();
							}
							else if (  (ReceiptTabApplication.CallStack[pos3] == FRAGMENT_WORK_DETAIL)
									 || (ReceiptTabApplication.CallStack[pos3] == FRAGMENT_MEETING_DETAIL))
							{
								startFragmentOrderConfirm();
							}
							break;

						case MSG_STATUS_BOOKING_COMP:
								ReceiptTabApplication.stackPos -= 2;
//								int		pos = ReceiptTabApplication.stackPos;

								if		(msg.arg1 == 1)		startFragmentWorkDetail();
								else if (msg.arg1 == 2)	startFragmentMeetingDetail();
								break;

						case MSG_PROVIDER_LOGIN_COMP:
								if		(msg.arg1 == 1)		startFragmentOfficeList();
								else if (msg.arg1 == 2)	startFragmentAppMain();
								break;

						case MSG_OFFICE_LIST_COMP:
								break;

						case MSG_TABLET_PORTRAIT_COMP:
								if		(msg.arg1 == 1)		startFragmentLoginIC();
								else if (msg.arg1 == 2)	startFragmentLoginQR();
								else if (msg.arg1 == 3)	startFragmentLoginPW();
								break;


						case MSG_BACK_CLICKED:
								int  pos2 = ReceiptTabApplication.stackPos - 1;
								if		(ReceiptTabApplication.CallStack[pos2] == FRAGMENT_LOGIN_IC)
								{
									//	ICCをクローズする
									try
									{
										mIICCReaderService.ICCCloseReader();
									}
									catch (android.os.RemoteException e)
									{
										e.printStackTrace();
									}

									backReqPending = false;
									doBack();
								}
								else if (ReceiptTabApplication.CallStack[pos2] == FRAGMENT_LOGIN_QR)
								{
									//	終了を待ち合わせる
									backReqPending = true;
								}
								else
								{
									backReqPending = false;
									doBack();
								}
								break;

						case MSG_HOME_CLICKED:
								startFragmentAppMain();
								break;

						case MSG_PLACE_CLICKED:
								startDialogFloorGuide();
								break;

						case MSG_START_PLACE:
								startDialogFloorGuide();
								break;

						case MSG_HIDE_ACTIONBAR:
								hide();
								break;

						case MSG_START_ICR_SERVICE:
								findIccBleDevice = null;				//	もういらない
								mBtDevice = (BluetoothDevice) msg.obj;
								startICRService();
								break;

						case MSG_START_PROVIDER_LOGIN:
								startFragmentProviderLogin();
								break;
					}
				}
			};

			Looper.loop();
		}
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  doBack()
	{
		ReceiptTabApplication.stackPos -= 2;
		int		pos = ReceiptTabApplication.stackPos;

		if		(ReceiptTabApplication.CallStack[pos] == FRAGMENT_APP_MAIN)			startFragmentAppMain();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_WORK_LIST)			startFragmentWorkList();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_WORK_DETAIL)		startFragmentWorkDetail();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_MEETING_LIST)		startFragmentMeetingList();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_MEETING_DETAIL)	startFragmentMeetingDetail();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_CHECK_BOOKING)		startFragmentCheckBooking();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_LOGIN_IC)			startFragmentLoginIC();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_LOGIN_QR)			startFragmentLoginQR();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_LOGIN_PW)			startFragmentLoginPW();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ENTRY_APP)			startFragmentEntryApp();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ENTRY_CARD)		startFragmentEntryCard();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ENTRY_INPUT)		startFragmentEntryInput();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ENTRY_INVOICE)		startFragmentEntryInvoice();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ENTRY_POLICY)		startFragmentEntryPolicy();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_BOOK_LIST)			startFragmentBookList();
//		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_BOOK_DETAIL)		startFragmentBookDetail(0, "");				//	ココに戻る事は無い
//		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ORDER_COMP)		startFragmentOrderComplete();				//	ココに戻る事は無い
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ORDER_CONFIRM)		startFragmentOrderConfirm();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_RULE_GUIDE)		startFragmentRuleGuide();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_START_LINK)		startFragmentStartLink();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ACC_LINK_START)	startFragmentAccountLink();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_ACC_LINK_COMP)		startFragmentLinkComplete();
//		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_STATUS_BOOKING)	startFragmentStatusBooking(0, 1);				//	goBackはない
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_PROVIDER_LOGIN)	startFragmentProviderLogin();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_OFFICE_LIST)		startFragmentOfficeList();
		else if (ReceiptTabApplication.CallStack[pos] == FRAGMENT_TABLET_PORTRAIT)	startFragmentTabletPortrait();
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  startFragmentAppMain()
	{
		FragmentAppMain		fragment = new FragmentAppMain();
		replaceFragment(fragment, TAG_APP_MAIN);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentAppMainListener();
		fragment.setOnFragmentInteractionListener((FragmentAppMain.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(1, ReceiptTabApplication.officeName);
				selectHeaderType(1);
			}
		});

		for (int i=0; i<30; i++)
		  {
			ReceiptTabApplication.CallStack[i] = -1;
		  }
		ReceiptTabApplication.stackPos = 0;

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_APP_MAIN;
	}


	private  void  startFragmentWorkList()
	{
		FragmentWorkList	fragment = new FragmentWorkList();
		replaceFragment(fragment, TAG_WORK_LIST);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentWorkListListener();
		fragment.setOnFragmentInteractionListener((FragmentWorkList.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_work_space));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_WORK_LIST;
	}


	private  void  startFragmentWorkDetail()
	{
		FragmentWorkDetail	fragment = new FragmentWorkDetail();
		replaceFragment(fragment, TAG_WORK_DETAIL);

		if (mListener != null)
		{
			mListener = null;
		}
		mListener = new FragmentWorkDetailListener(ReceiptTabApplication.currentWorkId);
		fragment.setOnFragmentInteractionListener((FragmentWorkDetail.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, ReceiptTabApplication.currentWorkName);
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_WORK_DETAIL;
	}


	private  void  startFragmentMeetingList()
	{
		FragmentMeetingList	fragment = new FragmentMeetingList();
		replaceFragment(fragment, TAG_MEETING_LIST);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentMeetingListListener();
		fragment.setOnFragmentInteractionListener((FragmentMeetingList.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_meeting_space));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_MEETING_LIST;
	}


	private  void  startFragmentMeetingDetail()
	{
		FragmentMeetingDetail	fragment = new FragmentMeetingDetail();
		replaceFragment(fragment, TAG_MEETING_DETAIL);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentMeetingDetailListener(ReceiptTabApplication.currentMeetingId);
		fragment.setOnFragmentInteractionListener((FragmentMeetingDetail.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, ReceiptTabApplication.currentMeetingName);
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_MEETING_DETAIL;
	}


	private  void  startFragmentCheckBooking()
	{
		FragmentCheckBooking	fragment = new FragmentCheckBooking();
		replaceFragment(fragment, TAG_CHECK_BOOKING);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentCheckBookingListener();
		fragment.setOnFragmentInteractionListener((FragmentCheckBooking.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_check_booking));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_CHECK_BOOKING;
	}


	private  void  startFragmentRuleGuide()
	{
		FragmentRuleGuide	fragment = new FragmentRuleGuide();
		replaceFragment(fragment, TAG_RULE_GUIDE);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentRuleGuideListener();
		fragment.setOnFragmentInteractionListener((FragmentRuleGuide.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_rule_guide));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_RULE_GUIDE;
	}


	private  void  startFragmentLoginIC()
	{
		FragmentLoginIC	fragment = new FragmentLoginIC();
		replaceFragment(fragment, TAG_LOGIN_IC);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentLoginICListener();
		fragment.setOnFragmentInteractionListener((FragmentLoginIC.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_login_ic));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_LOGIN_IC;
	}


	private  void  startFragmentLoginQR()
	{
		FragmentLoginQR	fragment = new FragmentLoginQR();
		replaceFragment(fragment, TAG_LOGIN_QR);

		if (mListener != null) {
			mListener = null;
		}

		mListener = new FragmentLoginQRListener();


		fragment.setOnFragmentInteractionListener((FragmentLoginQR.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_login_qr));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_LOGIN_QR;
	}


	private  void  startFragmentLoginPW()
	{
		FragmentLoginPW	fragment = new FragmentLoginPW();
		replaceFragment(fragment, TAG_LOGIN_PW);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentLoginPWListener();
		fragment.setOnFragmentInteractionListener((FragmentLoginPW.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_login_pw));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_LOGIN_PW;
	}


	private  void  startFragmentEntryApp()
	{
		FragmentEntryApp	fragment = new FragmentEntryApp();
		replaceFragment(fragment, TAG_ENTRY_APP);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentEntryAppListener();
		fragment.setOnFragmentInteractionListener((FragmentEntryApp.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_entry_app));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ENTRY_APP;
	}


	private  void  startFragmentEntryCard()
	{
		FragmentEntryCard	fragment = new FragmentEntryCard();
		replaceFragment(fragment, TAG_ENTRY_CARD);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentEntryCardListener();
		fragment.setOnFragmentInteractionListener((FragmentEntryCard.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_entry_card));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ENTRY_CARD;
	}


	private  void  startFragmentEntryInput()
	{
		FragmentEntryInput	fragment = new FragmentEntryInput();
		replaceFragment(fragment, TAG_ENTRY_INPUT);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentEntryInputListener();
		fragment.setOnFragmentInteractionListener((FragmentEntryInput.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_entry_input));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ENTRY_INPUT;
	}


	private  void  startFragmentEntryInvoice()
	{
		FragmentEntryInvoice	fragment = new FragmentEntryInvoice();
		replaceFragment(fragment, TAG_ENTRY_INVOICE);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentEntryInvoiceListener();
		fragment.setOnFragmentInteractionListener((FragmentEntryInvoice.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_entry_invoice));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ENTRY_INVOICE;
	}


	private  void  startFragmentEntryPolicy()
	{
		FragmentEntryPolicy	fragment = new FragmentEntryPolicy();
		replaceFragment(fragment, TAG_ENTRY_POLICY);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentEntryPolicyListener();
		fragment.setOnFragmentInteractionListener((FragmentEntryPolicy.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_entry_policy));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ENTRY_POLICY;
	}


	private  void  startFragmentBookList()
	{
		FragmentBookList	fragment = new FragmentBookList();
		replaceFragment(fragment, TAG_BOOK_LIST);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentBookListListener();
		fragment.setOnFragmentInteractionListener((FragmentBookList.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(3, getString(R.string.appmain_title_booking_list));
				selectHeaderType(3);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_BOOK_LIST;
	}


	private  void  startFragmentBookDetail(int id, final String title)
	{
		FragmentBookDetail	fragment = new FragmentBookDetail();
		replaceFragment(fragment, TAG_BOOK_DETAIL);

		if (mListener != null)
		{
			mListener = null;
		}
		mListener = new FragmentBookDetailListener(id);
		fragment.setOnFragmentInteractionListener((FragmentBookDetail.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(3, title);
				selectHeaderType(3);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_BOOK_DETAIL;
	}


	private  void  startFragmentOrderConfirm()
	{
		FragmentOrderConfirm	fragment = new FragmentOrderConfirm();
		replaceFragment(fragment, TAG_ORDER_CONFIRM);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentOrderConfirmListener();
		fragment.setOnFragmentInteractionListener((FragmentOrderConfirm.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(3, getString(R.string.appmain_title_order_comfirm));
				selectHeaderType(3);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ORDER_CONFIRM;
	}


	private  void  startFragmentOrderComplete(int id)
	{
		FragmentOrderComplete	fragment = new FragmentOrderComplete();
		replaceFragment(fragment, TAG_ORDER_COMP);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentOrderCompleteListener(id);
		fragment.setOnFragmentInteractionListener((FragmentOrderComplete.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(3, getString(R.string.appmain_title_order_complete));
				selectHeaderType(3);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ORDER_COMP;
	}


	private  void  startFragmentStartLink()
	{
		FragmentStartLink	fragment = new FragmentStartLink();
		replaceFragment(fragment, TAG_START_LINK);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentStartLinkListener();
		fragment.setOnFragmentInteractionListener((FragmentStartLink.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_select_login));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_START_LINK;
	}


	private  void  startFragmentAccountLink()
	{
		FragmentAccountLink	fragment = new FragmentAccountLink();
		replaceFragment(fragment, TAG_ACC_LINK_START);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentAccountLinkListener();
		fragment.setOnFragmentInteractionListener((FragmentAccountLink.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_account_link));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ACC_LINK_START;
	}


	private  void  startFragmentLinkComplete()
	{
		FragmentLinkComplete	fragment = new FragmentLinkComplete();
		replaceFragment(fragment, TAG_ACC_LINK_COMP);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentLinkCompleteListener();
		fragment.setOnFragmentInteractionListener((FragmentLinkComplete.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_link_complete));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_ACC_LINK_COMP;
	}


	private  void  startFragmentStatusBooking(int id, int kind)
	{
		FragmentStatusBooking	fragment = new FragmentStatusBooking();
		replaceFragment(fragment, TAG_STATUS_BOOKING);

		if (mListener != null)
		{
			mListener = null;
		}
		mListener = new FragmentStatusBookingListener(id, kind);
		fragment.setOnFragmentInteractionListener((FragmentStatusBooking.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_booking_status));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_STATUS_BOOKING;
	}


	private  void  startFragmentProviderLogin()
	{
		FragmentProviderLogin	fragment = new FragmentProviderLogin();
		replaceFragment(fragment, TAG_PROVIDER_LOGIN);

		if (mListener != null)
		{
			mListener = null;
		}
		mListener = new FragmentProviderLoginListener();
		fragment.setOnFragmentInteractionListener((FragmentProviderLogin.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (ReceiptTabApplication.providerId.equals("") == false)
				{
					selectHeaderTitle(1, getString(R.string.appmain_title_provider_login));
					selectHeaderType(1);
				}
				else
				{
					selectHeaderType(0);
				}
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_PROVIDER_LOGIN;
	}


	private  void  startFragmentOfficeList()
	{
		FragmentOfficeList	fragment = new FragmentOfficeList();
		replaceFragment(fragment, TAG_OFFICE_LIST);

		if (mListener != null)
		{
			mListener = null;
		}
		mListener = new FragmentOfficeListListener();
		fragment.setOnFragmentInteractionListener((FragmentOfficeList.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, getString(R.string.appmain_title_office_list));
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_OFFICE_LIST;
	}


	private  void  startFragmentTabletPortrait()
	{
		FragmentTabletPortrait	fragment = new FragmentTabletPortrait();
		replaceFragment(fragment, TAG_TABLET_PORTRAIT);

		if (mListener != null)
		{
			mListener = null;
		}
		mListener = new FragmentTabletPortraitListener();
		fragment.setOnFragmentInteractionListener((FragmentTabletPortrait.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderTitle(2, ReceiptTabApplication.officeName);
				selectHeaderType(2);
			}
		});

		ReceiptTabApplication.CallStack[ReceiptTabApplication.stackPos++] = FRAGMENT_TABLET_PORTRAIT;
	}


	private  void  showTopScreen()
	{
		FragmentAppMain		fragment = new FragmentAppMain();
		replaceFragment(fragment, TAG_APP_MAIN);

		if (mListener != null) {
			mListener = null;
		}
		mListener = new FragmentAppMainListener();
		fragment.setOnFragmentInteractionListener((FragmentAppMain.FragmentInteractionListener) mListener);

		//	ヘッダーの設定
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				selectHeaderType(1);
			}
		});
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  replaceFragment(Fragment fragment, String tag)
	{
		FragmentManager 	manager		= getSupportFragmentManager();
		FragmentTransaction transaction	= manager.beginTransaction();

		transaction.replace(R.id.content_container, fragment, tag);			//	container1
		transaction.commit();
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  startDialogFloorGuide()
	{
		FragmentDialogFloorGuide fragmentFloorGuide = new FragmentDialogFloorGuide();
		fragmentFloorGuide.show(getSupportFragmentManager(), "TAG_FLOOR_GUIDE");

		hide();
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  startICRService()
	{
		Intent  intent = new Intent();
		intent.setClassName("jp.spacee.app.android.spacee_app", "jp.spacee.app.android.spacee_app.service.ICCReaderService");

		stopService(intent);				// 先に一度サービスを停止しておく

		boolean  rc = bindService(intent, mIICCReaderServiceConnection, Context.BIND_AUTO_CREATE);
	}


	private ServiceConnection mIICCReaderServiceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mIICCReaderService = IICCReaderService.Stub.asInterface(service);

			try
			{
				mIICCReaderService.registerCallback(serviceCallback);
			}
			catch (android.os.RemoteException e)
			{
				e.printStackTrace();
			}

			try
			{
				mIICCReaderService.ICCInitializeReader(mBtDevice);
			}
			catch (RemoteException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(android.content.ComponentName name)
		{
			if (mIICCReaderService != null)
			{




			}

			mIICCReaderService = null;
		}
	};


	private IICCReaderServiceCallback  serviceCallback = new IICCReaderServiceCallback.Stub()
	{
		@Override
		public  void  ICCR_InitComp(int retcode)
		{
			ReceiptTabApplication.flagInitComp = true;			//	initialize completed

			Message msg = new Message();
			msg.what = MSG_HOME_CLICKED;
			msg.arg1 = 0;										//	initial
			mMsgHandler.sendMessage(msg);
		}


		@Override
		public  void  ICCR_OpenComp(int retcode)
		{
			if (retcode == 0)
			{
				try
				{
					mIICCReaderService.ICCReadIDm();
				}
				catch (RemoteException e)
				{
					e.printStackTrace();
				}
			}
			else
			{

			}
		}


		@Override
		public  void  ICCR_ReadData(String result)
		{
			String	auth = "";

			if ((result != null) && (result.length() == 16))
			{
				ReceiptTabApplication.currentUserIdm = result;

				auth = SpaceeAppMain.httpCommGlueRoutines.userAuthenticateIdm(result);
				if (auth != null)
				{
					try
					{
						org.json.JSONObject obj1 = new org.json.JSONObject(auth);
						String  status	= obj1.getString("status");

						if (status.equals("ok"))
						{
							ReceiptTabApplication.userAuthToken		= obj1.getString("auth_token");
							ReceiptTabApplication.currentUserMAddr	= obj1.getString("email");
//							ReceiptTabApplication.currentUserName	= obj1.getString("xxxxx");

							PlayWaveFile playWaveFile = new PlayWaveFile();
							playWaveFile.playWaveSound(R.raw.ok_sound);

							Message msg = new Message();
							msg.what = MSG_LOGIN_IC_COMP;
							msg.arg1 = 1;
							msg.obj  = auth;
							mMsgHandler.sendMessage(msg);
						}
						else
						{
							//	Serviceからエラーは戻ってこないはずだが・・とりあえず
							Message msg = new Message();
							msg.what = MSG_LOGIN_IC_COMP;
							msg.arg1 = 5;
							msg.obj  = result;
							mMsgHandler.sendMessage(msg);
						}
					}
					catch (org.json.JSONException e)
					{
						e.printStackTrace();
						return;
					}
				}
				else
				{
					PlayWaveFile playWaveFile = new PlayWaveFile();
					playWaveFile.playWaveSound(R.raw.ng_sound);

					//	未登録カード
					Message msg = new Message();
					msg.what = MSG_LOGIN_IC_COMP;
					msg.arg1 = 4;
					msg.obj  = result;
					mMsgHandler.sendMessage(msg);
				}
			}
			else
			{
				//	mifare 等でここに来る事は無い(FeliCaのみ通知してくる)
			}

			try
			{
				mIICCReaderService.ICCCloseReader();
			}
			catch (android.os.RemoteException e)
			{
				e.printStackTrace();
			}
		}


		@Override
		public  void  ICCR_Attention(int attentionKind, String info)
		{
			//	ＩＣリーダーで何かが起こった・・・
			if		(attentionKind == -1)
			{


			}
			else if (attentionKind == -2)
			{


			}
		}
	};


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  prepareService()
	{
		findIccBleDevice = new FindIccBleDevice();
		findIccBleDevice.prepareService();


		//	３秒後に画面を切り替える
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				showTopScreen();
			}
		}, 3000);
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  showErrorMsg(String ttl, org.json.JSONObject jsonObj)
	{
/*
		int		i;
		String	errMsg;

		if (jsonObj != null)
		{
			try
			{
				org.json.JSONArray arr1 = jsonObj.getJSONArray("error_messages");
				errMsg = "";
				for (i=0; i<arr1.length(); i++)
				{
					errMsg += (arr1.getString(i) + "\n");
				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return;
			}
		}
		else
		{
			errMsg = "データが取得できませんでした";
		}

		errLayout.setVisibility(android.view.View.VISIBLE);
		title.setText(ttl);
		content.setText(errMsg);

		ReceiptTabApplication.isMsgShown =true;

		msgOff.setOnClickListener(new android.view.View.OnClickListener()
		{
			@Override
			public void onClick(android.view.View v)
			{
				ReceiptTabApplication.isMsgShown =false;
			}
		});

		//	メッセージの下のエレメントをタップしても拾わないようにするため
		errLayout.setOnClickListener(new android.view.View.OnClickListener()
		{
			@Override
			public void onClick(android.view.View v)
			{
			}
		});
*/
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public  boolean  onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode != KeyEvent.KEYCODE_BACK)
		{
			return	super.onKeyDown(keyCode, event);
		}
		else
		{
			//	開発時は Back ===> finfish とする
			Intent intnt = new Intent();
			setResult(Activity.RESULT_CANCELED, intnt);
			finish();
			return	true;
		}
	}
}
