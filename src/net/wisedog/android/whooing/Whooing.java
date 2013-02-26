package net.wisedog.android.whooing;

import net.wisedog.android.whooing.activity.AccountSetting;
import net.wisedog.android.whooing.activity.MainFragmentActivity;
import net.wisedog.android.whooing.activity.Welcome;
import net.wisedog.android.whooing.auth.WhooingAuthMain;
import net.wisedog.android.whooing.engine.DataRepository;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean("execute_before", false)){
                this.finish();
                return;
            }           
        }
        Define.ROBOFONT = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        //String locale = getResources().getConfiguration().locale.getDisplayName();
        //Toast.makeText(this, "Locale :"+ locale, Toast.LENGTH_SHORT).show();
        //TODO SharedPreference 확인해서 회원가입 UI 띄우기. 아니면 바로 WhooingMain으로 이동
        setContentView(R.layout.main);
        mContext = this;
        
        getLoginInfo();
        
        if(Define.PIN == null || Define.REAL_TOKEN == null){
            Intent intent = new Intent(Whooing.this, WhooingAuthMain.class);
            startActivityForResult(intent, 1);
            return;
        }
        else{
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
                    Intent intent = new Intent(mContext, MainFragmentActivity.class);
                    startActivityForResult(intent, Define.MSG_AUTH_TOTAL_DONE);
                    /*Intent intent = new Intent(mContext, Welcome.class);
                    startActivityForResult(intent, Define.MSG_AUTH_TOTAL_DONE);*/
                }           
            };
            handler.sendEmptyMessageDelayed(0, 200);
        }
    }
    
    @Override
	public void onBackPressed() {
		// block back button event
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {   
        if (resultCode != RESULT_OK) {
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
}