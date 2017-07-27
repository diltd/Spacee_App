package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentOrderComplete;
import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentOrderCompleteListener  implements  FragmentOrderComplete.FragmentInteractionListener
{
	private							RelativeLayout 			errLayout			= null;
	private							TextView				title				= null;
	private							TextView				content				= null;
	private							ImageView				msgOff				= null;

	private							Bitmap[]				roomThumnails		= new android.graphics.Bitmap[1];

	private							String					pre_booking_id		= "";


	public  FragmentOrderCompleteListener(int id)
	{
		pre_booking_id = String.format("%d", id);
	}


	@Override
	public  void  onBtnFloorGuideClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ORDER_COMP_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnCompleteClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ORDER_COMP_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onListProcess(View view)
	{
		String[]	thumb_url = new String[1];

		//	各フィールドを設定する
		TextView	name		= (TextView)	view.findViewById(R.id.areaName);
		TextView	tgtMonth	= (TextView)	view.findViewById(R.id.targetMonth);
		TextView	tgtDay		= (TextView)	view.findViewById(R.id.targetDay);
		TextView	checkIn		= (TextView)	view.findViewById(R.id.checkInTime);
		TextView	checkOut	= (TextView)	view.findViewById(R.id.checkOutTime);
		TextView	amount		= (TextView)	view.findViewById(R.id.amount);
		TextView	explain		= (TextView)	view.findViewById(R.id.explain);
		ImageView	picture		= (ImageView)	view.findViewById(R.id.picture);

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		name.setText(ReceiptTabApplication.bookingRoomData.namePlace);
		tgtMonth.setText(String.format("%02d", ReceiptTabApplication.bookingRoomData.useMonth));
		tgtDay.setText(String.format("%02d", ReceiptTabApplication.bookingRoomData.useDay));
		checkIn.setText(ReceiptTabApplication.bookingRoomData.checkInTime);
		checkOut.setText(ReceiptTabApplication.bookingRoomData.checkOutTime);
		amount.setText(String.format("%,d", ReceiptTabApplication.bookingRoomData.payAmount));
		roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(ReceiptTabApplication.bookingRoomData.pictUrl);
		picture.setImageBitmap(roomThumnails[0]);

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveBookingInfo(pre_booking_id);
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				if (obj1 != null)
				{
					//					String	rc = obj1.getString("status");
					//					if (rc.equals("ok"))
					//					{
					org.json.JSONObject obj2 = obj1.getJSONObject("listing");
					org.json.JSONObject obj3 = obj1.getJSONObject("pre_booking");
					if (Integer.parseInt(pre_booking_id) == obj3.getInt("id"))
					{
						org.json.JSONObject obj4 = new org.json.JSONObject(obj2.getString("thumb"));
						thumb_url[0] = obj4.getString("url");
//						spaceName.setText(obj2.getString("subtitle"));
						explain.setText(obj2.getString("usage"));
/*	OrderConfirmの情報を使う
						try
						{
							java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							java.util.Date wDate = sdf.parse(obj3.getString("start_at"));
							timeBegin.setText(new java.text.SimpleDateFormat("HH:mm").format(wDate));
							wDate =  sdf.parse(obj3.getString("end_at"));
							timeEnd.setText(new java.text.SimpleDateFormat("HH:mm").format(wDate));
						}
						catch (java.text.ParseException e)
						{
							e.printStackTrace();
							return;
						}
*/
						roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(thumb_url);
						picture.setImageBitmap(roomThumnails[0]);
					}
					else
					{
					showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null, "");
					return;
					}
				}
				else
				{
					showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null, "");
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
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			return;
		}
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  showErrorMsg(String ttl, org.json.JSONObject jsonObj, String orgMsg)
	{
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
			if (orgMsg.equals("") == false)
				errMsg = orgMsg;
			else	errMsg = ReceiptTabApplication.AppContext.getResources().getString(R.string.error_msg_common2);
		}

		errLayout.setVisibility(View.VISIBLE);
		title.setText(ttl);
		content.setText(errMsg);

		jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =true;

		msgOff.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =false;

				android.os.Message msg = new android.os.Message();
				msg.what = SpaceeAppMain.MSG_HOME_CLICKED;
				msg.arg1 = 2;									//	id/pw ng
				SpaceeAppMain.mMsgHandler.sendMessage(msg);
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
	}
}
