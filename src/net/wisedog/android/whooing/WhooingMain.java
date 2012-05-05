package net.wisedog.android.whooing;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
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
		//For Debug
		Define.TOKEN = "ca01f5d4b108ae0fb14c60be98b24f353b57ba50";
		Define.PIN = "731251";
		Define.TOKEN_SECRET = "c753d2953d283694b378332d8f3919be155748b7";
		Define.USER_ID = "93aa0354c21629add8f373887b15f0e3";
		Define.APP_SECTION = "s2128";
		
		/*
		SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE, MODE_PRIVATE);
		Define.TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
		Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
		Define.TOKEN_SECRET = prefs.getString(Define.KEY_SHARED_TOKEN_SECRET, null);
		Define = prefs.getString(Define.KEY_SHARED_SECTION_ID, null);
		Define.USER_ID = prefs.getString(Define.KEY_SHARED_USER_ID, null);*/
    	if(Define.TOKEN == null || Define.PIN == null){
    		ThreadHandshake thread = new ThreadHandshake(mHandler, this, false);
    		thread.start();
    		dialog = ProgressDialog.show(this, "", getString(R.string.authorizing), true);
    		dialog.setCancelable(true);
    	}
    	else{
    		refreshAll();
    	}
	}
	
    @Override
	protected void onResume() {
		super.onResume();
	}
    
    public void refreshAll(){
    	ThreadRestAPI thread = new ThreadRestAPI(mHandler, this, Define.API_GET_MAIN);
		thread.start();
		/*ThreadRestAPI thread = new ThreadRestAPI(mHandler, this, Define.API_GET_BUDGET);
		thread.start();
		ThreadRestAPI thread1 = new ThreadRestAPI(mHandler, this, Define.API_GET_BALANCE);
		thread1.start();*/
    }
    
    Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Define.MSG_FAIL){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_fail), 1000).show();
			}
			else if(msg.what == Define.MSG_REQ_AUTH){
				Intent intent = new Intent(WhooingMain.this, WhooingAuth.class);
				intent.putExtra(Define.KEY_AUTHPAGE, msg.obj.toString());
				intent.putExtra("token", Define.TOKEN);
				
				startActivityForResult(intent, Define.REQUEST_AUTH);
				startActivity(intent);
			}
			else if(msg.what == Define.MSG_AUTH_DONE){
				ThreadRestAPI thread = new ThreadRestAPI(mHandler, mActivity, Define.API_GET_SECTIONS);
				thread.start();
			}
			else if(msg.what == Define.MSG_API_OK){
				if(msg.arg1 == Define.API_GET_MAIN){
					JSONObject obj = (JSONObject)msg.obj;
					TextView monthlyBudgetText = (TextView)findViewById(R.id.budget_monthly);
					TextView monthlyExpenseText = (TextView)findViewById(R.id.budget_monthly_expense);
					try {
						JSONObject total = obj.getJSONObject("budget").getJSONObject("aggregate")
								.getJSONObject("total");
						
						int budget = total.getInt("budget");
						int expenses = total.getInt("money");
						if(budget < expenses){
							monthlyExpenseText.setTextColor(Color.RED);
						}
						monthlyBudgetText.setText("예산:"+budget);
						monthlyExpenseText.setText("지출 : "+expenses);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					TextView currentBalance = (TextView)findViewById(R.id.balance_num);
					TextView inoutBalance = (TextView)findViewById(R.id.inout_num);
					try{
						JSONObject obj1 = obj.getJSONObject("bs").getJSONObject("capital");
						DecimalFormat df = new DecimalFormat("#,##0");
						currentBalance.setText(df.format(obj1.getLong("total")));
						
						JSONObject obj2 = obj.getJSONObject("bs").getJSONObject("liabilities");
						inoutBalance.setText(df.format(obj2.getLong("total")));						
						
					}catch(JSONException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(IllegalArgumentException e){
						e.printStackTrace();
					}
					
					TextView creditCard = (TextView)findViewById(R.id.text_credit_card);
					try {
						JSONArray array = obj.getJSONObject("bill").getJSONObject("aggregate")
								.getJSONArray("accounts");
/*						JSONObject total = obj.getJSONObject("bill").getJSONObject("aggregate")
								.getJSONObject("total");*/
						
						String fullString = "";
						for(int i = 0; i< array.length(); i++){
							JSONObject object =(JSONObject) array.get(i);
							String accountName = object.getString("account_id");
							long money = object.getLong("money");
							fullString = fullString + accountName + " : " + money+ "\n";
						}
						creditCard.setText(fullString);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
				else if(msg.arg1 == Define.API_GET_SECTIONS){
					JSONObject result = (JSONObject)msg.obj;					
					try {
						JSONArray array = result.getJSONArray("results");					
						JSONObject obj = (JSONObject) array.get(0);
						String section = obj.getString("section_id");
						if(section != null){
							Define.APP_SECTION = section;
							Log.i("Wisedog", "APP SECTION:"+ Define.APP_SECTION);
							SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
									Activity.MODE_PRIVATE);
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(Define.KEY_SHARED_SECTION_ID, section);
							editor.commit();
							dialog.dismiss();
							Toast.makeText(mActivity, getString(R.string.msg_auth_success),
									1000).show();
						}
						else{
							throw new JSONException("Error in getting section id");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(msg.arg1 == Define.API_GET_BUDGET){
					TextView monthlyBudgetText = (TextView)findViewById(R.id.budget_monthly);
					TextView monthlyExpenseText = (TextView)findViewById(R.id.budget_monthly_expense);
					JSONObject obj = (JSONObject)msg.obj;
					try {
						int budget = obj.getInt("budget");
						int expenses = obj.getInt("money");
						if(budget < expenses){
							monthlyExpenseText.setTextColor(Color.RED);
						}
						monthlyBudgetText.setText("예산:"+budget);
						monthlyExpenseText.setText("지출 : "+expenses);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				else if(msg.arg1 == Define.API_GET_BALANCE){
					TextView currentBalance = (TextView)findViewById(R.id.balance_num);
					TextView inoutBalance = (TextView)findViewById(R.id.inout_num);
					JSONObject obj = (JSONObject)msg.obj;
					try{
						JSONObject obj1 = obj.getJSONObject("assets");
						DecimalFormat df = new DecimalFormat("#,##0");
						currentBalance.setText(df.format(obj1.getLong("total")));
						
						JSONObject obj2 = obj.getJSONObject("liabilities");
						inoutBalance.setText(df.format(obj2.getLong("total")));						
						
					}catch(JSONException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(IllegalArgumentException e){
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
				SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(Define.KEY_SHARED_PIN, Define.PIN);
				Log.i("Wisedog", "PIN:"+Define.PIN);
				editor.commit();
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
