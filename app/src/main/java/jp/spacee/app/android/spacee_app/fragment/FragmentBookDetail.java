package jp.spacee.app.android.spacee_app.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;

import jp.spacee.app.android.spacee_app.R;


public  class  FragmentBookDetail  extends  Fragment
{
	private						LinearLayout					btnFloorGuide		= null;
	private						TextView						btnLogout1			= null;
	private						TextView						btnLogout2			= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentBookDetail()
	{
	}


	public  static  FragmentBookDetail  newInstance(String param1, String param2)
	{
		FragmentBookDetail fragment = new FragmentBookDetail();
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
		void	onBtnFloorGuideClicked	(View view);
		void	onBtnLogout1Clicked		(View view);
		void	onBtnLogout2Clicked		(View view);
		void	retrieveDetaildata		(View view);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public  View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View  view = inflater.inflate(R.layout.fragment_book_detail, container, false);

		btnFloorGuide	= (LinearLayout)	view.findViewById(R.id.btnFloorGuide);
		btnLogout1		= (TextView)		view.findViewById(R.id.btnLogout1);
		btnLogout2		= (TextView)		view.findViewById(R.id.btnLogout2);


		btnFloorGuide.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onBtnFloorGuideClicked(v);
			}
		});


		btnLogout1.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onBtnLogout1Clicked(v);
			}
		});


		btnLogout2.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onBtnLogout2Clicked(v);
			}
		});


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.retrieveDetaildata(view);

		return  view;
	}
}
