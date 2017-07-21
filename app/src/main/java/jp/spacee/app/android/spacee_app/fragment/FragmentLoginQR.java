package jp.spacee.app.android.spacee_app.fragment;


import android.os.Bundle;
import android.app.PendingIntent;
import android.app.AlarmManager;
import java.util.Calendar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.TextureView;
import android.view.SurfaceView;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraAccessException;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import jp.spacee.app.android.spacee_app.R;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.common.RGBLuminanceSource;


public  class  FragmentLoginQR  extends  Fragment
{
	private						TextureView						cameraView			= null;
	private						SurfaceView						surfaceView		= null;

	private						FragmentInteractionListener		mListener;

	private  static  final	String							ARG_PARAM1 = "param1";
	private  static  final	String							ARG_PARAM2 = "param2";
	private						String							mParam1;
	private						String							mParam2;


	public  FragmentLoginQR()
	{
	}


	public  static  FragmentLoginQR  newInstance(String param1, String param2)
	{
		FragmentLoginQR fragment = new FragmentLoginQR();
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
	public void onAttach(android.content.Context context)
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
		void	startQRReco		(View view1, TextureView view2);
	}


	public  void  setOnFragmentInteractionListener(FragmentInteractionListener listener)
	{
		mListener = listener;
	}


	@Override
	public  View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View  view = inflater.inflate(R.layout.fragment_login_qr, container, false);

		cameraView = (TextureView) view.findViewById(R.id.cameraView);
		cameraView.setVisibility(View.VISIBLE);
		surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
		surfaceView.setVisibility(View.INVISIBLE);


		while (mListener == null)
		{
			android.os.SystemClock.sleep(100);
		}
		mListener.startQRReco(view, cameraView);

		return  view;
	}
}
