package jp.spacee.app.android.spacee_app.comm;


import android.os.AsyncTask;
import android.graphics.Bitmap;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import android.app.ProgressDialog;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

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

	public  boolean  providerUserSignin(String id, String pw)
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
			return	 false;
		}


		outStr = commHttpCall(POST, ReceiptTabApplication.URL_PROVIDER_USER_SIGNIN, jsonObj, "");
		if (outStr != null)
		{
			try
			{
				JSONObject json = new JSONObject(outStr);
				if (json != null)
				{
					rc = json.getString("status");
					if (rc.equals("ok"))
					{
						ReceiptTabApplication.providerAuthToken = json.getString("auth_token");
					}
					else
					{
						return  false;
					}
				}
				else
				{
					return  false;
				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return  false;
			}
		}
		else
		{
			return  false;
		}

		return  true;
	}


	public  boolean  retrieveProviderOffices()
	{
		String		outStr;
		String		rc;

		outStr = commHttpCall(GET, ReceiptTabApplication.URL_PROVIDERS_OFFICES, null, "");
		if (outStr != null)
		{
			try
			{
				ReceiptTabApplication.Offices = new ArrayList<HashMap<String, String>>();
				JSONObject jsonObj1  = new JSONObject(outStr);
				JSONArray  jsonArray = jsonObj1.getJSONArray("offices");
				if (jsonArray != null)
				{
					for (int i=0; i<jsonArray.length(); i++)
					{
						HashMap<String, String>  map = new HashMap<String, String>();
						JSONObject json = jsonArray.getJSONObject(i);
						map.put("id",   json.getString("id"));
						map.put("name", json.getString("name"));
						ReceiptTabApplication.Offices.add(map);
					}

					//	tentative
//					if (ReceiptTabApplication.Offices.size() == 1)
//					{
						HashMap<String, String>  map = new HashMap<String, String>();
						map = ReceiptTabApplication.Offices.get(0);
						ReceiptTabApplication.officeId	 = map.get("id");
						ReceiptTabApplication.officeName = map.get("name");
//					}
				}
				else
				{
					return  false;
				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return  false;
			}
		}
		else
		{
			return  false;
		}

		return  true;
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
		if (outStr != null)
		{
			try
			{
				JSONObject obj = new org.json.JSONObject(outStr);
				result = obj.getString("auth_token");
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return "";
			}
		}
		else
		{
			return  null;
		}

		return	 result;
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
			jsonObj.put("department_name",		"");
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
		if (outStr != null)
		{
			try
			{
				JSONObject obj = new org.json.JSONObject(outStr);
				result = obj.getString("billing_destination_id");
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return "";
			}
		}
		else
		{
			return "";
		}

		return	 result;
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
		if (outStr != null)
		{
			try
			{
				JSONObject obj = new org.json.JSONObject(outStr);
				result = obj.getString("card_id");
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return "";
			}
		}
		else
		{
			return "";
		}

		return	 result;
	}


	//	利用者向けガイドの取得
	public  String  retrieveUserGuide()
	{
		String		outStr;

		String	url = ReceiptTabApplication.URL_GUIDE_USER;

		outStr = commHttpCall(GET, url, null, "");

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