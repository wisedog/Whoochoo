/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.network.ThreadRestAPI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class TransactionAdd extends SherlockActivity {
    
    protected static final int DATE_DIALOG_ID = 0;
    protected static final int REQUEST_CODE_LEFT = 10;
    protected static final int REQUEST_CODE_RIGHT = 11;
    
    private TextView    mDateDisplay;
    private ArrayList<AccountsEntity> mAccountsList = null;
    private AccountsEntity  mLeftAccount = null;
    private AccountsEntity  mRightAccount = null;
    
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
        
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, this, Define.API_GET_ENTRIES_LATEST);
        thread.start();
    }
    
    /**
     * Initialize UI
     * */
    public boolean initUi(){
        if(mAccountsList == null){
            return false;
        }
        mDateDisplay = (TextView)findViewById(R.id.add_transaction_text_date);
        Button dateChangeBtn = (Button)findViewById(R.id.add_transaction_change_btn);
        dateChangeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        
        //Initialize edittext
        ((EditText)findViewById(R.id.add_transaction_edit_item)).setText("");
        ((EditText)findViewById(R.id.add_transaction_edit_amount)).setText("");

        String date = DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
        mDateDisplay.setText(date);
        
        final Calendar c = Calendar.getInstance();
        
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        if(mLeftAccount != null && mRightAccount != null){
            TextView textLeft = (TextView)findViewById(R.id.add_transaction_text_left_account);
            TextView textRight = (TextView)findViewById(R.id.add_transaction_text_right_account);
            textLeft.setText(mLeftAccount.title + GeneralProcessor.getPlusMinus(mLeftAccount, true));
            textRight.setText(mRightAccount.title + GeneralProcessor.getPlusMinus(mRightAccount, false));
        }
        return true;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu1 = menu.addSubMenu("Lists");
        subMenu1.add("History");
        subMenu1.add("Menu2");
        subMenu1.add("Setting");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.menu_lists_button);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Click Listener for Left/Right accounts button
     * @param   v       View what be clicked
     * */
    public void onClickLRAccount(View v){
        Intent intent = new Intent(this, AccountsSelection.class);

        intent.putParcelableArrayListExtra("accounts_list", mAccountsList);
        intent.putExtra("year", mYear);
        intent.putExtra("month", mMonth);
        intent.putExtra("day", mDay);

        int reqCode = REQUEST_CODE_LEFT;
        
        if(v.getId() == R.id.add_transaction_text_left_account){
            intent.putExtra("mode", "left");
            
        }else{
            intent.putExtra("mode", "right");
            reqCode = REQUEST_CODE_RIGHT;
        }
        startActivityForResult(intent, reqCode);
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
        TextView textLeft = (TextView)findViewById(R.id.add_transaction_text_left_account);
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
        TextView textRight = (TextView)findViewById(R.id.add_transaction_text_right_account);
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
    
    protected Handler mHandler = new Handler(){

        /* (non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_ENTRIES_LATEST){
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        testPrint(obj.getJSONArray("results"));
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
    
    public void testPrint(JSONArray array){
        if(array == null){
            return;
        }
        String totalStr = "";
        for(int i = 0; i< array.length(); i++){
            String str = "";
            try {
                JSONObject obj = (JSONObject) array.get(i);
                str = str + obj.getInt("entry_id");
                str = str + " / " + obj.getString("l_account");
                str = str + " / " + obj.getString("r_account");
                str = str + " / " + obj.getString("item");
                str = str + " / " + obj.getInt("money");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            totalStr = totalStr + str + "\n";
        }
        ((TextView)findViewById(R.id.add_transaction_text_test)).setText(totalStr);
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
        
        Bundle bundle = new Bundle();
        int formattedDate = mYear * 10000 + mMonth * 100 + mDay;
        bundle.putInt("entry_date", formattedDate);
        bundle.putParcelable("l_account", mLeftAccount);
        bundle.putParcelable("r_account", mRightAccount);
        bundle.putString("item", ((EditText)findViewById(R.id.add_transaction_edit_item)).getText().toString());
        bundle.putDouble("money", amountDouble);
        bundle.putString("memo", "");
        
        
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, this, Define.API_GET_ENTRIES_INSERT, bundle);
        thread.run();
        //TODO ProgressBar 넣기 
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
        final EditText editItem = (EditText)findViewById(R.id.add_transaction_edit_item); 
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
}
