package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.dialog.AccountSettingCardDialog;
import net.wisedog.android.whooing.dialog.AccountSettingCardDialog.AccountCardSettingListener;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.widget.WiTextView;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class AccountsModify extends SherlockFragmentActivity implements OnItemSelectedListener, AccountCardSettingListener {

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
        
        if(entity != null){
            fillUi(entity);
        }
    }
    
    private void fillUi(AccountsEntity entity) {
        EditText editAccount = (EditText)findViewById(R.id.account_modify_account_edittext);
        EditText editMemo = (EditText)findViewById(R.id.account_modify_edittext_memo);
        editAccount.setText(entity.title);
        editMemo.setText(entity.memo);
        
        if(entity.accountType.compareTo("liabilities") == 0){
            showCardUi(entity.category, entity);
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
        if(accountType.compareTo("assets") == 0){
            list.add("일반");
            list.add("거래처관리항목");
        }
        else if(accountType.compareTo("expenses") == 0){
            list.add("유동비용");
            list.add("고정비용");            
        }
        else if(accountType.compareTo("capital") == 0){
            list.add("일반");            
        }
        else if(accountType.compareTo("liabilities") == 0){
            list.add("일반");
            list.add("신용카드");
            list.add("직불(체크)카드");
            list.add("거래처관리항목");            
        }
        else if(accountType.compareTo("income") == 0){
            list.add("유동수익");
            list.add("고정수익");            
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
            if(selected.compareTo("신용카드") == 0){
                showCardUi("creditcard", entity);
            }
            else if(selected.compareTo("직불(체크)카드") == 0){
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
        Toast.makeText(this, "useDate: " + useDate + " payDate :" + payDate + "account id : " + accountId, Toast.LENGTH_SHORT).show();
        
    }
}
