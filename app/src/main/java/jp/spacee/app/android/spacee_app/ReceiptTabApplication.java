package jp.spacee.app.android.spacee_app;

import android.app.Application;
import android.content.Context;
import android.bluetooth.BluetoothAdapter;
import android.media.AudioTrack;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import jp.spacee.app.android.spacee_app.common.UserRegisterData;
import jp.spacee.app.android.spacee_app.common.BookingRoomData;


public  class  ReceiptTabApplication  extends  Application
{
	public  static				Context						AppContext					= null;

	public  static				String						officeId					= "";
	public  static				String						providerId					= "";
	public  static				String						providerPw					= "";

	public  static				BluetoothAdapter			mBluetoothAdapter			= null;
	public  static				String						targetBLEDeviceName			= "ACR";

	public  static				boolean						flagInitComp				= false;

	public  static				int[]						CallStack					= null;
	public  static				int							stackPos					= 0;

	public  static				boolean						isMsgShown					= false;

	public  static				String						providerAuthToken			= "";
	public  static				String						userAuthToken				= "";

	public  static				List<HashMap<String, String>>	Offices					= null;
	public  static				String							officeName				= "";
	public  static				String							listingId				= "";

	public  static				UserRegisterData			userRegData					= null;
	public  static				BookingRoomData				bookingRoomData				= null;

	public  static				String						currentUserIdm				= "";
	public  static				String						currentUserMAddr			= "";
	public  static				String						currentUserName				= "";

	public  static				int							currentWorkId				= 0;
	public  static				int							currentWorkStatus			= 0;
	public  static				String						currentWorkName				= "";
	public  static				int							currentWorkDetailYear		= 0;
	public  static				int							currentWorkDetailMonth		= 0;
	public  static				int							currentWorkDetailDay		= 0;
	public  static				int							currentMeetingId			= 0;
	public  static				int							currentMeetingStatus		= 0;
	public  static				String						currentMeetingName			= "";
	public  static				int							currentMeetingDetailYear	= 0;
	public  static				int							currentMeetingDetailMonth	= 0;
	public  static				int							currentMeetingDetailDay		= 0;

	public  static				int							CommRetCode					= 0;
	public  static				String						CommResult					= "";
	public  static				String						PictFilePath				= "";
	public  static				boolean						flgCommCompleted			= false;

	public  static  final		int							COMM_RC_OK						=  0;
	public  static  final		int							COMM_RC_FALSE_HTTP_ERROR		= -1;

	public  static  final		String						URL_BASE						= "https://api-ws-staging.spacee.jp";
	public  static  final		String						URL_PROVIDERS_OFFICES			= URL_BASE + "/api/v2/providers/offices.json";											//	掲載者(provider_user)用データ取得
	public  static  final		String						URL_PROVIDERS_OFFICE_LIST	= URL_BASE + "/api/v2/providers/offices/:OFFICE_ID.json";								//	物件情報の取得
	public  static  final		String						URL_PROVIDERS_OFFICE_INFO	= URL_BASE + "/api/v2/providers/offices/:OFFICE_ID/listings.json";					//	物件情報一覧の取得
	public  static  final		String						URL_PROVIDERS_OFFICE_DETAIL	= URL_BASE + "/api/v2/providers/offices/:OFFICE_ID/listings/:ID.json";				//	物件の詳細情報の取得
	public  static  final		String						URL_PROVIDERS_CALENDAR		= URL_BASE + "/api/v2/providers/listings/:OFFICE_ID/calendar.json";					//	物件の営業時間・予約状況の取得
	public  static  final		String						URL_PROVIDERS_PLICE_LIST		= URL_BASE + "/api/v2/providers/listings/:OFFICE_ID/price_plans.json";				//	物件の料金プランの取得
	public  static  final		String						URL_PROVIDERS_ROOM_AVAILABLE	= URL_BASE + "/api/v2/providers/listings/:OFFICE_ID/room_calendars/available.json";//	物件の料金計算

	public  static  final		String						URL_PROVIDER_USERS_SMS		= URL_BASE + "/api/v2/provider_users/sign_in.json";									//	SMS			SMS送信

	public  static  final		String						URL_GUIDE_USER					= URL_BASE + "/api/v2/guide/user.json";													//	利用ガイド	利用者向けガイドの取得

	public  static  final		String						URL_PROVIDER_USER_SIGNIN		= URL_BASE + "/api/v2/provider_users/sign_in.json";									//	掲載者認証		ID・パスワード認証
	public  static  final		String						URL_PROVIDER_USER_SIGNOUT	= URL_BASE + "/api/v2/provider_users/sign_out.json";									//	掲載者認証		ログアウト

	public  static  final		String						URL_USERS_IDMS_REG			= URL_BASE + "/api/v2/users/idms/registration.json";									//	利用者のIDm登録・削除	IDm登録
	public  static  final		String						URL_USERS_IDMS_DEL			= URL_BASE + "/api/v2/users/idms.json";													//	利用者のIDm登録・削除	IDm削除

	public  static  final		String						URL_USERS_SIGNIN				= URL_BASE + "/api/v2/users/sign_in.json";												//	利用者(user)認証	ID・パスワード認証
	public  static  final		String						URL_USERS_IDMS_AUTH			= URL_BASE + "/api/v2/users/idms/auth.json";											//	利用者(user)認証	IDｍ認証
	public  static  final		String						URL_USERS_QR_AUTH				= URL_BASE + "/api/v2/users/qr/auth.json";												//	利用者(user)認証	QRコード認証
	public  static  final		String						URL_USERS_SIGNUP				= URL_BASE + "/api/v2/users/sign_up.json";												//	利用者登録・変更	利用者登録(ID・パスワード認証)
	public  static  final		String						URL_USERS						= URL_BASE + "/api/v2/users.json";														//	利用者登録・変更	利用者情報の取得
	public  static  final		String						URL_USERS_CARD					= URL_BASE + "/api/v2/users/card.json";													//	決済情報登録	クレジットカード登録
	public  static  final		String						URL_USERS_BILLING				= URL_BASE + "/api/v2/users/billing_destination.json";								//	決済情報登録	請求先登録

	public  static  final		String						URL_USERS_PRE_BOOKING_REG	= URL_BASE + "/api/v2/users/pre_bookings.json";										//	予約登録	予約登録
	public  static  final		String						URL_USERS_PRE_BOOKING_INFO	= URL_BASE + "/api/v2/users/pre_bookings/:ID.json";									//	利用者用	予約の詳細情報の取得
	public  static  final		String						URL_USERS_LISTING				= URL_BASE + "/api/v2/users/listing/<listing_id>/coupon/<coupon_code>.json";		//	予約登録	クーポン情報
	public  static  final		String						URL_USERS_PAYMENTS			= URL_BASE + "/api/v2/users/payment_means.json";										//	予約登録	決済方法一覧

	public  static  final		String						URL_SEND_SMS					= URL_BASE + "/api/v2/sms/entry_app.json";												//	SMS API		SMS送信

	public   static  final	String						GOOGLEAPIS_MAPS_GEOCODE		= "https://maps.googleapis.com/maps/api/geocode/xml";
}