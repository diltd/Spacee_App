package jp.spacee.app.android.spacee_card.spaceecard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(jp.spacee.app.android.spacee_app.R.layout.activity_main);

        mWebView = (WebView) findViewById(jp.spacee.app.android.spacee_app.R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new GmoTokenCallbackInterface(), "Native");

        mWebView.loadUrl("file:///android_asset/test.html");
        mWebView.setVisibility(View.GONE);
    }

    public void onButtonClicked(View view) {
        mWebView.loadUrl("javascript:window.getGmoToken('tshop00025908', '4777777777777777'" +
                ", '201812', '123', 'TARO TEST');");
    }
}
