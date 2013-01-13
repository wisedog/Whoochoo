package net.wisedog.android.whooing.activity;

import net.wisedog.android.whooing.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ExpBudgetFragment extends SherlockFragmentActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transaction);
	}
}
