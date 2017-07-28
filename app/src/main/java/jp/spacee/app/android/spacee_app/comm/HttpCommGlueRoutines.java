package jp.spacee.app.android.spacee_app.comm;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;


public  class  HttpCommGlueRoutines
{
	private 					Object						mListener				= null;
	public   static			Semaphore					mSemaphore				= null;
	private						ProgressDialog				VLPDlg					= null;

	private  static  final	int							POST					= 1;
	private  static  final	int							GET						= 2;


	public  String  commHttpCall(int type, String sUrl, JSONObject paramJsonObj, String  prmGet)
	{
		HttpParamAPICall	params = new HttpParamAPICall();

		mSemaphore = new Semaphore(0);

		ReceiptTabApplication.flgCommCompleted = false;
		params.type		  = type;
		params.sUrl		  = sUrl;
		params.jsonObj	  = paramJsonObj;
		params.paramGet  = prmGet;

		AsyncTaskAPICall httpAsyncTask = new AsyncTaskAPICall();
		httpAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		try
		{
			mSemaphore.acquire();                //	AsynkTask内のonPostExecuteでreleaseされる
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			return null;
		}

		return  ReceiptTabApplication.CommResult;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  String  providerUserSignin(String id, String pw)
	{
		String		outStr;
		String		rc;

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("email", id);
			jsonObj.put("password", pw);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 null;
		}

		outStr = commHttpCall(POST, ReceiptTabApplication.URL_PROVIDER_USER_SIGNIN, jsonObj, "");

		return  outStr;
	}


	public  String  retrieveProviderOffices()
	{
		String		outStr;
		String		rc;

		outStr = commHttpCall(GET, ReceiptTabApplication.URL_PROVIDERS_OFFICES, null, "");

		return  outStr;
	}


	//	物件情報一覧の取得
	public  String  retrieveOfficeList()
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_PROVIDERS_OFFICE_LIST;
		url = url.replace(":OFFICE_ID", ReceiptTabApplication.officeId);

		outStr = commHttpCall(GET, url, null, "");

