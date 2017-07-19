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
import android.widget.Spinner;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.Editable;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;


public  class  FragmentEntryCard  extends  Fragment
{
	private						TextView						kindInvoice		= null;
	private						TextView						btnInputComp		= null;
	private						EditText						cardNo1				= null;
	private						EditText						cardNo2				= null;
	private						EditText						cardNo3				= null;
	private						EditText						cardNo4				= null;
	private						EditText						cardNo5				= null;
	private						EditText						namePsn				= null;
	private						Spinner							expireMonth		= null;
	private						Spinner							expireYear			= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentEntryCard()
	{
	}


	public  static  FragmentEntryCard  newInstance(String param1, String param2)
	{
		FragmentEntryCard fragment = new FragmentEntryCard();
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
		void	onKindInvoiceSelected	(View view);
		void	onBtnInputCompClicked	(View view);
		void	setSpinnerValue			(View view1, View view2);
		void	setBtnInputCompView		(View view);
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
		final  View  view = inflater.inflate(R.layout.fragment_entry_card, container, false);

		kindInvoice	= (TextView)	view.findViewById(R.id.kindInvoice);
		btnInputComp	= (TextView)	view.findViewById(R.id.btnInputComp);
		cardNo1			= (EditText)	view.findViewById(R.id.cardNo1);
		cardNo2			= (EditText)	view.findViewById(R.id.cardNo2);
		cardNo3			= (EditText)	view.findViewById(R.id.cardNo3);
		cardNo4			= (EditText)	view.findViewById(R.id.cardNo4);
		cardNo5			= (EditText)	view.findViewById(R.id.cardNo5);
		namePsn			= (EditText)	view.findViewById(R.id.namePsn);
		expireMonth	= (Spinner)		view.findViewById(R.id.expireMonth);
		expireYear		= (Spinner)		view.findViewById(R.id.expireYear);


		// set paddingLeft & Right to EditText		AppCompat-V7 バグ対応
		float	density		= getResources().getDisplayMetrics().density;
		int		paddingH	= (int) (8 * density);
		cardNo1.setPadding(paddingH, cardNo1.getPaddingTop(), paddingH, cardNo1.getPaddingBottom());
		cardNo2.setPadding(paddingH, cardNo2.getPaddingTop(), paddingH, cardNo2.getPaddingBottom());
		cardNo3.setPadding(paddingH, cardNo3.getPaddingTop(), paddingH, cardNo3.getPaddingBottom());
		cardNo4.setPadding(paddingH, cardNo4.getPaddingTop(), paddingH, cardNo4.getPaddingBottom());
		cardNo5.setPadding(paddingH, cardNo5.getPaddingTop(), paddingH, cardNo5.getPaddingBottom());
		paddingH	= (int) (16 * density);
		namePsn.setPadding(paddingH, namePsn.getPaddingTop(), paddingH, namePsn.getPaddingBottom());

		InputFilter filter = new InputFilter()
		{
			@Override
			public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend)
			{
				if (src.toString().matches("[a-zA-Z ]"))
				{
					return src.toString().toUpperCase();
				}
				else
				{
					return "";
				}
			}
		};
		InputFilter[] filters = new InputFilter[] { filter };
		namePsn.setFilters(filters);


		kindInvoice.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
				SpaceeAppMain.mMsgHandler.sendMessage(msg);

				mListener.onKindInvoiceSelected(v);
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


		cardNo1.addTextChangedListener(new TextWatcher()
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
			public void afterTextChanged(Editable s)
			{
				mListener.setInputStatus(0, (s.toString().length() == 4));

				if (s.toString().length() == 4)		cardNo2.requestFocus(View.FOCUS_UP);
			}
		});


		cardNo2.addTextChangedListener(new TextWatcher()
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
			public void afterTextChanged(Editable s)
			{
				mListener.setInputStatus(1, (s.toString().length() == 4));

				if (s.toString().length() == 4)		cardNo3.requestFocus(View.FOCUS_UP);
			}
		});


		cardNo3.addTextChangedListener(new TextWatcher()
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
			public void afterTextChanged(Editable s)
			{
				mListener.setInputStatus(2, (s.toString().length() == 4));

				if (s.toString().length() == 4)		cardNo4.requestFocus(View.FOCUS_UP);
			}
		});


		cardNo4.addTextChangedListener(new TextWatcher()
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
			public void afterTextChanged(Editable s)
			{
				mListener.setInputStatus(3, (s.toString().length() == 4));

				if (s.toString().length() == 4)		cardNo5.requestFocus(View.FOCUS_UP);
			}
		});


		cardNo5.addTextChangedListener(new TextWatcher()
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
			public void afterTextChanged(Editable s)
			{
				mListener.setInputStatus(4, (s.toString().length() == 3));

				if (s.toString().length() == 3)		namePsn.requestFocus(View.FOCUS_UP);
			}
		});


		namePsn.addTextChangedListener(new TextWatcher()
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
			public void afterTextChanged(Editable s)
			{
				mListener.setInputStatus(5, (s.toString().length() > 0));
			}
		});


		//		エンターキ―がおされたらActionBarを閉じる
		cardNo1.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		cardNo2.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		cardNo3.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		cardNo4.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		cardNo5.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});


		namePsn.setOnKeyListener(new View.OnKeyListener()
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
		mListener.setSpinnerValue(expireMonth, expireYear);
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
