package jp.spacee.app.android.spacee_app.listener;


import android.view.View;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentStartLink;


public  class  FragmentStartLinkListener  implements  FragmentStartLink.FragmentInteractionListener
{
	public  FragmentStartLinkListener()
	{
	}


	@Override
	public  void  onWarningMsgClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_START_LINK;
		msg.arg1 = 0;											//	by Message Clicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}

	@Override
	public  void  onBtnLoginQRClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_START_LINK;
		msg.arg1 = 1;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}

	@Override
	public  void  onBtnLoginPWClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_START_LINK;
		msg.arg1 = 2;											//	by LoginPWClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}

	@Override
	public  void  onBtnRegByAppClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_START_LINK;
		msg.arg1 = 3;											//	by RegByAppClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}

	@Override
	public  void  onBtnRegByInputClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_START_LINK;
		msg.arg1 = 4;											//	by RegByInputClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}

	@Override
	public  void  setupWarningMsgOff(final View view)
	{
		new android.os.Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				view.setVisibility(View.INVISIBLE);
			}
		}, 5000);
	}
}