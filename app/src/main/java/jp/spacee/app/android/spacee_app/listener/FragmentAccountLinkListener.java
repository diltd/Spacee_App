package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.os.Message;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentAccountLink;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentAccountLinkListener  implements  FragmentAccountLink.FragmentInteractionListener
{

	private							RelativeLayout 		errLayout			= null;
	private							TextView			title				= null;
	private							TextView			content				= null;
	private							ImageView			msgOff				= null;


	public  FragmentAccountLinkListener()
	{
	}


	@Override
	public  void  onBtnDoLinkClicked(View view)
	{
		String		status;

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		String  result = SpaceeAppMain.httpCommGlueRoutines.registerIDm(ReceiptTabApplication.currentUserIdm);
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				if (obj1 != null)
				{
					String	rc = obj1.getString("status");
					if (rc.equals("ok"))
					{
						ReceiptTabApplication.userAuthToken = obj1.getString("auth_token");

						android.os.Message msg = new android.os.Message();
						msg.what = SpaceeAppMain.MSG_ACC_LINK_START;
						msg.arg1 = 1;											//	by btnDoLink
						SpaceeAppMain.mMsgHandler.sendMessage(msg);
					}
					else
					{
						showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj1, "");
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


	@Override
	public  void  onBtnCancelClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ACC_LINK_START;
		msg.arg1 = 2;											//	by Cancel
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  setUserName(View view)
	{
		TextView	tv	= (TextView)	view.findViewById(R.id.userName);
		if (ReceiptTabApplication.userRegData != null)
		{
			String wStr = ReceiptTabApplication.userRegData.nameFamily + " "
						+ ReceiptTabApplication.userRegData.nameGiven;
			tv.setText(wStr);

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

		ReceiptTabApplication.isMsgShown =true;

		msgOff.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ReceiptTabApplication.isMsgShown =false;

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