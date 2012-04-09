package net.wisedog.android.whooing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * Initial class that show Splash
 */
public class Whooing extends Activity {
	Context mContext = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = this;
        Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent(mContext, WhooingMain.class);
				startActivity(intent);
			}			
		};
		handler.sendEmptyMessageDelayed(0, 2000);
    }
    
    @Override
	public void onBackPressed() {
		// block back event
		//super.onBackPressed();
	}
    
    
    @Override
	protected void onResume() {
		super.onResume();
	}

}