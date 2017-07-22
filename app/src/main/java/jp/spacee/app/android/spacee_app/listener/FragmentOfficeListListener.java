package jp.spacee.app.android.spacee_app.listener;

import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import org.json.JSONObject;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentOfficeList;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentOfficeListListener  implements  FragmentOfficeList.FragmentInteractionListener
{
	private						List<HashMap<String, String>>	officeNameList		= null;
	private						ArrayAdapter					adapter				= null;

	private						ListView						 officeList		= null;

	private						RelativeLayout 					errLayout			= null;
	private						TextView						title				= null;
	private						TextView						content				= null;
	private						ImageView						msgOff				= null;


	public  FragmentOfficeListListener()
	{
	}



	@Override
	public  void  onListProcess(View view)
	{
		int			i;
		String		wStr;

		String	result = SpaceeAppMain.httpCommGlueRoutines.retrieveProviderOffices();

		if (result != null)
		{
			try
			{
				JSONObject json = new JSONObject(result);
				if (json != null)
				{
//					String	rc = json.getString("status");
//					if (rc.equals("ok"))
//					{
						ReceiptTabApplication.Offices = new ArrayList<HashMap<String, String>>();
						org.json.JSONArray arr = json.getJSONArray("offices");
						if (arr != null)
						{
							for (i=0; i<arr.length(); i++)
							{
								HashMap<String, String>  map = new HashMap<String, String>();
								JSONObject json2 = arr.getJSONObject(i);
								map.put("id",   json2.getString("id"));
								map.put("name", json2.getString("name"));
								ReceiptTabApplication.Offices.add(map);
							}

							HashMap<String, String>  map = new HashMap<String, String>();
							map = ReceiptTabApplication.Offices.get(0);
							ReceiptTabApplication.officeId	 = map.get("id");
							ReceiptTabApplication.officeName = map.get("name");
						}
						else
						{
							showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null, "");
							return;
						}
//					}
//					else
//					{
//						showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), json, "");
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


		officeList	= (ListView)	view.findViewById(R.id.officeList);

		officeNameList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mapout;
		HashMap<String, String> mapin = new HashMap<String, String>();
		for (i=0; i< ReceiptTabApplication.Offices.size(); i++)
		{
			mapin	= ReceiptTabApplication.Offices.get(i);
			mapout = new HashMap<String, String>();
			mapout.put("id",	mapin.get("id"));
			mapout.put("name",	mapin.get("name"));
			officeNameList.add(mapout);
		}

		redrawRuleList(officeList);
	}


	private void redrawRuleList(ListView listView)
	{
		int			i;
		String		wk1;

		HashMap<String, String> mapin;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, android.R.layout.simple_list_item_1);

		for (i = 0; i < officeNameList.size(); i++)
		{
			mapin = officeNameList.get(i);
			adapter.add(mapin.get("name"));
		}
		listView.setAdapter(adapter);


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
			{
				HashMap<String, String> map;
				map = officeNameList.get(pos);
				ReceiptTabApplication.officeId = map.get("id");

				SpaceeAppMain.saveSharedPreferences();

				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HOME_CLICKED;
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
