package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import jp.spacee.app.android.spacee_app.fragment.FragmentWorkDetail;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.common.BookingRoomData;


public  class  FragmentWorkDetailListener  implements  FragmentWorkDetail.FragmentInteractionListener
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
	private 					TextView						useMinTag		= null;
	private						ListView						priceList		= null;
	private 					TextView						amount			= null;
	private 					TextView						btnSelect		= null;

	private						RelativeLayout					errLayout		= null;
	private 					TextView						title			= null;
	private 					TextView						content			= null;
	private 					ImageView						msgOff			= null;

	private						String							desk_id			= "";

	private						List<HashMap<String, String>>	planList		= null;
	private						List<HashMap<String, String>>	schedList		= null;

	private						int								minBookUnit	= 0;
	private						int								bookStep		= 0;
	private						int								price			= 0;
	private						int								occupiedMin	= 0;

	private						int								schedNow		= 0;
	private						int								schedTime		= 0;

	private						String[]						pictUrl			= new String[1];


	public FragmentWorkDetailListener(int id)
	{
		desk_id =  String.format("%d", id);;
	}


	@Override
	public void onBtnDateListClicked(View view)
	{
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_WORK_DETAIL_COMP;
		msg.arg1 = 2;                                            //	by btnDateListClicked
		msg.arg2 = Integer.parseInt(desk_id);
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public void onBtnSelectClicked(View view)
	{
		if (price > 0)
		{
			if (ReceiptTabApplication.bookingRoomData == null)
			{
				ReceiptTabApplication.bookingRoomData = new BookingRoomData();
			}
			ReceiptTabApplication.bookingRoomData.roomId			= desk_id;
			ReceiptTabApplication.bookingRoomData.namePlace		= ReceiptTabApplication.currentWorkName;
			ReceiptTabApplication.bookingRoomData.useYear			= ReceiptTabApplication.currentWorkDetailYear;
			ReceiptTabApplication.bookingRoomData.useMonth			= ReceiptTabApplication.currentWorkDetailMonth;
			ReceiptTabApplication.bookingRoomData.useDay			= ReceiptTabApplication.currentWorkDetailDay;
			String	wStr = startTime.getSelectedItem().toString().replace("　", " ").trim();
			ReceiptTabApplication.bookingRoomData.checkInTime		= wStr;
			int	temp  = Integer.parseInt(wStr.substring(0, 2))*60 + Integer.parseInt(wStr.substring(3, 5));
				temp += useHour.getSelectedItemPosition()*60;
			if (useMin.getVisibility() == View.VISIBLE)
			{
				temp += Integer.parseInt(useMin.getSelectedItem().toString().replace("　", " ").trim());
			}
			wStr = String.format("%02d:%02d", (int)(temp/60), (int)(temp % 60));
			ReceiptTabApplication.bookingRoomData.checkOutTime		= wStr;
			ReceiptTabApplication.bookingRoomData.numPsn			= 1;
			ReceiptTabApplication.bookingRoomData.TotalPrice		= price;
			ReceiptTabApplication.bookingRoomData.discount			= 0;
			ReceiptTabApplication.bookingRoomData.payAmount		= price;
			ReceiptTabApplication.bookingRoomData.pricePlan		= planList;
			ReceiptTabApplication.bookingRoomData.pictUrl			= pictUrl;
			ReceiptTabApplication.bookingRoomData.payAmount		= price;
			ReceiptTabApplication.bookingRoomData.occupiedMin		= occupiedMin;

			android.os.Message msg = new android.os.Message();
			msg.what = SpaceeAppMain.MSG_WORK_DETAIL_COMP;
			msg.arg1 = 1;                                            //	by btnSelectClicked
			SpaceeAppMain.mMsgHandler.sendMessage(msg);
		}
	}


	@Override
	public void onBtnPrevClicked(View view)
	{
		if (schedTime >= 4*60)		schedTime -= 60*4;

		ImageView	timeLine	= (android.widget.ImageView)	view.findViewById(R.id.timeLine);
		drawSchedule(timeLine);
	}


	@Override
	public void onBtnNextClicked(View view)
	{
		if (schedTime < 18*60)		schedTime += 60*4;

		ImageView	timeLine	= (android.widget.ImageView)	view.findViewById(R.id.timeLine);
		drawSchedule(timeLine);
	}


	@Override
	public void retrieveDetaildata(View view)
	{
		int		i, k;
		String	wStr1, wStr2;

		imgSpace		= (ImageView)	view.findViewById(R.id.imgSpace);
		capacity		= (TextView)	view.findViewById(R.id.capacity);
		areaSquare		= (TextView)	view.findViewById(R.id.areaSquare);
		facilities		= (TextView)	view.findViewById(R.id.facilities);
		btnDateList	= (TextView)	view.findViewById(R.id.btnDateList);
		status			= (TextView)	view.findViewById(R.id.status);
		availNo			= (TextView)	view.findViewById(R.id.availNo);
		timeLine		= (ImageView)	view.findViewById(R.id.timeLine);
		startTime		= (Spinner)		view.findViewById(R.id.startTime);
		useHour			= (Spinner)		view.findViewById(R.id.useHour);
		useMin			= (Spinner)		view.findViewById(R.id.useMin);
		useMinTag		= (TextView)	view.findViewById(R.id.useMinTag);
		priceList		= (ListView)	view.findViewById(R.id.priceList);
		amount			= (TextView)	view.findViewById(R.id.amount);
		btnSelect		= (TextView)	view.findViewById(R.id.btnSelect);

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)	errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)	errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)	errLayout.findViewById(R.id.messageOff);


		String[]	detail_thumb_url	= null;
		Bitmap[]	roomThumnails		= null;


		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveOfficeDetail(desk_id);
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
							capacity.setText(String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_psn_no), obj2.getInt("capacity")));
							areaSquare.setText(String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_square), obj2.getInt("square")));
							JSONArray  arr1 = obj2.getJSONArray("equipments");
							wStr1 = "";
							for (i=0; i<arr1.length(); i++)
							{
								wStr1 += (String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_equipment), arr1.getString(i)));
							}
							if (arr1.length() > 0)
									facilities.setText(wStr1);
							else	facilities.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_work_detail_no_equipment));
							if (jp.spacee.app.android.spacee_app.ReceiptTabApplication.currentWorkStatus > 0)
							{
								if (obj2.getInt("available_amount") > 0)
								{
									status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_status_avail));
									status.setBackgroundResource(R.drawable.shape_oval_blue);
								}
								else
								{
									status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_status_full));
									status.setBackgroundResource(R.drawable.shape_oval_gray);
								}
								availNo.setText(String.format("%s/%s", obj2.getInt("available_amount"), obj2.getInt("capacity"))
												+ ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_seat_no));
							}
							else
							{
								status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_status_out_service));
								status.setBackgroundResource(R.drawable.shape_oval_gray);
								availNo.setText(String.format("- /%s", obj2.getInt("capacity"))
												+ ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_seat_no));
							}

							minBookUnit	= obj2.getInt("min_booking_minutes");
							bookStep		= obj2.getInt("booking_minute_step");

							roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(detail_thumb_url);
							imgSpace.setImageBitmap(roomThumnails[0]);
							pictUrl[0] = detail_thumb_url[0];
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


		Calendar	cal		= Calendar.getInstance();
		wStr1 = String.format(ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_work_detail_format),
							 ReceiptTabApplication.currentWorkDetailMonth, ReceiptTabApplication.currentWorkDetailDay);
		if (   (ReceiptTabApplication.currentWorkDetailYear	 == cal.get(Calendar.YEAR))
			&& (ReceiptTabApplication.currentWorkDetailMonth == cal.get(Calendar.MONTH) + 1)
			&& (ReceiptTabApplication.currentWorkDetailDay	 == cal.get(Calendar.DAY_OF_MONTH)) )
		{
			wStr1 += ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_work_detail_today);
		}
		btnDateList.setText(wStr1);

		setSpinnerValue();

		//	物件の料金プランの取得
		String	wTDate	= String.format("%04d%02d%02d",
										ReceiptTabApplication.currentWorkDetailYear,
										ReceiptTabApplication.currentWorkDetailMonth,
										ReceiptTabApplication.currentWorkDetailDay);

		result	= SpaceeAppMain.httpCommGlueRoutines.retrievePriceTable(desk_id, wTDate);
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
						planList = new ArrayList<java.util.HashMap<String, String>>();
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
									HashMap<String, String> map = new HashMap<String, String>();
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
									Date wDate = sdf.parse(obj3.getString("start_at"));
									wStr1 = (new SimpleDateFormat("HH:mm").format(wDate));
									map.put("bgnTime", wStr1);
									wDate = sdf.parse(obj3.getString("end_at"));
									wStr2 = ("-" + new SimpleDateFormat("HH:mm").format(wDate));
									map.put("endTime", wStr2);
									adapter.add(String.format("%s-%s%,10d円/1h\n", wStr1, wStr2, obj3.getInt("price")));
									map.put("price", ""+obj3.getInt("price"));
									planList.add(map);
								}
								catch (java.text.ParseException e)
								{
									e.printStackTrace();
									return;
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
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			return;
		}


		//	WorkListからスケジュールを取り出す
		result = SpaceeAppMain.httpCommGlueRoutines.retrieveOfficeInfo("desk");
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
						for (k=0; k<arr2.length(); k++)
						{
							HashMap<String, String> map = new HashMap<String, String>();
							JSONObject obj5 = arr2.getJSONObject(i);
							map.put("entNo", String.format("%d", (i)));
							map.put("start_at",	obj5.getString("start_at"));
							map.put("end_at",		obj5.getString("end_at"));
							schedList.add(map);
						}
