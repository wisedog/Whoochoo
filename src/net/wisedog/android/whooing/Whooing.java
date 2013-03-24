package net.wisedog.android.whooing;

import net.wisedog.android.whooing.activity.MainFragmentActivity;
import net.wisedog.android.whooing.auth.WhooingAuthMain;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.onLoadingMessage;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * Initial class that show Splash
 */
public class Whooing extends Activity implements onLoadingMessage{
    private final int MSG_GO_MAIN = 0;
    
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
            messageText.setText(getString(R.string.welcome_logging_in));
            WiTextView titleText = (WiTextView)findViewById(R.id.welcome_title_text);
            titleText.setText(getString(R.string.app_name));

            DataRepository repository = DataRepository.getInstance();
            repository.setLoadingMsgListener(this);
            repository.refreshUserInfo(this);
            repository.refreshDashboardValue(this);
            repository.refreshAccount(this);
            repository.refreshLastestItems(this);
        }
    }
    
    Handler mHandler = new Handler(){

        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_GO_MAIN){
                Intent intent = new Intent(Whooing.this, MainFragmentActivity.class);
                startActivityForResult(intent, Define.REQUEST_NORMAL);
            }
            super.handleMessage(msg);
        }
        
    };
    
    @Override
	public void onBackPressed() {
        // Allows back button press at login 
        if(Define.PIN == null || Define.REAL_TOKEN == null){
            super.onBackPressed();
        }
		// else block back button event
	}
    
    /**
     * Event handler for button next
     * @param   v   View to be pressed
     * */
    public void onClickNextBtn(View v){
        Intent intent = new Intent(this, WhooingAuthMain.class);
        startActivityForResult(intent, Define.MSG_AUTH_TOTAL_DONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            //When the authorization is completed 
            if(requestCode == Define.MSG_AUTH_TOTAL_DONE){
                Button nextBtn = (Button)findViewById(R.id.welcome_button_next);
                WiTextView messageText = (WiTextView)findViewById(R.id.welcome_message_board);
                messageText.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.GONE);
                getLoginInfo();
                DataRepository repository = DataRepository.getInstance();
                repository.setLoadingMsgListener(this);
                repository.refreshUserInfo(this);
                /*repository.refreshDashboardValue(this);*/
                repository.refreshAccount(this);
                repository.refreshLastestItems(this);
            }
        
        }else if(resultCode == RESULT_CANCELED){
            if(requestCode == Define.REQUEST_NORMAL){
                this.finish();
                return;
            }
            else if(requestCode == Define.MSG_AUTH_TOTAL_DONE){
                this.finish();
                return;
            }
        }
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
        if(Define.DEBUG){
            Define.REAL_TOKEN = "9772ff8e2f751ddf1c5cf74b0d9b328f392a4b71";
            Define.PIN = "339599";
            Define.TOKEN_SECRET = "7776e79057bc222254da0b108555afd86e3b7d3c";
            Define.APP_SECTION = "s10550";
            Define.USER_ID = 8955;
            Define.CURRENCY_CODE = "KRW";
            Define.COUNTRY_CODE="KR";
            Define.LOCALE_LANGUAGE = "ko";
        }
        
        /*SharedPreferences prefs = getSharedPreferences(Define.SHARED_PREFERENCE, MODE_PRIVATE);
        Define.REAL_TOKEN = prefs.getString(Define.KEY_SHARED_TOKEN, null);
        Define.PIN = prefs.getString(Define.KEY_SHARED_PIN, null);
        Define.TOKEN_SECRET = prefs.getString(Define.KEY_SHARED_TOKEN_SECRET, null);
        Define.APP_SECTION = prefs.getString(Define.KEY_SHARED_SECTION_ID, null);
        Define.USER_ID = prefs.getInt(Define.KEY_SHARED_USER_ID, 0);
        Define.CURRENCY_CODE = prefs.getString(Define.KEY_SHARED_CURRENCY_CODE, null);
        Define.COUNTRY_CODE = prefs.getString(Define.KEY_SHARED_COUNTRY_CODE, null);
        Define.LANGUAGE_APP = prefs.getString(Define.KEY_SHARED_LANGUAGE_APP, null);
        Define.TIMEZONE = prefs.getString(Define.KEY_SHARED_TIMEZONE, null);
        Log.i("wisedog", "user_id: " + Define.USER_ID + " app_section : " + Define.APP_SECTION + " real_token:" + Define.REAL_TOKEN
                + " pin : " + Define.PIN + " token_secret : " + Define.TOKEN_SECRET);
        Log.i("wisedog", "country: " + Define.COUNTRY_CODE + " currency: " + Define.CURRENCY_CODE 
                + " LanguageApp : " + Define.LANGUAGE_APP + " Timezone: " + Define.TIMEZONE );*/
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
    
    protected int loadingStatus = 0;

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.onLoadingMessage#onMessage(int)
     */
    @Override
    public void onMessage(int message) {
        WiTextView messageText = (WiTextView)findViewById(R.id.welcome_message_board);
        switch(message){
        case DataRepository.USER_MODE:
            loadingStatus++;
            messageText.setText(getString(R.string.welcome_loading_get_user_info));
            break;
        case DataRepository.LATEST_TRANSACTION:
            loadingStatus++;
            messageText.setText(getString(R.string.welcome_loading_get_transaction));
            break;
        case DataRepository.ACCOUNT_MODE:
            loadingStatus++;
            messageText.setText(getString(R.string.welcome_loading_get_account_info));
            break;
/*        case DataRepository.DASHBOARD_MODE:
            loadingStatus++;
            messageText.setText(getString(R.string.welcome_loading_get_data));
            break;*/
        }
        if(loadingStatus == 3){
            Intent intent = new Intent(Whooing.this, MainFragmentActivity.class);
            startActivityForResult(intent, Define.REQUEST_NORMAL);
        }
        
    }
}