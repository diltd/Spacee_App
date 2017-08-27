package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private						String						name				= "";
	private						int							spaceKind			= 0;
	private						int							spaceId				= 0;
	private						int							status				= 0;
	private						Date						bookDate			= null;
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
		msg.arg1 = 1;											//	フロアガイド
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLogout1Clicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_BOOK_DETAIL_COMP;
		msg.arg1 = 2;											//	ログアウトしてスペースを詳しく見る
		msg.arg2 = spaceKind;									//	1:ワークスペース / 2:ミーティングルーム

		if (msg.arg2 == 1)
		{
			ReceiptTabApplication.currentWorkId		 = spaceId;
			ReceiptTabApplication.currentWorkStatus	 = status;						//	0:時間外/1:時間内（利用可かどうかは各detailでチェックしている）
			ReceiptTabApplication.currentWorkName	 = name;
			msg.obj = name;
		}
		else
		{
			ReceiptTabApplication.currentMeetingId		= spaceId;
			ReceiptTabApplication.currentMeetingStatus	= status;					//	0:時間外/1:時間内（利用可かどうかは各detailでチェックしている）
			ReceiptTabApplication.currentMeetingName	= name;
			msg.obj = name;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(bookDate);
		ReceiptTabApplication.currentWorkDetailYear		= cal.get(Calendar.YEAR);
		ReceiptTabApplication.currentWorkDetailMonth	= cal.get(Calendar.MONTH) + 1;
		ReceiptTabApplication.currentWorkDetailDay		= cal.get(java.util.Calendar.DAY_OF_MONTH);

		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLogout2Clicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_BOOK_DETAIL_COMP;
		msg.arg1 = 3;											//	ログアウトしてホームに戻る
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void retrieveDetaildata(View view)
	{
		int		i, k;
		String[]	thumb_url = new String[1];

		TextView		status1		= (TextView)		view.findViewById(R.id.status1);
		TextView		status2		= (TextView)		view.findViewById(R.id.status2);
		LinearLayout	layoutFG	= (LinearLayout)	view.findViewById(R.id.btnFloorGuide);
		LinearLayout	layoutSts3	= (LinearLayout)	view.findViewById(R.id.layoutStatus3);
		TextView		residual	= (TextView)		view.findViewById(R.id.residual);
		TextView		spaceName	= (TextView)		view.findViewById(R.id.spaceName);
		TextView		timeBegin	= (TextView)		view.findViewById(R.id.timeBegin);
		TextView		timeEnd		= (TextView)		view.findViewById(R.id.timeEnd);
		TextView		usage		= (TextView)		view.findViewById(R.id.usage);
		ImageView		thumbnail	= (ImageView)		view.findViewById(R.id.thumbnail);

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
						JSONObject obj2 = obj1.getJSONObject("listing");
						JSONObject obj3 = obj1.getJSONObject("pre_booking");
						if (Integer.parseInt(id) == obj3.getInt("id"))
						{
							JSONObject obj4 = new JSONObject(obj2.getString("thumb"));
							thumb_url[0] = obj4.getString("url");
							spaceName.setText(obj2.getString("subtitle"));
							name = obj2.getString("subtitle");
							usage.setText(obj2.getString("usage"));
							try
							{
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
								Date bDate = sdf.parse(obj3.getString("start_at"));
								bookDate  = bDate;
								timeBegin.setText(new SimpleDateFormat("HH:mm").format(bDate));
								Date eDate =  sdf.parse(obj3.getString("end_at"));
								timeEnd.setText(new SimpleDateFormat("HH:mm").format(eDate));
								Calendar cal = Calendar.getInstance();
								Date now   = cal.getTime();

///								if (obj2.getString("category").equals("desk"))
								if (obj2.getInt("id") == 2980)
										spaceKind = 1;					// ワークスペース
								else	spaceKind = 2;					// ミーティングルーム
								spaceId = obj2.getInt("id");

								if ((bDate.compareTo(now) <= 0) && (now.compareTo(eDate) <= 0))
								{
									status1.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.xml_book_detail_space_avail));
									status1.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_oval_blue));
									status2.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.xml_book_detail_msg1));
									layoutFG.setVisibility(View.VISIBLE);
									layoutSts3.setVisibility(View.INVISIBLE);

									status = 1;					//	時間内
								}
								else
								{
									status1.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.xml_book_detail_space_not_avail));
									status1.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_oval_gray));
									status2.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.xml_book_detail_msg2));
									layoutFG.setVisibility(View.INVISIBLE);
									layoutSts3.setVisibility(View.VISIBLE);

									long diff = ((long) bDate.getTime() - (long) now.getTime()) / 1000;				//	sec
									residual.setText(String.format("%02d:%02d", diff/60, diff%60));

									status = 0;					//	時間外
								}
							}
							catch (java.text.ParseException e)
							{
								e.printStackTrace();
								return;
							}
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