/*
	for (k=9; k<=21; k+=3)
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("entNo",		String.format("%d", (i)));
		map.put("start_at",	String.format("2001-01-01T%02d:%02d:00+900", k,		0));
		map.put("end_at",		String.format("2001-01-01T%02d:%02d:00+900", k+2,	0));
		schedList.add(map);
	}
*/
					}
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

			schedNow  = (cal.get(java.util.Calendar.HOUR_OF_DAY)*60 + cal.get(java.util.Calendar.MINUTE) + 14)/15*15;
			schedTime = schedNow;
			ImageView	timeLine	= (android.widget.ImageView)	view.findViewById(R.id.timeLine);
			drawSchedule(timeLine);
		}
		else
		{
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
		}
	}


	private void setSpinnerValue()
	{
		int			i;
		String		wStr;

		Calendar	cal		= Calendar.getInstance();
		int			nowPos	= (cal.get(Calendar.HOUR_OF_DAY)*60 + cal.get(Calendar.MINUTE) + 14)/15;

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<24*60; i+=15)
		{
			wStr = String.format("%2d:%02d　", (Integer)(i/60), (Integer)(i%60));
			adapter1.add(wStr);
		}
		startTime.setAdapter(adapter1);
		startTime.setSelection(nowPos);

		startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Calendar cal = Calendar.getInstance();
				if ((ReceiptTabApplication.currentWorkDetailYear == cal.get(Calendar.YEAR))
						&& (ReceiptTabApplication.currentWorkDetailMonth == cal.get(Calendar.MONTH) + 1)
						&& (ReceiptTabApplication.currentWorkDetailDay == cal.get(Calendar.DAY_OF_MONTH))) {
					//	過去の時間を開いたら変更する
					int now = (cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE) + 14) / 15;
					if (startTime.getSelectedItemPosition() < now) {
						startTime.setSelection(now);
					}
				}

				hideActionBar();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				hideActionBar();
			}
		});


		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<=12; i++)
		{
			wStr = String.format("%2d　", i);
			adapter2.add(wStr);
		}
		useHour.setAdapter(adapter2);

		useHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				recalculatePrice();

				hideActionBar();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				hideActionBar();
			}
		});


		if ((bookStep > 0) && (bookStep < 60))
		{
			ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
			adapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
			for (i=0; i<60; i+=bookStep)
			{
				wStr = String.format("%2d　", i);
				adapter3.add(wStr);
			}
			useMin.setAdapter(adapter3);

			useMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
				{
					recalculatePrice();

					hideActionBar();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent)
				{
					hideActionBar();
				}
			});
		}
		else
		{
			useMin.setVisibility(View.GONE);
			useMinTag.setVisibility(View.GONE);
		}
	}


	private  void  drawSchedule(ImageView view)
	{
		int		i, wSTime, wETime;
		Date	wDate;

//		float	ratio	= SpaceeAppMain.scale * 3 / 4;
//		Bitmap	bmp		= Bitmap.createBitmap((int)(580*ratio), (int)(54*ratio), Bitmap.Config.ARGB_8888);
		Bitmap	bmp		= Bitmap.createBitmap(880, 145, Bitmap.Config.ARGB_8888);
		Canvas cvs		= new Canvas(bmp);
		Paint paint1	= new Paint();
		paint1.setStrokeWidth(2.0f);
		paint1.setTextSize(22);
		paint1.setColor(android.graphics.Color.BLACK);
		Paint paint2	= new Paint();
		paint2.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));
		Paint paint3	= new Paint();
		paint3.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.alert));
		Paint paint4	= new Paint();
		paint4.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.text_white));
		paint4.setStrokeWidth(3.0f);
		paint4.setTextSize(26);

		cvs.drawColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey_white));

		if ((schedTime <= schedNow) && (schedNow <= (schedTime+6*60)))
		{
			cvs.drawText(ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_meeting_detail_time_now),
					(schedNow-schedTime)*2+65, 140, paint1);
		}
		cvs.drawText(String.format("%02d:%02d", (int)((schedTime      ) / 60), (schedTime      ) % 60),  60, 115, paint1);
		cvs.drawText(String.format("%02d:%02d", (int)((schedTime +  60) / 60), (schedTime +  60) % 60), 180, 115, paint1);
		cvs.drawText(String.format("%02d:%02d", (int)((schedTime + 120) / 60), (schedTime + 120) % 60), 300, 115, paint1);
		cvs.drawText(String.format("%02d:%02d", (int)((schedTime + 180) / 60), (schedTime + 180) % 60), 420, 115, paint1);
		cvs.drawText(String.format("%02d:%02d", (int)((schedTime + 240) / 60), (schedTime + 240) % 60), 540, 115, paint1);
		cvs.drawText(String.format("%02d:%02d", (int)((schedTime + 300) / 60), (schedTime + 300) % 60), 660, 115, paint1);
		cvs.drawText(String.format("%02d:%02d", (int)((schedTime + 360) / 60), (schedTime + 360) % 60), 780, 115, paint1);

		cvs.drawRect( 0,  5, 880, 90, paint2);

		HashMap<String, String>  map = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		for (i=0; i<schedList.size(); i++)
		{
			map = schedList.get(i);
			try
			{
				wDate = sdf.parse(map.get("start_at"));
				wSTime = Integer.parseInt(new SimpleDateFormat("HH").format(wDate))*60 + Integer.parseInt(new SimpleDateFormat("mm").format(wDate));
				wDate = sdf.parse(map.get("end_at"));
				wETime = Integer.parseInt(new SimpleDateFormat("HH").format(wDate))*60 + Integer.parseInt(new SimpleDateFormat("mm").format(wDate));

				if ((schedTime <= wETime) && (wSTime <= (schedTime+6*60)))
				{
					if (wSTime < schedTime)			wSTime = schedTime;
					if ((schedTime+6*60) < wETime)		wETime = schedTime + 6*60;

					cvs.drawRect((wSTime-schedTime)*2+85,  5, (wETime-schedTime)*2+85, 90, paint3);

					cvs.drawText("満", ((wSTime+wETime)/2-schedTime)*2+65, 55, paint4);
				}
			}
			catch (java.text.ParseException e)
			{
				e.printStackTrace();
				return;
			}
		}

		view.setImageBitmap(bmp);
	}


	private  void  recalculatePrice()
	{
		occupiedMin = useHour.getSelectedItemPosition()*60;
		if (useMin.getVisibility() == View.VISIBLE)
		{
			occupiedMin += Integer.parseInt(useMin.getSelectedItem().toString().replace("　", " ").trim());
		}
		String stTime = String.format("%04d-%02d-%02dT%s:00+09:00", ReceiptTabApplication.currentWorkDetailYear,
				ReceiptTabApplication.currentWorkDetailMonth,
				ReceiptTabApplication.currentWorkDetailDay,
				startTime.getSelectedItem().toString().replace("　", " ").trim());

		if (occupiedMin > 0)
		{
			//	物件の料金計算
			String	result = SpaceeAppMain.httpCommGlueRoutines.retrieveRoomPrice(desk_id, stTime, occupiedMin, 1, "");
			if (result != null)
			{
				try
				{
					JSONObject obj1 = new JSONObject(result);
					price = obj1.getInt("total_price");
				}
				catch (org.json.JSONException e)
				{
					e.printStackTrace();
					return;
				}

				amount.setText(String.format("%,d", price));
				btnSelect.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_blue));
			}
			else
			{
				showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			}
		}
	}


	private  void  hideActionBar()
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
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

