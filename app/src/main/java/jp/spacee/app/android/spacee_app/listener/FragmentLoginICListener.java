package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.os.Message;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentLoginIC;
import jp.spacee.app.android.spacee_app.IICCReaderService;
import jp.spacee.app.android.spacee_app.IICCReaderServiceCallback;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentLoginICListener  implements  FragmentLoginIC.FragmentInteractionListener
{
	private						IICCReaderService			mIICCReaderService	= null;

	private  static  final	int							RC_OK					= 0;


	public  FragmentLoginICListener()
	{
	}


	@Override
	public  void  onBtnLoginQRClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_LOGIN_IC_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLoginPWClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_LOGIN_IC_COMP;
		msg.arg1 = 3;											//	by LoginPWClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onReadICC()
	{
		ReceiptTabApplication.currentUserIdm = "";				//	reset

		try
		{
			if (SpaceeAppMain.mIICCReaderService.ICCOpenReader() == RC_OK)		//	ICCをopenする
			{
				//	後はSpaceeAppMain内のCallback#ICCR_ReadDataにデータが返ってくる・・・
				//	この処理内でbindして処理する方が美しいが・・・
			}
			else
			{
				//	error ??
			}
		}
		catch (android.os.RemoteException e)
		{
			e.printStackTrace();
		}
	}
}