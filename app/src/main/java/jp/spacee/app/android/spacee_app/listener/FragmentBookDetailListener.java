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
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentBookDetailListener  implements  FragmentBookDetail.FragmentInteractionListener
{
	private						RelativeLayout 				errLayout			= null;
	private						TextView					title				= null;
	private						TextView					content				= null;
	private						ImageView					msgOff				= null;

	private						String						id					= "";
	private						Bitmap[]					thumbnails			= new android.graphics.Bitmap[1];


	public  FragmentBookDetailListener(int prmId)
	{
		id = String.format("%d",prmId);

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
		TextView	usage		= (TextView)	view.findViewById(R.id.usage);
		ImageView	thumbnail	= (ImageView)	view.findViewById(R.id.thumbnail);

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveBookingInfo(id);
		if (result != null)
		{
			try
			{
				JSONObject obj1 = new JSONObject(result);
				if (obj1 != null)
				{
//					String	rc = obj1.getString("status");
//					if (rc.equals("ok"))
//					{
						// FIXME: ここは配列では無くlistingの連想配列です
						JSONArray arr1 = obj1.getJSONArray("pre_bookings");
						if (arr1 != null)
						{
							for (i=0; i<arr1.length(); i++)
							{
								JSONObject obj2 = arr1.getJSONObject(i);
								JSONObject obj3 = obj2.getJSONObject("listing");
								if (id == obj3.getString("id"))
								{
									JSONObject obj4 = new JSONObject(obj3.getString("thumb"));
									thumb_url[0] = obj4.getString("url");
									spaceName.setText(obj3.getString("subtitle"));
									org.json.JSONObject obj5 = obj2.getJSONObject("pre_booking");
									try
									{
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
										Date wDate = sdf.parse(obj5.getString("start_at"));
										timeBegin.setText(new SimpleDateFormat("HH:mm").format(wDate));
										wDate =  sdf.parse(obj5.getString("end_at"));
										timeEnd.setText(new SimpleDateFormat("HH:mm").format(wDate));
									}
									catch (java.text.ParseException e)
									{
										e.printStackTrace();
										return;
									}
									usage.setText("");
								}
							}
						}
						else
						{
							showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null, "");
							return;
						}
//					}
//					else
//					{
//						showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj1, "");
//						return;
//					}
				}
				else
				{
					showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null, "");
					return;
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
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			return;
		}
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  showErrorMsg(String ttl, JSONObject jsonObj, String orgMsg)
	{
		int		i;
		String	errMsg;

		if (jsonObj != null)
		{
			try
			{
				org.json.JSONArray arr1 = jsonObj.getJSONArray("error_messages");
				errMsg = "";
				for (i=0; i<arr1.length(); i++)
				{
					errMsg += (arr1.getString(i) + "\n");
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
			if (orgMsg.equals("") == false)
					errMsg = orgMsg;
			else	errMsg = ReceiptTabApplication.AppContext.getResources().getString(R.string.error_msg_common2);
		}

		errLayout.setVisibility(View.VISIBLE);
		title.setText(ttl);
		content.setText(errMsg);

		jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =true;

		msgOff.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =false;

				android.os.Message msg = new android.os.Message();
				msg.what = SpaceeAppMain.MSG_HOME_CLICKED;
				msg.arg1 = 2;									//	id/pw ng
				SpaceeAppMain.mMsgHandler.sendMessage(msg);
			}
		});

		//	メッセージの下のエレメントをタップしても拾わないようにするため
		errLayout.setOnClickListener(new android.view.View.OnClickListener()
		{
			@Override
			public void onClick(android.view.View v)
			{
			}
		});
	}
}

