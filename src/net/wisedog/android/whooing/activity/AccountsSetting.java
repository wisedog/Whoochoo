package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.ui.AccountRowItem;
import android.os.Bundle;
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
        TableLayout tl = (TableLayout)findViewById(R.id.account_setting_table_asset);
        
        for(int i = 0; i < list.size(); i++)    {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(1.0f);
            AccountsEntity entity = list.get(i);
            AccountRowItem layout = new AccountRowItem(this);
            layout.setupListItem(entity);
            tr.addView(layout, new LayoutParams(0 , LayoutParams.WRAP_CONTENT, 1.0f));
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                  LayoutParams.WRAP_CONTENT));
        }
        tl.requestLayout();
	}

	@Override
	protected void onResume() {
		//TODO inflates data views from database
		
		super.onResume();
	}
}
