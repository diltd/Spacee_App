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


	public  FragmentLoginPWListener()
	{
	}


	@Override
	public  void  onBtnLoginPWClicked(View view)
	{
		String	status;

		String	id	= ((EditText) view.findViewById(R.id.editID)).getText().toString();
		String	pw	= ((EditText) view.findViewById(R.id.editPW)).getText().toString();

		String  result = SpaceeAppMain.httpCommGlueRoutines.signinUser(id, pw);
		if (result != null)
		{
			try
			{
				JSONObject obj1 = new JSONObject(result);
				status	= obj1.getString("status");
				ReceiptTabApplication.userAuthToken = obj1.getString("auth_token");

				if (status.equals("ok"))
				{
					android.os.Message msg = new android.os.Message();
					msg.what = SpaceeAppMain.MSG_LOGIN_PW_COMP;
					msg.arg1 = 5;									//	by RegByInputClicked
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
			final RelativeLayout	errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
			TextView		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
			TextView		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
			ImageView		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

			errLayout.setVisibility(View.VISIBLE);
			title.setText("ログインエラー");
			content.setText("ログインできません\nID/パスワードをご確認ください");

			ReceiptTabApplication.isMsgShown =true;

			msgOff.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ReceiptTabApplication.isMsgShown =false;

					errLayout.setVisibility(View.INVISIBLE);
				}
			});

			//	メッセージの下のエレメントをタップしても拾わないようにするため
			errLayout.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
				}
			});
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
}


