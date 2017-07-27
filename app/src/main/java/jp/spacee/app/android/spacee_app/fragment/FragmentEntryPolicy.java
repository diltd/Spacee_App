package jp.spacee.app.android.spacee_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import jp.spacee.app.android.spacee_app.BuildConfig;
import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.util.AssetsUtil;
import jp.spacee.app.android.spacee_app.web.GmoTokenCallbackInterface;


public  class  FragmentEntryPolicy  extends  Fragment
{
	public static final String TAG = "FragmentEntryPolicy";
	private						TextView						btnAgree			= null;
	private						LinearLayout					layoutAgree		= null;
	private						ImageView						chkBoxAgree		= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;
	private WebView mWebView;

	public  FragmentEntryPolicy()
	{
	}


	public  static  FragmentEntryPolicy  newInstance(String param1, String param2)
	{
		FragmentEntryPolicy fragment = new FragmentEntryPolicy();
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
		void	onBtnAgreeClicked		(View view);
		void	onLayoutAgreeClicked	(View view);
		void	setAgreeButtons			(View view1, View view2);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public  View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		final View	view = inflater.inflate(R.layout.fragment_entry_policy, container, false);

		btnAgree		= (TextView)		view.findViewById(R.id.btnAgree);
		layoutAgree	= (LinearLayout)	view.findViewById(R.id.layoutAgree);
		chkBoxAgree	= (ImageView)		view.findViewById(R.id.chkboxAgree);

		btnAgree.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onBtnAgreeClicked(view);						//	not v, view
			}
		});


		layoutAgree.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				mListener.onLayoutAgreeClicked(v);
			}
		});


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.setAgreeButtons(btnAgree, chkBoxAgree);

		mWebView = (WebView) view.findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new GmoTokenCallbackInterface(
				(GmoTokenCallbackInterface.GmoTokenCallbackListener) mListener), "Native");
		{
			String html = null;
			try {
				html = AssetsUtil.getStringAsset(getContext(), "card.html");
				html = html.replace("${GMO_JS_URL}", BuildConfig.GMO_JS_URL);
			} catch (IOException ex) {
				Log.d(TAG, Log.getStackTraceString(ex));
			}
			mWebView.loadData(html, "text/html", "utf8");
		}

		return  view;
	}
}
