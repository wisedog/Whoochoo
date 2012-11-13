package net.wisedog.android.whooing;

import java.text.DecimalFormat;

import net.wisedog.android.whooing.db.WhooingDbOpenHelper;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.engine.MainProcessor;
import net.wisedog.android.whooing.network.ThreadHandshake;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.ui.NavigationBar;
import net.wisedog.android.whooing.ui.NavigationBar.OnNavigationClick;

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
public class WhooingMain extends Activity implements OnNavigationClick {
	private ProgressDialog dialog;
	private Activity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whooing_main1);
		mActivity = this;
		
		NavigationBar navBar = (NavigationBar)findViewById(R.id.nav_bar);
		navBar.setNavigationListener(this);
		navBar.setupNavbar();
		
		//For Debug
		Define.REAL_TOKEN = "13165741351c21b2088c12706c1acd1d63cf7b49";
		Define.PIN = "992505";
		Define.TOKEN_SECRET = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
		Define.USER_ID = "8955";
		Define.APP_SECTION = "s10550";
		this.deleteDatabase(WhooingDbOpenHelper.DATABASE_NAME);
		
		
		/*SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE, MODE_PRIVATE);
		Define.REAL_TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
		Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
		Define.TOKEN_SECRET = prefs.getString(Define.KEY_SHARED_TOKEN_SECRET, null);
		Define.APP_SECTION = prefs.getString(Define.KEY_SHARED_SECTION_ID, null);
		Define.USER_ID = prefs.getString(Define.KEY_SHARED_USER_ID, null);*/
    	if(Define.PIN == null || Define.REAL_TOKEN == null){
    		ThreadHandshake thread = new ThreadHandshake(mHandler, this, false);
    		thread.start();
    		dialog = ProgressDialog.show(this, "", getString(R.string.authorizing), true);
    		dialog.setCancelable(true);
    	}
    	else{
    		GeneralProcessor generalProcessor = new GeneralProcessor(this);
    		if(generalProcessor.checkingAccountsInfo()){
    			MainProcessor mainProcessor = new MainProcessor(this);
        		mainProcessor.refreshAll();
    		}
    		else{
    			Toast.makeText(this, "Getting accounts information", 
    					Toast.LENGTH_LONG).show();
    			ThreadRestAPI thread = new ThreadRestAPI(mGeneralHandler, 
    					mActivity, Define.API_GET_ACCOUNTS);
    			thread.start();
    		}
    	}
	}
	
    @Override
	protected void onResume() {
		super.onResume();
	}
    
    public void refreshAll(){
    	ThreadRestAPI thread = new ThreadRestAPI(mHandler, this, Define.API_GET_MAIN);
		thread.start();
    }
    
    
    Handler mGeneralHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Define.MSG_FAIL){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Define.MSG_API_OK){
				if(msg.arg1 == Define.API_GET_ACCOUNTS){
					JSONObject result = (JSONObject)msg.obj;
					try {
						JSONObject objResult = result.getJSONObject("results");
						GeneralProcessor general = new GeneralProcessor(mActivity);
						general.fillAccountsTable(objResult);
						Toast.makeText(mActivity, "Complete", Toast.LENGTH_LONG).show();
						MainProcessor mainProcessor = new MainProcessor(mActivity);
		              mainProcessor.refreshAll();
					}
					catch(JSONException e){
					    e.printStackTrace();
					    Toast.makeText(mActivity, "Exception", Toast.LENGTH_LONG).show();
					}
				}
			}
			super.handleMessage(msg);
		}
    	
    };
    
    Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Define.MSG_FAIL){
				dialog.dismiss();
				Toast.makeText(mActivity, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
			}
			else if(msg.what == Define.MSG_REQ_AUTH){
				Intent intent = new Intent(mActivity, WhooingAuth.class);
				intent.putExtra("first_token", (String)msg.obj);
				
				startActivityForResult(intent, Define.REQUEST_AUTH);
			}
			else if(msg.what == Define.MSG_AUTH_DONE){
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
							Log.d("whooing", "APP SECTION:"+ Define.APP_SECTION);
							SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
									Activity.MODE_PRIVATE);
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString(Define.KEY_SHARED_SECTION_ID, section);
							editor.commit();
							dialog.dismiss();
							Toast.makeText(mActivity, getString(R.string.msg_auth_success),
									Toast.LENGTH_LONG).show();
						}
						else{
							throw new JSONException("Error in getting section id");
						}
					} catch (JSONException e) {
					    setErrorHandler("통신 오류! Err-SCT1");
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
					    setErrorHandler("통신 오류! Err-BDG1");
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
					    setErrorHandler("통신 오류! Err-BNC1");
						e.printStackTrace();
					}catch(IllegalArgumentException e){
					    setErrorHandler("통신 오류! Err-BNC2");
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
			    Define.PIN = pin;
				SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(Define.KEY_SHARED_PIN, pin);
				editor.commit();
				
				ThreadHandshake thread = new ThreadHandshake(mHandler, this, true, secondtoken);
	    		thread.start();
			}			
		}
		else if(requestCode == RESULT_CANCELED){
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

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.ui.NavigationBar.OnNavigationClick#onHeadButton(java.lang.String)
     */
    public void onHeadButton(String type) {
        String[] itemArray = new String[]{"추가","b"};
        new AlertDialog.Builder(this)
        .setTitle("asdf")
        .setItems(itemArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    Intent intent = new Intent(mActivity, TransactionAdd.class);
                    startActivityForResult(intent, 1);
                }
            }
        }).setCancelable(true).create().show();
    }
}
