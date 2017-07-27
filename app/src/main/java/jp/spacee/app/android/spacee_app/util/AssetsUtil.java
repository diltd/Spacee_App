package jp.spacee.app.android.spacee_app.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by kazuya on 2017/07/27.
 */

public class AssetsUtil {
    public static String getStringAsset(Context context, String filename) throws IOException {
        AssetManager assetManager = context.getResources().getAssets();
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            inputStream = assetManager.open(filename);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return builder.toString();
    }
}
