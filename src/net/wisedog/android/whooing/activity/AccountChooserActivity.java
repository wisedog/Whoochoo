/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import net.wisedog.android.whooing.widget.WiTextView;

import com.actionbarsherlock.app.SherlockActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountChooserActivity extends SherlockActivity {
    private static final int DYNAMIC_VIEW_ID = 10000;
    private static final int DYNAMIC_LAYOUT_ID = 20000;
    private static final int DYNAMIC_LAYOUT_ID_ADDED = 20100;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts_selection);
        setupUi();        
    }

    
    public void onSelectItem(View v){
        TextView textView = (TextView)v;
        
        AccountsEntity entity = (AccountsEntity) textView.getTag();
        Intent intent = new Intent();
        intent.putExtra("selection", entity);
        setResult(RESULT_OK, intent);
        finish();
    }
    
    /**
     * UI setup
     * */
    @SuppressLint("CutPasteId")
	private void setupUi(){
    	Intent intent = getIntent();
    	
        ArrayList<AccountsEntity> list = intent.getParcelableArrayListExtra("accounts_list");
        int mode = intent.getIntExtra("mode", -1);
        WiTextView textViewAssets = (WiTextView) findViewById(R.id.accounts_selection_title_assets);
        WiTextView textViewIncome = (WiTextView) findViewById(R.id.accounts_selection_title_income);
        WiTextView textViewExpenses = (WiTextView) findViewById(R.id.accounts_selection_title_expenses);
        WiTextView textViewLiability = (WiTextView) findViewById(R.id.accounts_selection_title_liabilities);
        WiTextView textViewCapital = (WiTextView) findViewById(R.id.accounts_selection_title_capital);
        LinearLayout layoutExpenses = (LinearLayout) findViewById(R.id.accounts_selection_expenses);
        LinearLayout layoutIncome = (LinearLayout) findViewById(R.id.accounts_selection_income);
        if(mode == TransactionAdd.LEFT_SIDE){
            textViewAssets.setText(getString(R.string.accounts_assets)+"+");
            textViewLiability.setText(getString(R.string.accounts_liabilities)+"-");
            textViewCapital.setText(getString(R.string.accounts_capital)+"-");
            textViewIncome.setVisibility(View.GONE);
            layoutIncome.setVisibility(View.GONE);
        }else if(mode == TransactionAdd.RIGHT_SIDE){
            textViewAssets.setText(getString(R.string.accounts_assets)+"-");
            textViewLiability.setText(getString(R.string.accounts_liabilities)+"+");
            textViewCapital.setText(getString(R.string.accounts_capital)+"+");
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
            llAlso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
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
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int maxWidth = metrics.widthPixels - 10;
        
        for(int i = 0; i < list.size(); i++){
            AccountsEntity entity = list.get(i);
            
            if(entity.close_date <= WhooingCalendar.getTodayYYYYMMDDint()){
                continue;
            }
            
            if(entity.type.compareTo("group") == 0){
            	continue;
            }
            
            //Creating accounts item
            WiTextView textView = new WiTextView(this);
            textView.setId(DYNAMIC_VIEW_ID+i);
            textView.setText(entity.title);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
            textView.setClickable(true);
            textView.setTag(entity);
            textView.setTextColor(Color.GRAY);
            textView.setEllipsize(null);
            textView.setMaxLines(1);
            
            //For touch selection effect
            textView.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        v.setBackgroundColor(Color.BLACK);
                        WiTextView textView = (WiTextView)v;
                        textView.setTextColor(Color.WHITE);
                    }
                    return false;
                }
            });
            textView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    v.setBackgroundColor(Color.WHITE);
                    WiTextView textView = (WiTextView)v;
                    textView.setTextColor(Color.BLACK);
                    onSelectItem(v);
                }
            });
            
            //Setup UI for textView
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 15);
            textView.setLayoutParams(layoutParams);
            textView.measure(0, 0);
            
            @SuppressWarnings("unchecked")
            ArrayList<Integer> tmp1 = (ArrayList<Integer>) map.get(entity.accountType);
            int layoutId = tmp1.get(tmp1.size()-1);
            LinearLayout ll = (LinearLayout) findViewById(layoutId);
            ll.measure(0, 0);
            
            if(ll.getMeasuredWidth() + textView.getMeasuredWidth() + 40 < maxWidth){    
                ll.addView(textView);
            }else{
                //Create new LinearLayout(Horizontal) and add the textview to new layout 
                //and add new layout to the parent layout,
                LinearLayout layout = null;
                if (entity.accountType.compareTo("assets") == 0) {
                    layout = (LinearLayout) findViewById(R.id.accounts_selection_assets);
                } else if (entity.accountType.compareTo("liabilities") == 0) {
                    layout = (LinearLayout) findViewById(R.id.accounts_selection_liabilities);
                } else if (entity.accountType.compareTo("capital") == 0) {
                    layout = (LinearLayout) findViewById(R.id.accounts_selection_capital);
                } else if (entity.accountType.compareTo("expenses") == 0) {
                    if (mode != TransactionAdd.RIGHT_SIDE) {
                        layout = (LinearLayout) findViewById(R.id.accounts_selection_expenses);
                    }else{
                        continue;
                    }
                } else if (entity.accountType.compareTo("income") == 0) {
                    if (mode != TransactionAdd.LEFT_SIDE) {
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
                llAlso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                llAlso.setOrientation(LinearLayout.HORIZONTAL);
                llAlso.addView(textView);
                layout.addView(llAlso);
                tmp1.add(DYNAMIC_LAYOUT_ID_ADDED + i);
            }
        }
    }
}