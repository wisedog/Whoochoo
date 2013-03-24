/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.adapter.TransactionAddAdapter;
import net.wisedog.android.whooing.api.Entries;
import net.wisedog.android.whooing.dataset.TransactionItem;
import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.dialog.AccountChooserDialog;
import net.wisedog.android.whooing.dialog.AccountChooserDialog.AccountChooserListener;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.widget.WiTextView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class TransactionAdd extends SherlockFragmentActivity implements AccountChooserListener{
    
    protected static final int DATE_DIALOG_ID = 0;
    protected static final int REQUEST_CODE_LEFT = 10;
    protected static final int REQUEST_CODE_RIGHT = 11;
    
    private WiTextView    mDateDisplay;
    private ArrayList<AccountsEntity> mAccountsList = null;
    private AccountsEntity  mLeftAccount = null;
    private AccountsEntity  mRightAccount = null;
    private ArrayList<TransactionItem> mDataArray = null;
    private ArrayList<TransactionItem> mEntryItemArray = null;
    private TransactionAddAdapter mLastestadapter = null;
    
    private int mYear;
    private int mMonth;
    private int mDay;
    

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
        
        Intent intent = getIntent();
        if(intent.getBooleanExtra("showEntries", false))
            ;
        this.setTitle(intent.getStringExtra("title"));
        
        if(getAllAccountsInfo() > 0){
            mLeftAccount = mAccountsList.get(0);
            mRightAccount = mAccountsList.get(0);
        }

        if(initUi() == false){
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        
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
                    Log.i("wisedog", "API Call - API_GET_ENTRIES_LATEST : " + result.toString());
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
    
    protected void setEntry(String itemName){
        if(itemName == null){
            return;
        }
        for(int i = 0; i < mEntryItemArray.size(); i++){
            TransactionItem item = mEntryItemArray.get(i);
            if(item.item.compareToIgnoreCase(itemName) == 0){
                GeneralProcessor processor = new GeneralProcessor(this);
                AccountsEntity leftEntity = processor.findAccountById(item.l_account_id);
                AccountsEntity rightEntity = processor.findAccountById(item.r_account_id);
                setLeftAccount(leftEntity);
                setRightAccount(rightEntity);
                break;
            }
        }
    }
    
    /**
     * Initialize UI
     * */
    public boolean initUi(){
        if(mAccountsList == null){
            return false;
        }
        mDateDisplay = (WiTextView)findViewById(R.id.add_transaction_text_date);
        ImageButton dateChangeBtn = (ImageButton)findViewById(R.id.add_transaction_change_btn);
        dateChangeBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        
        //Initialize edittext
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.add_transaction_auto_complete);
        textView.setText("");
        ((EditText)findViewById(R.id.add_transaction_edit_amount)).setText("");

        Locale locale = new Locale(Define.LOCALE_LANGUAGE, Define.COUNTRY_CODE);
        java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale);
        //String date = DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
        String date = df.format(new java.util.Date()).toString();
        mDateDisplay.setText(date);
        
        final Calendar c = Calendar.getInstance();
        
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        if(mLeftAccount != null && mRightAccount != null){
            WiTextView textLeft = (WiTextView)findViewById(R.id.add_transaction_text_left_account);
            WiTextView textRight = (WiTextView)findViewById(R.id.add_transaction_text_right_account);
            textLeft.setText(mLeftAccount.title + GeneralProcessor.getPlusMinus(mLeftAccount, true));
            textRight.setText(mRightAccount.title + GeneralProcessor.getPlusMinus(mRightAccount, false));
        }
        
        //To support transaction insert suggest
        DataRepository repository = DataRepository.getInstance();
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
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu1 = menu.addSubMenu("Lists");
        subMenu1.add("Setting");
        subMenu1.add("About");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.menu_lists_button);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == android.R.id.home) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Click Listener for Left/Right accounts button
     * @param   v       View what be clicked
     * */
    public void onClickLRAccount(View v){
        String mode = "";
        if(v.getId() == R.id.add_transaction_text_left_account){
            mode =  "left";
            
        }else{
            mode = "right";
        }
        DialogFragment newFragment = AccountChooserDialog.newInstance(mAccountsList, mYear, mMonth, mDay, mode);
        newFragment.show(getSupportFragmentManager(), "dialog");
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
        WiTextView textLeft = (WiTextView)findViewById(R.id.add_transaction_text_left_account);
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
        WiTextView textRight = (WiTextView)findViewById(R.id.add_transaction_text_right_account);
        textRight.setText(mRightAccount.title + GeneralProcessor.getPlusMinus(mRightAccount, false));
    }
    
    
    /**
     * Get AccountsEntity List
     * @return  Size of accounts list
     * */
    public int getAllAccountsInfo(){
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(this);
        mAccountsList = dbHelper.getAllAccountsInfo();
        if(mAccountsList != null){
            return mAccountsList.size();
        }
        
        return 0;
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
        ListView lastestTransactionList = (ListView)findViewById(R.id.list_lastest_transaction);
        mLastestadapter = new TransactionAddAdapter(this, mDataArray);
        lastestTransactionList.setAdapter(mLastestadapter);
    }

    private void getAccountsByDate(int year, int i, int dayOfMonth) {
        // TODO DB Open 
        //Convert Date(String) to integer
        // select * from accounts where open_date > date 
        
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateDisplay.setText(new StringBuilder()
            .append(year).append("-")
            .append(monthOfYear+1).append("-")
            .append(dayOfMonth));
            mYear = year;
            mMonth = monthOfYear+1;
            mDay = dayOfMonth;
            getAccountsByDate(year, monthOfYear+1, dayOfMonth);
        }
    };
    
    public void onClickGo(View v){
        if(checkValidation() == false){
            return;
        }
        Button goBtn = (Button)findViewById(R.id.add_transaction_btn_go);
        EditText amountEdit = (EditText)findViewById(R.id.add_transaction_edit_amount);
        String amount = amountEdit.getText().toString();
        double amountDouble = Double.valueOf(amount);
        
        final Bundle bundle = new Bundle();
        int formattedDate = mYear * 10000 + mMonth * 100 + mDay;
        bundle.putInt("entry_date", formattedDate);
        bundle.putParcelable("l_account", mLeftAccount);
        bundle.putParcelable("r_account", mRightAccount);
        bundle.putString("item", ((EditText)findViewById(R.id.add_transaction_auto_complete)).getText().toString());
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
                    Log.i("wisedog", "API Call - API_POST_ENTRIES : " + result.toString());
                }
				Button goBtn = (Button)findViewById(R.id.add_transaction_btn_go);
            	goBtn.setEnabled(true);
            	goBtn.setText("go");
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
            	Toast.makeText(TransactionAdd.this, TransactionAdd.this.getString(
            			R.string.add_transaction_add_complete), Toast.LENGTH_SHORT).show();
            	AutoCompleteTextView textView = (AutoCompleteTextView)
                        findViewById(R.id.add_transaction_auto_complete);
            	textView.setText("");
            	((EditText)findViewById(R.id.add_transaction_edit_amount)).setText("");
            	textView.requestFocus();
            	
            	//Clear cached data - mt, pl, bs ... 
            	DataRepository repository = DataRepository.getInstance();
            	repository.clearCachedData();
				super.onPostExecute(result);
			}

        };
        //Start task
        mTask.execute();
    }
    
    
    
    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(data == null){
                return;
            }
            if(requestCode == REQUEST_CODE_LEFT){
                AccountsEntity entity = data.getParcelableExtra("selection");
                setLeftAccount(entity);
            }else if(requestCode == REQUEST_CODE_RIGHT){
                AccountsEntity entity = data.getParcelableExtra("selection");
                setRightAccount(entity);
            }
            
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Check field values validation for put entry
     * @return  All is validated, return true, otherwise return false
     * */
    public boolean checkValidation(){
        //Check Item edit
        final AutoCompleteTextView editItem = (AutoCompleteTextView)findViewById(R.id.add_transaction_auto_complete); 
        String itemStr = editItem.getText().toString();
        if(itemStr.equals("")){
            Toast.makeText(this, "Check Item", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                public void run() {
                    editItem.requestFocus();
                }
            });
            return false;
        }
        
        //Check Amount edit
        final EditText editAmount = (EditText)findViewById(R.id.add_transaction_edit_amount);
        String itemAmount = editAmount.getText().toString();
        if(itemAmount.equals("")){
            Toast.makeText(this, "Check Amount", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                public void run() {
                    editAmount.requestFocus();
                }
            });
            
            return false;
        }
        
        if(mLeftAccount == null || mRightAccount == null){
            Toast.makeText(this, "Check left/right", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if(mLeftAccount.account_id.equals(mRightAccount.account_id)){
            Toast.makeText(this, "Left and Right are the same", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id)
        {
        case DATE_DIALOG_ID:
            final Calendar c = Calendar.getInstance();
            return new DatePickerDialog(this, mDateSetListener, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }
    
    /**
     * @return      return left-account entity
     * 
     * */
    public AccountsEntity getLeftAccounts(){
        return mLeftAccount;
    }
    
    /**
     * @return      return right-account entity
     * */
    public AccountsEntity getRightAccounts(){
        return mRightAccount;
    }

	public void onFinishingChoosing(AccountsEntity entity, String mode) {
		if(mode.equals("left")){
            setLeftAccount(entity);
        }else if(mode.equals("right")){
            setRightAccount(entity);
        }
		
	}
}
