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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;


public  class  FragmentProviderLogin  extends  Fragment
{
	private						TextView						btnLoginPW			= null;
	private						EditText						editID				= null;
	private						EditText						editPW				= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentProviderLogin()
	{
	}


	public  static  FragmentProviderLogin  newInstance(String param1, String param2)
	{
		FragmentProviderLogin fragment = new FragmentProviderLogin();
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
		void	onBtnLoginPWClicked		(View view);
		void	setBtnLoginPWView		(View view);
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
		final  View  view = inflater.inflate(R.layout.fragment_provider_login, container, false);

		btnLoginPW		= (TextView)	view.findViewById(R.id.btnLoginPW);
		editID			= (EditText)	view.findViewById(R.id.editID);
		editPW			= (EditText)	view.findViewById(R.id.editPW);


		// set paddingLeft & Right to EditText		AppCompat-V7 バグ対応
		float	density		= getResources().getDisplayMetrics().density;
		int		paddingH	= (int) (16 * density);
		editID.setPadding(paddingH, editID.getPaddingTop(), paddingH, editID.getPaddingBottom());
		editPW.setPadding(paddingH, editPW.getPaddingTop(), paddingH, editPW.getPaddingBottom());


		editID.addTextChangedListener(new android.text.TextWatcher()
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
				mListener.setInputStatus(0, (s.toString().length() > 0));
			}
		});


		editPW.addTextChangedListener(new android.text.TextWatcher()
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
				mListener.setInputStatus(1, (s.toString().length() >= 8));
			}
		});


		//		エンターキ―がおされたらActionBarを閉じる
		editID.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, android.view.KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});

		editPW.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, android.view.KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});
		//		エンターキ―がおされたらActionBarを閉じる


		btnLoginPW.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onBtnLoginPWClicked(view);				//	view, not v
			}
		});


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.setBtnLoginPWView(btnLoginPW);


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