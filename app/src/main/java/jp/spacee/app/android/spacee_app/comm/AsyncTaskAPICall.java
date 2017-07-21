package jp.spacee.app.android.spacee_app.comm;


import android.os.AsyncTask;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.NameValuePair;

import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  AsyncTaskAPICall  extends AsyncTask<HttpParamAPICall, Void, Void>
{
	public  AsyncTaskAPICall()
	{
		super();
	}


	@Override
	protected void onPreExecute()
	{
	}


	@Override
	protected  Void  doInBackground(HttpParamAPICall... params)
	{
		HttpParamAPICall	param  = params[0];
		String				inData = "";

		HttpPost		httpPost = null;
		HttpGet			httpGet	 = null;
		HttpResponse	httpResp = null;

		HttpClient	httpClient = new DefaultHttpClient();

		InputStream iStream = null;

		ReceiptTabApplication.CommRetCode = ReceiptTabApplication.COMM_RC_OK;
		ReceiptTabApplication.CommResult  = "";

		try
		{
			HttpParams httpParams = httpClient.getParams();
			ArrayList<BasicHeader> arryHdr = new ArrayList<BasicHeader>();
			httpParams.setParameter("http.default-headers", arryHdr);

			if (param.type == 1)
			{
				httpPost = new HttpPost(param.sUrl);
				StringEntity se = new StringEntity(param.jsonObj.toString());
				httpPost.setEntity(se);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("X-Provider-Auth-Token", ReceiptTabApplication.providerAuthToken);
				httpPost.setHeader("X-User-Auth-Token", ReceiptTabApplication.userAuthToken);
				httpResp = httpClient.execute(httpPost);
			}
			else
			{
				httpGet = new HttpGet(param.sUrl + "?" + param.paramGet);
				httpGet.setHeader("Accept", "application/json");
				httpGet.setHeader("Content-Type", "application/json");
				httpGet.setHeader("X-Provider-Auth-Token", ReceiptTabApplication.providerAuthToken);
				httpGet.setHeader("X-User-Auth-Token", ReceiptTabApplication.userAuthToken);
				httpResp = httpClient.execute(httpGet);
			}

			if (httpResp.getStatusLine().getStatusCode() <= 400)
			{
				iStream = httpResp.getEntity().getContent();

				InputStreamReader	iReader	= new InputStreamReader(iStream);
				BufferedReader		buffRdr	= new BufferedReader(iReader);

				String  sLine;
				while ((sLine = buffRdr.readLine()) != null)
				{
					inData += (sLine+"\r\n");
				}

				iStream.close();

				ReceiptTabApplication.CommRetCode	= ReceiptTabApplication.COMM_RC_OK;
				ReceiptTabApplication.CommResult	= inData;
			}
			else
			{
				ReceiptTabApplication.CommRetCode	= ReceiptTabApplication.COMM_RC_FALSE_HTTP_ERROR;
				ReceiptTabApplication.CommResult	= null;
			}
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
			ReceiptTabApplication.CommRetCode = ReceiptTabApplication.COMM_RC_FALSE_HTTP_ERROR;
			ReceiptTabApplication.CommResult  = null;
		}

		ReceiptTabApplication.flgCommCompleted = true;


		HttpCommGlueRoutines.mSemaphore.release();

		return	null;
	}


	@Override
	protected  void  onPostExecute(Void dmy)
	{
	}
}