		return	 outStr;
	}


	//	物件情報一覧の取得
	public  String  retrieveOfficeInfo(String type)
	{
		String		outStr;
		String		prmGet = "space_type=" + type;

		String	url = ReceiptTabApplication.URL_PROVIDERS_OFFICE_INFO;
		url = url.replace(":OFFICE_ID", ReceiptTabApplication.officeId);

		outStr = commHttpCall(GET, url, null, prmGet);

		return	 outStr;
	}


	//	物件情報詳細の取得
	public  String  retrieveOfficeDetail(String id)
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_PROVIDERS_OFFICE_DETAIL;
		url = url.replace(":OFFICE_ID", ReceiptTabApplication.officeId);
		url = url.replace(":ID", id);

		outStr = commHttpCall(GET, url, null, "");

		return	 outStr;
	}


	//	物件の営業時間・予約状況の取得
	public  String  retrieveBookingStatus(String id, int year, int month)
	{
		String		outStr;

		String	url		= ReceiptTabApplication.URL_PROVIDERS_CALENDAR;
				url		= url.replace(":OFFICE_ID", id);
		String	wGetPrm	= String.format("year=%04d&month=%02d", year, month);

		outStr = commHttpCall(GET, url, null, wGetPrm);

		return	 outStr;
	}


	//	決済方法一覧の取得
	public  String  retrieveUserPayments()
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_USERS_PAYMENTS;

		outStr = commHttpCall(GET, url, null, "");

		return	 outStr;
	}


	//	予約一覧の取得
	public  String  retrieveBookingList(String prm)
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_USERS_PRE_BOOKING_REG;

		String	prmGet = String.format("booking_list=%s&office_id=", prm);

		outStr = commHttpCall(GET, url, null, prmGet);

		return	 outStr;
	}



	//	物件の料金プランの取得
	public  String  retrievePriceTable(String prm, String date)
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_PROVIDERS_PLICE_LIST;
		url	= url.replace(":OFFICE_ID", prm);

		String	prmGet = String.format("date=%s", date);

		outStr = commHttpCall(GET, url, null, prmGet);

		return	 outStr;
	}


	//	SMS送信
	public  String  sendSMS(String telno)
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_SEND_SMS;

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("tel", telno);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 null;
		}

		outStr = commHttpCall(POST, url, jsonObj, "");

		return	 outStr;
	}


	//	利用者登録(ID・パスワード認証)
	public  String  signupUser()
	{
		String	outStr;
		String	result = "";

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("lname",		ReceiptTabApplication.userRegData.nameFamily);
			jsonObj.put("fname",		ReceiptTabApplication.userRegData.nameGiven);
			jsonObj.put("email",		ReceiptTabApplication.userRegData.emailAddress);
			jsonObj.put("password",	ReceiptTabApplication.userRegData.password);
			jsonObj.put("agreement",	true);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		String	url = ReceiptTabApplication.URL_USERS_SIGNUP;

		outStr = commHttpCall(POST, url, jsonObj, "");

		return	 outStr;
	}


	//	利用者のIDm登録・削除	IDm登録
	public  String  registerIDm(String idm)
	{
		String	outStr;
		String	result = "";

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("idm",		idm);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		String	url = ReceiptTabApplication.URL_USERS_IDMS_REG;

		outStr = commHttpCall(POST, url, jsonObj, "");

		return	 outStr;
	}


	//	IDmによるユーザー認証
	public  String  signinUser(String id, String pw)
	{
		String	outStr;
		String	result = "";

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("email",		id);
			jsonObj.put("password",	pw);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		String	url = ReceiptTabApplication.URL_USERS_SIGNIN;

		outStr = commHttpCall(POST, url, jsonObj, "");

		return	 outStr;
	}


	//	IDmによるユーザー認証
	public  String  userAuthenticateIdm(String idm)
	{
		String	outStr;
		String	result = "";

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("idm", idm);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		String	url = ReceiptTabApplication.URL_USERS_IDMS_AUTH;

		outStr = commHttpCall(POST, url, jsonObj, "");

		return	 outStr;
	}


	//	ＱＲコードによるユーザー認証
	public  String  userAuthenticateQR(String qrCode)
	{
		String	outStr;
		String	result = "";

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("qr_code", qrCode);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		String	url = ReceiptTabApplication.URL_USERS_QR_AUTH;

		outStr = commHttpCall(POST, url, jsonObj, "");

		return	 outStr;
	}


	//	請求先の登録
	public  String  registerBillingDestination()
	{
		String	outStr;
		String	result = "";

		JSONObject jsonPrm = new JSONObject();
		try
		{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("company_name",			ReceiptTabApplication.userRegData.companyName);
			jsonObj.put("responsible_person",		ReceiptTabApplication.userRegData.operName);
			jsonObj.put("zip_code",				ReceiptTabApplication.userRegData.postCode);
			jsonObj.put("address_before",			ReceiptTabApplication.userRegData.companyAddress1);
			jsonObj.put("address_after",			ReceiptTabApplication.userRegData.companyAddress2);
			jsonObj.put("phone",					ReceiptTabApplication.userRegData.companyPhoneNo);
			jsonPrm.put("billing_destination", jsonObj);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		String	url = ReceiptTabApplication.URL_USERS_BILLING;

		outStr = commHttpCall(POST, url, jsonPrm, "");

		return	 outStr;
	}


	//	クレジットカードの登録
	public  String  registerCreditCardInfo()
	{
		String	outStr;
		String	result = "";

		JSONObject jsonPrm = new JSONObject();
		try
		{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("card_token",	ReceiptTabApplication.userRegData.cardToken);
			jsonObj.put("card_id",		ReceiptTabApplication.userRegData.cardNo + ReceiptTabApplication.userRegData.secretCode);
			jsonPrm.put("token", jsonObj);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		String	url = ReceiptTabApplication.URL_USERS_CARD;

		outStr = commHttpCall(POST, url, jsonPrm, "");

		return	 outStr;
	}


	//	利用者向けガイドの取得
	public  String  retrieveUserGuide()
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_GUIDE_USER;

		outStr = commHttpCall(GET, url, null, "");

		return	 outStr;
	}


	//	物件の料金計算の取得
	public  String  retrieveRoomPrice(String id, String stTime, int useMin, int psnNo, String cpnCode)
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_PROVIDERS_ROOM_AVAILABLE;
		url	= url.replace(":OFFICE_ID", id);

		String	prmGet = String.format("start_at=%s&minutes=%d&party=%d", stTime, useMin, psnNo);
		if (!TextUtils.isEmpty(cpnCode)) {
			prmGet += String.format("&coupon_code=%s", cpnCode);
		}

		outStr = commHttpCall(GET, url, null, prmGet);

		return	 outStr;
	}


	//	予約の詳細情報の取得
	public  String  retrieveBookingInfo(String id)
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_USERS_PRE_BOOKING_INFO;
		url	= url.replace(":ID", id);

		outStr = commHttpCall(GET, url, null, "");

		return	 outStr;
	}


	//	予約登録
	public  String  bookingSpace()
	{
		String		outStr;
		int			diff, pos1, pos2;

		String	url = ReceiptTabApplication.URL_USERS_PRE_BOOKING_REG;

		JSONObject jsonPrm = new JSONObject();
		try
		{
			JSONObject jsonObj1 = new JSONObject();
			JSONObject jsonObj2 = new JSONObject();
			jsonObj1.put("listing_id",		ReceiptTabApplication.bookingRoomData.roomId);
			String wStr = String.format("%04d-%02d-%02dT%s:00+900",	ReceiptTabApplication.bookingRoomData.useYear,
																		ReceiptTabApplication.bookingRoomData.useMonth,
																		ReceiptTabApplication.bookingRoomData.useDay,
																		ReceiptTabApplication.bookingRoomData.checkInTime);
			jsonObj1.put("start_at",		wStr);
			pos1 = ReceiptTabApplication.bookingRoomData.checkInTime.indexOf(":");
			pos2 = ReceiptTabApplication.bookingRoomData.checkOutTime.indexOf(":");
			diff	= Integer.parseInt(ReceiptTabApplication.bookingRoomData.checkOutTime.substring(0, pos2))*60
					+ Integer.parseInt(ReceiptTabApplication.bookingRoomData.checkOutTime.substring(pos2+1, ReceiptTabApplication.bookingRoomData.checkOutTime.length()))
					- Integer.parseInt(ReceiptTabApplication.bookingRoomData.checkInTime.substring (0, pos1))*60
					- Integer.parseInt(ReceiptTabApplication.bookingRoomData.checkInTime.substring (pos2+1, ReceiptTabApplication.bookingRoomData.checkInTime.length()));
			jsonObj1.put("minutes",		String.format("%d", diff));
			jsonObj1.put("party",			String.format("%d", ReceiptTabApplication.bookingRoomData.numPsn));
//			jsonObj1.put("coupon_id",		ReceiptTabApplication.bookingRoomData.cardToken);
			if (  (ReceiptTabApplication.bookingRoomData.couponCode != null)
				&& (ReceiptTabApplication.bookingRoomData.couponCode.equals("") == false) )
			{
				jsonObj1.put("coupon_id", ReceiptTabApplication.bookingRoomData.couponCode);
			}
			if (ReceiptTabApplication.bookingRoomData.paidWay.equals("1"))
			{
				jsonObj1.put("payment_type",	"card");
				jsonObj2.put("card_id", ReceiptTabApplication.bookingRoomData.paidId);
				jsonObj1.put("card_payment", jsonObj2);
			}
			else
			{
				jsonObj1.put("payment_type",	"invoice");
				jsonObj2.put("billing_destination_id", ReceiptTabApplication.bookingRoomData.paidId);
				jsonObj1.put("invoice_payment", jsonObj2);
			}
			jsonPrm.put("pre_booking", jsonObj1);
		}
		catch (org.json.JSONException e)
		{
			e.printStackTrace();
			return	 "";
		}

		outStr = commHttpCall(POST, url, jsonPrm, "");

		return	 outStr;
	}


	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  boolean  retrieveUserStatus()
	{
		String		outStr = "";
		String		wStr1, wStr2;
/*
*/
		return	 true;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  String  retrieveAddrfromPostCode(String postcode)
	{
		String		outStr = "";

		String	prmGet = String.format("address=%s&language=ja", postcode);

		outStr = commHttpCall(2, ReceiptTabApplication.GOOGLEAPIS_MAPS_GEOCODE, null, prmGet);		//	GET interface
		return	outStr;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  boolean  retrieveEventAttendeeList(String EventId, boolean fUpdate)
	{
		String		outStr = "";
		String		wStr1, wStr2;
/*
*/
		return	 true;
	}


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public  void  downloadFloorMap(String url)
	{
		HttpParamDownloadImages	params = new HttpParamDownloadImages();

		params.type		= 2;				//	GET I/F only
		params.numPict	= 1;
		params.sUrls	= new String[1];
		params.sUrls[0] = url;


		AsyncTaskDownloadImages httpAsyncTask = new AsyncTaskDownloadImages(new AsyncTaskDownloadImages.AsyncTaskCallback()
		{
			@Override
			public void processCompleted(int retCode, Bitmap[] bmps)
			{
				//	全てのImageがダウンロードされたら戻ってくる
				if ((retCode == 0) && (bmps.length > 0))
				{
					SpaceeAppMain.FloorMap = bmps[0];
				}

				return;
			}
		});
		httpAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	}


	public  Bitmap[]  downloadBitmaps(String[] urls)
	{
		HashMap<String, String>		map;
		HttpParamDownloadImages	params = new HttpParamDownloadImages();

		params.type		= 3;				//	GET I/F only
		params.numPict	= urls.length;
		params.sUrls	= new String[urls.length];
		params.mBmps	= new Bitmap[urls.length];

		for (int i=0; i< urls.length; i++)
		{
			params.sUrls[i] = urls[i];
		}
/*
		AsyncTaskDownloadImages httpAsyncTask = new AsyncTaskDownloadImages(new AsyncTaskDownloadImages.AsyncTaskCallback()
		{
			@Override
			public void processCompleted(int retCode, Bitmap[] bmps)
			{
				//	全てのImageがダウンロードされたら戻ってくる
				if ((retCode == 0) && (bmps.length > 0))
				{
					SpaceeAppMain.bmpsWorkArea = new Bitmap[bmps.length];
					for (int k = 0; k<bmps.length; k++)
					{
						SpaceeAppMain.bmpsWorkArea[k] = bmps[k];
					}
				}

				return;
			}
		});
*/
		mSemaphore = new Semaphore(0);

		AsyncTaskDownloadImages httpAsyncTask = new AsyncTaskDownloadImages(null);

		httpAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

		try
		{
			mSemaphore.acquire();                //	AsynkTask内のonPostExecuteでreleaseされる
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			return null;
		}

		return	params.mBmps;
	}
}