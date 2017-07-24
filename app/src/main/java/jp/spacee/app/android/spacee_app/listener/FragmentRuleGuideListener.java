package jp.spacee.app.android.spacee_app.listener;


import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.content.Context;

import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentRuleGuide;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentRuleGuideListener  implements  FragmentRuleGuide.FragmentInteractionListener
{
	private						ListView						ruleIndexList	= null;
	private						TextView						ruleTitle		= null;
	private						TextView						explanation	= null;

	private						RelativeLayout 					errLayout		= null;
	private						TextView						title			= null;
	private						TextView						content			= null;
	private						ImageView						msgOff			= null;

	private						int								selectedENo	= -1;

	private						List<HashMap<String, String>>	ruleInfo		= null;

	private						SimpleAdapter					adapter			= null;


	public  FragmentRuleGuideListener()
	{
	}


	@Override
	public  void  onListProcess(View view)
	{
		ruleIndexList	= (ListView)	view.findViewById(R.id.ruleIndex);
		ruleTitle		= (TextView)	view.findViewById(R.id.ruleTitle);
		explanation	= (TextView)	view.findViewById(R.id.explanation);

		errLayout		= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title			= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content			= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff			= (ImageView)		errLayout.findViewById(R.id.messageOff);

		retrieveListInfo();

		redrawRuleList();
	}


	private void retrieveListInfo()
	{
		int		i, k;
		String	wStr;

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveUserGuide();
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				if (obj1 != null)
				{
//					String	rc = obj1.getString("status");
//					if (rc.equals("ok"))
//					{
						ruleInfo  = new ArrayList<HashMap<String, String>>();
						org.json.JSONObject obj2 = obj1.getJSONObject("guide");
						org.json.JSONArray  arr1 = obj2.getJSONArray("contents");
						if (arr1 != null)
						{
							for (i=0; i<arr1.length(); i++)
							{
								HashMap<String, String>  map = new HashMap<String, String>();
								org.json.JSONObject json1 = arr1.getJSONObject(i);
								map.put("title",	json1.getString("title"));
								map.put("body",		json1.getString("body"));
								ruleInfo.add(map);
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
	}


	private void redrawRuleList()
	{
		int			i;
		String		wk1;

		HashMap<String, String> mapin;
		HashMap<String, String> mapout;
		List<HashMap<String, String>> listInfo = new ArrayList<HashMap<String, String>>();

		for (i=0; i<ruleInfo.size(); i++)
		{
			mapin  = ruleInfo.get(i);
			mapout = new HashMap<String, String>();
			mapout.put("title", mapin.get("title"));
			listInfo.add(mapout);
		}


		adapter = new SimpleAdapter(ReceiptTabApplication.AppContext,
				listInfo,
				R.layout.layout_guide_list,
				new String[]{"title"},
				new int   []{R.id.title})
		{
			@Override
			public View getView(int pos, View cView, android.view.ViewGroup parent)
			{
				View retView = super.getView(pos, cView, parent);

				if (retView == null)
				{
					LayoutInflater inflater = (LayoutInflater) ReceiptTabApplication.AppContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					retView = inflater.inflate(R.layout.layout_guide_list, null);
				}

				TextView  title = (TextView)  retView.findViewById(R.id.title);
				ImageView mark  = (ImageView) retView.findViewById(R.id.mark);

				if (selectedENo == pos)
				{
					title.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.spacee_blue));
//					mark.setBackgroundDrawable(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.ic_arwright_selected));
					mark.setImageResource(R.drawable.ic_arwright_selected);
					retView.setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));
				}
				else if (selectedENo != -1)
				{
					title.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.sub));
//					mark.setBackgroundDrawable(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.ic_arwright));
					mark.setImageResource(R.drawable.ic_arwright);
					retView.setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.white));
				}
				else			//	初回のみ
				{
					title.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.spacee_blue));
//					mark.setBackgroundDrawable(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.ic_arwright_selected));
					mark.setImageResource(R.drawable.ic_arwright_selected);
					retView.setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.light_grey));

					HashMap<String, String>  map = new HashMap<String, String>();
					map = ruleInfo.get(0);

					ruleTitle.setText(map.get("title"));
					explanation.setText(map.get("body"));

					selectedENo = 0;
				}

				return  retView;
			}
		};
		ruleIndexList.setAdapter(adapter);
		ruleIndexList.setSelection(0);


		ruleIndexList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
			{
				selectedENo = pos;

				HashMap<String, String>  map = new HashMap<String, String>();
				map = ruleInfo.get(pos);

				ruleTitle.setText(map.get("title"));
				explanation.setText(map.get("body"));

				adapter.notifyDataSetChanged();
			}
		});
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	private  void  showErrorMsg(String ttl, org.json.JSONObject jsonObj, String orgMsg)
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