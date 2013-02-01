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
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

/**
 * @author wisedog(me@wisedog.net)
 *  @TODO   Account를 Spinner로 처리하도록 하기
 */
public class TransactionEntries extends SherlockFragmentActivity implements
DatePickerDialog.OnDateSetListener{
	int mFromDate;
	int mToDate;
	int mCalendarSelectionResId;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_entries);
        
        Intent intent = getIntent();
        this.setTitle(intent.getStringExtra("title"));
        
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
        
        startDate.setText(WhooingCalendar.getPreMonthYYYYMMDD(1)); 
        endDate.setText(WhooingCalendar.getTodayYYYYMMDD());
        
        setSupportProgress(Window.PROGRESS_END);
        setSupportProgressBarIndeterminateVisibility(true);
        
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_ENTRIES, bundle);
        thread.start();
    }
    
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_ENTRIES){
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        //testPrint(obj.getJSONArray("results"));
                        showTransaction(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(msg.arg1 == Define.API_GET_ENTRIES_INSERT){
                    //TODO 맞는 값이 왔다면 상단 ProgressBar 끄고 
                    //TODO 아래 ListView에 아이템 넣기
                }
            }
            super.handleMessage(msg);
        }
        
    };

    private void showTransaction(JSONObject obj) throws JSONException{
    	ArrayList<TransactionItem> dataArray = new ArrayList<TransactionItem>();
        Log.i("wisedog", "ShowLastestTransaction - " + obj.toString());
        JSONObject result = obj.getJSONObject("results");
        JSONArray array = result.getJSONArray("rows");
        
        int count = array.length();
        for(int i = 0; i < count; i++){
            JSONObject entity = array.getJSONObject(i);
            TransactionItem item = new TransactionItem(
                    entity.getString("entry_date"),
                    entity.getString("item"),
                    String.valueOf(entity.getInt("money")),
                    entity.getString("l_account_id"),
                    entity.getString("r_account_id")
                    );
            item.Entry_ID = entity.getInt("entry_id");
            dataArray.add(item);
        }
        ListView lastestTransactionList = (ListView)findViewById(R.id.transaction_entries_listview);
        TransactionAddAdapter adapter = new TransactionAddAdapter(this, dataArray);
        lastestTransactionList.setAdapter(adapter);
        setSupportProgressBarIndeterminateVisibility(false);
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
        
        String dateString = "" + String.valueOf(year);
 	   if(month >= 10){
 		   dateString = dateString + String.valueOf(month);
 	   }else{
 		   dateString = dateString + "0" + String.valueOf(month);
 	   }
 	   if(day >= 10){
 		   dateString = dateString + String.valueOf(day);
 	   }else{
 		   dateString = dateString + "0" + String.valueOf(day);
 	   }
 	   TextView textDate = (TextView)findViewById(mCalendarSelectionResId);
 	   if(textDate != null){
 		   textDate.setText(dateString);
 	   }
 	   
 	   if(mCalendarSelectionResId == R.id.transaction_entries_from_date){
 		   this.mFromDate = year * 10000 + month * 100 + day;
 	   }else if(mCalendarSelectionResId == R.id.transaction_entries_to_date){
 		  this.mToDate = year * 10000 + month * 100 + day;
 	   }
		
	}
	
	/**
	 * Search button onClick event handler
	 * @param	v	View of search button
	 * @see transaction_entries.xml
	 * */
	public void onSearchClick(View v){
		Bundle bundle = new Bundle();
        bundle.putString("end_date", String.valueOf(mToDate));
        bundle.putString("start_date", String.valueOf(mFromDate));
        bundle.putInt("limit", 20);
        
		ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_ENTRIES, bundle);
        thread.start();
        ListView lastestTransactionList = (ListView)findViewById(R.id.transaction_entries_listview);
        ((TransactionAddAdapter)lastestTransactionList.getAdapter()).clearAdapter();
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
