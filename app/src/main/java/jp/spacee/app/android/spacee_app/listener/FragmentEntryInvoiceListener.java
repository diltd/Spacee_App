package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentEntryInvoice;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


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
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_INVOICE_COMP;
		msg.arg1 = 1;
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnInputCompClicked(View view)
	{
		ReceiptTabApplication.userRegData.companyName		= ((TextView) view.findViewById(R.id.companyName)).getText().toString();
		ReceiptTabApplication.userRegData.operName			= ((TextView) view.findViewById(R.id.staffName)).getText().toString();
		ReceiptTabApplication.userRegData.postCode			= ((TextView) view.findViewById(R.id.postCode)).getText().toString();
		ReceiptTabApplication.userRegData.companyAddress1	= ((TextView) view.findViewById(R.id.companyAddr1)).getText().toString();
		ReceiptTabApplication.userRegData.companyAddress2	= ((TextView) view.findViewById(R.id.companyAddr2)).getText().toString();
		ReceiptTabApplication.userRegData.companyPhoneNo	= ((TextView) view.findViewById(R.id.companyPhone)).getText().toString();

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
	public  void  killFocusPostCode(View view)
	{
		EditText	pCode = (EditText)	view.findViewById(R.id.postCode);
		EditText	addr1 = (EditText)	view.findViewById(R.id.companyAddr1);
		String		pcode = pCode.getText().toString();
		String[]	addr  = new String[10];
		String		wStr1, wStr2;
		int			pos, idx;

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveAddrfromPostCode(pcode);
		if (result != null)
		{
			pos = 0;
			wStr1 = parseParam(result, "status" , pos);
			if (wStr1.equals("OK"))
			{
				idx = 0;
				while (wStr1.equals("") != true)
				{
					wStr1 = parseParam(result, "address_component" , pos);
					if (wStr1.equals("") != true)
					{
						addr[idx++] = parseParam(wStr1, "long_name" , 0);
					}
					pos += (wStr1.length() + 19 + 20);			//	19:len of <address_component>  20:</  >
				}

				wStr2 = "";
				idx --;
				if (addr[idx].equals("日本"))
				{
					idx --;
					while (idx >= 2)			//	addr[1]=addr[0]=postcode
					{
						wStr2 += addr[idx--];
					}
				}
				addr1.setText(wStr2);
			}
			else
			{

			}
		}
		else
		{

		}
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


	private  String	  parseParam(String inPrm, String keycode, int stPos)
	{
		String	outStr	= "";
		String	keyBgn	= "<"  + keycode + ">";
		String	keyEnd	= "</" + keycode + ">";
		int		idx		= stPos;
		int		bPos, ePos;

		if ((bPos = inPrm.indexOf(keyBgn, idx)) != -1)
		{
			if ((ePos = inPrm.indexOf(keyEnd, bPos+keyBgn.length())) != -1)
			{
				outStr = inPrm.substring(bPos + keyBgn.length(), ePos);
			}
		}

		return	outStr;
	}
}

