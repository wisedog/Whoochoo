package net.wisedog.android.whooing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WhooingMain extends Activity {
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_main);
		SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE, MODE_PRIVATE); 
		Define.TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
		Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
	}
	
    @Override
	protected void onResume() {
    	if(Define.TOKEN == null || Define.PIN == null){
    		ThreadHandshake thread = new ThreadHandshake(mHandler, this);
    		thread.start();
    		dialog = ProgressDialog.show(this, "", getString(R.string.authorizing), true);
    	}
		super.onResume();
	}
    
    Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Define.MSG_FAIL){
				return;
			}
			else if(msg.what == Define.MSG_OK){
				dialog.dismiss();		
			}
			
		}		
	};

	@Override
	public void onBackPressed() {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.exit));
		alert.setMessage(getString(R.string.is_exit));
		alert.setCancelable(true);
		alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				setResult(Define.RESPONSE_EXIT);
				finish();
			}
		});
		alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).show();
	}
	
}
