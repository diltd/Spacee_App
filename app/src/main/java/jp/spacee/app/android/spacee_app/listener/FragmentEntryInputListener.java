package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentEntryInput;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.common.UserRegisterData;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentEntryInputListener  implements  FragmentEntryInput.FragmentInteractionListener
{
	private 						boolean[]			fieldFilled		= new boolean[4];
	private							TextView			btnInputComp		= null;

	public  FragmentEntryInputListener()
	{
		ReceiptTabApplication.userRegData = new UserRegisterData();
	}


	@Override
	public  void  onBtnInputCompClicked(View view)
	{
		ReceiptTabApplication.userRegData.nameFamily		= ((TextView) view.findViewById(R.id.nameFamily)).getText().toString();
		ReceiptTabApplication.userRegData.nameGiven		= ((TextView) view.findViewById(R.id.nameGiven)).getText().toString();
		ReceiptTabApplication.userRegData.emailAddress		= ((TextView) view.findViewById(R.id.mailAddress)).getText().toString();
		ReceiptTabApplication.userRegData.password			= ((TextView) view.findViewById(R.id.password)).getText().toString();

		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_INPUT_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  setBtnInputCompView(View view)
	{
		btnInputComp = (TextView) view;
	}


	@Override
	public  void  setInputStatus(int eno, boolean sts)
	{
		fieldFilled[eno] = sts;

		if	(  (fieldFilled[0] == true)
			&& (fieldFilled[1] == true)
			&& (fieldFilled[2] == true)
			&& (fieldFilled[3] == true) )
		{
			btnInputComp.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_blue));
			btnInputComp.setClickable(true);
			btnInputComp.setEnabled(true);
		}
		else
		{
			btnInputComp.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_gray));
			btnInputComp.setClickable(false);
			btnInputComp.setEnabled(false);
		}
	}
};


