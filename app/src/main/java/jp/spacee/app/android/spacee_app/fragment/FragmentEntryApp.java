package jp.spacee.app.android.spacee_app.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;


public  class  FragmentEntryApp  extends  Fragment
{
	private						ImageView						btnSendSMS			= null;
	private						TextView						btnLoginIC			= null;
	private						TextView						btnLoginQR			= null;
	private						EditText						smsNo1				= null;
	private						EditText						smsNo2				= null;
	private						EditText						smsNo3				= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentEntryApp()
	{
	}


	public  static  FragmentEntryApp  newInstance(String param1, String param2)
	{
		FragmentEntryApp fragment = new FragmentEntryApp();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}


	@Override
	public  void  onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}


	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
/*
		//	Activity側からsetOnxxxxxxxxListenerを呼び出してListenerを設定する
		if (context instanceof CheckBookingListener)
		  {
			mListener = (CheckBookingListener) context;
		  }
		 else
		  {
			throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
		  }
*/
	}


	@Override
	public  void  onDetach()
	{
		super.onDetach();

		mListener = null;
	}


	public  interface  FragmentInteractionListener
	{
		// ここにイベントハンドリング用の関数を羅列する
		void	onBtnSendSMSClicked		(View view);
		void	onBtnLoginICClicked		(View view);
		void	onBtnLoginQRClicked		(View view);
		void	setBtnSendSMSView		(View view);
		void	setInputStatus			(int eno, boolean  sts);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public  View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		final View  view = inflater.inflate(R.layout.fragment_entry_app, container, false);

		btnSendSMS		= (ImageView)	view.findViewById(R.id.btnSendSMS);
		btnLoginIC		= (TextView)	view.findViewById(R.id.btnLoginIC);
		btnLoginQR		= (TextView)	view.findViewById(R.id.btnLoginQR);
		smsNo1			= (EditText)	view.findViewById(R.id.smsNo1);
		smsNo2			= (EditText)	view.findViewById(R.id.smsNo2);
		smsNo3			= (EditText)	view.findViewById(R.id.smsNo3);

		// set paddingLeft & Right to EditText		AppCompat-V7 バグ対応
		float	density		= getResources().getDisplayMetrics().density;
		int		paddingH	= (int) (16 * density);
		smsNo1.setPadding(paddingH, smsNo1.getPaddingTop(), paddingH, smsNo1.getPaddingBottom());
		smsNo2.setPadding(paddingH, smsNo2.getPaddingTop(), paddingH, smsNo2.getPaddingBottom());
		smsNo3.setPadding(paddingH, smsNo3.getPaddingTop(), paddingH, smsNo3.getPaddingBottom());


		btnSendSMS.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
				SpaceeAppMain.mMsgHandler.sendMessage(msg);

				mListener.onBtnSendSMSClicked(view);				//	not v, view		全体を送る事
			}
		});


		btnLoginIC.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
				SpaceeAppMain.mMsgHandler.sendMessage(msg);

				mListener.onBtnLoginICClicked(v);
			}
		});


		btnLoginQR.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
				SpaceeAppMain.mMsgHandler.sendMessage(msg);

				mListener.onBtnLoginQRClicked(v);
			}
		});


		smsNo1.addTextChangedListener(new android.text.TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(android.text.Editable s)
			{
				mListener.setInputStatus(0, (s.toString().length() == 3));

				if (s.toString().length() == 3)		smsNo2.requestFocus(View.FOCUS_UP);
			}
		});


		smsNo2.addTextChangedListener(new android.text.TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(android.text.Editable s)
			{
				mListener.setInputStatus(1, (s.toString().length() == 4));

				if (s.toString().length() == 4)		smsNo3.requestFocus(View.FOCUS_UP);
			}
		});


		smsNo3.addTextChangedListener(new android.text.TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(android.text.Editable s)
			{
				mListener.setInputStatus(2, (s.toString().length() == 4));
			}
		});


		//		エンターキ―がおされたらActionBarを閉じる
		smsNo1.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		smsNo2.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		smsNo3.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});
		//		エンターキ―がおされたらActionBarを閉じる


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.setBtnSendSMSView(btnSendSMS);


		return  view;
	}


	private  boolean  sendHideActionBar(int keyCode, KeyEvent event)
	{
		if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER))
		{
			Message msg = new Message();
			msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
			SpaceeAppMain.mMsgHandler.sendMessage(msg);
		}

		return  false;
	}
}
