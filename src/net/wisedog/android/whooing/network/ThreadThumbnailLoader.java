package net.wisedog.android.whooing.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
	 * Thread class for loading image from the web
	 * @author WiseDog(wisedog@buzzni.com)
	 * */
	public class ThreadThumbnailLoader extends Thread{
		Handler mHandler;
		URL mURL;

		public ThreadThumbnailLoader(Handler handler, URL url){
			mHandler = handler;
			mURL = url;
		}

		@Override
		public void run() {			
			URLConnection conn;
			Bitmap bm = null;
			try {
				conn = mURL.openConnection();
				conn.connect();
				BufferedInputStream input = new BufferedInputStream(
						conn.getInputStream());
				bm = BitmapFactory.decodeStream(input);
				input.close();
			} catch (IOException e) {
				bm = null;
			}						
			Message msg = mHandler.obtainMessage(0, bm);
			mHandler.sendMessage(msg);
		}
	}