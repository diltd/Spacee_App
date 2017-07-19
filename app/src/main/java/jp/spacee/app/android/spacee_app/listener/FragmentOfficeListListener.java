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


	public  FragmentOfficeListListener()
	{
	}



	@Override
	public  void  onListProcess(View view)
	{
		int			i;
		String		wStr;

		boolean  rc = SpaceeAppMain.httpCommGlueRoutines.retrieveProviderOffices();
		if (rc == true)
		{
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
		else
		{
			RelativeLayout	errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
			TextView		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
			TextView		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
			ImageView		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

			errLayout.setVisibility(View.VISIBLE);
			title.setText("通信エラー");
			content.setText("データが取得できません");

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

			//	メッセージの下のエレメントをタップしても拾わないようにするため
			errLayout.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
				}
			});
		}
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
}
