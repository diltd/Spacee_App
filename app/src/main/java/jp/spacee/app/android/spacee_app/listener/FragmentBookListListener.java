package jp.spacee.app.android.spacee_app.listener;


import android.widget.ListView;
import android.os.Message;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
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

	private						List<HashMap<String, String>>	seatsList		= null;
	private						SimpleAdapter 					adapter			= null;

	private						Bitmap[]						roomThumnails	= null;



	public  FragmentBookListListener()
	{
	}


	public void onListProcess(View view)
	{
		bookListView = (ListView)	view.findViewById(R.id.workSeats) ;

		retrieveBookingData(view);

		redrawSeatsList(bookListView);
	}


	private void retrieveBookingData(View view)
	{
		int		i, k;
		String	wStr;

		RelativeLayout	errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		TextView		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		TextView		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		ImageView		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveBookingList("present");
		if (result != null)
		{
			try
			{
				seatsList = new ArrayList<HashMap<String, String>>();
				JSONObject	obj1 = new JSONObject(result);
				JSONArray	arr1 = obj1.getJSONArray("pre_bookings");
				if (arr1 != null)
				{
					for (i=0; i<arr1.length(); i++)
					{
						java.util.HashMap<String, String> map = new java.util.HashMap<String, String>();
						JSONObject obj2 = arr1.getJSONObject(i);
						JSONObject obj3 = obj2.getJSONObject("listing");
						map.put("id",			"" + obj3.getInt("id"));
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
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
							Date wDate = sdf.parse(obj5.getString("start_at"));
							map.put("startDate", new SimpleDateFormat("MM月dd日").format(wDate));
							map.put("startTime", new SimpleDateFormat("hh:mm").format(wDate));
							wDate =  sdf.parse(obj5.getString("end_at"));
							map.put("endDate", new SimpleDateFormat("MM月dd日").format(wDate));
							map.put("endTime", new SimpleDateFormat("hh:mm").format(wDate));
						}
						catch (java.text.ParseException e)
						{
							e.printStackTrace();
						}
						map.put("price",		obj5.getString("price"));
						seatsList.add(map);
					}
				}

				if (seatsList.size() == 0)
				{
					errLayout.setVisibility(View.VISIBLE);
					title.setText("予約なし");
					content.setText("予約がありません");

					ReceiptTabApplication.isMsgShown =true;

					msgOff.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							ReceiptTabApplication.isMsgShown =false;

							Message msg = new Message();
							msg.what = SpaceeAppMain.MSG_HOME_CLICKED;
							SpaceeAppMain.mMsgHandler.sendMessage(msg);
						}
					});
				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return;
			}

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
			errLayout.setVisibility(View.VISIBLE);
			title.setText("通信エラー");
			content.setText("データがありません");

			ReceiptTabApplication.isMsgShown =true;

			msgOff.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ReceiptTabApplication.isMsgShown =false;

					Message msg = new Message();
					msg.what = SpaceeAppMain.MSG_HOME_CLICKED;
					SpaceeAppMain.mMsgHandler.sendMessage(msg);
				}
			});
		}
	}


	private void redrawSeatsList(ListView listView)
	{
		int			i;
		String		wk1;

		adapter = new SimpleAdapter(ReceiptTabApplication.AppContext,
									seatsList,
									R.layout.layout_list_book,
									new		String[]{"startDate",	 "endDate",	 "startTime",	 "endTime",	"subtitle", 	 "equipments"},
									new		int[]   {R.id.dateBegin, R.id.dateEnd, R.id.timeBegin, R.id.timeEnd, R.id.placeName, R.id.info2})
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

				//	status1/status2の設定が必要


				ImageView iv = (ImageView) retView.findViewById(R.id.imgSpace);
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
};


