package jp.spacee.app.android.spacee_app.web;

import android.webkit.JavascriptInterface;

/**
 * Created by kazuya on 2017/07/06.
 */

public class GmoTokenCallbackInterface {
    public static interface GmoTokenCallbackListener {
        public void onSuccess(String token);

        public void onError(String code);
    }

    private GmoTokenCallbackListener mListener = null;

    public GmoTokenCallbackInterface(GmoTokenCallbackListener listener) {
        mListener = listener;
    }

    @JavascriptInterface
    public void callbackToken(String token) {
        if (mListener != null) {
            mListener.onSuccess(token);
        }
    }

    @JavascriptInterface
    public void callbackCode(String code) {
        if (mListener != null) {
            mListener.onError(code);
        }
    }
}
