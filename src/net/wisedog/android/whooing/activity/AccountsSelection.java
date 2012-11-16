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
import android.util.TypedValue;
import android.view.View;
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
        String mode = getIntent().getStringExtra("mode");
        TextView textViewAssets = (TextView) findViewById(R.id.accounts_selection_title_assets);
        TextView textViewIncome = (TextView) findViewById(R.id.accounts_selection_title_income);
        TextView textViewExpenses = (TextView) findViewById(R.id.accounts_selection_title_expenses);
        TextView textViewLiability = (TextView) findViewById(R.id.accounts_selection_title_liabilities);
        if(mode.compareTo("left") == 0){
            textViewAssets.setText(getString(R.string.accounts_assets)+"+");
            textViewIncome.setVisibility(View.GONE);
        }else if(mode.compareTo("right") == 0){
            textViewExpenses.setVisibility(View.GONE);
        }else{  //All
            
        }
        
        for(int i = 0; i < mList.size(); i++){
            TextView textView = new TextView(this);
            textView.setId(DYNAMIC_VIEW_ID+i);
            
            AccountsEntity entity = mList.get(i);
            textView.setText(entity.title);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
            textView.setClickable(true);
            textView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    v.getId();
                    
                }
            });
            LinearLayout layout = null;
            if (entity.accountType.compareTo("assets") == 0) {
                layout = (LinearLayout) findViewById(R.id.accounts_selection_assets);
            } else if (entity.accountType.compareTo("liabilities") == 0) {
                layout = (LinearLayout) findViewById(R.id.accounts_selection_liabilities);
            } else if (entity.accountType.compareTo("capital") == 0) {
                layout = (LinearLayout) findViewById(R.id.accounts_selection_capital);
            } else if (entity.accountType.compareTo("expenses") == 0) {
                if (mode.equals("right") != true) {
                    layout = (LinearLayout) findViewById(R.id.accounts_selection_expenses);
                }else{
                    continue;
                }
            } else if (entity.accountType.compareTo("income") == 0) {
                if (mode.equals("left") != true) {
                    layout = (LinearLayout) findViewById(R.id.accounts_selection_income);
                }else{
                    continue;
                }
            } else {
                continue;
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 20);
            textView.setLayoutParams(layoutParams);
            
            layout.addView(textView);
        }
    }
}
