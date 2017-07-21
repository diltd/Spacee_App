package jp.spacee.app.android.spacee_app.listener;


import android.os.Message;
import android.view.View;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentAppMain;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentAppMainListener  implements  FragmentAppMain.FragmentInteractionListener
{
	private							String				numDesk			= "0";
	private							String				numSpace		= "0";
	private							String				mapUrl			= "";

	private							RelativeLayout		errLayout		= null;
	private							TextView			title			= null;
	private							TextView			content			= null;
	private 						ImageView			msgOff			= null;

	private 						int					cntLogoTapped	= 0;


	public FragmentAppMainListener()
	{
	}


	@Override
	public  void  initAppMain(View view)
	{
		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		if (ReceiptTabApplication.providerId.equals("") == false)
		{
			retrieveOfficeInfo();

			redrawPanel(view);
		}
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
				if (obj1 != null)
				{
//					String	rc = obj1.getString("status");
//					if (rc.equals("ok"))
//					{
						obj1 = new JSONObject(result);
						obj2 = obj1.getJSONObject("office");
						obj3 = obj2.getJSONObject("desk_info");
						numDesk  = obj3.getString("available");
						obj3 = obj2.getJSONObject("space_info");
						numSpace = obj3.getString("available");
						obj3 = obj2.getJSONObject("floor_maps");
						obj4 = obj3.getJSONObject("map");
						mapUrl	  = obj4.getString("image");
//					}
//					else
//					{
//						showErrorMsg("エラー", obj1, "");
//						return;
//					}
				}
				else
				{
					showErrorMsg("エラー", null, "");
					return;
				}
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
		else
		{
			showErrorMsg("通信エラー", null, "");
			return;
		}
	}


	private  void  redrawPanel(View view)
	{
		android.widget.TextView tv1 = (android.widget.TextView) view.findViewById(R.id.textInfo1);
		android.widget.TextView tv2 = (android.widget.TextView) view.findViewById(R.id.textInfo2);

		tv1.setText(numDesk + ReceiptTabApplication.AppContext.getString(R.string.frag_appmain_num_desk));
		if (Integer.parseInt(numDesk) > 0)
				tv1.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.text_black));
		else	tv1.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.alert));

		tv2.setText(numSpace + ReceiptTabApplication.AppContext.getString(R.string.frag_appmain_num_room));
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


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  showErrorMsg(String ttl, JSONObject jsonObj, String orgMsg)
	{
		int		i;
		String	errMsg;

		if (jsonObj != null)
		{
			try
			{
				JSONArray arr1 = jsonObj.getJSONArray("error_messages");
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
			else	errMsg = "データが取得できませんでした";
		}

		errLayout.setVisibility(View.VISIBLE);
		title.setText(ttl);
		content.setText(errMsg);

		ReceiptTabApplication.isMsgShown =true;

		msgOff.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ReceiptTabApplication.isMsgShown =false;

				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HOME_CLICKED;
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