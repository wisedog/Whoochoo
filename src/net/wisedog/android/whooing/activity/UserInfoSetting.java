/**
 * 
 */
package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.OnUserChangeListener;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * UserInfo setting class. User can set country, currency, language, nickname
 * @author Wisedog(me@wisedog.net)
 */
public class UserInfoSetting extends Activity implements OnUserChangeListener{

    private ProgressDialog mProgress;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
        setEnableUi(false);
        DataRepository repository = DataRepository.getInstance();
        if(repository.getUserValue() == null){
            mProgress = ProgressDialog.show(this, "", 
                    getString(R.string.account_setting_progress));
            repository.refreshUserInfo(this);
            repository.registerObserver(this, DataRepository.USER_MODE);
        }
        else{
            initUi(repository.getUserValue());
        }
    }
    
    protected void setEnableUi(boolean flag){
        Spinner countrySpinner = (Spinner)findViewById(R.id.account_setting_spinner_country);
        countrySpinner.setEnabled(flag);
        Spinner timezoneSpinner = (Spinner)findViewById(R.id.account_setting_spinner_timezone);
        timezoneSpinner.setEnabled(flag);
        Spinner langAppSpinner = (Spinner)findViewById(R.id.account_setting_spinner_language_app);
        langAppSpinner.setEnabled(flag);
        Spinner currencySpinner = (Spinner)findViewById(R.id.account_setting_spinner_currency);
        currencySpinner.setEnabled(flag);
        EditText nicknameEdit = (EditText)findViewById(R.id.account_setting_nickname_edit);
        nicknameEdit.setEnabled(flag);
        Button completeBtn = (Button)findViewById(R.id.account_setting_complete);
        completeBtn.setEnabled(flag);
        Button checkBtn = (Button)findViewById(R.id.account_setting_check_btn);
        checkBtn.setEnabled(flag);
        
        
    }
    
    protected void initUi(JSONObject obj){
        
        setEnableUi(true);
        
        //Spinner adapter
        Spinner countrySpinner = (Spinner)findViewById(R.id.account_setting_spinner_country);
        Spinner timezoneSpinner = (Spinner)findViewById(R.id.account_setting_spinner_timezone);
        Spinner langAppSpinner = (Spinner)findViewById(R.id.account_setting_spinner_language_app);
        Spinner currencySpinner = (Spinner)findViewById(R.id.account_setting_spinner_currency);
        
        String country = null;
        String currency = null;
        String username = null;
        String timezone = null;
        try {
            JSONObject objResult = obj.getJSONObject("results");
            country = objResult.getString("country");
            currency = objResult.getString("currency");
            username = objResult.getString("username");
            timezone = objResult.getString("timezone");
            Log.i("wisedog", "USER INFO :" + objResult.toString());
            
        } catch (JSONException e) {
            e.printStackTrace();            
        }
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
      //Nickname asynctask
        //Complete button Asynctask
    }

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnUserChangeListener#onUserUpdate(org.json.JSONObject)
     */
    @Override
    public void onUserUpdate(JSONObject obj) {
        if(mProgress != null){
            mProgress.dismiss();
        }
        initUi(obj);
        
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        DataRepository repository = DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.USER_MODE);
        super.onDestroy();
    }
    
    /**
     * Event handler for complete button
     * @param   v   view to press
     * 
     * */
    public void onClickComplete(View v){
        SharedPreferences prefs = this.getSharedPreferences(Define.SHARED_PREFERENCE,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		Spinner countrySpinner = (Spinner)findViewById(R.id.account_setting_spinner_country);
		int idx = countrySpinner.getSelectedItemPosition();
		if(idx <= 0){
			//TODO Error
		}else if(idx > 0){
			editor.putString(Define.KEY_SHARED_COUNTRY_CODE, WhooingCurrency.COUNTRY_CODE[idx]);
			editor.commit();
			Define.COUNTRY_CODE = WhooingCurrency.COUNTRY_CODE[idx];
		}
		
		Spinner timezoneSpinner = (Spinner)findViewById(R.id.account_setting_spinner_timezone);
		idx = timezoneSpinner.getSelectedItemPosition();
		if(idx <= 0){
			//TODO Error
		}else if(idx > 0){
			editor.putString(Define.KEY_SHARED_TIMEZONE, WhooingCurrency.TIMEZONE[idx]);
			editor.commit();
			Define.TIMEZONE = WhooingCurrency.TIMEZONE[idx];
		}
        Spinner langAppSpinner = (Spinner)findViewById(R.id.account_setting_spinner_language_app);
        idx = langAppSpinner.getSelectedItemPosition();
        if(idx <= 0){
			//TODO Error
        }else if(idx > 0){
			editor.putString(Define.KEY_SHARED_LOCALE_LANGUAGE, WhooingCurrency.LOCALE_LANGUAGE_CODE[idx]);
			editor.commit();
			Define.LOCALE_LANGUAGE = WhooingCurrency.LOCALE_LANGUAGE_CODE[idx];
		}
        Spinner currencySpinner = (Spinner)findViewById(R.id.account_setting_spinner_currency);
        idx = currencySpinner.getSelectedItemPosition();
		if(idx <= 0){
			//TODO Error
		}else if(idx > 0){
			editor.putString(Define.KEY_SHARED_CURRENCY_CODE, WhooingCurrency.CURRENCY[idx]);
			editor.commit();
			Define.CURRENCY_CODE = WhooingCurrency.CURRENCY[idx];
		}
		
        setResult(RESULT_OK);
        finish();
    }
    
    @Override
    public void onBackPressed() {
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
    
    
}
