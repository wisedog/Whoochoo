/**
 * 
 */
package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.OnUserChangeListener;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Account setting class. User can set country, currency, language, nickname
 * @author Wisedog(me@wisedog.net)
 */
public class AccountSetting extends Activity implements OnUserChangeListener{

    private ProgressDialog mProgress;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
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
    
    protected void initUi(JSONObject obj){
        
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
                android.R.layout.select_dialog_item, WhooingCurrency.COUNTRY){

                    /* (non-Javadoc)
                     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
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
                android.R.layout.select_dialog_item, WhooingCurrency.TIMEZONE){

                    /* (non-Javadoc)
                     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
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
                android.R.layout.select_dialog_item, WhooingCurrency.LANGUAGE){

                    /* (non-Javadoc)
                     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
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
                android.R.layout.select_dialog_item, WhooingCurrency.CURRENCY){

                    /* (non-Javadoc)
                     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
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
    
    
}
