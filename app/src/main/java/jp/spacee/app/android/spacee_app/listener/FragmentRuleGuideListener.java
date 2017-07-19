package jp.spacee.app.android.spacee_app.listener;


import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
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

	private						int								selectedENo	= -1;

	private						List<HashMap<String, String>>	ruleInfo		= null;

	private						SimpleAdapter					adapter			= null;


	public  FragmentRuleGuideListener()
	{
	}


	@Override
	public  void  onListProcess(ListView view1, TextView view2, TextView view3)
	{
		ruleIndexList	= view1;
		ruleTitle		= view2;
		explanation	= view3;

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
				ruleInfo  = new ArrayList<HashMap<String, String>>();
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
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

				}

			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return;
			}
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
}