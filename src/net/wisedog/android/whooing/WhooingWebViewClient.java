package net.wisedog.android.whooing;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
