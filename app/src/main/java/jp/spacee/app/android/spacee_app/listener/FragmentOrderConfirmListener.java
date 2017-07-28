package jp.spacee.app.android.spacee_app.listener;


import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.LinearLayout;
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

	private							RelativeLayout 					errLayout				= null;
	private							TextView						title					= null;
	private							TextView						content					= null;
	private							ImageView						msgOff					= null;

	private							int								newPos					= 0;

	private							SimpleAdapter					adapter					= null;

	private							List<HashMap<String, String>>	payment					= null;



	public  FragmentOrderConfirmListener()
	{
	}


	@Override
	public  void  onListEntrySelected(View view)
	{


	}


	@Override
	public  void  onBtnShowDetailClicked(View view)
	{
		ReceiptTabApplication.userRegData		= null;
		ReceiptTabApplication.bookingRoomData	= null;

		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_ORDER_CONFIRM_COMP;
		msg.arg1 = 2;											//	by ShowDetailClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnUseCouponClicked(View view)
	{
		RelativeLayout	couponLayout = (RelativeLayout)	view.findViewById(R.id.couponLayout);
		LinearLayout	couponPanel1 = (LinearLayout)	view.findViewById(R.id.couponPanel1);

//		couponPanel1.setVisibility(View.INVISIBLE);
		couponLayout.setVisibility(View.VISIBLE);
	}


	@Override
	public  void  onBtnApplyCouponClicked(View view)
	{
		EditText	cpnNo	= (EditText)	view.findViewById(R.id.couponCode);
		String		cpnCode	= cpnNo.getText().toString();

		RelativeLayout	couponLayout = (RelativeLayout)	view.findViewById(R.id.couponLayout);
		couponLayout.setVisibility(View.INVISIBLE);

		LinearLayout	couponPanel1 = (LinearLayout)	view.findViewById(R.id.couponPanel1);
		couponPanel1.setVisibility(View.INVISIBLE);
		LinearLayout	couponPanel3 = (LinearLayout)	view.findViewById(R.id.couponPanel3);
		couponPanel3.setVisibility(View.VISIBLE);


		//	物件の料金計算
		String	result = SpaceeAppMain.httpCommGlueRoutines.retrieveRoomPrice(ReceiptTabApplication.bookingRoomData.roomId,
																			  ReceiptTabApplication.bookingRoomData.checkInTime,
																			  ReceiptTabApplication.bookingRoomData.occupiedMin,
																			  ReceiptTabApplication.bookingRoomData.numPsn,
																			  cpnCode);
		if (result != null)
		{
			try
			{
				JSONObject obj1 = new JSONObject(result);
				ReceiptTabApplication.bookingRoomData.payAmount  = obj1.getInt("total_price");
				JSONObject obj2 = obj1.getJSONObject("coupon");
				ReceiptTabApplication.bookingRoomData.discount	  = obj2.getInt("discount_price");
				ReceiptTabApplication.bookingRoomData.couponCode = cpnCode;
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
		}

		TextView	discount		= (TextView)	view.findViewById(R.id.discount);
		discount.setText(String.format("%,d", ReceiptTabApplication.bookingRoomData.discount));
		TextView	amount2			= (TextView)	view.findViewById(R.id.amount2);
		amount2.setText(String.format("%,d", ReceiptTabApplication.bookingRoomData.payAmount));
	}


	@Override
	public  void  onBtnCancelApplyClicked(View view)
	{
		RelativeLayout	couponLayout = (RelativeLayout)	view.findViewById(R.id.couponLayout);
		couponLayout.setVisibility(View.INVISIBLE);
	}


	@Override
	public  void  onBtnAgreeClicked(View view)
	{
		int		pre_booking_id;

		HashMap<String, String> map = new HashMap<String, String>();
		map = payment.get(newPos);

		ReceiptTabApplication.bookingRoomData.paidWay	= map.get("kind");
		ReceiptTabApplication.bookingRoomData.paidId	= map.get("id");

		String  result = SpaceeAppMain.httpCommGlueRoutines.bookingSpace();
		if (result != null)
		{
			try
			{
				JSONObject obj1 = new JSONObject(result);
				if (obj1 != null)
				{
					String	rc = obj1.getString("status");
					if (rc.equals("ok"))
					{
						pre_booking_id = obj1.getInt("pre_booking_id");
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

		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_ORDER_CONFIRM_COMP;
		msg.arg1 = 1;											//	by AgreeClicked
		msg.arg2 = pre_booking_id;
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onListProcess(View view)
	{
		int		i, wPrice;
		String	wStr1, wStr2;

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		payment	 = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>  map = null;

		ListView	paymentListView	= (ListView)	view.findViewById(R.id.paymentList);

		String  result = SpaceeAppMain.httpCommGlueRoutines.retrieveUserPayments();
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
						JSONArray  arr1 = obj1.getJSONArray("cards");
						if (arr1 != null)
						{
							for (i=0; i<arr1.length(); i++)
							{
								JSONObject obj2 = arr1.getJSONObject(i);
								map = new HashMap<String, String>();
								map.put("kind",		"1");
								map.put("upper",	ReceiptTabApplication.AppContext.getString(R.string.frag_order_confirm_pay_credit));
								map.put("lower",	String.format(ReceiptTabApplication.AppContext.getString(R.string.frag_order_confirm_card_id), obj2.getString("last4")));
								map.put("id", obj2.getString("id"));
								payment.add(map);
							}
						}
						else
						{
							showErrorMsg("エラー", null, "");
							return;
						}

						org.json.JSONArray  arr2 = obj1.getJSONArray("billing_destinations");
						if (arr2 != null)
						{
							for (i=0; i<arr2.length(); i++)
							{
								JSONObject obj2 = arr2.getJSONObject(i);
								map = new HashMap<String, String>();
								map.put("kind",		"2");
								map.put("upper",	ReceiptTabApplication.AppContext.getString(R.string.frag_order_confirm_pay_invoice));
								map.put("lower",	String.format("%s(%s)", obj2.getString("company_name"), obj2.getString("responsible_person")));
								map.put("id", obj2.getString("id"));
								payment.add(map);
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
//						showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj1, "");
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
						android.view.LayoutInflater inflater = (android.view.LayoutInflater) ReceiptTabApplication.AppContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
						retView = inflater.inflate(R.layout.layout_payment_list, null);
					}

					TextView	kind = (TextView)	retView.findViewById(R.id.kind);
					ImageView	icon = (ImageView)	retView.findViewById(R.id.icon);
					TextView	uTxt = (TextView)	retView.findViewById(R.id.upperText);
					TextView	lTxt = (TextView)	retView.findViewById(R.id.lowerText);
					ImageView	mark = (ImageView)	retView.findViewById(R.id.markCheck);

					if		(pos == newPos)
					{
						if (kind.getText().toString().equals("1"))
								icon.setBackgroundResource(R.drawable.ic_card_selected);
						else	icon.setBackgroundResource(R.drawable.ic_invoice_selected);
						uTxt.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.spacee_blue_dark));
						lTxt.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.spacee_blue_dark));
						mark.setBackgroundResource(R.drawable.ic_check);
					}
					else
					{
						if (kind.getText().toString().equals("1"))
								icon.setBackgroundResource(R.drawable.ic_card);
						else	icon.setBackgroundResource(R.drawable.ic_invoice);
						uTxt.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.text_black));
						lTxt.setTextColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.text_black));
						mark.setBackgroundColor(ReceiptTabApplication.AppContext.getResources().getColor(R.color.white));
					}
					return  retView;
				}
			};
			paymentListView.setAdapter(adapter);
			paymentListView.setSelection(0);

			paymentListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener()
			{
				public void onItemClick(android.widget.AdapterView<?> parent, View view, final int pos, long id)
				{
					newPos = pos;
					adapter.notifyDataSetChanged();
				}
			});
		}
		else
		{
///			showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), null,
///						 ReceiptTabApplication.AppContext.getResources().getString(R.string.frag_order_confirm_error_msg));
		}

		//	各フィールドを設定する
		TextView	name		= (TextView)	view.findViewById(R.id.areaName);
		TextView	tgtMonth	= (TextView)	view.findViewById(R.id.targetMonth);
		TextView	tgtDay		= (TextView)	view.findViewById(R.id.targetDay);
		TextView	checkIn		= (TextView)	view.findViewById(R.id.checkInTime);
		TextView	checkOut	= (TextView)	view.findViewById(R.id.checkOutTime);
		TextView	numPsn		= (TextView)	view.findViewById(R.id.numPsn);
		ListView	planList	= (ListView)	view.findViewById(R.id.planList);
		TextView	amount1		= (TextView)	view.findViewById(R.id.amount1);
		TextView	amount2		= (TextView)	view.findViewById(R.id.amount2);

		name.setText(ReceiptTabApplication.bookingRoomData.namePlace);
		tgtMonth.setText(String.format("%02d", ReceiptTabApplication.bookingRoomData.useMonth));
		tgtDay.setText(String.format("%02d", ReceiptTabApplication.bookingRoomData.useDay));
		checkIn.setText(ReceiptTabApplication.bookingRoomData.checkInTime);
		checkOut.setText(ReceiptTabApplication.bookingRoomData.checkOutTime);
		numPsn.setText("" + ReceiptTabApplication.bookingRoomData.numPsn);
		amount1.setText(String.format("%,d", ReceiptTabApplication.bookingRoomData.TotalPrice));
		amount2.setText(String.format("%,d", ReceiptTabApplication.bookingRoomData.payAmount));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReceiptTabApplication.AppContext, android.R.layout.simple_list_item_1);
		HashMap<String, String> mapin = new HashMap<String, String>();
		for (i=0; i<ReceiptTabApplication.bookingRoomData.pricePlan.size(); i++)
		{
			mapin = ReceiptTabApplication.bookingRoomData.pricePlan.get(i);
			wStr1  = mapin.get("bgnTime");
			wStr2  = mapin.get("endTime");
			wPrice = Integer.parseInt(mapin.get("price"));
			adapter.add(String.format("%s-%s%,10d円/1h", wStr1, wStr2, wPrice));
		}
		planList.setAdapter(adapter);
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

