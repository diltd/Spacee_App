package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.os.Message;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentMeetingDetail;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentMeetingDetailListener  implements  FragmentMeetingDetail.FragmentInteractionListener
{
	private 					ImageView						imgSpace		= null;
	private 					TextView						capacity		= null;
	private 					TextView						areaSquare		= null;
	private 					TextView						facilities		= null;
	private 					TextView						btnDateList	= null;
	private 					TextView						status			= null;
	private 					TextView						availNo			= null;
	private 					ImageView						timeLine		= null;
	private 					Spinner							startTime		= null;
	private 					Spinner							useHour			= null;
	private 					Spinner							useMin			= null;
	private 					Spinner							numPsn			= null;
	private						ListView						priceList		= null;
	private 					TextView						amount			= null;
	private 					TextView						btnSelect		= null;

	private						RelativeLayout					errLayout		= null;
	private 					TextView						title			= null;
	private 					TextView						content			= null;
	private 					ImageView						msgOff			= null;

	private						String							space_id		= "";

	private						List<HashMap<String, String>>	schedList		= null;

	private						int								avail_No		= 0;
	private						int								minBookUnit	= 0;
	private						int								bookStep		= 0;


	public  FragmentMeetingDetailListener(int id)
	{
		space_id = String.format("%d", id);
	}


	@Override
	public void onBtnDateListClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_MEETING_DETAIL_COMP;
		msg.arg1 = 2;                                            //	by btnDateListClicked
		msg.arg2 = Integer.parseInt(space_id);
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnSelectClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_MEETING_DETAIL_COMP;
		msg.arg1 = 1;											//	by btnSelectClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void retrieveDetaildata(View view)
	{
		int		i, k;
		String	wStr;

		imgSpace		= (ImageView)	view.findViewById(R.id.imgSpace);
		capacity		= (TextView)	view.findViewById(R.id.capacity);
		areaSquare		= (TextView)	view.findViewById(R.id.areaSquare);
		facilities		= (TextView)	view.findViewById(R.id.facilities);
		status			= (TextView)	view.findViewById(R.id.status);
		availNo			= (TextView)	view.findViewById(R.id.availNo);
		timeLine		= (ImageView)	view.findViewById(R.id.timeLine);
		startTime		= (Spinner)		view.findViewById(R.id.startTime);
		useHour			= (Spinner)		view.findViewById(R.id.useHour);
		useMin			= (Spinner)		view.findViewById(R.id.useMin);
		numPsn			= (Spinner)		view.findViewById(R.id.numPsn);
		priceList		= (ListView)	view.findViewById(R.id.priceList);
		amount			= (TextView)	view.findViewById(R.id.amount);

		errLayout			= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		TextView	title	= (TextView)		errLayout.findViewById(R.id.errorTitle);
		TextView	content	= (TextView)		errLayout.findViewById(R.id.errorMessage);
		ImageView	msgOff	= (ImageView)		errLayout.findViewById(R.id.messageOff);


		String[]	detail_thumb_url	= null;
		Bitmap[]	roomThumnails		= null;


		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveOfficeDetail(space_id);
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
						if (obj2 != null)
						{
							JSONObject obj3 = new JSONObject(obj2.getString("thumb"));
							detail_thumb_url	= new String[1];
							detail_thumb_url[0]	= obj3.getString("url");
							capacity.setText(String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_psn_no), obj2.getInt("capacity")));
							areaSquare.setText(String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_square), obj2.getInt("square")));
							JSONArray  arr1 = obj2.getJSONArray("equipments");
							wStr = "";
							for (i=0; i<arr1.length(); i++)
							{
								wStr += (String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_equipment), arr1.getString(i)));
							}
							facilities.setText(wStr);
							if (jp.spacee.app.android.spacee_app.ReceiptTabApplication.currentMeetingStatus > 0)
							{
								if (obj2.getInt("available_amount") > 0)
								{
									status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_status_avail));
									status.setBackgroundResource(R.drawable.shape_oval_blue);
								}
								else
								{
									status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_status_full));
									status.setBackgroundResource(R.drawable.shape_oval_gray);
								}
								availNo.setText(String.format("%1s/%2s", obj2.getInt("available_amount"), obj2.getInt("capacity"))
												+ ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_seat_no));
							}
							else
							{
								status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_status_out_service));
								status.setBackgroundResource(R.drawable.shape_oval_gray);
								availNo.setText(String.format("- /%2s", obj2.getInt("capacity"))
												+ ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_seat_no));
							}

							avail_No		= obj2.getInt("capacity");
							minBookUnit	= obj2.getInt("min_booking_minutes");
							bookStep		= obj2.getInt("booking_minute_step");

							roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(detail_thumb_url);
							imgSpace.setImageBitmap(roomThumnails[0]);
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
		}
		else
		{
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			return;
		}

		setSpinnerValue();

		//	物件の料金プランの取得
		Calendar	cal		= Calendar.getInstance();
		String		wTDate	= String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));

		result	= SpaceeAppMain.httpCommGlueRoutines.retrievePriceTable(space_id, wTDate);
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
						JSONObject obj2 = obj1.getJSONObject("price_plans");
						if (obj2 != null)
						{
							JSONArray	arr1 = obj2.getJSONArray("plan");
							for (i=0; i<arr1.length(); i++)
							{
								JSONObject	obj3 = arr1.getJSONObject(i);
								ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, android.R.layout.simple_list_item_1);
								try
								{
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
									Date wDate = sdf.parse(obj3.getString("start_at"));
									wStr   = (new SimpleDateFormat("hh:mm").format(wDate));
									wDate  = sdf.parse(obj3.getString("end_at"));
									wStr  += ("-" + new SimpleDateFormat("hh:mm").format(wDate));
									adapter.add(String.format("%s%,10d円/1h\n", wStr, obj3.getInt("price")));
								}
								catch (java.text.ParseException e)
								{
									e.printStackTrace();
								}
								priceList.setAdapter(adapter);
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
		}
		else
		{
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null, "");
			return;
		}


		//	WorkListからスケジュールを取り出す
		result = SpaceeAppMain.httpCommGlueRoutines.retrieveOfficeInfo("space");
		if (result != null)
		{
			try
			{
				schedList = new ArrayList<java.util.HashMap<String, String>>();
				JSONObject obj1 = new JSONObject(result);
				JSONArray  arr1 = obj1.getJSONArray("listings");
				if (arr1 != null)
				{
					for (i=0; i<arr1.length(); i++)
					{
						JSONObject obj2 = arr1.getJSONObject(i);
						JSONObject obj3 = new JSONObject(obj2.getString("room_calendars"));
						JSONObject obj4 = new JSONObject(obj3.getString("calendar"));
						JSONArray  arr2 = obj4.getJSONArray("room_schedules");
						HashMap<String, String> map = new HashMap<String, String>();
						for (k=0; k<arr2.length(); k++)
						{
							JSONObject obj5 = arr2.getJSONObject(i);
							map.put("entNo", String.format("%d", (+i)));
							map.put("start_at",	obj5.getString("start_at"));
							map.put("end_at",		obj5.getString("end_at"));
						}
						schedList.add(map);
					}
				}
				else
				{

				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return;
			}

			ImageView	timeLine	= (android.widget.ImageView)	view.findViewById(R.id.timeLine);
			drawSchedule(timeLine);
		}
	}


	private void setSpinnerValue()
	{
		int			i;
		String		wStr;

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<24*60; i+=15)
		{
			wStr = String.format("%2d:%02d　", (Integer)(i/60), (Integer)(i%60));
			adapter1.add(wStr);
		}
		startTime.setAdapter(adapter1);


		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<=24; i++)
		{
			wStr = String.format("%2d　", i);
			adapter2.add(wStr);
		}
		useHour.setAdapter(adapter2);


		if (bookStep == 0)		bookStep = 30;			//	dummy

		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<60; i+=bookStep)
		{
			wStr = String.format("%2d　", i);
			adapter3.add(wStr);
		}
		useMin.setAdapter(adapter3);


		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter4.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=1; i<=avail_No; i++)
		{
			wStr = String.format("%2d　", i);
			adapter4.add(wStr);
		}
		numPsn.setAdapter(adapter4);
	}


	private  void  drawSchedule(ImageView view)
	{
		//		float	ratio	= SpaceeAppMain.scale * 3 / 4;
		//		Bitmap	bmp		= Bitmap.createBitmap((int)(580*ratio), (int)(54*ratio), Bitmap.Config.ARGB_8888);
		Bitmap	bmp		= Bitmap.createBitmap(880, 145, Bitmap.Config.ARGB_8888);
		android.graphics.Canvas cvs		= new android.graphics.Canvas(bmp);
		android.graphics.Paint paint1	= new android.graphics.Paint();
		paint1.setStrokeWidth(2.0f);
		paint1.setTextSize(22);
		paint1.setColor(android.graphics.Color.BLACK);
		android.graphics.Paint paint2	= new android.graphics.Paint();
		paint2.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));

		cvs.drawColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey_white));

		cvs.drawText(ReceiptTabApplication.AppContext.getString(R.string.frag_meeting_detail_time_now),  60, 120, paint1);
		cvs.drawText("13:00", 180, 120, paint1);
		cvs.drawText("14:00", 300, 120, paint1);
		cvs.drawText("15:00", 420, 120, paint1);
		cvs.drawText("16:00", 540, 120, paint1);
		cvs.drawText("17:00", 660, 120, paint1);
		cvs.drawText("18:00", 780, 120, paint1);

		cvs.drawRect( 0,  5, 880, 95, paint2);

		view.setImageBitmap(bmp);
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

		ReceiptTabApplication.isMsgShown =true;

		msgOff.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ReceiptTabApplication.isMsgShown =false;

				android.os.Message msg = new android.os.Message();
				msg.what = SpaceeAppMain.MSG_PROVIDER_LOGIN_COMP;
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

