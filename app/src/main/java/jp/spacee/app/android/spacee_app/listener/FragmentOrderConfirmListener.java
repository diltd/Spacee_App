package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.Message;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentOrderConfirm;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentOrderConfirmListener  implements  FragmentOrderConfirm.FragmentInteractionListener
{
	private							ListView						paymentListView		= null;

	private							SimpleAdapter					adapter					= null;


	public  FragmentOrderConfirmListener()
	{
	}


	@Override
	public  void  onListEntrySelected(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_ORDER_CONFIRM_COMP;
		msg.arg1 = 1;											//	by LoginICClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnAgreeClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_ORDER_CONFIRM_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onListProcess(View view)
	{
		int			i;
		List<java.util.HashMap<String, String>>		payment = new ArrayList<java.util.HashMap<String, String>>();
		HashMap<String, String>  map = null;

		RelativeLayout	errLayout	= (RelativeLayout) view.findViewById(R.id.errorMessagePanel);
		TextView title		= (TextView)	errLayout.findViewById(R.id.errorTitle);
		TextView content	= (TextView)	errLayout.findViewById(R.id.errorMessage);
		ImageView msgOff	= (ImageView)	errLayout.findViewById(R.id.messageOff);

		ListView	paymentListView	= (ListView)	view.findViewById(R.id.paymentList);

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveUserPayments();
		if (result != null)
		{
			try
			{
				JSONObject obj1 = new JSONObject(result);
				JSONArray  arr1 = obj1.getJSONArray("cards");
				if (arr1 != null)
				{
					for (i=0; i<arr1.length(); i++)
					{
						JSONObject obj2 = arr1.getJSONObject(i);
						map = new HashMap<String, String>();
						map.put("kind",		"1");
						map.put("upper",	"クレジットカード支払い");
						map.put("lower",	String.format("****.****.****.%s", obj2.getString("last4")));
						payment.add(map);
					}
				}

				org.json.JSONArray  arr2 = obj1.getJSONArray("billing_destinations");
				if (arr2 != null)
				{
					for (i=0; i<arr2.length(); i++)
					{
						JSONObject obj2 = arr2.getJSONObject(i);
						map = new HashMap<String, String>();
						map.put("kind",		"2");
						map.put("upper",	"請求書払い");
						map.put("lower",	String.format("%s(%s)", obj2.getString("company_name"), obj2.getString("responsible_person")));
						payment.add(map);
					}
				}
			}
			catch (org.json.JSONException e)
			{
				e.printStackTrace();
				return;
			}

			if (payment.size() > 0)
			{
				adapter = new SimpleAdapter(ReceiptTabApplication.AppContext,
												payment,
												R.layout.layout_payment_list,
												new String[]{"kind",	"upper", 		"lower"},
												new int   []{R.id.kind, R.id.upperText, R.id.lowerText})
				{
					@Override
					public View getView(int pos, View cView, android.view.ViewGroup parent)
					{
						View retView = super.getView(pos, cView, parent);

						if (retView == null)
						{


						}

						TextView	kind = (TextView)	retView.findViewById(R.id.kind);
						ImageView	icon = (ImageView)	retView.findViewById(R.id.icon);
						if (kind.getText().toString().equals("1"))
								icon.setBackgroundResource(R.drawable.shape_oval_blue);
						else	icon.setBackgroundResource(R.drawable.shape_oval_gray);

						return  retView;
					}
				};
				paymentListView.setAdapter(adapter);

				paymentListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
				{
					public void onItemClick(android.widget.AdapterView<?> parent, View view, final int pos, long id)
					{




					}
				});
			}
			else
			{
				errLayout.setVisibility(View.VISIBLE);
				title.setText("エラー");
				content.setText("支払方法が登録されていません");

				jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =true;

				msgOff.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =false;

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
		else
		{
			errLayout.setVisibility(View.VISIBLE);
			title.setText("エラー");
			content.setText("支払方法が登録されていません");

			jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =true;

			msgOff.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =false;

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
}


