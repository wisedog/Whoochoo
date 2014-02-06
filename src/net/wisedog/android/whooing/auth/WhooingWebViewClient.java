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
package net.wisedog.android.whooing.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.utils.RegExUtil;

public class WhooingWebViewClient extends WebViewClient {
	private Activity mActivity;
	
	public WhooingWebViewClient(Activity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		String pin = RegExUtil.takeParam(url, "pin");
		String secondToken = RegExUtil.takeParam(url, "token");
		if(pin != null && secondToken != null){
			Define.PIN = pin;
			Log.d("whooing", "PIN : " + pin);
			Log.d("whooing", "Second Token : " + secondToken);
			Intent intent = new Intent();
			intent.putExtra("pin", pin);
			intent.putExtra("token", secondToken);
			mActivity.setResult(Activity.RESULT_OK, intent);
			mActivity.finish();
			return true;
		}			
		else{
			return false;
		}
	}

	
}
