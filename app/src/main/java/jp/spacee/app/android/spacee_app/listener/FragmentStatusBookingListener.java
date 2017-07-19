package jp.spacee.app.android.spacee_app.listener;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.LinearLayout;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import org.json.JSONObject;
import android.os.Message;
import org.json.JSONArray;
import org.json.JSONException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.text.SimpleDateFormat;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentStatusBooking;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentStatusBookingListener  implements  jp.spacee.app.android.spacee_app.fragment.FragmentStatusBooking.FragmentInteractionListener
{
	private								ListView					bookingStatusList	= null;

	private								ArrayList<HashMap<String, String>>	daylyList	= null;

	private								SimpleAdapter				adapter				= null;

	private								int							this_id				= -1;



	public  FragmentStatusBookingListener(int id)
	{
		this_id = id;
	}


	@Override
	public  void  onMonth1Clicked(android.view.View view)
	{
		redrawBookingSchedules(0);
	}


	@Override
	public  void  onMonth2Clicked(android.view.View view)
	{
		redrawBookingSchedules(1);
	}


	@Override
	public  void  onMonth3Clicked(android.view.View view)
	{
		redrawBookingSchedules(2);
	}

	@Override
	public  void  onMonth4Clicked(android.view.View view)
	{
		redrawBookingSchedules(3);
	}


	@Override
	public  void  onMonth5Clicked(android.view.View view)
	{
		redrawBookingSchedules(4);
	}


	@Override
	public  void  onMonth6Clicked(android.view.View view)
	{
		redrawBookingSchedules(5);
	}


	@Override
	public void onListProcess(android.widget.ListView view)
	{
		bookingStatusList = view;

		redrawBookingSchedules(0);
	}


	private  void  redrawBookingSchedules(int eno)
	{
		int		i, year, month, day, dayNo, stNo;

		Calendar  cal = Calendar.getInstance();
		year	= cal.get(Calendar.YEAR);
		month	= cal.get(Calendar.MONTH) + eno + 1;			//	0 rorigin   jan=0 feb=1... -> +1
		day		= cal.get(Calendar.DAY_OF_MONTH);
		if (month > 12)
		{
			year ++;
			month -= 12;
		}
		retrieveListInfo(eno, year, month);

		if ((month == 4) || (month == 6) || (month == 9) || (month == 11))
		{
			dayNo = 30;
		}
		else if (month == 2)
		{
			if ((year % 4) == 0)				//	100で割るとゼロでない事、かつ400で割るとゼロの場合は２１００年までこない
					dayNo = 29;
			else	dayNo = 29;
		}
		else
		{
			dayNo = 31;
		}


		HashMap<String, String> mapout;
		ArrayList<HashMap<String, String>> bookingInfo = new ArrayList<java.util.HashMap<String, String>>();

		if (eno == 0)
				stNo = day;
		else	stNo = 1;
		for (i=stNo; i<=dayNo; i++)
		{
			mapout = new HashMap<String, String>();
			mapout.put("day", String.format("%02d/%02d", month, i));
			bookingInfo.add(mapout);
		}


		adapter = new android.widget.SimpleAdapter(ReceiptTabApplication.AppContext,
													bookingInfo,
													R.layout.layout_book_status,
													new String[]{"day"},
													new int   []{R.id.day})
		{
			@Override
			public View getView(int pos, View cView, ViewGroup parent)
			{
				View retView = super.getView(pos, cView, parent);
				boolean		selected;

				if (retView == null)
				{
					LayoutInflater inflater = (LayoutInflater) ReceiptTabApplication.AppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					retView = inflater.inflate(R.layout.layout_book_status, null);
				}

				LinearLayout	ll		= (LinearLayout)	retView.findViewById(R.id.BookStatusLayout);
				TextView		sts		= (TextView)		retView.findViewById(R.id.todayMark);
				TextView		date	= (TextView)		retView.findViewById(R.id.day);
				ImageView		mark	= (ImageView)		retView.findViewById(R.id.select);
				if (pos == 0)			//	tentative
				{
					sts.setText("今日");
					mark.setImageResource(R.drawable.ic_check);
					ll.setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));
					selected = true;
				}
				else
				{
					sts.setText("");
					mark.setImageBitmap(null);
					ll.setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey_white));
					selected = false;
				}

				ImageView	sched	= (ImageView)	retView.findViewById(R.id.timeLine);
				drawSchedules(sched, date.getText().toString(), selected);

				return  retView;
			}
		};
		bookingStatusList.setAdapter(adapter);


		bookingStatusList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
		{
			public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, final int pos, long id)
			{
//				HashMap<String, String>  map = new HashMap<String, String>();
//				map = workList.get(pos);

				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_STATUS_BOOKING_COMP;
				msg.arg1 = pos;									//	リストのEntry番号
//				msg.arg2 = Integer.parseInt(map.get("id"));	//	id
//				msg.obj  = map.get("subtitle");				//	subtitle
				SpaceeAppMain.mMsgHandler.sendMessage(msg);
			}
		});
	}


	//
	private  void  retrieveListInfo(int eno, int year, int month)
	{
		int		i, k;
		String	wStr1, wStr2;

		daylyList = new ArrayList<HashMap<String, String>>();

		wStr1 = String.format("%d", this_id);
		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveBookingStatus(wStr1, year, month);
		if (result != null)
		{
			try
			{
				daylyList = new ArrayList<HashMap<String, String>>();
				JSONObject	obj1 = new JSONObject(result);
				JSONObject	obj2 = obj1.getJSONObject("listing");
				JSONArray	arr1 = obj2.getJSONArray("room_calendars");
				if (arr1 != null)
				{
					for (i=0; i<arr1.length(); i++)
					{
						HashMap<String, String> map = new HashMap<String, String>();

						JSONObject obj3 = arr1.getJSONObject(i);
						JSONObject obj4 = obj3.getJSONObject("calendar");
						try
						{
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							Date	wDate	= sdf1.parse(obj4.getString("date"));
							map.put("date", new SimpleDateFormat("dd").format(wDate));
							JSONObject obj5 = obj4.getJSONObject("room_calendar");
							SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
							wDate	= sdf2.parse(obj5.getString("start_at"));
							wStr1	= new SimpleDateFormat("HH").format(wDate);
							wStr2	= new SimpleDateFormat("mm").format(wDate);
							map.put("bgnTime", String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
							wDate	= sdf2.parse(obj5.getString("end_at"));
							wStr1	= new SimpleDateFormat("HH").format(wDate);
							wStr2	= new SimpleDateFormat("mm").format(wDate);
							map.put("endTime", String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
							JSONArray  arr2 = obj4.getJSONArray("room_schedules");
							map.put("schedNo", String.format("%d", arr2.length()));
							for (k=0; k<arr2.length(); k++)
							{
								JSONObject obj6 = arr2.getJSONObject(k);
								wDate	= sdf2.parse(obj6.getString("start_at"));
								wStr1	= new SimpleDateFormat("HH").format(wDate);
								wStr2	= new SimpleDateFormat("mm").format(wDate);
								map.put(String.format("bgnTm%d", (k+1)), String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
								wDate	= sdf2.parse(obj6.getString("end_at"));
								wStr1	= new SimpleDateFormat("HH").format(wDate);
								wStr2	= new SimpleDateFormat("mm").format(wDate);
								map.put(String.format("endTm%d", (k+1)), String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
							}
							daylyList.add(map);
						}
						catch (java.text.ParseException e)
						{
							e.printStackTrace();
						}
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
		}
		else			//	エラー
		{

		}
	}


	private  void  drawSchedules(ImageView sched, String date, boolean selected)
	{
		int		i, k, nn, wSt, wEnd;

//		float	ratio	= SpaceeAppMain.scale * 3 / 4;
//		Bitmap	bmp		= Bitmap.createBitmap((int)(580*ratio), (int)(54*ratio), Bitmap.Config.ARGB_8888);
		Bitmap bmp		= Bitmap.createBitmap(880, 80, android.graphics.Bitmap.Config.ARGB_8888);
		Canvas cvs		= new Canvas(bmp);
		Paint paint1	= new Paint();
		paint1.setStrokeWidth(2.0f);
		paint1.setTextSize(22);
		paint1.setColor(Color.BLACK);
		Paint paint2	= new Paint();
		paint2.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_blue_grey));

		if (selected)
				cvs.drawColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));
		else	cvs.drawColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey_white));

		cvs.drawText(" 0:00",   20, 60, paint1);
		cvs.drawText(" 3:00",  120, 60, paint1);
		cvs.drawText(" 6:00",  220, 60, paint1);
		cvs.drawText(" 9:00",  320, 60, paint1);
		cvs.drawText("12:00",  420, 60, paint1);
		cvs.drawText("15:00",  520, 60, paint1);
		cvs.drawText("18:00",  620, 60, paint1);
		cvs.drawText("21:00",  720, 60, paint1);
		cvs.drawText("24:00",  820, 60, paint1);

		cvs.drawRect( 40, 20,  860, 40, paint2);

		Paint paint3	= new Paint();
		paint3.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.sub));
		Paint paint4	= new Paint();
		paint4.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.alert));
		for (i=0; i<daylyList.size(); i++)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			map = daylyList.get(i);
			if (Integer.parseInt(date.substring(3, 5)) == Integer.parseInt(map.get("date")))
			{
				wSt  = 0;
				wEnd = Integer.parseInt(map.get("bgnTime")) * 25 / 45;
				cvs.drawRect( 40+wSt, 20, 40+wEnd, 40, paint3);
				wSt  = Integer.parseInt(map.get("endTime")) * 25 / 45;
				wEnd = 820;
				cvs.drawRect( 40+wSt, 20, 40+wEnd, 40, paint3);

				nn = Integer.parseInt(map.get("schedNo"));
				for (k=0; k<nn; k++)
				{
					wSt  = Integer.parseInt(map.get(String.format("bgnTm%d", (k+1)))) * 25 / 45;
					wEnd = Integer.parseInt(map.get(String.format("endTm%d", (k+1)))) * 25 / 45;
					cvs.drawRect( 40+wSt, 20, 40+wEnd, 40, paint4);
				}
				break;
			}
		}

		sched.setImageBitmap(bmp);
	}
}