/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountsSelection extends Activity {
    private ArrayList<AccountsEntity> mList;
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_selection);
        Intent intent = getIntent();
        
        mList = intent.getParcelableArrayListExtra("accounts_list");

    }
    
    private void setupUi(){
        TextView textView = new TextView(this);

    }
    

}
