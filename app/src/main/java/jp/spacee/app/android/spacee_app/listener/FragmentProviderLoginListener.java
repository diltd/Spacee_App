package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import org.json.JSONObject;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentProviderLogin;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentProviderLoginListener  implements  FragmentProviderLogin.FragmentInteractionListener
{
	private 						boolean[]			fieldFilled		= new boolean[2];
	private							TextView			btnLoginPW			= null;


	public  FragmentProviderLoginListener()
	{
	}


	@Override
	public  void  onBtnLoginPWClicked(View view)
	{
		String	status;

		String	id	= ((EditText) view.findViewById(R.id.editID)).getText().toString();
		String	pw	= ((EditText) view.findViewById(R.id.editPW)).getText().toString();

		boolean  rc = SpaceeAppMain.httpCommGlueRoutines.providerUserSignin(id, pw);
		if (rc == true)
		{
			ReceiptTabApplication.providerId = id;
			ReceiptTabApplication.providerPw = pw;

			android.os.Message msg = new android.os.Message();
			msg.what = SpaceeAppMain.MSG_PROVIDER_LOGIN_COMP;
			msg.arg1 = 1;									//	id/pw ok
			SpaceeAppMain.mMsgHandler.sendMessage(msg);
		}
		else
		{
			android.os.Message msg = new android.os.Message();
			msg.what = SpaceeAppMain.MSG_PROVIDER_LOGIN_COMP;
			msg.arg1 = 2;									//	id/pw ng
			SpaceeAppMain.mMsgHandler.sendMessage(msg);
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
