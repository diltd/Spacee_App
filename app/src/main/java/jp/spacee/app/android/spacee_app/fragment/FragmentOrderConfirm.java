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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;


public  class  FragmentOrderConfirm  extends  Fragment
{
	private						ListView					paymentList		= null;
	private						TextView					btnShowDetail		= null;
	private						LinearLayout				btnUseCoupon		= null;
	private						TextView					btnApplyCoupon		= null;
	private						ImageView					btnCancelApply		= null;
	private						TextView					btnAgree			= null;
	private						TextView					couponCode			= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentOrderConfirm()
	{
	}


	public  static  FragmentOrderConfirm  newInstance(String param1, String param2)
	{
		FragmentOrderConfirm fragment = new FragmentOrderConfirm();
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
		void	onListEntrySelected		(View view);
		void	onBtnShowDetailClicked	(View view);
		void	onBtnUseCouponClicked	(View view);
		void	onBtnApplyCouponClicked	(View view);
		void	onBtnCancelApplyClicked	(View view);
		void	onBtnAgreeClicked		(View view);
		void	onListProcess			(View view);

	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public  View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		final View  view = inflater.inflate(R.layout.fragment_order_confirm, container, false);

		paymentList	= (ListView)		view.findViewById(R.id.paymentList);
		btnShowDetail	= (TextView)		view.findViewById(R.id.btnShowDetail);
		btnUseCoupon	= (LinearLayout)	view.findViewById(R.id.btnUseCoupon);
		btnApplyCoupon	= (TextView)		view.findViewById(R.id.btnApplyCoupon);
		btnCancelApply	= (ImageView)		view.findViewById(R.id.messageOff);
		btnAgree		= (TextView)		view.findViewById(R.id.btnAgree);
		couponCode		= (TextView)		view.findViewById(R.id.couponCode);


		// set paddingLeft & Right to EditText		AppCompat-V7 バグ対応
		float	density		= getResources().getDisplayMetrics().density;
		int		paddingH	= (int) (8 * density);
		couponCode.setPadding(paddingH, couponCode.getPaddingTop(), paddingH, couponCode.getPaddingBottom());


		//		エンターキ―がおされたらActionBarを閉じる
		couponCode.setOnKeyListener(new View.OnKeyListener()
		{
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				return  sendHideActionBar(keyCode, event);
			}
		});
		//		エンターキ―がおされたらActionBarを閉じる


		btnShowDetail.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public  void  onClick(View v)
			{
				mListener.onBtnShowDetailClicked(v);
			}
		});


		btnUseCoupon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public  void  onClick(View v)
			{
				mListener.onBtnUseCouponClicked(view);				//	not v, view
			}
		});


		btnApplyCoupon.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public  void  onClick(View v)
			{
				mListener.onBtnApplyCouponClicked(view);				//	not v, view

				sendHideActionBar();
			}
		});


		btnCancelApply.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public  void  onClick(View v)
			{
				mListener.onBtnCancelApplyClicked(view);				//	not v, view

				sendHideActionBar();
			}
		});


		btnAgree.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Message msg = new Message();
				msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
				SpaceeAppMain.mMsgHandler.sendMessage(msg);

				mListener.onBtnAgreeClicked(v);
			}
		});


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.onListProcess(view);

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


	private  void  sendHideActionBar()
	{
		Message msg = new Message();
		msg.what = SpaceeAppMain.MSG_HIDE_ACTIONBAR;
		SpaceeAppMain.mMsgHandler.sendMessage(msg);
	}
}
