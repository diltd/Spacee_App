package jp.spacee.app.android.spacee_app.listener;


import android.os.Message;
import android.view.View;
import android.graphics.Bitmap;
import android.widget.TextView;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentAppMain;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentAppMainListener  implements  FragmentAppMain.FragmentInteractionListener
{
	private							String			numDesk			= "0";
	private							String			numSpace		= "0";
	private							String			mapUrl			= "";

	private 						int				cntLogoTapped	= 0;


	public FragmentAppMainListener()
	{
	}


	@Override
	public  void  initAppMain(View view)
	{
		if (ReceiptTabApplication.providerId.equals("") == false)
		{
			retrieveOfficeInfo();

			redrawPanel(view);		}
		else
		{
			Message msg = new Message();
			msg.what = SpaceeAppMain.MSG_START_PROVIDER_LOGIN;
			SpaceeAppMain.mMsgHandler.sendMessage(msg);
		}
	}


	private  void  retrieveOfficeInfo()
	{
		int			i, k;
		JSONObject	obj1, obj2, obj3, obj4;

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveOfficeList();
		if (result != null)
		{
			try
			{
				obj1 = new JSONObject(result);
				obj2 = obj1.getJSONObject("office");
				obj3 = obj2.getJSONObject("desk_info");
				numDesk  = obj3.getString("available");
				obj3 = obj2.getJSONObject("space_info");
				numSpace = obj3.getString("available");
				obj3 = obj2.getJSONObject("floor_maps");
				obj4 = obj3.getJSONObject("map");
				mapUrl	  = obj4.getString("image");
			}
			catch (JSONException e)
			{
				e.printStackTrace();
				return;
			}

			if (SpaceeAppMain.FloorMap == null)
			{
				String[]	urls = new String[1];
				urls[0] = mapUrl;
				Bitmap[] bmp = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(urls);
				SpaceeAppMain.FloorMap = bmp[0];
			}
		}
	}


	private  void  redrawPanel(View view)
	{
		TextView	tv1 = (TextView) view.findViewById(R.id.textInfo1);
		TextView	tv2 = (TextView) view.findViewById(R.id.textInfo2);

		tv1.setText(numDesk + "人分");
		if (Integer.parseInt(numDesk) > 0)
				tv1.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.text_black));
		else	tv1.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.alert));

		tv2.setText(numSpace + "部屋");
		if (Integer.parseInt(numSpace) > 0)
				tv2.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.text_black));
		else	tv2.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.alert));
	}


	@Override
	public void onBtnWorkSpaceClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_APP_MAIN_COMP;
		msg.arg1 = 1;										//	by WorkSpaceClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void onBtnMeetingRoomClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_APP_MAIN_COMP;
		msg.arg1 = 2;										//	by MeetingRoomClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void onBtnCheckBookingClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_APP_MAIN_COMP;
		msg.arg1 = 3;										//	by CheckBookingClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void onBtnRuleGuideClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_APP_MAIN_COMP;
		msg.arg1 = 4;										//	by RuleGuideClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void onBtnFloorGuideClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_APP_MAIN_COMP;
		msg.arg1 = 5;										//	by FloorGuideClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void onImgLogoClicked(View view)
	{
		cntLogoTapped ++;
		if (cntLogoTapped >= 3)
		{
		Message msg = new Message();
			msg.what = SpaceeAppMain.MSG_APP_MAIN_COMP;
			msg.arg1 = 6;										//	by ImgLogoClicked(triple)
			SpaceeAppMain.mMsgHandler.sendMessage(msg);

			cntLogoTapped= 0;					//	reset	本来、90_ProviderLoginへ移行するので必要ないが
		}
	}
}