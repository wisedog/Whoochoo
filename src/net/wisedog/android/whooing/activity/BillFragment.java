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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.adapter.TransactionAddAdapter;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.ui.BillMonthlyEntity;
import net.wisedog.android.whooing.utils.WhooingAlert;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author wisedog(me@wisedog.net)
 *
 */
public class BillFragment extends Fragment implements
DatePickerDialog.OnDateSetListener{
	int mFromDate;
	int mToDate;
	int mCalendarSelectionResId;
	
	public static BillFragment newInstance(Bundle b){
		BillFragment f = new BillFragment();
		f.setArguments(b);
		return f;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bill_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Bundle bundle = new Bundle();
        String endDateStr = WhooingCalendar.getTodayYYYYMMDD();
        String startDateStr = WhooingCalendar.getPreMonthYYYYMMDD(1);
        mToDate = Integer.valueOf(endDateStr);
        mFromDate = Integer.valueOf(startDateStr);
        bundle.putString("end_date", WhooingCalendar.getNextMonthYYYYMM(1));
        bundle.putString("start_date", WhooingCalendar.getTodayYYYYMM());
        
        if(getView() != null){
        	ProgressBar progress = (ProgressBar) getView().findViewById(R.id.bill_progress);
        	if(progress != null){
        		progress.setVisibility(View.VISIBLE);
        	}
        }
        
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_BILL, bundle);
        thread.start();

		super.onActivityCreated(savedInstanceState);
	}

    @SuppressLint("HandlerLeak")
	protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BILL){
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                    	int returnCode = obj.getInt("code");
                    	if(returnCode == Define.RESULT_INSUFFIENT_API 
                    			&& Define.SHOW_NO_API_INFORM == false){
                    		Define.SHOW_NO_API_INFORM = true;
                    		WhooingAlert.showNotEnoughApi(getActivity());
                    		return;
                    	}
                        showBill(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.handleMessage(msg);
        }
        
    };

    private void showBill(JSONObject obj) throws JSONException{
    	LinearLayout baseLayout = (LinearLayout)getView().findViewById(R.id.bill_main_layout);
    	if(baseLayout == null){
    		return;
    	}
        JSONObject result = obj.getJSONObject("results");
        JSONArray array = result.getJSONArray("rows");
        
        int count = array.length();
        for(int i = 0; i < count; i++){
        	JSONObject objRowItem = array.getJSONObject(i);
        	BillMonthlyEntity monthly = new BillMonthlyEntity(getActivity());
        	monthly.setupMonthlyCard(objRowItem);
        	baseLayout.addView(monthly);
        }
        baseLayout.requestLayout();
        
        if(getView() != null){
        	ProgressBar progress = (ProgressBar) getView().findViewById(R.id.bill_progress);
        	if(progress != null){
        		progress.setVisibility(View.GONE);
        	}
        }
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
    	DatePickerDialog datePickerDlg = new DatePickerDialog(
				getActivity(), new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						Toast.makeText(getActivity(), 
								"year : " + year + " month : " + monthOfYear + " day : " + dayOfMonth, 
								Toast.LENGTH_LONG).show();
						
					}
				}, 2013, 12, 4);
    	datePickerDlg.show();
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
 	   TextView textDate = (TextView)getView().findViewById(mCalendarSelectionResId);
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
        ListView lastestTransactionList = (ListView)getView().findViewById(R.id.transaction_entries_listview);
        ((TransactionAddAdapter)lastestTransactionList.getAdapter()).clearAdapter();
	}
	/*
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
            		(BillFragment)getActivity(), year, month, day);
        }

	}*/
}
