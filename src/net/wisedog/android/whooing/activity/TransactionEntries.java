/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.adapter.TransactionAddAdapter;
import net.wisedog.android.whooing.dataset.TransactionItem;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * @author wisedog(me@wisedog.net)
 *  @TODO   Account를 Spinner로 처리하도록 하기
 */
public class TransactionEntries extends SherlockFragmentActivity implements
        DatePickerDialog.OnDateSetListener {
	protected int mFromDate;
    protected int mToDate;
    protected int mCalendarSelectionResId;
    protected ArrayList<AccountsEntity> mAccountsArray;
	private AdView adView;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_entries);
        
        setTitle(getIntent().getStringExtra("title"));
        
        Bundle bundle = new Bundle();
        String endDateStr = WhooingCalendar.getTodayYYYYMMDD();
        String startDateStr = WhooingCalendar.getPreMonthYYYYMMDD(1);
        mToDate = Integer.valueOf(endDateStr);
        mFromDate = Integer.valueOf(startDateStr);
        bundle.putString("end_date", WhooingCalendar.getTodayYYYYMMDD());
        bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMMDD(1));
        bundle.putInt("limit", 20);
        
        TextView startDate = (TextView)findViewById(R.id.transaction_entries_from_date);
        TextView endDate = (TextView)findViewById(R.id.transaction_entries_to_date);
        
        startDate.setText(WhooingCalendar.getPreMonthLocale(this, 1)); 
        endDate.setText(WhooingCalendar.getTodayLocale(this));
        
        GeneralProcessor processor = new GeneralProcessor(this);
        mAccountsArray = processor.getAllAccount(false);
        ArrayList<String> stringArray = new ArrayList<String>();
        stringArray.add("");
        for(int i = 0; i < mAccountsArray.size(); i++){
            AccountsEntity entity = mAccountsArray.get(i);
            stringArray.add(entity.title);
        }
        String[] notArrayListStrArray = stringArray.toArray(new String[stringArray.size()]);

        Spinner accountSpinner = (Spinner) findViewById(R.id.transaction_entries_spinner_account);
        ArrayAdapter<String> accountAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, notArrayListStrArray) {

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
        accountSpinner.setAdapter(accountAdapter);
        
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_ENTRIES, bundle);
        thread.start();
        
        // adView 만들기
	    adView = new AdView(this, AdSize.SMART_BANNER, "a15147cd53daa26");
	    LinearLayout layout = (LinearLayout)findViewById(R.id.transaction_entries_ads);

	    // 찾은 LinearLayout에 adView를 추가
	    layout.addView(adView);

	    // 기본 요청을 시작하여 광고와 함께 요청을 로드
	    AdRequest adRequest = new AdRequest();
	    if(Define.DEBUG){
	    	adRequest.addTestDevice("65E3B8CB214707370B559D98093D74AA");
	    }
	    adView.loadAd(adRequest);
    }
    
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_ENTRIES){
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        ProgressBar progress = (ProgressBar)findViewById(R.id.transaction_entries_progress);
                        if(progress != null){
                            progress.setVisibility(View.INVISIBLE);
                        }
                        showTransaction(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.handleMessage(msg);
        }
        
    };

    /**
     * Show transaction from give data
     * @param	obj		JSON formatted data
     * @exception	JSONException
     * */
    private void showTransaction(JSONObject obj) throws JSONException{
    	ArrayList<TransactionItem> dataArray = new ArrayList<TransactionItem>();
        JSONObject result = obj.getJSONObject("results");
        JSONArray array = result.getJSONArray("rows");
        
        int count = array.length();
        for(int i = 0; i < count; i++){
            JSONObject entity = array.getJSONObject(i);
            TransactionItem item = new TransactionItem(
                    entity.getString("entry_date"),
                    entity.getString("item"),
                    entity.getDouble("money"),
                    entity.getString("l_account"),
                    entity.getString("l_account_id"),
                    entity.getString("r_account"),
                    entity.getString("r_account_id")
                    );
            item.Entry_ID = entity.getInt("entry_id");
            dataArray.add(item);
        }
        ListView lastestTransactionList = (ListView)findViewById(R.id.transaction_entries_listview);
        TransactionAddAdapter adapter = new TransactionAddAdapter(this, dataArray);
        lastestTransactionList.setAdapter(adapter);
    }
    
    /**
     * Event Handler for 
     * */
    public void onCalendarClick(View v){
    	if(v.getId() == R.id.transaction_entries_imgbtn_calendar_from){
    		mCalendarSelectionResId = R.id.transaction_entries_from_date;
    	}
    	else if(v.getId() == R.id.transaction_entries_imgbtn_calendar_to){
    		mCalendarSelectionResId = R.id.transaction_entries_to_date;
    	}
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
	    TextView textDate = (TextView)findViewById(mCalendarSelectionResId);
	       if(textDate != null){
	           textDate.setText(WhooingCalendar.getLocaleDateString(year, month, day));
	       }
 	   
 	   if(mCalendarSelectionResId == R.id.transaction_entries_from_date){
 		   this.mFromDate = year * 10000 + month * 100 + day;
 	   }else if(mCalendarSelectionResId == R.id.transaction_entries_to_date){
 		  this.mToDate = year * 10000 + month * 100 + day;
 	   }
		
	}
	
    /**
     * Search button onClick event handler
     * 
     * @param v
     *            View of search button
     * @see transaction_entries.xml
     * */
    public void onSearchClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putString("end_date", String.valueOf(mToDate));
        bundle.putString("start_date", String.valueOf(mFromDate));
        bundle.putInt("limit", 20);
        
        //setting searching item value
        String itemText = ((EditText)findViewById(R.id.transaction_entries_edit_item)).getText().toString();
        if("".equals(itemText) || itemText == null){
            bundle.putString("item", null);
        }
        else{
            bundle.putString("item", itemText);
        }
        
        //setting searching account
        Spinner accountSpinner = (Spinner) findViewById(R.id.transaction_entries_spinner_account);
        if(accountSpinner != null){
            int idx = accountSpinner.getSelectedItemPosition();
            if(idx == 0){
            	bundle.putString("account", null);
            	bundle.putString("account_id", null);
            }else{
            	AccountsEntity entity = mAccountsArray.get(idx - 1);
            	bundle.putString("account", entity.accountType);
            	bundle.putString("account_id", entity.account_id);
            }
        }
        

        ProgressBar progress = (ProgressBar)findViewById(R.id.transaction_entries_progress);
        if(progress != null){
            progress.setVisibility(View.VISIBLE);
        }
        
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_ENTRIES, bundle);
        thread.start();
        ListView lastestTransactionList = (ListView) findViewById(R.id.transaction_entries_listview);
        ((TransactionAddAdapter) lastestTransactionList.getAdapter()).clearAdapter();
    }
	
	static public class DatePickerFragment extends DialogFragment  {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), 
            		(TransactionEntries)getActivity(), year, month, day);
        }
	}
}
