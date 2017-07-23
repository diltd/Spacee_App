package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.os.Message;

import org.json.JSONObject;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentLoginPW;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentLoginPWListener  implements  FragmentLoginPW.FragmentInteractionListener
{
	private 						boolean[]			fieldFilled		= new boolean[2];
	private							TextView			btnLoginPW			= null;

	private							RelativeLayout		errLayout			= null;
	private							TextView			title				= null;
	private							TextView			content				= null;
	private							ImageView			msgOff				= null;


	public  FragmentLoginPWListener()
	{
	}


	@Override
	public  void  onBtnLoginPWClicked(View view)
	{
		String	status;

		String	id	= ((EditText) view.findViewById(R.id.editID)).getText().toString();
		String	pw	= ((EditText) view.findViewById(R.id.editPW)).getText().toString();

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);


		String  result = SpaceeAppMain.httpCommGlueRoutines.signinUser(id, pw);
		if (result != null)
		{
			try
			{
				JSONObject obj1 = new JSONObject(result);
				if (obj1 != null)
				{
					String	rc = obj1.getString("status");
					if (rc.equals("ok"))
					{
						ReceiptTabApplication.userAuthToken		= obj1.getString("auth_token");
						ReceiptTabApplication.currentUserMAddr	= obj1.getString("email");
//						ReceiptTabApplication.currentUserName	= obj1.getString("xxxxx");

						android.os.Message msg = new android.os.Message();
						msg.what = SpaceeAppMain.MSG_LOGIN_PW_COMP;
						msg.arg1 = 5;									//	by RegByInputClicked
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
	public  void  setBtnLoginPWView(View view)
	{
		btnLoginPW = (TextView) view;
	}


	@Override
	public  void  setInputStatus(int eno, boolean sts)
	{
		fieldFilled[eno] = sts;

		if	(  (fieldFilled[0] == true)
			&& (fieldFilled[1] == true) )
		{
			btnLoginPW.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_blue));
			btnLoginPW.setClickable(true);
			btnLoginPW.setEnabled(true);
		}
		else
		{
			btnLoginPW.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_gray));
			btnLoginPW.setClickable(false);
			btnLoginPW.setEnabled(false);
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
