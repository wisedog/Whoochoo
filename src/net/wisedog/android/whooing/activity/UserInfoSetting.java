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
package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import net.wisedog.android.whooing.widget.WiButton;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * UserInfo setting class. User can set country, currency, language, nickname
 * @author Wisedog(me@wisedog.net)
 */
public class UserInfoSetting extends Activity{

    private ProgressDialog mProgress;
    private String mUserName;
    private boolean isChecked = false;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting);
        setEnableUi(false);
        DataRepository repository = WhooingApplication.getInstance().getRepo();
        if(repository.getUserValue() == null){
            mProgress = ProgressDialog.show(this, "", 
                    getString(R.string.user_info_progress));

    		ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_USER_INFO);
    		thread.start();
        }
        else{
            initUi(repository.getUserValue());
        }
    }
    
    protected void setEnableUi(boolean flag){
        Spinner countrySpinner = (Spinner)findViewById(R.id.user_setting_spinner_country);
        countrySpinner.setEnabled(flag);
        Spinner timezoneSpinner = (Spinner)findViewById(R.id.user_setting_spinner_timezone);
        timezoneSpinner.setEnabled(flag);
        Spinner langAppSpinner = (Spinner)findViewById(R.id.user_setting_spinner_language_app);
        langAppSpinner.setEnabled(flag);
        Spinner currencySpinner = (Spinner)findViewById(R.id.user_setting_spinner_currency);
        currencySpinner.setEnabled(flag);
        EditText nicknameEdit = (EditText)findViewById(R.id.user_setting_nickname_edit);
        nicknameEdit.setEnabled(flag);
        WiButton completeBtn = (WiButton)findViewById(R.id.user_setting_complete);
        completeBtn.setEnabled(flag);
        WiButton checkBtn = (WiButton)findViewById(R.id.user_setting_check_btn);
        checkBtn.setEnabled(flag);
        
        
    }
    
    protected void initUi(JSONObject obj){
        
        setEnableUi(true);
        
        if(getIntent().getBooleanExtra("from_menu", false)){
            WiButton cancelBtn = (WiButton)findViewById(R.id.user_setting_cancel);
            if(cancelBtn != null){
                cancelBtn.setVisibility(View.VISIBLE);
            }
        }
        else{
            WiTextView textUsername = (WiTextView)findViewById(R.id.user_setting_text_username);
            if(textUsername != null){
                textUsername.setVisibility(View.GONE);
            }
            LinearLayout ll = (LinearLayout)findViewById(R.id.user_setting_layout_username);
            if(ll != null){
                ll.setVisibility(View.GONE);
            }
        }
        
        //Spinner adapter
        Spinner countrySpinner = (Spinner)findViewById(R.id.user_setting_spinner_country);
        Spinner timezoneSpinner = (Spinner)findViewById(R.id.user_setting_spinner_timezone);
        Spinner langAppSpinner = (Spinner)findViewById(R.id.user_setting_spinner_language_app);
        Spinner currencySpinner = (Spinner)findViewById(R.id.user_setting_spinner_currency);
        
        String country = null;
        String currency = null;
        String username = null;
        String timezone = null;
        String language = null;
        try {
            JSONObject objResult = obj.getJSONObject("results");
            country = objResult.getString("country");
            currency = objResult.getString("currency");
            username = objResult.getString("username");
            timezone = objResult.getString("timezone");
            language = objResult.getString("language");
            
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error while getting info. Please contact the developer.", Toast.LENGTH_LONG).show();
            return;
        }
        
        if(getIntent().getBooleanExtra("from_menu", false)){
            if(Define.LOCALE_LANGUAGE != null){
                language = Define.LOCALE_LANGUAGE;
            }            
        }
        
        mUserName = username;
        
        EditText editText = (EditText)findViewById(R.id.user_setting_nickname_edit);
        editText.setText(username);
        
        int idxCountry = 0;
        for(int i = 0; i < WhooingCurrency.COUNTRY_CODE.length; i++){
            if(WhooingCurrency.COUNTRY_CODE[i].compareToIgnoreCase(country) == 0){
                idxCountry = i;
                break;
            }
        }
        
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, WhooingCurrency.COUNTRY) {

            /*
             * (non-Javadoc)
             * 
             * @see android.widget.ArrayAdapter#getView(int, android.view.View,
             * android.view.ViewGroup)
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }

        };
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setSelection(idxCountry);
        
        int idxTimeZone = 0;
        for(int i = 0; i < WhooingCurrency.TIMEZONE.length; i++){
            if(WhooingCurrency.TIMEZONE[i].compareToIgnoreCase(timezone) == 0){
                idxTimeZone = i;
                break;
            }
        }
        
        ArrayAdapter<String> timezoneAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, WhooingCurrency.TIMEZONE) {

            /*
             * (non-Javadoc)
             * 
             * @see android.widget.ArrayAdapter#getView(int, android.view.View,
             * android.view.ViewGroup)
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }

        };
        timezoneSpinner.setAdapter(timezoneAdapter);
        timezoneSpinner.setSelection(idxTimeZone);
        
        int idxLanguage = 0;
        for(int i = 0; i < WhooingCurrency.LOCALE_LANGUAGE_CODE.length; i++){
            if(WhooingCurrency.LOCALE_LANGUAGE_CODE[i].compareToIgnoreCase(language) == 0){
                idxLanguage = i;
                break;
            }
        }
        ArrayAdapter<String> langAppAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, WhooingCurrency.LOCALE_LANGUAGE_NAME) {

            /*
             * (non-Javadoc)
             * 
             * @see android.widget.ArrayAdapter#getView(int, android.view.View,
             * android.view.ViewGroup)
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }

        };
        langAppSpinner.setAdapter(langAppAdapter);
        langAppSpinner.setSelection(idxLanguage);
        
        int idxCurrency = 0;
        for(int i = 0; i < WhooingCurrency.CURRENCY.length; i++){
            if(WhooingCurrency.CURRENCY[i].compareToIgnoreCase(currency) == 0){
                idxCurrency = i;
                break;
            }
        }
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, WhooingCurrency.CURRENCY) {

            /*
             * (non-Javadoc)
             * 
             * @see android.widget.ArrayAdapter#getView(int, android.view.View,
             * android.view.ViewGroup)
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }

        };
        currencySpinner.setAdapter(currencyAdapter);
        currencySpinner.setSelection(idxCurrency);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        /*DataRepository repository = WhooingApplication.getInstance().getRepo(); //DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.USER_MODE);*/
        super.onDestroy();
    }
    
    /**
     * Event handler for complete button
     * @param   v   view to press
     * 
     * */
    public void onClickComplete(View v){
        if(getIntent().getBooleanExtra("from_menu", false)){
            EditText editText = (EditText)findViewById(R.id.user_setting_nickname_edit);
            if(editText.getText().toString().compareTo(mUserName) == 0){    //not be changed name. 
                ;// go ahead
            }else{
                if(isChecked == false){
                    Toast.makeText(this, getString(R.string.user_info_alert_check_name), Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    ; //go ahead
                }
            }
        }
        SharedPreferences prefs = this.getSharedPreferences(Define.SHARED_PREFERENCE,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		Spinner countrySpinner = (Spinner)findViewById(R.id.user_setting_spinner_country);
		int idx = countrySpinner.getSelectedItemPosition();
		if(idx <= 0){
		    Toast.makeText(this, getString(R.string.user_info_alert_country), Toast.LENGTH_LONG).show();
		    return;
		}else if(idx > 0){
			editor.putString(Define.KEY_SHARED_COUNTRY_CODE, WhooingCurrency.COUNTRY_CODE[idx]);
			editor.commit();
			Define.COUNTRY_CODE = WhooingCurrency.COUNTRY_CODE[idx];
		}
		
		Spinner timezoneSpinner = (Spinner)findViewById(R.id.user_setting_spinner_timezone);
		idx = timezoneSpinner.getSelectedItemPosition();
		if(idx <= 0){
			; //do nothing. Timezone information is not import for us
		}else if(idx > 0){
			editor.putString(Define.KEY_SHARED_TIMEZONE, WhooingCurrency.TIMEZONE[idx]);
			editor.commit();
			Define.TIMEZONE = WhooingCurrency.TIMEZONE[idx];
		}
		
        Spinner langAppSpinner = (Spinner)findViewById(R.id.user_setting_spinner_language_app);
        idx = langAppSpinner.getSelectedItemPosition();
        if(idx <= 0){
            Toast.makeText(this, getString(R.string.user_info_alert_language), Toast.LENGTH_LONG).show();
            return;
        }else if(idx > 0){
			editor.putString(Define.KEY_SHARED_LOCALE_LANGUAGE, WhooingCurrency.LOCALE_LANGUAGE_CODE[idx]);
			editor.commit();
			Define.LOCALE_LANGUAGE = WhooingCurrency.LOCALE_LANGUAGE_CODE[idx];
		}
        
        Spinner currencySpinner = (Spinner)findViewById(R.id.user_setting_spinner_currency);
        idx = currencySpinner.getSelectedItemPosition();
		if(idx <= 0){
		    Toast.makeText(this, R.string.user_info_alert_currency, Toast.LENGTH_LONG).show();
            return;
		}else if(idx > 0){
			editor.putString(Define.KEY_SHARED_CURRENCY_CODE, WhooingCurrency.CURRENCY[idx]);
			editor.commit();
			Define.CURRENCY_CODE = WhooingCurrency.CURRENCY[idx];
		}
		
        setResult(RESULT_OK);
        finish();
    }
    
    public void onClickCancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        if(getIntent().getBooleanExtra("from_menu", false)){
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.exit));
        alert.setMessage(getString(R.string.is_exit));
        alert.setCancelable(true);
        alert.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }
    
    public void onClickCheckName(View v){
        EditText editText = (EditText)findViewById(R.id.user_setting_nickname_edit);
        ProgressBar bar = (ProgressBar)findViewById(R.id.user_setting_progress_bar);
        if(bar != null){
            bar.setVisibility(View.VISIBLE);
        }
        v.setEnabled(false);
        if(editText != null){
            Bundle b = new Bundle();
            b.putString("username", editText.getText().toString());
            ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_PUT_USER_INFO, b);
            thread.start();
        }        
    }
    
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
            	JSONObject obj = (JSONObject)msg.obj;
            	if(msg.arg1 == Define.API_GET_USER_INFO){
            		DataRepository repository = WhooingApplication.getInstance().getRepo();
            		repository.setUserInfo(obj);
            		if(mProgress != null){
                        mProgress.dismiss();
                    }
                    initUi(obj);
            	}
            	else if(msg.arg1 == Define.API_PUT_USER_INFO){
            		
                    ProgressBar bar = (ProgressBar)findViewById(R.id.user_setting_progress_bar);
                    if(bar != null){
                        bar.setVisibility(View.INVISIBLE);
                    }
                    WiButton btn = (WiButton)findViewById(R.id.user_setting_check_btn);
                    if(btn != null){
                        btn.setEnabled(true);
                    }
                    int result = 0;
                    try {
                        result = obj.getInt("code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    if(result != Define.RESULT_OK){
                        Toast.makeText(UserInfoSetting.this, getString(R.string.user_info_alert_name_taken), Toast.LENGTH_LONG).show();
                        isChecked = false;
                    }
                    else{
                        isChecked = true;
                        Toast.makeText(UserInfoSetting.this, getString(R.string.user_info_alert_name_ok), Toast.LENGTH_LONG).show();
                    }
            	}
                
                
            }
            super.handleMessage(msg);
        }
        
    };
    
}
