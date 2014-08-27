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

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.dialog.AccountDeleteConfirmDialog;
import net.wisedog.android.whooing.dialog.AccountDeleteConfirmDialog.AccountDeleteListener;

import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.ui.AccountRowItem;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;


/**
 * Setting user accounts(for banking account - like budget, expenses)
 * @author	Wisedog(me@wisedog.net)
 * */
public class AccountsSetting extends Activity implements AccountDeleteListener{
    public static final int REQUEST_CODE_ADD = 0;
    public static final int REQUEST_CODE_MODIFY = 1;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.account_setting);		
		inflateAccountLayout();
        onSetupUi();
	}
	
	protected void inflateAccountLayout(){
	    GeneralProcessor general = new GeneralProcessor(this);
        ArrayList<AccountsEntity> list = general.getAllAccount(true);
        
        
        for(int i = 0; i < list.size(); i++)    {
            final AccountsEntity entity = list.get(i);
            if(entity.close_date <= WhooingCalendar.getTodayYYYYMMDDint()){
                continue;
            }
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(1.0f);
            
            TableLayout tl = null;
            if(entity.accountType.equals("assets")){
                tl = (TableLayout)findViewById(R.id.account_setting_table_asset);
            }
            else if(entity.accountType.equals("expenses")){
                tl = (TableLayout)findViewById(R.id.account_setting_table_expenses);
            }
            else if(entity.accountType.equals("capital")){
                tl = (TableLayout)findViewById(R.id.account_setting_table_capital);
            }
            else if(entity.accountType.equals("income")){
                tl = (TableLayout)findViewById(R.id.account_setting_table_income);
            }
            else if(entity.accountType.equals("liabilities")){
                tl = (TableLayout)findViewById(R.id.account_setting_table_liabilities);
            }
            AccountRowItem layout = new AccountRowItem(this);
            layout.setupListItem(entity); 
            layout.findViewById(R.id.account_setting_item_icon_modify).setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AccountsSetting.this, AccountsModify.class);
                    intent.putExtra("account_entity", entity);
                    startActivityForResult(intent, REQUEST_CODE_MODIFY);                    
                }
            });
            layout.findViewById(R.id.account_setting_item_icon_del).setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    AccountDeleteConfirmDialog newFragment = AccountDeleteConfirmDialog.newInstance(entity.account_id, entity.accountType);
                    newFragment.show(getFragmentManager(), "account_del_dialog");
                }
            });
            
            tr.addView(layout, new LayoutParams(0 , LayoutParams.WRAP_CONTENT, 1.0f));
            if(tl != null){
                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }            
        }
	}
	
	protected void onSetupUi(){
	    int[] btnIds = new int[]{R.id.account_setting_asset_btn, R.id.account_setting_capital_btn, R.id.account_setting_expenses_btn, 
	            R.id.account_setting_income_btn, R.id.account_setting_liabilities_btn};
	    String[] accountsType = new String[]{"assets", "capital", "expenses", "income", "liabilities"};
	    for(int i = 0; i < 5; i++){
	        Button btn = (Button)findViewById(btnIds[i]);
	        final String type = accountsType[i];
	        btn.setOnClickListener(new OnClickListener() {
                
                public void onClick(View v) {
                    Intent intent = new Intent(AccountsSetting.this, AccountsModify.class);
                    intent.putExtra("account_type", type);
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                }
            });
	    }
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this, getString(R.string.account_setting_msg_fail_add_modify), Toast.LENGTH_LONG).show();
                return;
            }
            
            AccountsEntity entity = data.getParcelableExtra("account_entity");
            GeneralProcessor generic = new GeneralProcessor(this);
            if(requestCode == REQUEST_CODE_ADD){
                if(generic.addAccount(entity)){
                    clearLayout();
                    inflateAccountLayout();
                }
                
            }else if(requestCode == REQUEST_CODE_MODIFY){
                if(generic.modifyAccount(entity)){
                    clearLayout();
                    inflateAccountLayout();
                }
            }else{
                ;//Show error
            }
            
        }
        else{
            ; // Nothing happen!
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearLayout() {
        int[] tableIds = new int[]{R.id.account_setting_table_asset, R.id.account_setting_table_expenses, R.id.account_setting_table_capital,
                R.id.account_setting_table_income, R.id.account_setting_table_liabilities};
       for(int i = 0; i < 5; i++)
       {
           TableLayout tl = (TableLayout)findViewById(tableIds[i]);
           if(tl != null){
               tl.removeAllViews();
           }
       }
    }

    @Override
    public void onFinishingDeleting() {
        clearLayout();
        inflateAccountLayout();        
    }
}
