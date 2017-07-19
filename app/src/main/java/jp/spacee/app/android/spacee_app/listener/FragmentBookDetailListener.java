package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentBookDetail;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentBookDetailListener  implements  FragmentBookDetail.FragmentInteractionListener
{
	private						int								id				= -1;
	private						Bitmap[]						thumbnails		= new android.graphics.Bitmap[1];


	public  FragmentBookDetailListener(int prmId)
	{
		id = prmId;

	}


	@Override
	public  void  onBtnFloorGuideClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_BOOK_DETAIL_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLogout1Clicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_BOOK_DETAIL_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLogout2Clicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_BOOK_DETAIL_COMP;
		msg.arg1 = 3;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void retrieveDetaildata(View view)
	{
		int		i, k;
		String[]	thumb_url = new String[1];

		TextView	spaceName	= (TextView)	view.findViewById(R.id.spaceName);
		TextView	timeBegin	= (TextView)	view.findViewById(R.id.timeBegin);
		TextView	timeEnd		= (TextView)	view.findViewById(R.id.timeEnd);
		ImageView	thumbnail	= (ImageView)	view.findViewById(R.id.thumbnail);


		RelativeLayout errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		TextView	title			= (TextView)		errLayout.findViewById(R.id.errorTitle);
		TextView	content			= (TextView)		errLayout.findViewById(R.id.errorMessage);
		ImageView	msgOff			= (ImageView)		errLayout.findViewById(R.id.messageOff);

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveBookingList("present");
		if (result != null)
		{
			try
			{
				JSONObject obj1 = new JSONObject(result);
				JSONArray arr1 = obj1.getJSONArray("pre_bookings");
				if (arr1 != null)
				{
					for (i=0; i<arr1.length(); i++)
					{
						JSONObject obj2 = arr1.getJSONObject(i);
						JSONObject obj3 = obj2.getJSONObject("listing");
						if (id == obj3.getInt("id"))
						{
							JSONObject obj4 = new JSONObject(obj3.getString("thumb"));
							thumb_url[0] = obj4.getString("url");
							spaceName.setText(obj3.getString("subtitle"));
							org.json.JSONObject obj5 = obj2.getJSONObject("pre_booking");
							try
							{
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
								Date wDate = sdf.parse(obj5.getString("start_at"));
								timeBegin.setText(new SimpleDateFormat("hh:mm").format(wDate));
								wDate =  sdf.parse(obj5.getString("end_at"));
								timeEnd.setText(new SimpleDateFormat("hh:mm").format(wDate));
							}
							catch (java.text.ParseException e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return;
			}

			thumbnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(thumb_url);
			thumbnail.setImageBitmap(thumbnails[0]);
		}
		else
		{
			errLayout.setVisibility(View.VISIBLE);
			title.setText("通信エラー");
			content.setText("データがありません");

			jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =true;

			msgOff.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =false;

					android.os.Message msg = new android.os.Message();
					msg.what = SpaceeAppMain.MSG_HOME_CLICKED;
					SpaceeAppMain.mMsgHandler.sendMessage(msg);
				}
			});
		}
	}
}


