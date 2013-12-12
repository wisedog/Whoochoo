/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.HashMap;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import net.wisedog.android.whooing.widget.WiTextView;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AccountSelectFragment extends SherlockDialogFragment {
	private static final int DYNAMIC_VIEW_ID = 10000;
    private static final int DYNAMIC_LAYOUT_ID = 20000;
    private static final int DYNAMIC_LAYOUT_ID_ADDED = 20100;
    
    private int mMode;
    
    public interface AccountSelectListener {
        void onFinishSelect(int requestCode, AccountsEntity entity);
    }
    
    private AccountSelectListener mListener;

    public static AccountSelectFragment newInstance(Bundle bundle) {
    	AccountSelectFragment fragment = new AccountSelectFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.accounts_selection, container, false);
		
		return view;
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		setupUi(getView());
		super.onActivityCreated(arg0);
	}

	public void onSelectItem(View v){
        TextView textView = (TextView)v;
        
        AccountsEntity entity = (AccountsEntity) textView.getTag();
        try{
        	mListener.onFinishSelect(mMode, entity);
        }
        catch(NullPointerException e){
        	e.printStackTrace();
        }
        
        this.dismiss();
	}
    /**
     * Setup UI
     * @param	v	view instance of this dialog
     * */
	@SuppressLint("CutPasteId")
	private void setupUi(View v){
    	//Intent intent = getIntent();
		if(v == null){
			return;
		}
    	Bundle b = getArguments();
        ArrayList<AccountsEntity> list = b.getParcelableArrayList("accounts_list");
        mMode = b.getInt("mode", -1);
        WiTextView textViewAssets = (WiTextView) v.findViewById(R.id.accounts_selection_title_assets);
        WiTextView textViewIncome = (WiTextView) v.findViewById(R.id.accounts_selection_title_income);
        WiTextView textViewExpenses = (WiTextView) v.findViewById(R.id.accounts_selection_title_expenses);
        WiTextView textViewLiability = (WiTextView) v.findViewById(R.id.accounts_selection_title_liabilities);
        WiTextView textViewCapital = (WiTextView) v.findViewById(R.id.accounts_selection_title_capital);
        LinearLayout layoutExpenses = (LinearLayout) v.findViewById(R.id.accounts_selection_expenses);
        LinearLayout layoutIncome = (LinearLayout) v.findViewById(R.id.accounts_selection_income);
        if(mMode == TransactionAddFragment.LEFT_SIDE){
            textViewAssets.setText(getString(R.string.accounts_assets)+"+");
            textViewLiability.setText(getString(R.string.accounts_liabilities)+"-");
            textViewCapital.setText(getString(R.string.accounts_capital)+"-");
            textViewIncome.setVisibility(View.GONE);
            layoutIncome.setVisibility(View.GONE);
        }else if(mMode == TransactionAddFragment.RIGHT_SIDE){
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
            LinearLayout llAlso = new LinearLayout(getSherlockActivity());
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
        
        DisplayMetrics metrics = new DisplayMetrics();
        getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        int maxWidth = (int) (metrics.widthPixels * 0.7);
        
        for(int i = 0; i < list.size(); i++){
            AccountsEntity entity = list.get(i);
            
            if(entity.close_date <= WhooingCalendar.getTodayYYYYMMDDint()){
                continue;
            }
            
            if(entity.type.compareTo("group") == 0){
            	continue;
            }
            
            //Creating accounts item
            WiTextView textView = new WiTextView(getSherlockActivity());
            textView.setId(DYNAMIC_VIEW_ID + i);
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
                    if (mMode != TransactionAddFragment.RIGHT_SIDE) {
                        layout = (LinearLayout) v.findViewById(R.id.accounts_selection_expenses);
                    }else{
                        continue;
                    }
                } else if (entity.accountType.compareTo("income") == 0) {
                    if (mMode != TransactionAddFragment.LEFT_SIDE) {
                        layout = (LinearLayout) v.findViewById(R.id.accounts_selection_income);
                    }else{
                        continue;
                    }
                } else {
                    continue;
                }
              //creating linearlayout for item wrapping. 
                LinearLayout llAlso = new LinearLayout(getSherlockActivity());
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

	public void setListner(AccountSelectListener listener) {
		mListener = listener;
	}
}
    
   