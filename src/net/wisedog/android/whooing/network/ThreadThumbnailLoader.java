/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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