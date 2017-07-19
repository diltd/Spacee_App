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
	public  FragmentAccountLinkListener()
	{
	}


	@Override
	public  void  onBtnDoLinkClicked(View view, RelativeLayout errLayout)
	{
		String		status;

		String  result = SpaceeAppMain.httpCommGlueRoutines.registerIDm(ReceiptTabApplication.currentUserIdm);
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				status	= obj1.getString("status");
				jp.spacee.app.android.spacee_app.ReceiptTabApplication.userAuthToken = obj1.getString("auth_token");

				if (status.equals("ok"))
				{
					android.os.Message msg = new android.os.Message();
					msg.what = SpaceeAppMain.MSG_ACC_LINK_START;
					msg.arg1 = 1;											//	by btnDoLink
					SpaceeAppMain.mMsgHandler.sendMessage(msg);
				}
				else
				{
					//	エラーを表示する


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
			TextView title		= (TextView)	errLayout.findViewById(R.id.errorTitle);
			TextView content	= (TextView)	errLayout.findViewById(R.id.errorMessage);
			ImageView msgOff	= (ImageView)	errLayout.findViewById(R.id.messageOff);

			errLayout.setVisibility(View.VISIBLE);
			title.setText("連携エラー");
			content.setText("連携に失敗しました");

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


	@Override
	public  void  onBtnCancelClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ACC_LINK_START;
		msg.arg1 = 2;											//	by Cancel
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}
};