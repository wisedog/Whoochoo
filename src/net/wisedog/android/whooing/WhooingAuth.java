package net.wisedog.android.whooing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WhooingAuth extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_auth);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		String content = intent.getStringExtra(Define.KEY_AUTHPAGE);
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

		//webView.loadData(content, "text/html", "UTF-8");
		webView.loadDataWithBaseURL("https://whooing.com", content, "text/html", "UTF-8", null);
	}
}
