package jp.spacee.app.android.spacee_app.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ImageView;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentAppMain  extends  Fragment
{
	private						LinearLayout					btnWorkSpace		= null;
	private						LinearLayout					btnMeetingRoom		= null;
	private						LinearLayout					btnCheckBooking	= null;
	private						LinearLayout					btnRuleGuide		= null;
	private						LinearLayout					btnFloorGuide		= null;
	private 					ImageView						imgLogo				= null;

	private 					FragmentInteractionListener		mListener			= null;

	private  static  final	String							ARG_PARAM1			= "param1";
	private  static  final	String							ARG_PARAM2			= "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentAppMain()
	{
	}


	public  static  FragmentAppMain  newInstance(String param1, String param2)
	{
		FragmentAppMain fragment = new FragmentAppMain();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return  fragment;
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
	public  void  onAttach(Context context)
	{
		super.onAttach(context);
/*
		//	Activity側からsetOnFragmentInteractionListenerを呼び出してListenerを設定する
		if (context instanceof OnFragmentInteractionListener)
		  {
			mListener = (OnFragmentInteractionListener) context;
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
		void	initAppMain					(View view);
		void	onBtnWorkSpaceClicked		(View view);
		void	onBtnMeetingRoomClicked		(View view);
		void	onBtnCheckBookingClicked	(View view);
		void	onBtnRuleGuideClicked		(View view);
		void	onBtnFloorGuideClicked		(View view);
		void	onImgLogoClicked			(View view);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_app_main, container, false);

		btnWorkSpace		= (LinearLayout)	view.findViewById(R.id.btnWorkSpace);
		btnMeetingRoom		= (LinearLayout)	view.findViewById(R.id.btnMeetingRoom);
		btnCheckBooking	= (LinearLayout)	view.findViewById(R.id.btnCheckBooking);
		btnRuleGuide		= (LinearLayout)	view.findViewById(R.id.btnRuleGuide);
		btnFloorGuide		= (LinearLayout)	view.findViewById(R.id.btnFloorGuide);
		imgLogo				= (ImageView) 		view.findViewById(R.id.TopLogo);


		btnWorkSpace.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ReceiptTabApplication.flagInitComp == true)
				{
					mListener.onBtnWorkSpaceClicked(v);
				}
			}
		});


		btnMeetingRoom.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ReceiptTabApplication.flagInitComp == true)
				{
					mListener.onBtnMeetingRoomClicked(v);
				}
			}
		});


		btnCheckBooking.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ReceiptTabApplication.flagInitComp == true)
				{
					mListener.onBtnCheckBookingClicked(v);
				}
			}
		});


		btnRuleGuide.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ReceiptTabApplication.flagInitComp == true)
				{
					mListener.onBtnRuleGuideClicked(v);
				}
			}
		});


		btnFloorGuide.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ReceiptTabApplication.flagInitComp == true)
				{
					mListener.onBtnFloorGuideClicked(v);
				}
			}
		});


		imgLogo.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if (ReceiptTabApplication.flagInitComp == true)
				{
					mListener.onImgLogoClicked(v);
				}
			}
		});


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.initAppMain(view);

		return  view;
	}
}
