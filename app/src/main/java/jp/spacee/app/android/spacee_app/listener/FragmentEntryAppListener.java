package jp.spacee.app.android.spacee_app.listener;

import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.RelativeLayout;

import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.fragment.FragmentEntryApp;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentEntryAppListener  implements  FragmentEntryApp.FragmentInteractionListener
{
	private 						boolean[]			fieldFilled		= new boolean[3];
	private							ImageView			btnSendSMS			= null;

	private							RelativeLayout		errLayout			= null;
	private							TextView			title				= null;
	private							TextView			content				= null;
	private							ImageView			msgOff				= null;


	public  FragmentEntryAppListener()
	{
	}


	@Override
	public  void  onBtnSendSMSClicked(View view)
	{
		String		telNo = "";

		EditText	smsNo1	= (EditText)	view.findViewById(R.id.smsNo1);
		EditText	smsNo2	= (EditText)	view.findViewById(R.id.smsNo2);
		EditText	smsNo3	= (EditText)	view.findViewById(R.id.smsNo3);

		errLayout	= (RelativeLayout)	view.findViewById(R.id.errorMessagePanel);
		title		= (TextView)		errLayout.findViewById(R.id.errorTitle);
		content		= (TextView)		errLayout.findViewById(R.id.errorMessage);
		msgOff		= (ImageView)		errLayout.findViewById(R.id.messageOff);

		telNo = smsNo1.getText().toString() + smsNo2.getText().toString() + smsNo3.getText().toString();
		String  result = SpaceeAppMain.httpCommGlueRoutines.sendSMS(telNo);
		if (result != null)
		{
			try
			{
				org.json.JSONObject obj1 = new org.json.JSONObject(result);
				if (obj1 != null)
				{
					String	rc	= obj1.getString("status");
					String	msg	= obj1.getString("message");
					if (rc.equals("ok"))
					{
						showMessage(true, msg);
					}
					else
					{
						showMessage(false, msg);
					}
				}
				else
				{
					showMessage(false, "");
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
			showMessage(false, "");
		}
	}


	@Override
	public  void  onBtnLoginICClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_APP_COMP;
		msg.arg1 = 2;											//	by LoginQRClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  onBtnLoginQRClicked(View view)
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_ENTRY_APP_COMP;
		msg.arg1 = 3;											//	by LoginPWClicked
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}


	@Override
	public  void  setBtnSendSMSView(View view)
	{
		btnSendSMS = (ImageView) view;
	}


	@Override
	public  void  setInputStatus(int eno, boolean sts)
	{
		fieldFilled[eno] = sts;

		if	(  (fieldFilled[0] == true)
			&& (fieldFilled[1] == true)
			&& (fieldFilled[2] == true) )
		{
			btnSendSMS.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.ic_middle_btnsend));
			btnSendSMS.setClickable(true);
			btnSendSMS.setEnabled(true);
		}
		else
		{
			btnSendSMS.setBackground(ReceiptTabApplication.AppContext.getResources().getDrawable(R.drawable.ic_middle_btnsend_disabled));
			btnSendSMS.setClickable(false);
			btnSendSMS.setEnabled(false);
		}
	}


	private  void  showMessage(boolean result, String msg)
	{
		if (result == true)
		{
			errLayout.setVisibility(View.VISIBLE);
			title.setText("送信メッセージ");
			content.setText(msg);

			jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =true;

			msgOff.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =false;

					Message msg = new Message();
					msg.what = SpaceeAppMain.MSG_ENTRY_APP_COMP;
					msg.arg1 = 1;
					///		SpaceeAppMain.mMsgHandler.sendMessage(msg);
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
		else
		{
			errLayout.setVisibility(View.VISIBLE);
			title.setText("SMS送信");
			if (msg.equals(""))
					content.setText("エラーが発生しました");
			else	content.setText(msg);

			jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =true;

			msgOff.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					jp.spacee.app.android.spacee_app.ReceiptTabApplication.isMsgShown =false;

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


