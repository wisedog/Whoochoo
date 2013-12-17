/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing;

import net.wisedog.android.whooing.activity.MainFragmentActivity;
import net.wisedog.android.whooing.auth.WhooingAuthMain;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.onLoadingMessage;
import net.wisedog.android.whooing.widget.WiTextView;
import android.annotation.SuppressLint;
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
public class Whooing extends Activity implements onLoadingMessage{
    private final int MSG_GO_MAIN = 0;
    
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
        
        Define.gettingLoginInfo(this);
        
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

            DataRepository repository = WhooingApplication.getInstance().getRepo();
            repository.setLoadingMsgListener(this);
            repository.refreshUserInfo(this);
            repository.refreshAccount(this);
            repository.refreshLastestItems(this);
        }
    }
    
    @SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
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
                Define.gettingLoginInfo(this);
                DataRepository repository = WhooingApplication.getInstance().getRepo();
                repository.setLoadingMsgListener(this);
                repository.refreshUserInfo(this);
                repository.refreshAccount(this);
                repository.refreshLastestItems(this);
            }
            else if(requestCode == Define.REQUEST_NORMAL){
            	Button nextBtn = (Button)findViewById(R.id.welcome_button_next);
            	nextBtn.setVisibility(View.VISIBLE);
            	WiTextView titleText = (WiTextView)findViewById(R.id.welcome_title_text);
                titleText.setText(getString(R.string.welcome_msg));
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
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("execute_before", true);
        super.onSaveInstanceState(outState);
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
        }
        if(loadingStatus == 3){
            Intent intent = new Intent(Whooing.this, MainFragmentActivity.class);
            startActivityForResult(intent, Define.REQUEST_NORMAL);
        }
        
    }
}