package jp.spacee.app.android.spacee_app.listener;

import android.os.Handler;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
	private								TextView[]					monthName			= null;
	private								View[]						monthMark			= null;
	private								RelativeLayout				statusProgress		= null;

	private								RelativeLayout 				errLayout			= null;
	private								TextView					title				= null;
	private								TextView					content				= null;
	private								ImageView					msgOff				= null;

	private								ArrayList<HashMap<String, String>>	daylyList	= null;

	private								SimpleAdapter				adapter				= null;

	private								int							this_id				= -1;
	private								int							caller				= -1;

	private								int							targetYear			= 0;
	private								int							targetMonth		= 0;
	private								int							targetDayNo		= 0;
	private								int							targetStDay		= 0;


	public  FragmentStatusBookingListener(int id, int kind)
	{
		this_id = id;
		caller	 = kind;
	}


	@Override
	public  void  onMonth1Clicked(android.view.View view)
	{
		redrawBookingSchedules1(0);
	}


	@Override
	public  void  onMonth2Clicked(android.view.View view)
	{
		redrawBookingSchedules1(1);
	}


	@Override
	public  void  onMonth3Clicked(android.view.View view)
	{
		redrawBookingSchedules1(2);
	}

	@Override
	public  void  onMonth4Clicked(android.view.View view)
	{
		redrawBookingSchedules1(3);
	}


	@Override
	public  void  onMonth5Clicked(android.view.View view)
	{
		redrawBookingSchedules1(4);
	}


	@Override
	public  void  onMonth6Clicked(android.view.View view)
	{
		redrawBookingSchedules1(5);
	}


	@Override
	public void onListProcess(View view)
	{
		int		i, month;

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		bookingStatusList	= (ListView)		view.findViewById(R.id.bookingStatus);
		statusProgress		= (RelativeLayout)	view.findViewById(R.id.statusProgress);

		monthName	  = new TextView[6];
		monthName[0] = (TextView)	view.findViewById(R.id.monthName1);
		monthName[1] = (TextView)	view.findViewById(R.id.monthName2);
		monthName[2] = (TextView)	view.findViewById(R.id.monthName3);
		monthName[3] = (TextView)	view.findViewById(R.id.monthName4);
		monthName[4] = (TextView)	view.findViewById(R.id.monthName5);
		monthName[5] = (TextView)	view.findViewById(R.id.monthName6);

		monthMark	  = new android.view.View[6];
		monthMark[0] = (View)		view.findViewById(R.id.monthMark1);
		monthMark[1] = (View)		view.findViewById(R.id.monthMark2);
		monthMark[2] = (View)		view.findViewById(R.id.monthMark3);
		monthMark[3] = (View)		view.findViewById(R.id.monthMark4);
		monthMark[4] = (View)		view.findViewById(R.id.monthMark5);
		monthMark[5] = (View)		view.findViewById(R.id.monthMark6);

		Calendar  cal = Calendar.getInstance();
		month = cal.get(Calendar.MONTH) + 1;			//	0 rorigin   jan=0 feb=1... -> +1
		for (i=0; i<6; i++)
		{
			if ((month+i) <= 12)
					monthName[i].setText(String.format("%02d月", (month+i)));
			else	monthName[i].setText(String.format("%02d月", (month+i-12)));
		}

		redrawBookingSchedules1(0);
	}


	private  void  redrawBookingSchedules1(final int eno)
	{
		int		i;

		for (i=0; i<6; i++)
		{
			monthMark[i].setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.white));
		}
		monthMark[eno].setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.spacee_blue));


		Calendar  cal = Calendar.getInstance();
		targetYear		= cal.get(Calendar.YEAR);
		targetMonth	= cal.get(Calendar.MONTH) + eno + 1;			//	0 rorigin   jan=0 feb=1... -> +1
		targetDayNo	= cal.get(Calendar.DAY_OF_MONTH);
		if (targetMonth > 12)
		{
			targetYear ++;
			targetMonth -= 12;
		}
		statusProgress.setVisibility(View.VISIBLE);

		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				retrieveListInfo(eno, targetYear, targetMonth);

				redrawBookingSchedules2(eno, targetYear, targetMonth, targetDayNo);
			}
		}, 500);
	}


	private  void  redrawBookingSchedules2(int eno, int year, int month, int day)
	{
		int		i, dayNo;

		statusProgress.setVisibility(View.INVISIBLE);

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
				targetStDay = day;
		else	targetStDay = 1;
		for (i=targetStDay; i<=dayNo; i++)
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

				//	今日の表示
				Calendar	cal = Calendar.getInstance();
				String	wStr = date.getText().toString();
				int		wPos = wStr.indexOf("/");
				if (   (cal.get(Calendar.MONTH) + 1		== Integer.parseInt(wStr.substring(0, wPos)))
					&& (cal.get(Calendar.DAY_OF_MONTH)	== Integer.parseInt(wStr.substring(wPos+1, wStr.length()))) )
				{
					sts.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_status_booking_today));
				}
				else
				{
					sts.setText("");
				}

				//	Selected
				if (  (  (caller == 1)
					  && (ReceiptTabApplication.currentWorkDetailMonth	== Integer.parseInt(wStr.substring(0, wPos)))
					  && (ReceiptTabApplication.currentWorkDetailDay	== Integer.parseInt(wStr.substring(wPos+1, wStr.length()))))
				   || (  (caller == 2)
					  && (ReceiptTabApplication.currentMeetingDetailMonth == Integer.parseInt(wStr.substring(0, wPos)))
					  && (ReceiptTabApplication.currentMeetingDetailDay	  == Integer.parseInt(wStr.substring(wPos+1, wStr.length())))) )
				{
					mark.setImageResource(R.drawable.ic_check);
					ll.setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));
					selected = true;
				}
				else
				{
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
				if (caller == 1)
				{
					ReceiptTabApplication.currentWorkDetailYear	 = targetYear;
					ReceiptTabApplication.currentWorkDetailMonth = targetMonth;
					ReceiptTabApplication.currentWorkDetailDay	 = targetStDay + pos;
				}
				else
				{
					ReceiptTabApplication.currentMeetingDetailYear	= targetYear;
					ReceiptTabApplication.currentMeetingDetailMonth = targetMonth;
					ReceiptTabApplication.currentMeetingDetailDay	= targetStDay + pos;
				}

				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_STATUS_BOOKING_COMP;
				msg.arg1 = caller;									//	呼び出し元  1:workDetail  2:meetingDdetail
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
				JSONObject	obj1 = new JSONObject(result);
				if (obj1 != null)
				{
					String	rc = obj1.getString("status");
					if (rc.equals("ok"))
					{
						daylyList = new ArrayList<HashMap<String, String>>();
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
									return;
								}
							}
						}
						else
						{
							showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null, "");
							return;
						}
					}
					else
					{
						showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj1, "");
						return;
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
		}
		else
		{
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			return;
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