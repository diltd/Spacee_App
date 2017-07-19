package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentEntryInvoice;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentEntryInvoiceListener  implements  FragmentEntryInvoice.FragmentInteractionListener
{
	private 						boolean[]			fieldFilled		= new boolean[6];
	private							TextView			btnInputComp		= null;


	public  FragmentEntryInvoiceListener()
	{
		if (ReceiptTabApplication.userRegData != null)
		{
			ReceiptTabApplication.userRegData.paymentKind = 2;					//	by Invoice
		}
	}


	@Override
	public  void  onKindCardSelected(View view)
	{
		ReceiptTabApplication.userRegData.companyName		= ((TextView) view.findViewById(jp.spacee.app.android.spacee_app.R.id.companyName)).getText().toString();
		ReceiptTabApplication.userRegData.operName			= ((TextView) view.findViewById(jp.spacee.app.android.spacee_app.R.id.staffName)).getText().toString();
		ReceiptTabApplication.userRegData.postCode			= ((TextView) view.findViewById(jp.spacee.app.android.spacee_app.R.id.postCode)).getText().toString();
		ReceiptTabApplication.userRegData.companyAddress1	= ((TextView) view.findViewById(jp.spacee.app.android.spacee_app.R.id.companyAddr1)).getText().toString();
		ReceiptTabApplication.userRegData.companyAddress2	= ((TextView) view.findViewById(jp.spacee.app.android.spacee_app.R.id.companyAddr2)).getText().toString();
		ReceiptTabApplication.userRegData.companyPhoneNo	= ((TextView) view.findViewById(jp.spacee.app.android.spacee_app.R.id.companyPhone)).getText().toString();

		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_INVOICE_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnInputCompClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_INVOICE_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
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
			&& (fieldFilled[3] == true)
			&& (fieldFilled[4] == true)
			&& (fieldFilled[5] == true) )
		{
			btnInputComp.setBackground(jp.spacee.app.android.spacee_app.ReceiptTabApplication.AppContext.getResources().getDrawable(jp.spacee.app.android.spacee_app.R.drawable.shape_button_blue));
			btnInputComp.setClickable(true);
			btnInputComp.setEnabled(true);
		}
		else
		{
			btnInputComp.setBackground(jp.spacee.app.android.spacee_app.ReceiptTabApplication.AppContext.getResources().getDrawable(jp.spacee.app.android.spacee_app.R.drawable.shape_button_gray));
			btnInputComp.setClickable(false);
			btnInputComp.setEnabled(false);
		}
	}
};
