package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.ui.AccountRowItem;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Setting user accounts(for banking account - like budget, expenses)
 * @author	Wisedog(me@wisedog.net)
 * */
public class AccountsSetting extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.account_setting);
		GeneralProcessor general = new GeneralProcessor(this);
        ArrayList<AccountsEntity> list = general.getAllAccount();
        
        
        for(int i = 0; i < list.size(); i++)    {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(1.0f);
            AccountsEntity entity = list.get(i);
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
            tr.addView(layout, new LayoutParams(0 , LayoutParams.WRAP_CONTENT, 1.0f));
            if(tl != null){
                tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
            }            
        }
        
        //TODO add event handler to delete, modified button        
		
	}

	@Override
	protected void onResume() {
	    
		
		super.onResume();
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
                    //TODO call dialog with passing "type" data                    
                }
            });
	    }
	}
}
