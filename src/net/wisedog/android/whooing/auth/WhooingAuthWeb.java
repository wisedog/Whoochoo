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
