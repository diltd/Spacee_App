package jp.spacee.app.android.spacee_app.listener;


import android.widget.ListView;
import android.os.Message;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.view.LayoutInflater;
import android.content.Context;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.fragment.FragmentBookList;


public  class  FragmentBookListListener  implements  FragmentBookList.FragmentInteractionListener
{
	private						ListView						bookListView	= null;

	private						RelativeLayout 					errLayout		= null;
	private						TextView						title			= null;
	private						TextView						content			= null;
	private						ImageView						msgOff			= null;

	private						List<HashMap<String, String>>	seatsList		= null;
	private						SimpleAdapter 					adapter			= null;

	private						Bitmap[]						roomThumnails	= null;



	public  FragmentBookListListener()
	{
	}


	public void onListProcess(View view)
	{
		bookListView = (ListView)	view.findViewById(R.id.workSeats) ;

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		retrieveBookingData(view);

		redrawSeatsList(bookListView);
	}


	private void retrieveBookingData(View view)
	{
		int		i, k;
		String	wStr;

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveBookingList("present");
		if (result != null)
		{
			try
			{
				JSONObject	obj1 = new JSONObject(result);
				if (obj1 != null)
				{
//					String	rc = obj1.getString("status");
//					if (rc.equals("ok"))
//					{
						seatsList = new ArrayList<HashMap<String, String>>();
						JSONArray	arr1 = obj1.getJSONArray("pre_bookings");
						if (arr1 != null)
						{
							for (i=0; i<arr1.length(); i++)
							{
								java.util.HashMap<String, String> map = new java.util.HashMap<String, String>();
								JSONObject obj2 = arr1.getJSONObject(i);
								JSONObject obj3 = obj2.getJSONObject("listing");
								map.put("title",		obj3.getString("title"));
								JSONObject obj4 = new JSONObject(obj3.getString("thumb"));
								map.put("thumb_url",	obj4.getString("url"));
								map.put("subtitle",	obj3.getString("subtitle"));
								JSONArray  arr2 = obj3.getJSONArray("equipments");
								wStr = "";
								for (k=0; k<arr2.length(); k++)
								{
									if (wStr.equals(""))
											wStr += arr2.getString(k);
									else	wStr += ("/" + arr2.getString(k));
								}
								map.put("equipments",	wStr);
								JSONObject obj5 = obj2.getJSONObject("pre_booking");
								try
								{
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
									Date wDate = sdf.parse(obj5.getString("start_at"));
									map.put("start_at", obj5.getString("start_at"));
									map.put("startDate", new SimpleDateFormat("MM月dd日").format(wDate));
									map.put("startTime", new SimpleDateFormat("HH:mm").format(wDate));
									wDate =  sdf.parse(obj5.getString("end_at"));
									map.put("end_at", obj5.getString("end_at"));
									map.put("endDate", new SimpleDateFormat("MM月dd日").format(wDate));
									map.put("endTime", new SimpleDateFormat("HH:mm").format(wDate));
									map.put("id",		"" + obj5.getInt("id"));
								}
								catch (java.text.ParseException e)
								{
									e.printStackTrace();
								}
								map.put("price",		obj5.getString("price"));
								seatsList.add(map);
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
//						showErrorMsg("エラー", obj1, "");
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


		if (seatsList.size() > 0)
		{
			String[]	urls = new String[seatsList.size()];
			for (i=0; i<seatsList.size(); i++)
			{
				HashMap<String, String> map = new HashMap<String, String>();
				map = seatsList.get(i);
				urls[i] = map.get("thumb_url");
			}
			roomThumnails = SpaceeAppMain.httpCommGlueRoutines.downloadBitmaps(urls);
		}
		else
		{
			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_booklist_error_title), null,
						 ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_booklist_error_msg));
			return;
		}
	}


	private void redrawSeatsList(ListView listView)
	{
		int			i;
		String		wk1;

		adapter = new SimpleAdapter(ReceiptTabApplication.AppContext,
									seatsList,
									R.layout.layout_list_book,
									new		String[]{"startDate",	 "endDate",	 "startTime",	 "endTime",	"subtitle", 	  "title",	   "equipments"},
									new		int[]   {R.id.dateBegin, R.id.dateEnd, R.id.timeBegin, R.id.timeEnd, R.id.placeName, R.id.info1, R.id.info2})
		{
			@Override
			public View getView(int pos, View cView, ViewGroup parent)
			{
				View retView = super.getView(pos, cView, parent);

				if (retView == null)
				{
					LayoutInflater inflater = (LayoutInflater) ReceiptTabApplication.AppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					retView = inflater.inflate(R.layout.layout_list_book, null);
				}

				HashMap<String, String> map = seatsList.get(pos);
				Date	bDate = null;
				Date	eDate = null;
				Date	now	  = null;
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					bDate = sdf.parse(map.get("start_at"));
					eDate = sdf.parse(map.get("end_at"));
					now   = cal.getTime();
				}
				catch (java.text.ParseException e)
				{
					e.printStackTrace();
					return	 retView;
				}

				TextView	status1  = (TextView)	retView.findViewById(R.id.status1);
				TextView	status2  = (TextView)	retView.findViewById(R.id.status2);
				TextView	residual = (TextView)	retView.findViewById(R.id.residual);
				if ((bDate.compareTo(now) <= 0) && (now.compareTo(eDate) <= 0))
				{
					status1.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.xml_book_list_space_avail));
					status1.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_oval_blue));
					status2.setVisibility(View.INVISIBLE);
					residual.setVisibility(View.INVISIBLE);
				}
				else
				{
					status1.setText(ReceiptTabApplication.AppContext.getResources().getString(R.string.xml_book_list_space_not_avail));
					status1.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_oval_gray));
					status2.setVisibility(View.VISIBLE);
					residual.setVisibility(View.VISIBLE);

					long diff = ((long) bDate.getTime() - (long) now.getTime()) / 1000;				//	sec
					residual.setText(String.format("%02d:%02d", diff/60, diff%60));
				}

				ImageView iv = (ImageView) retView.findViewById(R.id.thumbnail);
				if (roomThumnails != null)
				{
					iv.setImageBitmap(roomThumnails[pos]);
				}

				return  retView;
			}
		};
		listView.setAdapter(adapter);


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
			{
				HashMap<String, String> map;
				map = seatsList.get(pos);

				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_BOOK_LIST_COMP;
				msg.arg1 = Integer.parseInt(map.get("id"));	//	id
				msg.obj  = map.get("subtitle");				//	次のタイトル用
				SpaceeAppMain.mMsgHandler.sendMessage(msg);
			}
		});
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

