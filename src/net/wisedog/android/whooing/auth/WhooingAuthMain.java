package net.wisedog.android.whooing.auth;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.activity.AccountSetting;
import net.wisedog.android.whooing.network.ThreadHandshake;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.widget.WiTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Authorization activity
 * @author Wisedog(me@wisedog.net)
 * */
public class WhooingAuthMain extends Activity {
	private ProgressDialog dialog;
	private Activity mActivity;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_auth_main);
		mActivity = this;

    	if(Define.PIN == null || Define.REAL_TOKEN == null){
    		ThreadHandshake thread = new ThreadHandshake(mHandler, this, false);
    		thread.start();
    		dialog = ProgressDialog.show(this, "", getString(R.string.authorizing), true);
    		dialog.setCancelable(true);
    	}
	}
    
    protected void updateProgress(int index){
        WiTextView indicator0 = (WiTextView)findViewById(R.id.auth_doing_first_step);
        WiTextView indicator1 = (WiTextView)findViewById(R.id.auth_doing_second_step);
        WiTextView indicator2 = (WiTextView)findViewById(R.id.auth_doing_third_step);
        
        WiTextView text0 = (WiTextView)findViewById(R.id.auth_text_first_step);
        WiTextView text1 = (WiTextView)findViewById(R.id.auth_text_second_step);
        WiTextView text2 = (WiTextView)findViewById(R.id.auth_text_third_step);
        
        ProgressBar progress0 = (ProgressBar)findViewById(R.id.auth_progress_first);
        ProgressBar progress1 = (ProgressBar)findViewById(R.id.auth_progress_second);
        ProgressBar progress2 = (ProgressBar)findViewById(R.id.auth_progress_third);
        
        if(index == 1){
            indicator0.setVisibility(View.GONE);
            indicator1.setVisibility(View.VISIBLE);
            progress0.setVisibility(View.INVISIBLE);
            progress1.setVisibility(View.VISIBLE);
            text0.setTextColor(0x777777);
            text0.setTypeface(Define.ROBOFONT, Typeface.NORMAL);
            text1.setTextColor(0x333333);
            text1.setTypeface(Define.ROBOFONT, Typeface.BOLD);
            
        }else if(index == 2){
            indicator1.setVisibility(View.GONE);
            indicator2.setVisibility(View.VISIBLE);
            progress1.setVisibility(View.INVISIBLE);
            progress2.setVisibility(View.VISIBLE);
            text1.setTextColor(0x777777);
            text1.setTypeface(Define.ROBOFONT, Typeface.NORMAL);
            text2.setTextColor(0x333333);
            text2.setTypeface(Define.ROBOFONT, Typeface.BOLD);
        }
    }
    
    Handler mHandler = new Handler(){
		@Override
		public void handleMessage(final Message msg) {
			if(msg.what == Define.MSG_FAIL){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Define.MSG_REQ_AUTH){
			    updateProgress(1);
			    postDelayed(new Runnable(){

                    @Override
                    public void run() {
                        Intent intent = new Intent(mActivity, WhooingAuthWeb.class);
                        intent.putExtra("first_token", (String)msg.obj);
                        
                        startActivityForResult(intent, Define.REQUEST_AUTH);
                        
                    }}, 500);
				
			}
			else if(msg.what == Define.MSG_AUTH_DONE){
			    updateProgress(2);
				ThreadRestAPI thread = new ThreadRestAPI(mHandler, mActivity, Define.API_GET_SECTIONS);
				thread.start();
			}
			else if(msg.what == Define.MSG_API_OK){
				if(msg.arg1 == Define.API_GET_SECTIONS){
					JSONObject result = (JSONObject)msg.obj;					
					try {
						JSONArray array = result.getJSONArray("results");
						JSONObject obj = (JSONObject) array.get(0);
						String section = obj.getString("section_id");
						if(section != null){
							Define.APP_SECTION = section;
							Log.d("wisedog", "APP SECTION:"+ Define.APP_SECTION);
							SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
									Activity.MODE_PRIVATE);
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(Define.KEY_SHARED_SECTION_ID, section);
							editor.commit();
							dialog.dismiss();
							Toast.makeText(mActivity, getString(R.string.msg_auth_success),
									Toast.LENGTH_LONG).show();
							Intent intent = new Intent(WhooingAuthMain.this, AccountSetting.class);
							startActivityForResult(intent, Define.MSG_SETTING_DONE);
							//setResult(Define.MSG_AUTH_TOTAL_DONE);
							//finish();    //TODO USER Setting here
						}
						else{
							throw new JSONException("Error in getting section id");
						}
					} catch (JSONException e) {
					    setErrorHandler("통신 오류! Err-SCT1");
						e.printStackTrace();
					}
				}
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
			    if(data == null){
			        setErrorHandler("인증오류! Err No.1");
			        return;
			    }
			    String secondtoken = data.getStringExtra("token");
			    String pin = data.getStringExtra("pin");
			    if(secondtoken == null || pin == null){
			        setErrorHandler("인증오류! Err No.2");
			        return;
			    }
			    Log.d("wisedog", "APP pin:"+ pin);
			    Define.PIN = pin;
				SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(Define.KEY_SHARED_PIN, pin);
				editor.commit();
				
				ThreadHandshake thread = new ThreadHandshake(mHandler, this, true, secondtoken);
	    		thread.start();
			}
			else if(requestCode == Define.MSG_SETTING_DONE){
			    
			}
		}
		else if(resultCode == RESULT_CANCELED){
			if(requestCode == Define.REQUEST_AUTH){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void setErrorHandler(String errorMsg){
	    if(dialog != null){
	        dialog.dismiss();
	    }
	    Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
	}
}
