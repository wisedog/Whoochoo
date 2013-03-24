package net.wisedog.android.whooing.activity;

import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;

public class DashboardHelp extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContent(R.layout.blahblah);		
	}
	
	public void onClickScreen(View v){
		setResult(RESULT_OK);
		finish();
	}

}
