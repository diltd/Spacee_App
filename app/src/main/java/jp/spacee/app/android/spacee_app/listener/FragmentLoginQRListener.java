package jp.spacee.app.android.spacee_app.listener;


import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Environment;
import android.content.Context;
import android.app.Activity;
import android.view.View;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.TextureView;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.ToneGenerator;
import android.media.AudioManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.PointF;
import android.graphics.SurfaceTexture;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.support.annotation.NonNull;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import org.json.JSONObject;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.LuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import jp.spacee.app.android.spacee_app.fragment.FragmentLoginQR;
import jp.spacee.app.android.spacee_app.ReceiptTabApplication;
import jp.spacee.app.android.spacee_app.activity.SpaceeAppMain;
import jp.spacee.app.android.spacee_app.common.RGBLuminanceSource;
import jp.spacee.app.android.spacee_app.util.PlayWaveFile;
import jp.spacee.app.android.spacee_app.R;


public  class  FragmentLoginQRListener  implements  FragmentLoginQR.FragmentInteractionListener
{
	private						TextureView						cameraView						= null;

	//
	private						String							cameraId						= "";
	private						CameraDevice					mCameraDevice					= null;
	private						Handler							mBackgroundHandler			= null;
	private						HandlerThread					mBackgroundThread				= null;
	private						ImageReader						mRawImageReader				= null;

//	private						CameraManager					mCameraManager					= null;
	private						CameraCaptureSession			mCaptureSession				= null;
	private						CaptureRequest.Builder			mPreviewBuilder				= null;

	private  static  final	int								REQUEST_CAMERA_PERMISSION	= 200;


	public  FragmentLoginQRListener()
	{
	}


	@Override
	public  void  startQRReco(TextureView view)
	{
		cameraView = view;
		cameraView.setSurfaceTextureListener(textureViewListener);

		//	処理はtextureViewListenerから始まる
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	TextureView.SurfaceTextureListener textureViewListener = new TextureView.SurfaceTextureListener()
	{
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height)
		{
			//open your camera here
			openCamera();
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height)
		{
			// Transform you image captured size according to the surface width and height
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface)
		{
			return false;
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surface)
		{
		}
	};


	private void openCamera()
	{
		CameraManager manager = (CameraManager) ReceiptTabApplication.AppContext.getSystemService(Context.CAMERA_SERVICE);

		try {
			for (String strCameraId : manager.getCameraIdList())
			{
				CameraCharacteristics characteristics = manager.getCameraCharacteristics(strCameraId);
				if (characteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT)
				{
					// Front Cameraならbreak
					StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

					cameraId = strCameraId;
					break;
				}
			}

			manager.openCamera(cameraId, new CameraDevice.StateCallback()
			{
				@Override
				public void onOpened(CameraDevice camera)
				{
					mCameraDevice = camera;
					createCameraPreview();
				}

				@Override
				public void onDisconnected(CameraDevice camera)
				{
					mCameraDevice.close();
				}

				@Override
				public void onError(CameraDevice camera, int error)
				{
					mCameraDevice.close();
					mCameraDevice = null;
				}
			}, null);
		}
		catch (CameraAccessException e)
		{
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
	}


	protected void createCameraPreview()
	{
		try {
			List outputSurfaces = new ArrayList(2);

//			mRawImageReader = ImageReader.newInstance(640, 480, ImageFormat.YUV_420_888, 1);
			mRawImageReader = ImageReader.newInstance(640, 480, ImageFormat.JPEG, 1);
			outputSurfaces.add(mRawImageReader.getSurface());
			mRawImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener()
			{
				@Override
				public void onImageAvailable(ImageReader reader)
				{
					Image image = null;

					image = reader.acquireLatestImage();

					java.nio.ByteBuffer buffer = image.getPlanes()[0].getBuffer();
					byte[] img = new byte[buffer.capacity()];
					buffer.get(img);

					String  result = recognizeQRCode(img);
					if ((result != null) && (result.compareTo("") != 0))
					{
						String	auth = SpaceeAppMain.httpCommGlueRoutines.userAuthenticateQR(result);
						if (auth.compareTo("") != 0)
						{
							String  rslt = SpaceeAppMain.httpCommGlueRoutines.userAuthenticateQR(auth);
							if (result != null)
							{
								try
								{
									JSONObject obj1 = new JSONObject(rslt);
									String  status	= obj1.getString("status");
									ReceiptTabApplication.userAuthToken = obj1.getString("auth_token");

									if (status.equals("ok"))
									{
										PlayWaveFile playWaveFile = new PlayWaveFile();
										playWaveFile.playWaveSound(R.raw.ok_sound);

										android.os.Message msg = new android.os.Message();
										msg.what = SpaceeAppMain.MSG_LOGIN_QR_COMP;
										msg.arg1 = 1;
										msg.obj  = result;
										SpaceeAppMain.mMsgHandler.sendMessage(msg);
									}
									else
									{

									}
								}
								catch (org.json.JSONException e)
								{
									e.printStackTrace();
									return;
								}
							}
						}
						else
						{
							PlayWaveFile playWaveFile = new PlayWaveFile();
							playWaveFile.playWaveSound(R.raw.ng_sound);

							android.os.Message msg = new android.os.Message();
							msg.what = SpaceeAppMain.MSG_LOGIN_QR_COMP;
							msg.arg1 = 0;
							msg.obj  = "";
							SpaceeAppMain.mMsgHandler.sendMessage(msg);
						}
					}
					else
					{
						if (SpaceeAppMain.backReqPending == true)
						  {
							PlayWaveFile playWaveFile = new PlayWaveFile();
							playWaveFile.playWaveSound(R.raw.ng_sound);

							  android.os.Message msg = new android.os.Message();
							msg.what = SpaceeAppMain.MSG_LOGIN_QR_COMP;
							msg.arg1 = 0;
							msg.obj  = "";
							SpaceeAppMain.mMsgHandler.sendMessage(msg);
						  }
						 else
						  {
							createCameraPreview();
						  }
					}
				}
			}, null);

			SurfaceTexture texture = cameraView.getSurfaceTexture();
			configureTransform(cameraView, 640, 480);					//	Landscape固定表示なので９０度回転させる（２７０度は考慮不要）
			texture.setDefaultBufferSize(640, 480);
			Surface surface = new Surface(texture);
			mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			mPreviewBuilder.addTarget(surface);
			outputSurfaces.add(surface);

			mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback()
			{
				@Override
				public void onConfigured(@android.support.annotation.NonNull CameraCaptureSession cameraCaptureSession)
				{
					mCaptureSession = cameraCaptureSession;

					updatePreview();

					try
					{
						CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
						captureBuilder.addTarget(mRawImageReader.getSurface());
						captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
						captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);

						mCaptureSession.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback()
						{
							@Override
							public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result)
							{
								super.onCaptureCompleted(session, request, result);
							}
						}, null);
					}
					catch (CameraAccessException e)
					{
					}
				}

