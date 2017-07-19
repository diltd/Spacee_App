package jp.spacee.app.android.spacee_app.comm;


import org.json.JSONObject;


class	HttpParamAPICall
{
	int			type;			//	1:POST	2:GET	3:GET(Image)
	String 		sUrl;
	JSONObject	jsonObj;
	String		paramGet;
}
