package net.wisedog.android.whooing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.wisedog.android.whooing.utils.RegExUtil;

public class WhooingWebViewClient extends WebViewClient {
	private Activity mActivity;
	
	public WhooingWebViewClient(Activity mActivity) {
		super();
		this.mActivity = mActivity;
	}

	@SuppressWarnings("static-access")
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		String pin = RegExUtil.takePin(url);
		if(pin == null){
			Define.PIN = pin;
			SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
					mActivity.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(Define.KEY_SHARED_PIN, pin);
			editor.commit();
			mActivity.setResult(mActivity.RESULT_OK);
			mActivity.finish();
		}			
		else{
			return false;
		}
		return super.shouldOverrideUrlLoading(view, url);
	}

	
}
