package jp.spacee.app.android.spacee_app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.spacee.app.android.spacee_app.R;
import java.util.TimerTask;
import java.util.Timer;

import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  CustomBaseWindow  extends  AppCompatActivity
{
	public						View				appArea				= null;

	private						LinearLayout		formatTitle0		= null;

	private						LinearLayout		formatTitle1		= null;
	private						TextView			windowTitle1		= null;
	private						TextView			titleDate1			= null;
	private						TextView			titleTime1			= null;
	private						ImageView			btnPlace1			= null;

	private						LinearLayout		formatTitle2		= null;
	private						ImageView			btnBack2			= null;
	private						ImageView			btnHome2			= null;
	private						TextView			windowTitle2		= null;
	private						TextView			titleDate2			= null;
	private						TextView			titleTime2			= null;
	private						ImageView			btnPlace2			= null;

	private						LinearLayout		formatTitle3		= null;
	private						ImageView			btnBack3			= null;
	private						ImageView			btnHome3			= null;
	private						TextView			windowTitle3		= null;
	private						TextView			titleDate3			= null;
	private						TextView			titleTime3			= null;
	private						ImageView			btnPlace3			= null;
	private						TextView			btnOpt3				= null;

	private						boolean				isRunning			= false;

	private						View				mContentView		= null;
	private						boolean				mVisible			= false;

	private  static  final	boolean				AUTO_HIDE		    		= true;
	private  static  final	int					AUTO_HIDE_DELAY_MILLIS	= 1500;
	private  static  final	int					UI_ANIMATION_DELAY		=  800;
	private			   final	Handler				mHideHandler	    		= new Handler();



	@Override
	public  void  onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.window_base_layout);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		appArea			= (View)		findViewById(R.id.fullscreen_content);

		formatTitle0	= (LinearLayout) findViewById(R.id.formatTitle0);

		formatTitle1	= (LinearLayout) findViewById(R.id.formatTitle1);
		windowTitle1	= (TextView)	findViewById(R.id.titleText1);
		titleDate1		= (TextView)	findViewById(R.id.titleDate1);
		titleTime1		= (TextView)	findViewById(R.id.titleTime1);
		btnPlace1		= (ImageView)	findViewById(R.id.btnPlace1);

		formatTitle2	= (LinearLayout) findViewById(R.id.formatTitle2);
		btnBack2		= (ImageView)	findViewById(R.id.btnBack2);
		btnHome2		= (ImageView)	findViewById(R.id.btnHome2);
		windowTitle2	= (TextView)	findViewById(R.id.titleText2);
		titleDate2		= (TextView)	findViewById(R.id.titleDate2);
		titleTime2		= (TextView)	findViewById(R.id.titleTime2);
		btnPlace2		= (ImageView)	findViewById(R.id.btnPlace2);


		formatTitle3	= (LinearLayout) findViewById(R.id.formatTitle3);
		btnBack3		= (ImageView)	findViewById(R.id.btnBack3);
		btnHome3		= (ImageView)	findViewById(R.id.btnHome3);
		windowTitle3	= (TextView)	findViewById(R.id.titleText3);
		titleDate3		= (TextView)	findViewById(R.id.titleDate3);
		titleTime3		= (TextView)	findViewById(R.id.titleTime3);
		btnPlace3		= (ImageView)	findViewById(R.id.btnPlace3);
		btnOpt3			= (TextView)	findViewById(R.id.btnOpt3);


		btnPlace1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnPlace1Clicked();
			}
		});


		btnBack2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnBack2Clicked();
			}
		});


		btnHome2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnHome2Clicked();
			}
		});


		btnPlace2.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnPlace2Clicked();
			}
		});


		btnBack3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnBack3Clicked();
			}
		});


		btnHome3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnHome3Clicked();
			}
		});


		btnPlace3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnPlace3Clicked();
			}
		});


		btnOpt3.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onBtnOpt3Clicked();
			}
		});
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	protected  void  onBtnPlace1Clicked()
	{
	}


	protected  void  onBtnBack2Clicked()
	{
	}


	protected  void  onBtnHome2Clicked()
	{
	}


	protected  void  onBtnPlace2Clicked()
	{
	}


	protected  void  onBtnBack3Clicked()
	{
	}


	protected  void  onBtnHome3Clicked()
	{
	}


	protected  void  onBtnPlace3Clicked()
	{
	}


	protected  void  onBtnOpt3Clicked()
	{
	}


	protected  void  onHideHeader()
	{
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	public  void  selectHeaderType(int type)
	{
		if		(type == 0)
		{
			formatTitle0.setVisibility(View.VISIBLE);
			formatTitle1.setVisibility(View.INVISIBLE);
			formatTitle2.setVisibility(View.INVISIBLE);
			formatTitle3.setVisibility(View.INVISIBLE);
		}
		else if (type == 1)
		{
			formatTitle0.setVisibility(View.INVISIBLE);
			formatTitle1.setVisibility(View.VISIBLE);
			formatTitle2.setVisibility(View.INVISIBLE);
			formatTitle3.setVisibility(View.INVISIBLE);
		}
		else if (type == 2)
		{
			formatTitle0.setVisibility(View.INVISIBLE);
			formatTitle1.setVisibility(View.INVISIBLE);
			formatTitle2.setVisibility(View.VISIBLE);
			formatTitle3.setVisibility(View.INVISIBLE);
		}
		else if (type == 3)
		{
			formatTitle0.setVisibility(View.INVISIBLE);
			formatTitle1.setVisibility(View.INVISIBLE);
			formatTitle2.setVisibility(View.INVISIBLE);
			formatTitle3.setVisibility(View.VISIBLE);
			String wStr = String.format("%s\nからログアウト", ReceiptTabApplication.currentUserMAddr);
			btnOpt3.setText(wStr);
		}
	}


	public  void  selectHeaderTitle(int type, String title)
	{
		if		(type == 1)		windowTitle1.setText(title);
		else if (type == 2)		windowTitle2.setText(title);
		else if (type == 3)		windowTitle3.setText(title);
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	public  void  fullScreenControl(View view)
	{
		mContentView	= view;
		mVisible		= true;

		// Set up the user interaction to manually show or hide the system UI.
		mContentView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				toggle();
			}
		});
	}


	private  final  Runnable  mShowPart2Runnable = new Runnable()
	{
		@Override
		public void run()
		{
			// Delayed display of UI elements
			ActionBar actionBar = getSupportActionBar();
			if (actionBar != null) {
				actionBar.show();
			}
		}
	};

	private	  final  Runnable  mHidePart2Runnable = new Runnable()
	{
		@android.annotation.SuppressLint("InlinedApi")
		@Override
		public void run()
		{
			mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
											  |  View.SYSTEM_UI_FLAG_FULLSCREEN
											  |  View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
											  |  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
											  |  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
											  |  View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
											  |  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		}
	};


	private final Runnable mHideRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			hide();
		}
	};


	private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View view, android.view.MotionEvent motionEvent)
		{
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};


	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Trigger the initial hide() shortly after the activity has been created,
		// to briefly hint to the user that UI controls are available.
		//
		// <<重要>> この値を小さくし過ぎるとActionBarはinvisibleになるが、
		// 			screenは全画面にならない（下部の64linesが黒いまま）ので注意する事
		delayedHide(1500);
	}


	private void toggle()
	{
		if (mVisible) {
			hide();
		}
		else {
			show();
		}
	}


	public  void  hide()
	{
		// Hide UI first
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.hide();
		}

		// Schedule a runnable to remove the status and navigation bar after a delay
		mHideHandler.removeCallbacks(mShowPart2Runnable);
		mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
	}


	@android.annotation.SuppressLint("InlinedApi")
	public  void  show()
	{
		// Show the system bar
		mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
										  | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
										  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

		mVisible = true;

		// Schedule a runnable to display UI elements after a delay
		mHideHandler.removeCallbacks(mHidePart2Runnable);
		mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
	}


	private void delayedHide(int delayMillis)
	{
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}
}
