package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.os.Message;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.view.LayoutInflater;
import android.content.Context;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.fragment.FragmentWorkList;


public  class  FragmentWorkListListener  implements  FragmentWorkList.FragmentInteractionListener
{
	private						RelativeLayout					errLayout		= null;
	private						TextView						title			= null;
	private						TextView						content			= null;
	private 					ImageView						msgOff			= null;

	private						List<HashMap<String, String>>	workList		= null;
	private						List<HashMap<String, String>>	schedList		= null;

	private						Bitmap[]						roomThumnails	= null;

	private						SimpleAdapter 					adapter			= null;


	public FragmentWorkListListener()
	{
	}


	@Override
	public void onListProcess(View view)
	{
		ListView	workList = (ListView)	view.findViewById(R.id.workSeats);

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		retrieveListInfo(view);

		if (workList != null)
		{
			redrawWorkList(workList);
		}
	}


	private void retrieveListInfo(View view)
	{
		int		i, k;
		String	wStr1, wStr2;
		Date	wDate;

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveOfficeInfo("desk");
		if (result != null)
		{
			try
			{
				JSONObject jsonObj1 = new JSONObject(result);
				if (jsonObj1 != null)
				{
//					String	rc = jsonObj1.getString("status");
//					if (rc.equals("ok"))
//					{
						workList  = new ArrayList<java.util.HashMap<String, String>>();
						schedList = new ArrayList<java.util.HashMap<String, String>>();
						JSONArray  jsonArr1 = jsonObj1.getJSONArray("listings");
						if (jsonArr1 != null)
						{
							for (i=0; i<jsonArr1.length(); i++)
							{
								java.util.HashMap<String, String> map = new java.util.HashMap<String, String>();
								JSONObject json1 = jsonArr1.getJSONObject(i);
								map.put("id",			json1.getString("id"));
								map.put("title",		json1.getString("title"));
								JSONObject json2 = new JSONObject(json1.getString("thumb"));
								map.put("thumb_url",	json2.getString("url"));
								map.put("book_unit",	json1.getString("min_booking_minutes"));
								map.put("capacity",	json1.getString("capacity"));
								map.put("price",		json1.getString("price"));
								map.put("subtitle",	json1.getString("subtitle"));
								map.put("category",	json1.getString("category"));
								JSONArray  jsonArr3 = json1.getJSONArray("equipments");
								wStr1 = "";
								for (k=0; k<jsonArr3.length(); k++)
								{
									if (wStr1.equals(""))
											wStr1 += jsonArr3.getString(k);
									else	wStr1 += ("/" + jsonArr3.getString(k));
								}
								map.put("equipments",	wStr1);
								map.put("available",	json1.getString("available_amount"));
								json2 = new JSONObject(json1.getString("room_calendars"));
								json2 = new JSONObject(json2.getString("calendar"));
								map.put("date",			json2.getString("date"));
								JSONArray  jsonArr2 = json2.getJSONArray("room_schedules");
								json2 = new JSONObject(json2.getString("room_calendar"));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
								try
								{
									wDate	= sdf.parse(json2.getString("start_at"));
									wStr1	= new SimpleDateFormat("HH").format(wDate);
									wStr2	= new SimpleDateFormat("mm").format(wDate);
									map.put("bgnTime", String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
									wDate	= sdf.parse(json2.getString("end_at"));
									wStr1	= new SimpleDateFormat("HH").format(wDate);
									wStr2	= new SimpleDateFormat("mm").format(wDate);
									map.put("endTime", String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
								}
								catch (java.text.ParseException e)
								{
									e.printStackTrace();
								}
								workList.add(map);


								map = new java.util.HashMap<String, String>();
								map.put("id",	json1.getString("id"));
								map.put("schedNo", String.format("%d", jsonArr2.length()));
								for (k=0; k<jsonArr2.length(); k++)
								{
									json2 = jsonArr2.getJSONObject(i);
									try
									{
										wDate	= sdf.parse(json2.getString("start_at"));
										wStr1	= new java.text.SimpleDateFormat("HH").format(wDate);
										wStr2	= new java.text.SimpleDateFormat("mm").format(wDate);
										map.put(String.format("bgnTm%d", (k+1)), String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
										wDate	= sdf.parse(json2.getString("end_at"));
										wStr1	= new java.text.SimpleDateFormat("HH").format(wDate);
										wStr2	= new java.text.SimpleDateFormat("mm").format(wDate);
										map.put(String.format("endTm%d", (k+1)), String.format("%d", (Integer.parseInt(wStr1)*60 + Integer.parseInt(wStr2))));
									}
									catch (java.text.ParseException e)
									{
										e.printStackTrace();
										return;
									}
								}
								schedList.add(map);
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
//						showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), jsonObj1, "");
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

			String[]	urls = new String[workList.size()];
			for (i=0; i<workList.size(); i++)
			{
				HashMap<String, String> map = new HashMap<String, String>();
				map = workList.get(i);
				urls[i] = map.get("thumb_url");
			}
			roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(urls);
		}
		else
		{
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
			return;
		}
	}


	private void redrawWorkList(ListView listView)
	{
		int		i, nowMin;

		HashMap<String, String> mapin;
		HashMap<String, String> mapout;
		final  List<HashMap<String, String>> workAreaInfo = new ArrayList<HashMap<String, String>>();

		Calendar  cal = Calendar.getInstance();
		nowMin = cal.get(Calendar.HOUR_OF_DAY)*60 + cal.get(Calendar.MINUTE);
		for (i = 0; i < workList.size(); i++)
		{
			mapin = workList.get(i);
			mapout = new HashMap<String, String>();
			if ((Integer.parseInt(mapin.get("bgnTime")) <= nowMin) && (nowMin <= Integer.parseInt(mapin.get("endTime"))))
			{
				if (Integer.parseInt(mapin.get("available")) > 0)
				{
					mapout.put("Status",	ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_status_avail));
					mapout.put("StsCode", "1");
				}
				else
				{
					mapout.put("Status",	ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_status_full));
					mapout.put("StsCode", "2");
				}
				mapout.put("Avail",		mapin.get("available") + "/" + mapin.get("capacity") + ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_seat));
			}
			else
			{
				mapout.put("Status",	ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_status_out_service));
				mapout.put("StsCode", "0");
				mapout.put("Avail",		"- /" + mapin.get("capacity") + ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_seat));
			}
			mapout.put("Name", 		mapin.get("subtitle"));
			if (mapin.get("equipments").equals(""))
				mapout.put("Facility", ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_no_equipment));
			else
			{
				mapout.put("Facility", mapin.get("equipments"));
			}
			mapout.put("Price",		String.format("%,d", Integer.parseInt(mapin.get("price"))));
			mapout.put("placeExp", mapin.get("title"));
			mapout.put("remarks",	ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_max_psn)
					  + mapin.get("capacity")  + ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_psn_avail)
					  + mapin.get("book_unit") + ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_min_unit));
			workAreaInfo.add(mapout);
		}
		adapter = new SimpleAdapter(ReceiptTabApplication.AppContext,
										workAreaInfo,
										R.layout.layout_list_workroom,
										new String[]{"Status", 	"Avail",		"Name", 		"Facility", 		"Price", 		"placeExp",	"remarks"},
										new int   []{R.id.status, R.id.availNum, R.id.placeName, R.id.facilities, R.id.price,	R.id.placeExp, R.id.remarks})
		{
			@Override
			public View getView(int pos, View cView, android.view.ViewGroup parent)
			{
				View retView = super.getView(pos, cView, parent);

				if (retView == null)
				{
					LayoutInflater inflater = (LayoutInflater) ReceiptTabApplication.AppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					retView = inflater.inflate(R.layout.layout_list_workroom, null);
				}

				TextView sts = (TextView) retView.findViewById(R.id.status);
				if (sts.getText().toString().equals(ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_status_avail)))
						sts.setBackgroundResource(R.drawable.shape_oval_blue);
				else	sts.setBackgroundResource(R.drawable.shape_oval_gray);

				ImageView iv = (ImageView) retView.findViewById(R.id.imgSpace);
				if (roomThumnails != null)
				{
					iv.setImageBitmap(roomThumnails[pos]);
				}

				ImageView sched = (ImageView) retView.findViewById(R.id.timeLine);
				drawSchedule(pos, sched);

				return  retView;
			}
		};
		listView.setAdapter(adapter);


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
			{
				HashMap<String, String> map = new HashMap<String, String>();

				//	日付を設定しておく
				Calendar cal = Calendar.getInstance();
				ReceiptTabApplication.currentWorkDetailYear	 = cal.get(Calendar.YEAR);
				ReceiptTabApplication.currentWorkDetailMonth = cal.get(Calendar.MONTH) + 1;
				ReceiptTabApplication.currentWorkDetailDay	 = cal.get(Calendar.DAY_OF_MONTH);

				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_WORK_LIST_COMP;
				map = workList.get(pos);
				msg.arg1 = Integer.parseInt(map.get("id"));			//	id
				msg.obj  = map.get("subtitle");						//	subtitle
				map = workAreaInfo.get(pos);
				msg.arg2 = Integer.parseInt(map.get("StsCode"));		//	status code 0/1/2
				SpaceeAppMain.mMsgHandler.sendMessage(msg);
			}
		});
	}


	private  void  drawSchedule(int pos, ImageView view)
	{
		int		i, k, wBgn, wEnd, sNo;
		String	wStr;

//		float	ratio	= SpaceeAppMain.scale * 3 / 4;
//		Bitmap	bmp		= Bitmap.createBitmap((int)(580*ratio), (int)(54*ratio), Bitmap.Config.ARGB_8888);
		Bitmap	bmp		= Bitmap.createBitmap(580, 54, Bitmap.Config.ARGB_8888);
		Canvas	cvs		= new Canvas(bmp);
		Paint	paint1	= new Paint();
		paint1.setStrokeWidth(2.0f);
		paint1.setTextSize(18);
		paint1.setColor(Color.BLACK);
		Paint	paint2	= new Paint();
		paint2.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));

