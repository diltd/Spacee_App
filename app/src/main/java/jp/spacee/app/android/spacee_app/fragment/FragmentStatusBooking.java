package jp.spacee.app.android.spacee_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentStatusBooking  extends  Fragment
{
	private						TextView						monthName1			= null;
	private						TextView						monthName2			= null;
	private						TextView						monthName3			= null;
	private						TextView						monthName4			= null;
	private						TextView						monthName5			= null;
	private						TextView						monthName6			= null;
	private						View							monthMark1			= null;
	private						View							monthMark2			= null;
	private						View							monthMark3			= null;
	private						View							monthMark4			= null;
	private						View							monthMark5			= null;
	private						View							monthMark6			= null;
	private 					ListView						bookingStatus		= null;

	private 					FragmentInteractionListener		mListener			= null;

	private  static  final	String							ARG_PARAM1			= "param1";
	private  static  final	String							ARG_PARAM2			= "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentStatusBooking()
	{
	}


	public  static  FragmentTop  newInstance(String param1, String param2)
	{
		FragmentTop fragment = new FragmentTop();
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
		void	onMonth1Clicked			(View view);
		void	onMonth2Clicked			(View view);
		void	onMonth3Clicked			(View view);
		void	onMonth4Clicked			(View view);
		void	onMonth5Clicked			(View view);
		void	onMonth6Clicked			(View view);
		void	onListProcess			(View view);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_status_booking, container, false);

		monthName1		= (TextView)	view.findViewById(R.id.monthName1);
		monthName2		= (TextView)	view.findViewById(R.id.monthName2);
		monthName3		= (TextView)	view.findViewById(R.id.monthName3);
		monthName4		= (TextView)	view.findViewById(R.id.monthName4);
		monthName5		= (TextView)	view.findViewById(R.id.monthName5);
		monthName6		= (TextView)	view.findViewById(R.id.monthName6);
		monthMark1		= (View)		view.findViewById(R.id.monthMark1);
		monthMark2		= (View)		view.findViewById(R.id.monthMark2);
		monthMark3		= (View)		view.findViewById(R.id.monthMark3);
		monthMark4		= (View)		view.findViewById(R.id.monthMark4);
		monthMark5		= (View)		view.findViewById(R.id.monthMark5);
		monthMark6		= (View)		view.findViewById(R.id.monthMark6);
		bookingStatus	= (ListView)	view.findViewById(R.id.bookingStatus);


		monthName1.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth1Clicked(v);
			}
		});

		monthName2.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth2Clicked(v);
			}
		});

		monthName3.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth3Clicked(v);
			}
		});

		monthName4.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth4Clicked(v);
			}
		});

		monthName5.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth5Clicked(v);
			}
		});

		monthName6.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth6Clicked(v);
			}
		});

		monthMark1.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth1Clicked(v);
			}
		});

		monthMark2.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth2Clicked(v);
			}
		});

		monthMark3.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth3Clicked(v);
			}
		});

		monthMark4.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth4Clicked(v);
			}
		});

		monthMark5.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth5Clicked(v);
			}
		});

		monthMark6.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onMonth6Clicked(v);
			}
		});


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.onListProcess(view);

		return  view;
	}
}
