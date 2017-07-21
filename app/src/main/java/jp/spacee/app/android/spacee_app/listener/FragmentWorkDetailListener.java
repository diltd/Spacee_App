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
	private						ListView						priceList		= null;
	private 					TextView						amount			= null;
	private 					TextView						btnSelect		= null;

	private						RelativeLayout					errLayout		= null;
	private 					TextView						title			= null;
	private 					TextView						content			= null;
	private 					ImageView						msgOff			= null;

	private						String							desk_id			= "";

	private						List<HashMap<String, String>>	schedList		= null;

	private						int								minBookUnit	= 0;
	private						int								bookStep		= 0;



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
		android.os.Message msg = new android.os.Message();
		msg.what = SpaceeAppMain.MSG_WORK_DETAIL_COMP;
		msg.arg1 = 1;                                            //	by btnSelectClicked
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
		priceList		= (ListView)	view.findViewById(R.id.priceList);
		amount			= (TextView)	view.findViewById(R.id.amount);

		errLayout		= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		TextView	title	= (TextView)	errLayout.findViewById(R.id.errorTitle);
		TextView	content	= (TextView)	errLayout.findViewById(R.id.errorMessage);
		ImageView	msgOff	= (ImageView)	errLayout.findViewById(R.id.messageOff);


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
							wStr = "";
							for (i=0; i<arr1.length(); i++)
							{
								wStr += (String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_equipment), arr1.getString(i)));
							}
							facilities.setText(wStr);
							if (obj2.getInt("available_amount") > 0)
									status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_avail));
							else	status.setText(ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_full));
							availNo.setText(String.format("%s/%s", obj2.getInt("available_amount"), obj2.getInt("capacity"))
											+ ReceiptTabApplication.AppContext.getString(R.string.frag_work_detail_seat_no));

							minBookUnit	= obj2.getInt("min_booking_minutes");
							bookStep		= obj2.getInt("booking_minute_step");

							roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(detail_thumb_url);
							imgSpace.setImageBitmap(roomThumnails[0]);
						}
						else
						{
							showErrorMsg("エラー", null, "");
							return;
						}
//					}
//					else
//					{
//						showErrorMsg("エラー", obj1, "");
//						return;
//					}
				}
				else
				{
					showErrorMsg("エラー", null, "");
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
			showErrorMsg("通信エラー", null, "");
			return;
		}

		setSpinnerValue();

		//	物件の料金プランの取得
		Calendar	cal		= Calendar.getInstance();
		String		wTDate	= String.format("%04d%02d%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));

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
									return;
								}
								priceList.setAdapter(adapter);
							}
						}
						else
						{
							showErrorMsg("エラー", null, "");
							return;
						}
//					}
//					else
//					{
//						showErrorMsg("エラー", obj1, "");
//						return;
//					}
				}
				else
				{
					showErrorMsg("エラー", null, "");
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
			showErrorMsg("エラー", null, "");
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

		android.widget.ArrayAdapter<String> adapter1 = new android.widget.ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<24*60; i+=15)
		{
			wStr = String.format("%2d:%02d　", (Integer)(i/60), (Integer)(i%60));
			adapter1.add(wStr);
		}
		startTime.setAdapter(adapter1);


		android.widget.ArrayAdapter<String> adapter2 = new android.widget.ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<=24; i++)
		{
			wStr = String.format("%2d　", i);
			adapter2.add(wStr);
		}
		useHour.setAdapter(adapter2);


		if (bookStep == 0)		bookStep = 30;			//	dummy

		android.widget.ArrayAdapter<String> adapter3 = new android.widget.ArrayAdapter<String>(ReceiptTabApplication.AppContext, R.layout.spinner_item);
		adapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
		for (i=0; i<60; i+=bookStep)
		{
			wStr = String.format("%2d　", i);
			adapter3.add(wStr);
		}
		useMin.setAdapter(adapter3);
	}


	private  void  drawSchedule(ImageView view)
	{
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

		cvs.drawColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey_white));

		cvs.drawText("現在",   60, 120, paint1);
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
			else	errMsg = "データが取得できませんでした";
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

