package jp.spacee.app.android.spacee_card.spaceecard;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by kazuya on 2017/07/06.
 */

public class GmoTokenCallbackInterface {
    @JavascriptInterface
    public void setToken(String code, String token) {
        Log.d("test", "code: " + code);
        Log.d("test", "token: " + token);
    }
}
