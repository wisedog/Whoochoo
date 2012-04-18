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
		//FIXME This activity is not finished() when finish() is called in WhooingWebViewClient
		// So I can't help avoiding dirty
		if(Define.PIN != null){
			setResult(RESULT_OK);
			this.finish();
		}
		setContentView(R.layout.whooing_auth);
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

		webView.loadDataWithBaseURL("https://whooing.com", content, "text/html", "UTF-8", null);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void finishActivity(int requestCode) {
		// TODO Auto-generated method stub
		super.finishActivity(requestCode);
	}
	
	
}
