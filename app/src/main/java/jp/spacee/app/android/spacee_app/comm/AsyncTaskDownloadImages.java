package jp.spacee.app.android.spacee_app.comm;


import android.os.AsyncTask;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.NameValuePair;

import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  AsyncTaskDownloadImages  extends AsyncTask<HttpParamDownloadImages, Void, Void>
{
	public  interface  AsyncTaskCallback
	{
		void processCompleted(int retCode, Bitmap[] bmps);
	}


	private		AsyncTaskCallback 	callbackroutine = null;


	public  AsyncTaskDownloadImages(AsyncTaskCallback  callback)
	{
		super();

		this.callbackroutine = callback;
	}


	@Override
	protected void onPreExecute()
	{
	}


	@Override
	protected  Void  doInBackground(HttpParamDownloadImages... params)
	{
		HttpParamDownloadImages param = params[0];

		String			inData		= "";

		Bitmap[]		bmps		= new Bitmap[param.numPict];

		HttpPost		httpPost	= null;
		HttpGet			httpGet		= null;
		HttpResponse	httpResp	= null;

		HttpClient httpClient	= new DefaultHttpClient();

		InputStream iStream		= null;

		ReceiptTabApplication.CommRetCode	= ReceiptTabApplication.COMM_RC_OK;
		ReceiptTabApplication.CommResult	= "";

		try
		{
			for (int nn=0; nn<param.numPict; nn++)
			{
				HttpParams httpParams = httpClient.getParams();

				ArrayList<BasicHeader> arryHdr = new ArrayList<org.apache.http.message.BasicHeader>();

				httpParams.setParameter("http.default-headers", arryHdr);
				List<NameValuePair> listPrms = new ArrayList<NameValuePair>();
				String query = URLEncodedUtils.format(listPrms, "UTF-8");
				httpGet	 = new HttpGet(param.sUrls[nn]);
				httpResp = httpClient.execute(httpGet);

				if (httpResp.getStatusLine().getStatusCode() < 400)
				{
					iStream = httpResp.getEntity().getContent();

					Bitmap tmpBmp = BitmapFactory.decodeStream(iStream);

					try
					{
						File dir  = ReceiptTabApplication.AppContext.getCacheDir();
						File file = new File(dir.getAbsolutePath(), "cache.img");
						FileOutputStream oStream = new FileOutputStream(file);
						tmpBmp.compress(Bitmap.CompressFormat.PNG, 100, oStream);
						oStream.close();

						bmps[nn] = tmpBmp;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					iStream.close();
				}
				else
				{
					ReceiptTabApplication.CommRetCode = ReceiptTabApplication.COMM_RC_FALSE_HTTP_ERROR;
					ReceiptTabApplication.CommResult  = null;
				}
			}

			ReceiptTabApplication.CommRetCode = ReceiptTabApplication.COMM_RC_OK;
			ReceiptTabApplication.CommResult  = "";
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
			ReceiptTabApplication.CommRetCode = ReceiptTabApplication.COMM_RC_FALSE_HTTP_ERROR;
			ReceiptTabApplication.CommResult  = null;
		}

		if (callbackroutine != null)
		{
			callbackroutine.processCompleted(ReceiptTabApplication.CommRetCode, bmps);
		}
		else
		{
			for (int i = 0; i<param.numPict; i++)
			{
				param.mBmps[i] = bmps[i];
			}
			HttpCommGlueRoutines.mSemaphore.release();
		}

		return null;
	}
}