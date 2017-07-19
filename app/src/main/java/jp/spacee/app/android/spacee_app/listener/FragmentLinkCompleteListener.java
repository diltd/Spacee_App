package jp.spacee.app.android.spacee_app.listener;


import android.view.View;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentLinkComplete;


public  class  FragmentLinkCompleteListener  implements  FragmentLinkComplete.FragmentInteractionListener
{
	public  FragmentLinkCompleteListener()
	{
	}


	@Override
	public  void  onBtnGoPaymentClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ACC_LINK_COMP;
		msg.arg1 = 1;											//	by GoPayment
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}
};
