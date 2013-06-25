package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.adapter.SmsConfirmAdapter;
import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.db.SmsDbOpenHelper;
import net.wisedog.android.whooing.db.SmsInfoEntity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class SmsConfirmList extends SherlockActivity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_confirm);
	}

	@Override
	protected void onResume() {
		SmsDbOpenHelper db = new SmsDbOpenHelper(this);
		AccountsDbOpenHelper accountDb = new AccountsDbOpenHelper(this);
		ArrayList<SmsInfoEntity> array = db.getAllAccountsInfo();
		if(array == null){
			Toast.makeText(this, getString(R.string.sms_no_received), Toast.LENGTH_LONG).show();
		}else if(array.size() == 0){
			Toast.makeText(this, getString(R.string.sms_no_received), Toast.LENGTH_LONG).show();
		}
		
		ArrayList<AccountsEntity> accountArray = accountDb.getAllAccountsInfo(true);
		SmsConfirmAdapter adapter = new SmsConfirmAdapter(this, array, accountArray);
		ListView list = (ListView) findViewById(R.id.sms_confirm_listview);
		list.setAdapter(adapter);
		
		super.onResume();
	}
	
	

}
