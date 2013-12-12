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

import net.wisedog.android.whooing.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class WhooingAuthWeb extends Activity{

	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_auth_web);
	}
	
	

	/* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        Intent intent = getIntent();
        String firstToken = intent.getStringExtra("first_token");
        WebView webView = (WebView) findViewById(R.id.webViewAuth);
        webView.setWebViewClient(new WhooingWebViewClient(this));
        // remove horizontal/vertical scroll bar
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setLightTouchEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(true);

        webView.loadUrl("https://whooing.com/simple_auth/authorize?token="+firstToken);
        super.onResume();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        WebView webView = (WebView) findViewById(R.id.webViewAuth);
        if(webView != null){
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }

}
