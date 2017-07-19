package jp.spacee.app.android.spacee_app.listener;


import android.view.View;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentTabletPortrait;


public  class  FragmentTabletPortraitListener  implements  FragmentTabletPortrait.FragmentInteractionListener
{
	public  FragmentTabletPortraitListener()
	{
	}


	@Override
	public  void  onBtnLoginICClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_TABLET_PORTRAIT_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLoginQRClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_TABLET_PORTRAIT_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLoginPWClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_TABLET_PORTRAIT_COMP;
		msg.arg1 = 3;											//	by LoginPWClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}
}


