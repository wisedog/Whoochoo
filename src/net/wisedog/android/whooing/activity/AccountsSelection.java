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
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountsSelection extends Activity {
    private static final int DYNAMIC_VIEW_ID = 10000;
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
        setupUi();
    }
    
    private void setupUi(){
        for(int i = 0; i < mList.size(); i++){
            TextView textView = new TextView(this);
            textView.setId(DYNAMIC_VIEW_ID+i);
            
            AccountsEntity entity = mList.get(i);
            textView.setText(entity.title);
            LinearLayout layout = null;
            if(entity.accountType.compareTo("assets") == 0){
                layout = (LinearLayout)findViewById(R.id.accounts_selection_assets);
                
            }else if(entity.accountType.compareTo("liabilities") == 0){
                layout = (LinearLayout)findViewById(R.id.accounts_selection_liabilities);
            }else{
                continue;
            }
            layout.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            
            
        }
        
        
    }
}
