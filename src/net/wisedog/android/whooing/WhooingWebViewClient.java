package net.wisedog.android.whooing;

import android.app.Activity;
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

	@SuppressWarnings("static-access")
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		String pin = RegExUtil.takePin(url);
		if(pin != null){
			Define.PIN = pin;
			mActivity.setResult(mActivity.RESULT_OK);
			mActivity.finish();	//FIXME Geez. WhooingAuth activity will create again! not finish
			return false;
		}			
		else{
			return true;
		}
	}

	
}
