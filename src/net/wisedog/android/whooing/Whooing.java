package net.wisedog.android.whooing;

import net.wisedog.android.whooing.activity.MainFragmentActivity;
import net.wisedog.android.whooing.auth.WhooingAuthMain;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;


/**
 * Initial class that show Splash
 */
public class Whooing extends Activity {
	Context mContext = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("execute_before", false)){
                this.finish();
                return;
            }           
        }
        Define.ROBOFONT = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setContentView(R.layout.welcome);
        mContext = this;
        if(checkNetworkConnection() == false){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getString(R.string.welcome_no_internet_title));
            alert.setMessage(getString(R.string.welcome_no_internet));
            alert.setCancelable(false);
            alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    finish();
                }
            }).show();
            return;
        }
        
        getLoginInfo();
        
        Button nextBtn = (Button)findViewById(R.id.welcome_button_next);
        WiTextView messageText = (WiTextView)findViewById(R.id.welcome_message_board);
        //First Login
        if(Define.PIN == null || Define.REAL_TOKEN == null){
            messageText.setVisibility(View.GONE);
        }
        else{
            nextBtn.setVisibility(View.GONE);
            messageText.setVisibility(View.VISIBLE);
            messageText.setText("Logging in....");
            WiTextView titleText = (WiTextView)findViewById(R.id.welcome_title_text);
            titleText.setText(getString(R.string.app_name));
            //TODO Show "Logging in ...., Getting Account Info ... "
            DataRepository repository = DataRepository.getInstance();
            repository.refreshUserInfo(this);
            repository.refreshDashboardValue(this);
            repository.refreshAccount(this);
            repository.refreshLastestItems(this);
               
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    /*
                     * Account Setting 테스트할때 repository.refreshUserInfo(this); 주석처리하고할것
                     * Intent intent = new Intent(mContext, AccountSetting.class);
                    startActivityForResult(intent, Define.MSG_USER_SETTING_DONE);*/
                    Intent intent = new Intent(Whooing.this, MainFragmentActivity.class);
                    startActivityForResult(intent, Define.REQUEST_NORMAL);
                    /*Intent intent = new Intent(mContext, Welcome.class);
                    startActivityForResult(intent, Define.MSG_AUTH_TOTAL_DONE);*/
                }           
            };
            handler.sendEmptyMessageDelayed(0, 400);
        }
    }
    
    @Override
	public void onBackPressed() {
        // Allows back button press at login 
        if(Define.PIN == null || Define.REAL_TOKEN == null){
            super.onBackPressed();
        }
		// else block back button event
	}
    
    public void onClickNextBtn(View v){
        Intent intent = new Intent(this, WhooingAuthMain.class);
        startActivityForResult(intent, Define.MSG_AUTH_TOTAL_DONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == Define.MSG_AUTH_TOTAL_DONE){
                Intent intent = new Intent(Whooing.this, MainFragmentActivity.class);
                startActivityForResult(intent, Define.REQUEST_NORMAL);
            }
        
        }else if(resultCode == RESULT_CANCELED){
            if(requestCode == Define.REQUEST_NORMAL){
                this.finish();
                return;
            }
        }
        /*if (resultCode != RESULT_OK) {
            switch (resultCode) {
            case Define.RESPONSE_EXIT:
                setResult(Define.RESPONSE_EXIT);
                this.finish();
                break;
            case Define.MSG_AUTH_TOTAL_DONE:
                DataRepository repository = DataRepository.getInstance();
                repository.refreshUserInfo(this);
                Intent intent = new Intent(mContext, AccountSetting.class);
                startActivityForResult(intent, Define.MSG_USER_SETTING_DONE);
                break;
            case Define.MSG_USER_SETTING_DONE:
                getLoginInfo();
                DataRepository repository2 = DataRepository.getInstance();
                repository2.refreshDashboardValue(this);
                repository2.refreshAccount(this);
                Intent intent2 = new Intent(mContext, MainFragmentActivity.class);
                startActivityForResult(intent2, 1);
                break;
            }
        }*/
    }
	
	/* (non-Javadoc)
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("execute_before", true);
        super.onSaveInstanceState(outState);
    }
    
    protected void getLoginInfo(){
        //FIXME for debug
        Define.REAL_TOKEN = "70a73d146ca6209947ad59bdfdb1ee3755b06873";
        Define.PIN = "409713";
        Define.TOKEN_SECRET = "8bd064ccc751575cef4c0235a8ac946a2f2924a4";
        Define.APP_SECTION = "s10550";
        Define.USER_ID = 8955;
        Define.CURRENCY_CODE = "KRW";
        
        /*SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE, MODE_PRIVATE);
        Define.REAL_TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
        Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
        Define.TOKEN_SECRET = prefs.getString(Define.KEY_SHARED_TOKEN_SECRET, null);
        Define.APP_SECTION = prefs.getString(Define.KEY_SHARED_SECTION_ID, null);
        Define.USER_ID = prefs.getString(Define.KEY_SHARED_USER_ID, null);
        Log.i("wisedog", "user_id: " + Define.USER_ID + " app_section : " + Define.APP_SECTION + " real_token:" + Define.REAL_TOKEN
                + " pin : " + Define.PIN + " token_secret : " + Define.TOKEN_SECRET);*/
    }
    
    /**
     * Check now on network or not
     * 
     * @return True if now on internet, or false
     */
    protected boolean checkNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}