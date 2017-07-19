package jp.spacee.app.android.spacee_app.util;


import java.io.InputStream;
import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.AudioManager;

import jp.spacee.app.android.spacee_app.ReceiptTabApplication;


public  class  PlayWaveFile
{
	public  void  playWaveSound(int resourceId)
	{
		InputStream		input;
		byte[]			waveData	= null;
		AudioTrack		audioTrack	= null;

		try
		{
			// wavを読み込む
			input = ReceiptTabApplication.AppContext.getResources().openRawResource(resourceId);
			waveData	= new byte[(int) input.available()];
			input.read(waveData);
			input.close();

			// バッファサイズの計算
			int bufSize = android.media.AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

			// MODE_STREAM にてインスタンス生成
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufSize, AudioTrack.MODE_STREAM);
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
		}

		if ((audioTrack != null) && (waveData != null))
		{
			// 再生
			audioTrack.play();

			// ヘッダ44byteをオミット
			audioTrack.write(waveData, 44, waveData.length-44);
		}
	}
}
