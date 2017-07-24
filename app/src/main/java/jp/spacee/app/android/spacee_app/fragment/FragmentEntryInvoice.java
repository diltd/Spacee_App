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


public  class  FragmentEntryInvoice  extends  Fragment
{
	private						TextView						kindCard			= null;
	private						TextView						btnInputComp		= null;
	private						EditText						companyName		= null;
	private						EditText						staffName			= null;
	private						EditText						postCode			= null;
	private						EditText						companyAddr1		= null;
	private						EditText						companyAddr2		= null;
	private						EditText						companyPhone		= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentEntryInvoice()
	{
	}


	public  static  FragmentEntryInvoice  newInstance(String param1, String param2)
	{
		FragmentEntryInvoice fragment = new FragmentEntryInvoice();
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
		void	onKindCardSelected		(View view);
		void	onBtnInputCompClicked	(View view);
		void	setBtnInputCompView		(View view);
		void	killFocusPostCode		(View view);
		void	setInputStatus			(int eno, boolean sts);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public  View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		final  View  view = inflater.inflate(R.layout.fragment_entry_invoice, container, false);

		kindCard		= (TextView)	view.findViewById(R.id.kindCard);
		btnInputComp	= (TextView)	view.findViewById(R.id.btnInputComp);
		companyName	= (EditText)	view.findViewById(R.id.companyName);
		staffName		= (EditText)	view.findViewById(R.id.staffName);
		postCode		= (EditText)	view.findViewById(R.id.postCode);
		companyAddr1	= (EditText)	view.findViewById(R.id.companyAddr1);
		companyAddr2	= (EditText)	view.findViewById(R.id.companyAddr2);
		companyPhone	= (EditText)	view.findViewById(R.id.companyPhone);


		// set paddingLeft & Right to EditText		AppCompat-V7 バグ対応
		float	density		= getResources().getDisplayMetrics().density;
		int		paddingH	= (int) (16 * density);
		companyName.setPadding(paddingH, companyName.getPaddingTop(), paddingH, companyName.getPaddingBottom());
		staffName.setPadding(paddingH, staffName.getPaddingTop(), paddingH, staffName.getPaddingBottom());
		postCode.setPadding(paddingH, postCode.getPaddingTop(), paddingH, postCode.getPaddingBottom());
		companyAddr1.setPadding(paddingH, companyAddr1.getPaddingTop(), paddingH, companyAddr1.getPaddingBottom());
		companyAddr2.setPadding(paddingH, companyAddr2.getPaddingTop(), paddingH, companyAddr2.getPaddingBottom());
		companyPhone.setPadding(paddingH, companyPhone.getPaddingTop(), paddingH, companyPhone.getPaddingBottom());

		//	改行キーで次のフィールドへ移行させる
		companyName.setNextFocusDownId(R.id.staffName);
		staffName.setNextFocusDownId(R.id.postCode);
		postCode.setNextFocusDownId(R.id.companyAddr1);
		companyAddr1.setNextFocusDownId(R.id.companyAddr2);
		companyAddr2.setNextFocusDownId(R.id.companyPhone);


		companyName.addTextChangedListener(new android.text.TextWatcher()
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


		staffName.addTextChangedListener(new android.text.TextWatcher()
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
				mListener.setInputStatus(1, (s.toString().length() > 0));
			}
		});


		postCode.addTextChangedListener(new android.text.TextWatcher()
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
				mListener.setInputStatus(2, (s.toString().length() == 7));
			}
		});


		companyAddr1.addTextChangedListener(new android.text.TextWatcher()
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
				mListener.setInputStatus(3, (s.toString().length() > 0));
			}
		});


		companyAddr2.addTextChangedListener(new android.text.TextWatcher()
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
				mListener.setInputStatus(4, (s.toString().length() > 0));
			}
		});


		companyPhone.addTextChangedListener(new android.text.TextWatcher()
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
				mListener.setInputStatus(5, (s.toString().length() >= 10));
			}
		});


		postCode.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(android.view.View v, boolean hasFocus)
			{
				if (hasFocus == false)
				{
					mListener.killFocusPostCode(view);				//	not v, view
				}
			}
		});


		//		エンターキ―がおされたらActionBarを閉じる
		companyName.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		staffName.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		postCode.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		companyAddr1.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		companyAddr2.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		companyPhone.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});
		//		エンターキ―がおされたらActionBarを閉じる


		kindCard.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
				SpaceeAppMain.mMsgHandler.sendMessage(msg);

				mListener.onKindCardSelected(v);
			}
		});


		btnInputComp.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
				SpaceeAppMain.mMsgHandler.sendMessage(msg);

				mListener.onBtnInputCompClicked(view);					//	view,  not v
			}
		});


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.setBtnInputCompView(btnInputComp);

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
