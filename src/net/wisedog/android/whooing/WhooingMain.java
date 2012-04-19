package net.wisedog.android.whooing;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 
 * */
public class WhooingMain extends Activity {
	private ProgressDialog dialog;
	private Activity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_main);
		mActivity = this;
		SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE, MODE_PRIVATE); 
		
		Define.TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
		Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
		Define.TOKEN_SECRET = prefs.getString(Define.KEY_SHARED_TOKEN_SECRET, null);
		Define.USER_ID = prefs.getString(Define.KEY_SHARED_USER_ID, null);
	}
	
    @Override
	protected void onResume() {
    	//TODO Move this!
    	if(Define.TOKEN == null || Define.PIN == null){
    		ThreadHandshake thread = new ThreadHandshake(mHandler, this, false);
    		thread.start();
    		dialog = ProgressDialog.show(this, "", getString(R.string.authorizing), true);
    		dialog.setCancelable(true);
    	}
    	else{
    		//TODO Move this!
    		ThreadRestAPI thread = new ThreadRestAPI(mHandler, this);
    		thread.start();
    	}
		super.onResume();
	}
    
    Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Define.MSG_FAIL){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_fail), 1000).show();
				return;
			}
			else if(msg.what == Define.MSG_REQ_AUTH){
				Intent intent = new Intent(WhooingMain.this, WhooingAuth.class);
				intent.putExtra(Define.KEY_AUTHPAGE, msg.obj.toString());
				intent.putExtra("token", Define.TOKEN);
				
				startActivityForResult(intent, Define.REQUEST_AUTH);
				startActivity(intent);
			}
			else if(msg.what == Define.MSG_DONE){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_success), 1000).show();
				ThreadRestAPI thread = new ThreadRestAPI(mHandler, mActivity);
	    		thread.start();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == Define.REQUEST_AUTH){
				SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(Define.KEY_SHARED_PIN, Define.PIN);
				editor.commit();
				//dialog.dismiss();
				//Toast.makeText(mContext, getString(R.string.msg_auth_success), 1000).show();
				ThreadHandshake thread = new ThreadHandshake(mHandler, this, true);
	    		thread.start();
			}			
		}
		else if(requestCode == RESULT_CANCELED){
			if(requestCode == Define.REQUEST_AUTH){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_fail), 1000).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
}
