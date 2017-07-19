package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.Calendar;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentEntryCard;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentEntryCardListener  implements  FragmentEntryCard.FragmentInteractionListener
{
	private 						boolean[]			fieldFilled		= new boolean[6];
	private							TextView			btnInputComp		= null;

	public  FragmentEntryCardListener()
	{
		if (ReceiptTabApplication.userRegData != null)
		{
			ReceiptTabApplication.userRegData.paymentKind = 1;					//	by Card
		}
	}


	@Override
	public  void  onKindInvoiceSelected(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_CARD_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnInputCompClicked(View view)
	{
		String	wStr  = ((TextView) view.findViewById(R.id.cardNo1)).getText().toString();
		wStr += ((TextView) view.findViewById(R.id.cardNo2)).getText().toString();
		wStr += ((TextView) view.findViewById(R.id.cardNo3)).getText().toString();
		wStr += ((TextView) view.findViewById(R.id.cardNo4)).getText().toString();
		ReceiptTabApplication.userRegData.cardNo			= wStr;
		ReceiptTabApplication.userRegData.secretCode		= ((TextView) view.findViewById(R.id.cardNo5)).getText().toString();
		ReceiptTabApplication.userRegData.cardHolder		= ((TextView) view.findViewById(R.id.namePsn)).getText().toString();
		ReceiptTabApplication.userRegData.cardExpireYear	= ((Spinner) view.findViewById(R.id.expireYear)).getSelectedItemPosition();
		ReceiptTabApplication.userRegData.cardExpireMonth	= ((Spinner) view.findViewById(R.id.expireMonth)).getSelectedItemPosition();

		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_CARD_COMP;
		msg.arg1 = 2;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  setSpinnerValue(View view1, android.view.View view2)
	{
		int			i;
		String		wStr;

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=1; i<=12; i++)
		{
			wStr = String.format("%02d", i);
			adapter1.add(wStr);
		}
		((Spinner)view1).setAdapter(adapter1);


		Calendar cal = Calendar.getInstance();
		int		year = cal.get(Calendar.YEAR);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=year; i<=year+10; i++)
		{
			wStr = String.format("%04d", i);
			adapter2.add(wStr);
		}
		((Spinner)view2).setAdapter(adapter2);
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
}