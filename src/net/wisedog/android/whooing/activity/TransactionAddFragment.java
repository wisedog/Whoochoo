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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.activity.AccountSelectFragment.AccountSelectListener;
import net.wisedog.android.whooing.adapter.TransactionAddAdapter;
import net.wisedog.android.whooing.api.Entries;
import net.wisedog.android.whooing.dataset.TransactionItem;
import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;

public class TransactionAddFragment extends SherlockFragment implements AccountSelectListener{
	
	public static final int LEFT_SIDE = 10;
    public static final int RIGHT_SIDE = 11;
	
	private ArrayList<AccountsEntity> mAccountsList = null;
	private AccountsEntity  mLeftAccount = null;
    private AccountsEntity  mRightAccount = null;
    
    private WiTextView    mDateDisplay;
    private ArrayList<TransactionItem> mDataArray = null;
    private ArrayList<TransactionItem> mEntryItemArray = null;
    private TransactionAddAdapter mLastestadapter = null;
    
    private int mYear;
    private int mMonth;
    private int mDay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_transaction, container, false);
		return view;
	}

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
    	//Fill Init values
    	int count = getAllAccountsInfo();
		if(count > 0){
	        mLeftAccount = mAccountsList.get(0);
	        mRightAccount = mAccountsList.get(0);
	    }
		if(initUi() == false)
			return;
		
		if(mDataArray == null){
			AsyncTask<Void, Integer, JSONObject> task = new AsyncTask<Void, Integer, JSONObject>(){

	            @Override
	            protected JSONObject doInBackground(Void... arg0) {
	                Entries entries = new Entries();
	                JSONObject result = entries.getLatest(Define.APP_SECTION, Define.APP_ID, 
	                        Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, 5);
	                return result;
	            }

	            @Override
	            protected void onPostExecute(JSONObject result) {
	                if(Define.DEBUG && result != null){
	                    Log.d("wisedog", "API Call - API_GET_ENTRIES_LATEST : " + result.toString());
	                }
	                try {
	                    showLatestTransaction(result);                    
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	                super.onPostExecute(result);
	            }

	        };
	        
	        task.execute();			
		}
		else{
			refreshLastestList();
		}
		super.onActivityCreated(savedInstanceState);
	}
    
    /**
     * Build date string and set text
     * 
     * */
    private void setDateText(int year, int month, int day){
    	Define.gettingLoginInfo(getSherlockActivity());
        Locale locale = new Locale(Define.LOCALE_LANGUAGE, Define.COUNTRY_CODE);
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale);
        Calendar cal = Calendar.getInstance(locale);
        cal.set(year, month, day);
        df.setCalendar(cal);
        String dateStr = df.format(cal.getTime()).toString();
        if(mDateDisplay == null){
            mDateDisplay = (WiTextView)(getView().findViewById(R.id.add_transaction_text_date));
        }
        
        if(mDateDisplay != null){
        	mDateDisplay.setText(dateStr);
        }
    }

	/**
     * Initialize UI
     * */
    public boolean initUi(){
        if(mAccountsList == null || getView() == null){
            return false;
        }
        
        //setting up Go button
        Button goBtn = (Button)(getView().findViewById(R.id.add_transaction_btn_go));
        goBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickGo(v);
			}
		});
        
        //setting up account selection button
        WiTextView leftAccountBtn = (WiTextView)(getView().findViewById(R.id.add_transaction_text_left_account));
        WiTextView rightAccountBtn = (WiTextView)(getView().findViewById(R.id.add_transaction_text_right_account));
        
        if(leftAccountBtn != null && rightAccountBtn != null){
        	leftAccountBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onClickLRAccount(v);
				}
			});
        	
        	rightAccountBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onClickLRAccount(v);
				}
			});
        }
        
        final Calendar c = Calendar.getInstance();
        
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        mDateDisplay = (WiTextView)(getView().findViewById(R.id.add_transaction_text_date));
        ImageButton dateChangeBtn = (ImageButton)(getView().findViewById(R.id.add_transaction_change_btn));
		dateChangeBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DatePickerDialog dpdFromDate = new DatePickerDialog(
						getSherlockActivity(), new DatePickerDialog.OnDateSetListener() {
							
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {
								setDateText(year, monthOfYear, dayOfMonth);								
							}
						}, mYear, mMonth, mDay);
				dpdFromDate.show();
				}

		});

        //Initialize edittext
        AutoCompleteTextView textView = (AutoCompleteTextView)
                (getView().findViewById(R.id.add_transaction_auto_complete));
        textView.setText("");
        ((EditText)(getView().findViewById(R.id.add_transaction_edit_amount))).setText("");

        Define.gettingLoginInfo(getSherlockActivity());
        setDateText(mYear, mMonth, mDay);
        
        if(mLeftAccount != null && mRightAccount != null){
            
        	leftAccountBtn.setText(mLeftAccount.title + GeneralProcessor.getPlusMinus(mLeftAccount, true));
        	rightAccountBtn.setText(mRightAccount.title + GeneralProcessor.getPlusMinus(mRightAccount, false));
        }
        
        //To support transaction insert suggest
        DataRepository repository = WhooingApplication.getInstance().getRepo(); //DataRepository.getInstance();
        JSONObject obj = repository.getLastestItems();
        JSONArray array = null;
        mEntryItemArray = new ArrayList<TransactionItem>();
        ArrayList<String> entryItemStringArray = new ArrayList<String>();
        if(obj != null){
            try {
                array = obj.getJSONArray("results");
                for(int i = 0; i < array.length(); i++){
                    JSONObject objItem = (JSONObject) array.get(i);
                    TransactionItem item = new TransactionItem("",objItem.getString("item"), objItem.getDouble("money"), 
                            objItem.getString("l_account"), objItem.getString("l_account_id"), 
                            objItem.getString("l_account_id"), objItem.getString("r_account_id"));
                    
                    mEntryItemArray.add(item);
                    entryItemStringArray.add(item.item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        String[] lastestStringItems = entryItemStringArray.toArray(new String[entryItemStringArray.size()]);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSherlockActivity(),
                android.R.layout.select_dialog_item, lastestStringItems);
        textView.setAdapter(adapter);

        
        textView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String str = (String) arg0.getAdapter().getItem(position);
                setEntry(str);
            }
        });
        
        return true;
    }
	
	/**
     * Get AccountsEntity List
     * @return  Size of accounts list
     * */
    public int getAllAccountsInfo(){
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(getSherlockActivity());
        mAccountsList = dbHelper.getAllAccountsInfo(true);
        if(mAccountsList != null){
            return mAccountsList.size();
        }
        
        return 0;
    }
    
    protected void setEntry(String itemName){
        if(itemName == null){
            return;
        }
        for(int i = 0; i < mEntryItemArray.size(); i++){
            TransactionItem item = mEntryItemArray.get(i);
            if(item.item.compareToIgnoreCase(itemName) == 0){
                GeneralProcessor processor = new GeneralProcessor(getSherlockActivity());
                AccountsEntity leftEntity = processor.findAccountById(item.l_account_id);
                AccountsEntity rightEntity = processor.findAccountById(item.r_account_id);
                setLeftAccount(leftEntity);
                setRightAccount(rightEntity);
                break;
            }
        }
    }
    

    /**
     * Click Listener for Left/Right accounts button
     * @param   v       View what be clicked
     * */
    public void onClickLRAccount(View v){
        int mode = 0;
        if(v.getId() == R.id.add_transaction_text_left_account){
            mode = LEFT_SIDE;
            
        }else{
            mode = RIGHT_SIDE;
        }
        
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("accounts_list", mAccountsList);
        bundle.putInt("year", mYear);
        bundle.putInt("month", mMonth);
        bundle.putInt("day", mDay);
        bundle.putInt("mode", mode);
        
        AccountSelectFragment newFragment = AccountSelectFragment.newInstance(bundle);
        newFragment.setListner(this);
        newFragment.show(getFragmentManager(), "dialog");
    }
    
    /**
     * Set left account from selection of AccountsSelection 
     * @param       entity      Chosen item from AccountsSelection Activity
     * */
    public void setLeftAccount(AccountsEntity entity){
        if(entity == null){
            return;
        }
        this.mLeftAccount = entity;
        WiTextView textLeft = (WiTextView)(getView().findViewById(R.id.add_transaction_text_left_account));
        textLeft.setText(mLeftAccount.title + GeneralProcessor.getPlusMinus(mLeftAccount, true));
    }
    
    /**
     * Set right account from selection of AccountsSelection 
     * @param       entity      Chosen item from AccountsSelection Activity
     * */
    public void setRightAccount(AccountsEntity entity){
        if(entity == null){
            return;
        }
        this.mRightAccount = entity;
        WiTextView textRight = (WiTextView)(getView().findViewById(R.id.add_transaction_text_right_account));
        textRight.setText(mRightAccount.title + GeneralProcessor.getPlusMinus(mRightAccount, false));
    }
    
    /**
     * Show lastest Transaction 
     * @param   obj     JSON formatted data from calling API_GET_ENTRIES_LATEST
     * */
    public void showLatestTransaction(JSONObject obj) throws JSONException{
        mDataArray = new ArrayList<TransactionItem>();
        JSONArray array = obj.getJSONArray("results");
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
            mDataArray.add(item);
        }
        refreshLastestList();
    }
    
    public void refreshLastestList(){
    	if(mDataArray != null && getView() != null){
    		ListView lastestTransactionList = (ListView)(getView().findViewById(R.id.list_lastest_transaction));
    		mLastestadapter = new TransactionAddAdapter(getSherlockActivity(), mDataArray);
    		lastestTransactionList.setAdapter(mLastestadapter);
    	}
    }
    
    public void onClickGo(View v){
        if(checkValidation() == false){
            return;
        }
        Button goBtn = (Button)(getView().findViewById(R.id.add_transaction_btn_go));
        EditText amountEdit = (EditText)(getView().findViewById(R.id.add_transaction_edit_amount));
        String amount = amountEdit.getText().toString();
        double amountDouble = Double.valueOf(amount);
        
        final Bundle bundle = new Bundle();
        int formattedDate = mYear * 10000 + mMonth * 100 + mDay;
        bundle.putInt("entry_date", formattedDate);
        bundle.putParcelable("l_account", mLeftAccount);
        bundle.putParcelable("r_account", mRightAccount);
        String str = ((EditText)getView().findViewById(R.id.add_transaction_auto_complete)).getText().toString();
        bundle.putString("item", str);
        bundle.putDouble("money", amountDouble);
        bundle.putString("memo", "");
        
        goBtn.setEnabled(false);
        goBtn.setText(getString(R.string.text_loading));
        
        AsyncTask<Void, Integer, JSONObject> mTask = new AsyncTask<Void, Integer, JSONObject>(){

			@Override
			protected JSONObject doInBackground(Void... arg0) {
				Entries entryInsert = new Entries();
	            JSONObject result = entryInsert.insertEntry(Define.APP_SECTION, Define.APP_ID, 
	                    Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, bundle);
				return result;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
			    if(Define.DEBUG && result != null){
                    Log.d("wisedog", "API Call - API_POST_ENTRIES : " + result.toString());
                }
				Button goBtn = (Button)(getView().findViewById(R.id.add_transaction_btn_go));
            	goBtn.setEnabled(true);
            	goBtn.setText(getString(R.string.add_transaction_add));
            	try {
                    JSONArray objResult = result.getJSONArray("results");
                    if(mDataArray != null){
                        for(int i = 0; i < objResult.length() ; i++){
                            TransactionItem item = new TransactionItem((JSONObject) objResult.get(i));
                            mDataArray.add(i, item);
                        }
                        if(mLastestadapter != null){
                            mLastestadapter.setData(mDataArray);
                            mLastestadapter.notifyDataSetChanged();
                        }
                    }
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            	Toast.makeText(getSherlockActivity(), getString(
            			R.string.add_transaction_add_complete), Toast.LENGTH_SHORT).show();
            	AutoCompleteTextView textView = (AutoCompleteTextView)
                        (getView().findViewById(R.id.add_transaction_auto_complete));
            	textView.setText("");
            	((EditText)getView().findViewById(R.id.add_transaction_edit_amount)).setText("");
            	textView.requestFocus();
            	
            	//Clear cached data - mt, pl, bs ... 
            	DataRepository repository = WhooingApplication.getInstance().getRepo();
            	repository.clearCachedData();
				super.onPostExecute(result);
			}

        };
        //Start task
        mTask.execute();
    }
    

    /**
     * Check field values validation for put entry
     * @return  All is validated, return true, otherwise return false
     * */
    public boolean checkValidation(){
        //Check Item edit
        final AutoCompleteTextView editItem = (AutoCompleteTextView)getView().findViewById(
        		R.id.add_transaction_auto_complete); 
        String itemStr = editItem.getText().toString();
        if(itemStr.equals("")){
            Toast.makeText(getSherlockActivity(), "Check Item", Toast.LENGTH_SHORT).show();
            editItem.requestFocus();
            return false;
        }
        
        //Check Amount edit
        final EditText editAmount = (EditText)getView().findViewById(R.id.add_transaction_edit_amount);
        String itemAmount = editAmount.getText().toString();
        if(itemAmount.equals("")){
            Toast.makeText(getSherlockActivity(), getString(R.string.add_transaction_msg_check_amount), 
            		Toast.LENGTH_LONG).show();
            editAmount.requestFocus();
            
            return false;
        }
        
        if(mLeftAccount == null || mRightAccount == null){
            Toast.makeText(getSherlockActivity(), getString(R.string.add_transaction_msg_check_lr), 
            		Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if(mLeftAccount.account_id.equals(mRightAccount.account_id)){
            Toast.makeText(getSherlockActivity(), getString(R.string.add_transaction_msg_same), 
            		Toast.LENGTH_LONG).show();
            return false;
        }
        
        return true;
    }

	@Override
	public void onFinishSelect(int requestCode, AccountsEntity entity) {
		if(requestCode == LEFT_SIDE){
            setLeftAccount(entity);
        }else if(requestCode == RIGHT_SIDE){
            setRightAccount(entity);
        }
	}
}
