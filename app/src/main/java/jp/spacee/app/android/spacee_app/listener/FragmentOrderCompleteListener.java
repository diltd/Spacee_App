package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentOrderComplete;
import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentOrderCompleteListener  implements  FragmentOrderComplete.FragmentInteractionListener
{
	private							Bitmap[]				roomThumnails		= new android.graphics.Bitmap[1];


	public  FragmentOrderCompleteListener()
	{
	}


	@Override
	public  void  onBtnFloorGuideClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ORDER_COMP_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnCompleteClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_ORDER_COMP_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onListProcess(View view)
	{
		//	各フィールドを設定する
		TextView	name		= (TextView)	view.findViewById(R.id.areaName);
		TextView	tgtMonth	= (TextView)	view.findViewById(R.id.targetMonth);
		TextView	tgtDay		= (TextView)	view.findViewById(R.id.targetDay);
		TextView	checkIn		= (TextView)	view.findViewById(R.id.checkInTime);
		TextView	checkOut	= (TextView)	view.findViewById(R.id.checkOutTime);
		TextView	amount		= (TextView)	view.findViewById(R.id.amount);
		ImageView	picture		= (ImageView)	view.findViewById(R.id.picture);

		name.setText(ReceiptTabApplication.bookingRoomData.namePlace);
		tgtMonth.setText(String.format("%02d", ReceiptTabApplication.bookingRoomData.useMonth));
		tgtDay.setText(String.format("%02d", ReceiptTabApplication.bookingRoomData.useDay));
		checkIn.setText(ReceiptTabApplication.bookingRoomData.checkInTime);
		checkOut.setText(ReceiptTabApplication.bookingRoomData.checkOutTime);
		amount.setText(String.format("%,d", ReceiptTabApplication.bookingRoomData.payAmount));
		roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(ReceiptTabApplication.bookingRoomData.pictUrl);
		picture.setImageBitmap(roomThumnails[0]);

		//	usageを取込む
		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveBookingInfo(ReceiptTabApplication.bookingRoomData.roomId);
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				if (obj1 != null)
				{
				}
				else
				{
					return;
				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return;
			}
		}
		else
		{
//			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			return;
		}

	}
}