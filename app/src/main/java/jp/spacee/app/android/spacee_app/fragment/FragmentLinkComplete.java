package jp.spacee.app.android.spacee_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  FragmentLinkComplete  extends  Fragment
{
	private						TextView						btnGoPayment		= null;

	private 					FragmentInteractionListener		mListener			= null;

	private  static  final	String							ARG_PARAM1			= "param1";
	private  static  final	String							ARG_PARAM2			= "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentLinkComplete()
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
		void	onBtnGoPaymentClicked		(View view);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_link_complete, container, false);

		btnGoPayment	= (TextView)	view.findViewById(R.id.btnGoPayment);

		btnGoPayment.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onBtnGoPaymentClicked(v);
			}
		});

		return  view;
	}
}
