/**
 * 
 */
package net.wisedog.android.whooing.dialog;

import java.util.ArrayList;
import java.util.HashMap;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;

import com.actionbarsherlock.app.SherlockDialogFragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountChooserDialog extends SherlockDialogFragment {
    private static final int DYNAMIC_VIEW_ID = 10000;
    private static final int DYNAMIC_LAYOUT_ID = 20000;
    private static final int DYNAMIC_LAYOUT_ID_ADDED = 20100;
    
    
    static public AccountChooserDialog newInstance(
            ArrayList<AccountsEntity> accountsList, int year, int month, int day, String mode) {
        AccountChooserDialog f = new AccountChooserDialog();
     // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelableArrayList("accounts_list", accountsList);
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putString("mode", mode);
        f.setArguments(args);
        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.accounts_selection, container, false);
        setupUi(v);
        return v;
    }
    
    public void onSelectItem(View v){
        /*TextView textView = (TextView)v;
        
        AccountsEntity entity = (AccountsEntity) textView.getTag();
        String mode = getIntent().getStringExtra("mode");
        Intent intent = new Intent();
        intent.putExtra("selection",entity);
        intent.putExtra("mode", mode);
        setResult(RESULT_OK, intent);*/
        this.dismiss();
    }
    
    /**
     * UI setup
     * */
    private void setupUi(View v){
        ArrayList<AccountsEntity> list = getArguments().getParcelableArrayList("accounts_list");
        String mode = getArguments().getString("mode");
        TextView textViewAssets = (TextView) v.findViewById(R.id.accounts_selection_title_assets);
        TextView textViewIncome = (TextView) v.findViewById(R.id.accounts_selection_title_income);
        TextView textViewExpenses = (TextView) v.findViewById(R.id.accounts_selection_title_expenses);
        TextView textViewLiability = (TextView) v.findViewById(R.id.accounts_selection_title_liabilities);
        TextView textViewCapital = (TextView) v.findViewById(R.id.accounts_selection_title_capital);
        LinearLayout layoutExpenses = (LinearLayout) v.findViewById(R.id.accounts_selection_expenses);
        LinearLayout layoutIncome = (LinearLayout) v.findViewById(R.id.accounts_selection_income);
        if(mode.compareTo("left") == 0){
            textViewAssets.setText(getString(R.string.accounts_assets)+"+");
            textViewLiability.setText(getString(R.string.accounts_liabilities)+"-");
            textViewCapital.setText(getString(R.string.accounts_capital)+"-");
            textViewIncome.setVisibility(View.GONE);
            layoutIncome.setVisibility(View.GONE);
        }else if(mode.compareTo("right") == 0){
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
            LinearLayout llAlso = new LinearLayout(getActivity());
            llAlso.setId(DYNAMIC_LAYOUT_ID + j);
            llAlso.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            llAlso.setOrientation(LinearLayout.HORIZONTAL);
            
            //Add View
            LinearLayout layout = (LinearLayout) v.findViewById(accountsItemContainer[j]);
            layout.addView(llAlso);
            
          //ArrayList for containing layout resource id
            ArrayList<Integer> tmp = new ArrayList<Integer>();
            tmp.add(DYNAMIC_LAYOUT_ID + j);
            map.put(accountsArray[j], tmp);
        }
        
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int maxWidth = display.getWidth() - 10;
        
        for(int i = 0; i < list.size(); i++){
            AccountsEntity entity = list.get(i);
            
            //Creating accounts item
            TextView textView = new TextView(getActivity());
            textView.setId(DYNAMIC_VIEW_ID+i);
            textView.setText(entity.title);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
            textView.setClickable(true);
            textView.setTag(entity);
            textView.setTextColor(Color.GRAY);
            
            //For touch selection effect
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
                    onSelectItem(v);
                }
            });
            
            //Setup UI for textView
            textView.measure(0, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 0, 0, 15);
            textView.setLayoutParams(layoutParams);
            
            @SuppressWarnings("unchecked")
            ArrayList<Integer> tmp1 = (ArrayList<Integer>) map.get(entity.accountType);
            int layoutId = tmp1.get(tmp1.size()-1);
            LinearLayout ll = (LinearLayout) v.findViewById(layoutId);
            ll.measure(0, 0);
            
            if(ll.getMeasuredWidth() + textView.getMeasuredWidth() < maxWidth){    
                ll.addView(textView);
            }else{
                //Create new LinearLayout(Horizontal) and add the textview to new layout 
                //and add new layout to the parent layout, 
                LinearLayout layout = null;
                if (entity.accountType.compareTo("assets") == 0) {
                    layout = (LinearLayout) v.findViewById(R.id.accounts_selection_assets);
                } else if (entity.accountType.compareTo("liabilities") == 0) {
                    layout = (LinearLayout) v.findViewById(R.id.accounts_selection_liabilities);
                } else if (entity.accountType.compareTo("capital") == 0) {
                    layout = (LinearLayout) v.findViewById(R.id.accounts_selection_capital);
                } else if (entity.accountType.compareTo("expenses") == 0) {
                    if (mode.equals("right") != true) {
                        layout = (LinearLayout) v.findViewById(R.id.accounts_selection_expenses);
                    }else{
                        continue;
                    }
                } else if (entity.accountType.compareTo("income") == 0) {
                    if (mode.equals("left") != true) {
                        layout = (LinearLayout) v.findViewById(R.id.accounts_selection_income);
                    }else{
                        continue;
                    }
                } else {
                    continue;
                }
              //creating linearlayout for item wrapping. 
                LinearLayout llAlso = new LinearLayout(getActivity());
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