//		cvs.drawColor(Color.rgb(255, 255, 240));
		Calendar  cal = Calendar.getInstance();
		int		crntHour	= cal.get(Calendar.HOUR_OF_DAY);
		int		crntMinute	= ((cal.get(Calendar.MINUTE) + 14) / 15) * 15;				//	15分で切り上げる
		if (crntMinute >= 60)
		{
			crntMinute -= 60;
			crntHour ++;
		}
		int		limitBgn	=  crntHour		*60 + crntMinute;
		int		limitEnd	= (crntHour + 4)*60 + crntMinute;


		cvs.drawText(ReceiptTabApplication.AppContext.getString(R.string.frag_work_list_time_now),   20, 50, paint1);
		for (i=0; i<4; i++) {
			wStr = String.format("%02d:%02d", (crntHour + (i+1)), crntMinute);
			cvs.drawText(wStr, ((i+1)*120)+20, 50, paint1);
		}

		cvs.drawRect( 40,  5, 580, 30, paint2);

		Paint paint3	= new Paint();
		paint3.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.sub));
		Paint paint4	= new Paint();
		paint4.setColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.alert));

		HashMap<String, String> map = new HashMap<String, String>();
		map  = workList.get(pos);
		wBgn = Integer.parseInt(map.get("bgnTime"));
		if ((limitBgn <= wBgn) && (wBgn < limitEnd))
		{
			cvs.drawRect( 40, 5, 40+((wBgn-limitBgn)*30/15), 30, paint3);
		}
		wEnd = Integer.parseInt(map.get("endTime"));
		if ((limitBgn <= wEnd) && (wEnd < limitEnd))
		{
			cvs.drawRect( 40+((wEnd-limitBgn)*30/15), 5, 580, 30, paint3);
		}

		map = schedList.get(pos);
		sNo = Integer.parseInt(map.get("schedNo"));
		for (k=0; k<sNo; k++)
		{
			wBgn = Integer.parseInt(map.get(String.format("bgnTm%d", (k+1))));
			wEnd = Integer.parseInt(map.get(String.format("endTm%d", (k+1))));
			if ((limitBgn <= wEnd) && (wBgn < limitEnd))
			{
				if (limitBgn > wBgn)	wBgn = limitBgn;
				if (wEnd < limitEnd)
						cvs.drawRect( 40+((wBgn-limitBgn)*30/15), 5,  40+((wEnd-limitBgn)*30/15), 30, paint4);
				else	cvs.drawRect( 40+((wBgn-limitBgn)*30/15), 5,  						 580, 30, paint4);
			}
		}

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