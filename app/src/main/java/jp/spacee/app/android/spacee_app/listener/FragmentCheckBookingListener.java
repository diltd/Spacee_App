package jp.spacee.app.android.spacee_app.listener;


import android.view.View;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentCheckBooking;


public  class  FragmentCheckBookingListener  implements  FragmentCheckBooking.FragmentInteractionListener
{
	public  FragmentCheckBookingListener()
	{
	}


	@Override
	public  void  onBtnLoginICClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_CHECK_BOOKING_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLoginQRClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_CHECK_BOOKING_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLoginPWClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_CHECK_BOOKING_COMP;
		msg.arg1 = 3;											//	by LoginPWClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnRegByAppClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_CHECK_BOOKING_COMP;
		msg.arg1 = 4;											//	by RegByAppClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnRegByInputClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_CHECK_BOOKING_COMP;
		msg.arg1 = 5;											//	by RegByInputClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}
}