				@Override
				public void onConfigureFailed(@android.support.annotation.NonNull CameraCaptureSession cameraCaptureSession)
				{
				}
			}, null);

		}
		catch (CameraAccessException e)
		{
			e.printStackTrace();
		}
	}


	protected void updatePreview()
	{
		mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
		try
		{
			mCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
		}
		catch (CameraAccessException e)
		{
			e.printStackTrace();
		}
	}


	private  void  configureTransform(TextureView textureView, int previewWidth, int previewHeight)
	{
		Matrix	matrix		= new Matrix();
		RectF	viewRect	= new android.graphics.RectF(0, 0, textureView.getWidth(), textureView.getHeight());
		RectF	bufferRect	= new android.graphics.RectF(0, 0, previewHeight, previewWidth);
		PointF	center		= new android.graphics.PointF(viewRect.centerX(), viewRect.centerY());

		bufferRect.offset(center.x - bufferRect.centerX(), center.y - bufferRect.centerY());
		matrix.setRectToRect(viewRect, bufferRect, android.graphics.Matrix.ScaleToFit.FILL);
		float scale = Math.max((float) textureView.getHeight() / previewHeight,
								(float) textureView.getWidth() / previewHeight);
		matrix.postScale(scale, scale, center.x, center.y);
		matrix.postRotate(-90, center.x, center.y);

		textureView.setTransform(matrix);
	}


	private void closeCamera()
	{
		if (mCameraDevice != null)
		{
			mCameraDevice.close();
			mCameraDevice = null;
		}

		if (mRawImageReader != null)
		{
			mRawImageReader.close();
			mRawImageReader = null;
		}
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	protected void startBackgroundThread()
	{
		mBackgroundThread = new HandlerThread("Camera Background");
		mBackgroundThread.start();
		mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
	}


	protected void stopBackgroundThread()
	{
		mBackgroundThread.quitSafely();
		try
		{
			mBackgroundThread.join();
			mBackgroundThread = null;
			mBackgroundHandler = null;
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////

	private  String  recognizeQRCode(byte[] data)
	{
		BitmapFactory.Options opts = new BitmapFactory.Options();

		Bitmap rbitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		rbitmap.copy(rbitmap.getConfig(), true);

		Matrix mat	  = new Matrix();
		mat.postRotate(90);

		//	高速化を図る為、中心の３２０ｘ３２０を用いる（全体の１／３のサイズとする⇒処理時間的に１／３になる）
		android.graphics.Bitmap bitmap;
		bitmap = android.graphics.Bitmap.createBitmap(rbitmap, 160, 80, 320, 320, mat, true);

		LuminanceSource source = new RGBLuminanceSource(bitmap);
		BinaryBitmap bbmp = new BinaryBitmap(new HybridBinarizer(source));

		QRCodeReader QRRdr = new QRCodeReader();
		try
		{
			Result result = QRRdr.decode(bbmp);

			return  result.getText();
		}
		catch (Exception e)
		{
		}

		return  null;
	}
}