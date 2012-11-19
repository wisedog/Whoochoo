/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountsSelection extends Activity {
    private static final int DYNAMIC_VIEW_ID = 10000;
    private static final int DYNAMIC_LAYOUT_ID = 20000;
    private static final int DYNAMIC_LAYOUT_ID_ADDED = 20100;
    private ArrayList<AccountsEntity> mList;
    private Context mContext;
    
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_selection);
        Intent intent = getIntent();
        mContext = this;
        mList = intent.getParcelableArrayListExtra("accounts_list");
        setupUi();
    }
    
    /**
     * UI 셋업
     * */
    private void setupUi(){
        String mode = getIntent().getStringExtra("mode");
        TextView textViewAssets = (TextView) findViewById(R.id.accounts_selection_title_assets);
        TextView textViewIncome = (TextView) findViewById(R.id.accounts_selection_title_income);
        TextView textViewExpenses = (TextView) findViewById(R.id.accounts_selection_title_expenses);
        TextView textViewLiability = (TextView) findViewById(R.id.accounts_selection_title_liabilities);
        LinearLayout layoutExpenses = (LinearLayout) findViewById(R.id.accounts_selection_expenses);
        LinearLayout layoutIncome = (LinearLayout) findViewById(R.id.accounts_selection_income);
        if(mode.compareTo("left") == 0){
            textViewAssets.setText(getString(R.string.accounts_assets)+"+");
            textViewIncome.setVisibility(View.GONE);
            layoutIncome.setVisibility(View.GONE);
        }else if(mode.compareTo("right") == 0){
            textViewExpenses.setVisibility(View.GONE);
            layoutExpenses.setVisibility(View.GONE);
            
        }else{  //All
            
        }
        
        HashMap<String, Object> map = new HashMap<String, Object>();
        String [] accountsArray = new String[]{"assets", "liabilities", "capital", "expenses", "income"};
        int[] accountsItemContainer = new int[]{R.id.accounts_selection_assets, R.id.accounts_selection_liabilities,
                R.id.accounts_selection_capital, R.id.accounts_selection_expenses, R.id.accounts_selection_income};
        
        for(int j = 0; j < 5; j++){
          //creating linearlayout for item wrapping. 
            LinearLayout llAlso = new LinearLayout(this);
            llAlso.setId(DYNAMIC_LAYOUT_ID + j);
            llAlso.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            llAlso.setOrientation(LinearLayout.HORIZONTAL);
            
            //Add View
            LinearLayout layout = (LinearLayout) findViewById(accountsItemContainer[j]);
            layout.addView(llAlso);
            
          //ArrayList for containing layout resource id
            ArrayList<Integer> tmp = new ArrayList<Integer>();
            tmp.add(DYNAMIC_LAYOUT_ID + j);
            map.put(accountsArray[j], tmp);
        }
        Display display = getWindowManager().getDefaultDisplay();
        int maxWidth = display.getWidth() - 10;
        
        for(int i = 0; i < mList.size(); i++){
            AccountsEntity entity = mList.get(i);
            //Creating accounts item
            TextView textView = new TextView(this);
            textView.setId(DYNAMIC_VIEW_ID+i);
            textView.setText(entity.title);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f);
            textView.setClickable(true);
            textView.setOnTouchListener(new View.OnTouchListener() {
                
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        v.setBackgroundColor(Color.BLACK);
                        TextView textView = (TextView)v;
                        textView.setTextColor(Color.WHITE);
                    }
                    return false;
                }
            });
            textView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    v.setBackgroundColor(Color.WHITE);
                    TextView textView = (TextView)v;
                    textView.setTextColor(Color.BLACK);
                    Toast.makeText(mContext, "ID : "+ v.getId(), Toast.LENGTH_SHORT).show();
                }
            });
            
            textView.measure(0, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 20);
            textView.setLayoutParams(layoutParams);
            
            @SuppressWarnings("unchecked")
            ArrayList<Integer> tmp1 = (ArrayList<Integer>) map.get(entity.accountType);
            int layoutId = tmp1.get(tmp1.size()-1);
            LinearLayout ll = (LinearLayout) findViewById(layoutId);
            ll.measure(0, 0);
            
            if(ll.getMeasuredWidth() + textView.getMeasuredWidth() < maxWidth){    
                ll.addView(textView);
            }else{
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
              //creating linearlayout for item wrapping. 
                LinearLayout llAlso = new LinearLayout(this);
                llAlso.setId(DYNAMIC_LAYOUT_ID_ADDED + i);
                llAlso.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.WRAP_CONTENT));
                llAlso.setOrientation(LinearLayout.HORIZONTAL);
                llAlso.addView(textView);
                layout.addView(llAlso);
                tmp1.add(DYNAMIC_LAYOUT_ID_ADDED + i);
            }
        }
    }
}
