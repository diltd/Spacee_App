package jp.spacee.app.android.spacee_app.listener;


import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import jp.spacee.app.android.spacee_app.BuildConfig;
import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.web.GmoTokenCallbackInterface;


public  class  FragmentEntryPolicyListener  implements  jp.spacee.app.android.spacee_app.fragment.FragmentEntryPolicy.FragmentInteractionListener,
		GmoTokenCallbackInterface.GmoTokenCallbackListener
{
	public static final String TAG = "FragmentEntryPolicyL";
	private						TextView				btnAgree				= null;
	private						ImageView				chkBoxAgree			= null;

	private						RelativeLayout			errLayout				= null;
	private						TextView				title					= null;
	private						TextView				content					= null;
	private						ImageView				msgOff					= null;
	private WebView mWebView;

	private						boolean					statusChkBox			= false;


	public  FragmentEntryPolicyListener()
	{
	}


	@Override
	public  void  onBtnAgreeClicked(View view)
	{
		int		i, k;
		String	status, message;
		String	result = null;

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);
		mWebView = (WebView) view.findViewById(R.id.webView);

		result = jp.spacee.app.android.spacee_app.activity.SpaceeAppMain.httpCommGlueRoutines.signupUser();
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				status	= obj1.getString("status");

				if (status.equals("ok"))
				{
					message	= obj1.getString("message");
					ReceiptTabApplication.userAuthToken = obj1.getString("auth_token");

					if (jp.spacee.app.android.spacee_app.ReceiptTabApplication.userRegData.paymentKind == 1)			//	カードで登録
					{
						// card_token を取得します。
						callGetGmoToken();

						// FIXME: クレジットカード登録API呼び出しは onSuccess 時に行って下さい。
//						result = SpaceeAppMain.httpCommGlueRoutines.registerCreditCardInfo();
//
//						if (result != null)
//						{
//							try
//							{
//								org.json.JSONObject obj2 = new org.json.JSONObject(result);
//								status	= obj2.getString("status");
//
//								if (status.equals("ok"))
//								{
//									result = obj2.getString("card_id");
//									//	resultにはcard_idが入っている		<<<<<<<<<<<<<<<<<	どこにどうする？
//
//									Message msg = new Message();
//									msg.what = SpaceeAppMain.MSG_ENTRY_POLICY_COMP;
//									msg.arg1 = 1;											//	by btnAgree Clicked
//									SpaceeAppMain.mMsgHandler.sendMessage(msg);
//								}
//								else
//								{
//									showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj2, "");
//								}
//							}
//							catch (org.json.JSONException e)
//							{
//								e.printStackTrace();
//								return;
//							}
//						}
//						else
//						{
//							showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title2), null, "");
//						}
					}
					else
					{
						result = SpaceeAppMain.httpCommGlueRoutines.registerBillingDestination();

						if (result != null)
						{
							try
							{
								org.json.JSONObject obj2 = new org.json.JSONObject(result);
								status	= obj2.getString("status");

								if (status.equals("ok"))
								{
									result = obj2.getString("billing_destination_id");
									//	resultにはbilling_destination_idが入っている		<<<<<<<<<<<<<<<<<	どこにどうする？

									Message msg = new Message();
									msg.what = SpaceeAppMain.MSG_ENTRY_POLICY_COMP;
									msg.arg1 = 1;											//	by btnAgree Clicked
									SpaceeAppMain.mMsgHandler.sendMessage(msg);
								}
								else
								{
									showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj2, "");
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
				}
				else
				{
					showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj1, "");
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


	@Override
	public  void  onLayoutAgreeClicked(android.view.View view)
	{
		if (statusChkBox == false)
		{
			chkBoxAgree.setImageResource(R.drawable.ic_middle_checkbox_selected);
			btnAgree.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_blue));
			statusChkBox = true;
		}
		else
		{
			chkBoxAgree.setImageResource(R.drawable.ic_middle_checkbox);
			btnAgree.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_gray));
			statusChkBox = false;
		}
	}


	@Override
	public  void  setAgreeButtons(View view1, View view2)
	{
		btnAgree		= (TextView) view1;
		chkBoxAgree	= (ImageView) view2;

		chkBoxAgree.setImageResource(R.drawable.ic_middle_checkbox);
		btnAgree.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.shape_button_gray));
		statusChkBox = false;
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
				JSONArray arr1 = jsonObj.getJSONArray("error_messages");
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

				Message msg = new Message();
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

	private void callGetGmoToken() {
		StringBuilder builder = new StringBuilder();
		builder.append("javascript:window.getGmoToken('")
				.append(BuildConfig.GMO_SHOP_ID).append("','")
				.append(ReceiptTabApplication.userRegData.cardNo).append("','")
				.append(ReceiptTabApplication.userRegData.cardExpireYear)
				.append(ReceiptTabApplication.userRegData.cardExpireMonth).append("','")
				.append(ReceiptTabApplication.userRegData.secretCode).append("','")
				.append(ReceiptTabApplication.userRegData.cardHolder).append("');");
		mWebView.loadUrl(builder.toString());
	}

	@Override
	public void onSuccess(String token)
	{
		ReceiptTabApplication.userRegData.cardToken = token;

		String	result = SpaceeAppMain.httpCommGlueRoutines.registerCreditCardInfo();

		if (result != null)
		{
			try
			{
				org.json.JSONObject obj2 = new org.json.JSONObject(result);
				String	status	= obj2.getString("status");

				if (status.equals("ok"))
				{
					result = obj2.getString("card_id");
					//	resultにはcard_idが入っている		<<<<<<<<<<<<<<<<<	どこにどうする？

					Message msg = new Message();
					msg.what = SpaceeAppMain.MSG_ENTRY_POLICY_COMP;
					msg.arg1 = 1;											//	by btnAgree Clicked
					SpaceeAppMain.mMsgHandler.sendMessage(msg);
				}
				else
				{
					showErrorMsg(ReceiptTabApplication.AppContext.getResources().getString(R.string.error_title1), obj2, "");
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
		}
	}

	@Override
	public void onError(String code) {
		// TODO: エラー表示して下さい。
		Log.d(TAG, "error code:" + code);
	}
}