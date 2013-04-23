package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.dialog.AccountSettingCardDialog;
import net.wisedog.android.whooing.dialog.DatePickerFragment;
import net.wisedog.android.whooing.dialog.AccountSettingCardDialog.AccountCardSettingListener;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AccountsModify extends SherlockFragmentActivity implements OnItemSelectedListener, AccountCardSettingListener, DatePickerDialog.OnDateSetListener {
    private String useDate = "p25";
    private String accountId = null;
    private int payDate = 25;    
    private int selectDay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_modify);
        Intent intent = getIntent();
        String accountType = intent.getStringExtra("account_type");
        AccountsEntity entity = intent.getParcelableExtra("account_entity");
        
        
        if(accountType != null){
            setupUi(accountType);
        }else {
            setupUi(entity.accountType);
        }
        
        fillUi(entity);
    }
    
    private void fillUi(AccountsEntity entity) {
        WiTextView dateText = (WiTextView) findViewById(R.id.account_modify_text_open_date);
        if(entity ==null){  //Newly added
            String today = WhooingCalendar.getTodayLocale();            
            dateText.setText(today);
            selectDay = WhooingCalendar.getTodayYYYYMMDDint();
        }
        else{   //Modify
            EditText editAccount = (EditText)findViewById(R.id.account_modify_account_edittext);
            EditText editMemo = (EditText)findViewById(R.id.account_modify_edittext_memo);
            editAccount.setText(entity.title);
            editMemo.setText(entity.memo);
            if(entity.accountType.compareTo("liabilities") == 0){
                showCardUi(entity.category, entity);
            }
            String today = WhooingCalendar.getLocaleDateInt(entity.open_date);
            dateText.setText(today);
            selectDay = entity.open_date;
        }
    }
    
    /**
     * Show Card UI depends on category, new added
     * @param   category    to distinguish creditcard/checkcard
     * @param   entity        if entity is null, it is newly added, or it is modifyed item
     * */
    private void showCardUi(String category, AccountsEntity entity){
        if(category.compareTo("creditcard") == 0){
            findViewById(R.id.account_modify_card_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.account_modify_btn_card).setVisibility(View.VISIBLE);
            findViewById(R.id.account_modify_btn_checkcard).setVisibility(View.GONE);
            WiTextView textView = (WiTextView)findViewById(R.id.acount_modify_text_card);

            if(entity == null){
                String msg = "사용기간 : 전월 25 ~ 한 달, 결제일 : 25";
                textView.setText(msg);
            }else{
                String msg = "사용기간 : 전월 " + entity.opt_use_date + "~ 한 달, 결제일 : " + entity.opt_pay_date;
                textView.setText(msg);
                Spinner spinner = (Spinner)findViewById(R.id.account_modify_spinner_kind);
                if(spinner != null){
                    spinner.setSelection(1);    //Creditcard
                }                
            }            
        }
        else if(category.compareTo("checkcard") == 0){
            findViewById(R.id.account_modify_card_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.account_modify_btn_checkcard).setVisibility(View.VISIBLE);
            findViewById(R.id.account_modify_btn_card).setVisibility(View.GONE);
            GeneralProcessor processor = new GeneralProcessor(this);
            if(entity == null){
                WiTextView textView = (WiTextView)findViewById(R.id.acount_modify_text_card);
                textView.setText("대금결제 항목:지정해주세요");
                
            }else{
                AccountsEntity findEntity = processor.findAccountById(entity.account_id);
                if(findEntity != null){
                    WiTextView textView = (WiTextView)findViewById(R.id.acount_modify_text_card);
                    textView.setText("대금결제 항목: " + findEntity.title);
                }                
            }
        }
    }

    private void setupUi(String accountType) {
        ArrayList<String> list = new ArrayList<String>();
        
        // Ordering are below
        // 일반, 거래처관리항목, 유동비용, 고정비용, 신용카드, 직불(체크)카드, 유동수익, 고정수익
        Resources res = getResources();
        String[] accountCategoryArray = res.getStringArray(R.array.account_category_array);
        if(accountType.compareTo("assets") == 0){
            list.add(accountCategoryArray[0]);
            list.add(accountCategoryArray[1]);
        }
        else if(accountType.compareTo("expenses") == 0){
            list.add(accountCategoryArray[2]);
            list.add(accountCategoryArray[3]);            
        }
        else if(accountType.compareTo("capital") == 0){
            list.add(accountCategoryArray[0]);            
        }
        else if(accountType.compareTo("liabilities") == 0){
            list.add(accountCategoryArray[0]);
            list.add(accountCategoryArray[4]);
            list.add(accountCategoryArray[5]);
            list.add(accountCategoryArray[1]);            
        }
        else if(accountType.compareTo("income") == 0){
            list.add(accountCategoryArray[6]);
            list.add(accountCategoryArray[7]);            
        }
        String[] kind = list.toArray(new String[list.size()]);
        Spinner spinner = (Spinner)findViewById(R.id.account_modify_spinner_kind);
        
        ArrayAdapter<String> kindAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, kind) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }
        };
        spinner.setAdapter(kindAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);
    }
    
    public void onClickBtnCheckCard(View v){
        
        /*DialogFragment newFragment = AccountSettingCardDialog.newInstance(list);
        newFragment.show(getSupportFragmentManager(), "dialog");   */ 
    }
    
    public void onClickBtnCard(View v){
        GeneralProcessor general = new GeneralProcessor(this);
        ArrayList<AccountsEntity> list = general.getAllAccount();
        DialogFragment newFragment = AccountSettingCardDialog.newInstance(list);
        newFragment.show(getSupportFragmentManager(), "dialog");    
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        String selected = parent.getItemAtPosition(pos).toString();
        Intent intent = getIntent();
        AccountsEntity entity = intent.getParcelableExtra("account_entity");
        if(selected != null){
            Resources res = getResources();
            
            // Ordering are below
            // 일반, 거래처관리항목, 유동비용, 고정비용, 신용카드, 직불(체크)카드, 유동수익, 고정수익    
            String[] accountCategoryArray = res.getStringArray(R.array.account_category_array);
            if(selected.compareTo(accountCategoryArray[4]) == 0){
                showCardUi("creditcard", entity);
            }
            else if(selected.compareTo(accountCategoryArray[5]) == 0){
                showCardUi("checkcard", entity);
            }
        }       
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // Do nothing        
    }

    @Override
    public void onFinishingSetting(String useDate, int payDate, String accountId) {
        this.useDate = useDate;
        this.payDate = payDate;
        this.accountId = accountId;
        Toast.makeText(this, "useDate: " + useDate + " payDate :" + payDate + "account id : " + accountId, Toast.LENGTH_SHORT).show();

    }
    
    public void onCancel(View v){
        setResult(RESULT_CANCELED);
        this.finish();
    }
    
    public void onConfirm(View v){
        Intent intent = getIntent();
        AccountsEntity beforeEntity = intent.getParcelableExtra("account_entity");
        EditText text = (EditText)findViewById(R.id.account_modify_account_edittext);
        String name = text.getText().toString();
        EditText memoText = (EditText)findViewById(R.id.account_modify_edittext_memo);
        String memo = memoText.getText().toString();
        if(beforeEntity == null){   //newly added
            AccountsEntity entity = new AccountsEntity();
            entity.title = name;
            entity.memo = memo;
            entity.type = "account";
            entity.accountType = intent.getStringExtra("account_type");
            entity.category = getCurrentCategoryString();
            entity.open_date = selectDay;
        }else{  //modify
            
        }
        
        //TODO RPC-Call
        setResult(RESULT_OK);
        this.finish();
    }
    
    public void onClickChangeOpenDate(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    
    private String getCurrentCategoryString(){
        Spinner spinner = (Spinner)findViewById(R.id.account_modify_spinner_kind);
        
        String category = (String) spinner.getSelectedItem();
        if(category == null){
            return null;
        }
        Resources res = getResources();
        
        // Ordering are below
        // 일반, 거래처관리항목, 유동비용, 고정비용, 신용카드, 직불(체크)카드, 유동수익, 고정수익    
        String[] accountCategoryArray = res.getStringArray(R.array.account_category_array);
        String cate;
        if(category.compareTo(accountCategoryArray[0]) == 0){
            cate = "normal";
        }
        else if(category.compareTo(accountCategoryArray[1]) == 0){
            cate = "client";
        }
        else if(category.compareTo(accountCategoryArray[2]) == 0){
            cate = "floating";
        }
        else if(category.compareTo(accountCategoryArray[3]) == 0){
            cate = "steady";
        }
        else if(category.compareTo(accountCategoryArray[4]) == 0){
            cate = "creditcard";
        }
        else if(category.compareTo(accountCategoryArray[5]) == 0){
            cate = "checkcard";
        }
        else if(category.compareTo(accountCategoryArray[6]) == 0){
            cate = "floating";
        }
        else if(category.compareTo(accountCategoryArray[7]) == 0){
            cate = "steady";
        }
        else{
            cate = null;
        }
        return cate;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        WiTextView dateText = (WiTextView) findViewById(R.id.account_modify_text_open_date);
        String today = WhooingCalendar.getLocaleDateString(year, month, day);
        dateText.setText(today);
        selectDay = year * 10000 + month * 100 + day;
    }
}
