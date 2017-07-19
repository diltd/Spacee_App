package jp.spacee.app.android.spacee_app.listener;


import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentEntryPolicy;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentEntryPolicyListener  implements  jp.spacee.app.android.spacee_app.fragment.FragmentEntryPolicy.FragmentInteractionListener
{
	private						TextView				btnAgree				= null;
	private						ImageView				chkBoxAgree			= null;
	private						RelativeLayout			errLayout				= null;

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

		errLayout = (RelativeLayout) view.findViewById(R.id.errorMessagePanel);
		TextView	title	= (TextView)	errLayout.findViewById(R.id.errorTitle);
		TextView	content	= (TextView)	errLayout.findViewById(R.id.errorMessage);
		ImageView	msgOff	= (ImageView)	errLayout.findViewById(R.id.messageOff);


		result = jp.spacee.app.android.spacee_app.activity.SpaceeAppMain.httpCommGlueRoutines.signupUser();
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				status	= obj1.getString("status");
				message	= obj1.getString("message");
				jp.spacee.app.android.spacee_app.ReceiptTabApplication.userAuthToken = obj1.getString("auth_token");

				if (status.equals("ok"))
				{
					if (jp.spacee.app.android.spacee_app.ReceiptTabApplication.userRegData.paymentKind == 1)			//	カードで登録
					{
						result = SpaceeAppMain.httpCommGlueRoutines.registerCreditCardInfo();
						if (result != null)
						{
							//	resultにはcard_idが入っている		<<<<<<<<<<<<<<<<<	どこにどうする？


							Message msg = new Message();
							msg.what = SpaceeAppMain.MSG_ENTRY_POLICY_COMP;
							msg.arg1 = 1;											//	by btnAgree Clicked
							SpaceeAppMain.mMsgHandler.sendMessage(msg);
						}
						else
						{
							errLayout.setVisibility(View.VISIBLE);
							title.setText("通信エラー");
							content.setText("クレジットカード登録が失敗しました");

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
					}
					else
					{
						result = SpaceeAppMain.httpCommGlueRoutines.registerBillingDestination();
						if (result != null)
						{
							//	resultにはbilling_destination_idが入っている		<<<<<<<<<<<<<<<<<	どこにどうする？


							Message msg = new Message();
							msg.what = SpaceeAppMain.MSG_ENTRY_POLICY_COMP;
							msg.arg1 = 1;											//	by btnAgree Clicked
							SpaceeAppMain.mMsgHandler.sendMessage(msg);
						}
						else
						{
							errLayout.setVisibility(View.VISIBLE);
							title.setText("通信エラー");
							content.setText("請求先登録が失敗しました");

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
};


