package net.wisedog.android.whooing.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.engine.DataRepository.OnPlChangeListener;
import net.wisedog.android.whooing.utils.FragmentUtil;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public final class ProfitLossFragment extends SherlockFragment implements OnPlChangeListener{
    private Activity mActivity;
    
    public static ProfitLossFragment newInstance() {
        ProfitLossFragment fragment = new ProfitLossFragment();
        return fragment;
    }	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.profit_loss_fragment, null);
    	
        return view;
    }
    
    

    @Override
	public void onResume() {
        TableLayout tl = (TableLayout) mActivity
                .findViewById(R.id.pl_fragment_expenses_table);
        if(tl.getChildCount() > 2){
            super.onResume();
            return;
        }
        DataRepository repository = DataRepository.getInstance();
        if(repository.getPlValue() != null){
           showPl(repository.getPlValue());
           super.onResume();
           return;
        }
        else{
            repository.refreshPlValue(getSherlockActivity());
            repository.registerObserver(this, DataRepository.PL_MODE);
        }
		super.onResume();
	}
    

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
        DataRepository repository = DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.PL_MODE);
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}
    
    /**
     * @param obj       Profit/Loss value formatted JSON
     */
    private void showPl(JSONObject obj) {
        TextView labelTotalExpensesValue = (TextView)mActivity.findViewById(R.id.pl_fragment_total_expenses_value);
        
        TableLayout tl = (TableLayout) mActivity
                .findViewById(R.id.pl_fragment_expenses_table);
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int secondColumnWidth = (int) (metrics.widthPixels * 0.6);
        Resources r = getResources();
        final int valueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95,
                r.getDisplayMetrics());
        final int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14,
                r.getDisplayMetrics());
        final int rightMargin4Dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                r.getDisplayMetrics());
        
        WhooingCurrency currency = WhooingCurrency.getInstance();
        try {
            JSONObject objResult = obj.getJSONObject("results");
            JSONObject objExpenses = objResult.getJSONObject("expenses");
            double totalExpensesValue = objExpenses.getDouble("total");
            if (labelTotalExpensesValue != null) {
            	double value1 = objExpenses.getDouble("total");
                labelTotalExpensesValue.setText(currency.getFormattedValue(value1));
                View bar = (View) mActivity.findViewById(R.id.bar_total_expense);
                int barWidth = FragmentUtil.getBarWidth(objExpenses.getInt("total"), totalExpensesValue,
                        secondColumnWidth, valueWidth);
                
                android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        barWidth, viewHeight);
                lParams.setMargins(0, 0, rightMargin4Dip, 0);
                lParams.gravity = Gravity.CENTER_VERTICAL;
                bar.setLayoutParams(lParams);
            }
            
            JSONArray objExpensesAccounts = objExpenses.getJSONArray("accounts");
            showPlEntities(objExpensesAccounts, totalExpensesValue, secondColumnWidth, 
                    valueWidth, tl, 0xFFF08537);
            
            JSONObject objIncome = objResult.getJSONObject("income");
            JSONArray objIncomeAccounts = objIncome.getJSONArray("accounts");
            TableLayout tableIncome = (TableLayout) mActivity
                    .findViewById(R.id.pl_fragment_income_table);
            
            TextView labelTotalIncomeValue = (TextView)mActivity.findViewById(R.id.pl_fragment_total_income_value);
            if(labelTotalIncomeValue != null){
            	double value1 = objIncome.getDouble("total");
                labelTotalIncomeValue.setText(currency.getFormattedValue(value1));
                View bar = (View)mActivity.findViewById(R.id.bar_total_income);
                int barWidth = FragmentUtil.getBarWidth(objIncome.getInt("total"), totalExpensesValue, 
                        secondColumnWidth, valueWidth);
                android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        barWidth, viewHeight);
                lParams.setMargins(0, 0, rightMargin4Dip, 0);
                lParams.gravity = Gravity.CENTER_VERTICAL;
                bar.setLayoutParams(lParams);
            }
            
            showPlEntities(objIncomeAccounts, totalExpensesValue, secondColumnWidth, 
                    valueWidth, tableIncome, 0xFFBED431);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private void showPlEntities(JSONArray accounts, double totalAssetValue, 
            int secondColumnWidth, int labelWidth, TableLayout tl, int color) throws JSONException{
        if(accounts == null){
            return;
        }
        GeneralProcessor genericProcessor = new GeneralProcessor(mActivity);
        for(int i = 0; i < accounts.length(); i++){
            JSONObject accountItem = (JSONObject) accounts.get(i);
            
            /* Create a new row to be added. */
            TableRow tr = new TableRow(mActivity);
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(1.0f);
            
            TextView accountText = new TextView(mActivity);
            AccountsEntity entity = genericProcessor.findAccountById(accountItem.getString("account_id"));
            if(entity == null){
                continue;
            }
            accountText.setText(entity.title);
            accountText.setLayoutParams(new LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, 0.4f));

            tr.addView(accountText);
            
            LinearLayout amountLayout = new LinearLayout(mActivity);
            amountLayout.setLayoutParams(new TableRow.LayoutParams(
                    0, 
                    LayoutParams.WRAP_CONTENT,0.6f));

            int barWidth = FragmentUtil.getBarWidth(accountItem.getDouble("money"), totalAssetValue, 
                    secondColumnWidth, labelWidth);
            Resources r = getResources();
            int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
            android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    barWidth, px);
            
            //set up view for horizontally bar graph 
            View barView = new View(mActivity);
            barView.setBackgroundColor(color);
            int rightMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
            lParams.setMargins(0, 0, rightMargin, 0);
            lParams.gravity = Gravity.CENTER_VERTICAL;
            barView.setLayoutParams(lParams);
            
            //set up textview for showing amount
            TextView amountText = new TextView(mActivity);
            double value1 = accountItem.getDouble("money");
            amountText.setText(WhooingCurrency.getFormattedValue(value1));
            amountLayout.addView(barView);
            amountLayout.addView(amountText);
            tr.addView(amountLayout);
            
            /* Add row to TableLayout. */
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnPlChangeListener#onPlUpdate(org.json.JSONObject)
     */
    public void onPlUpdate(JSONObject obj) {
        if(isAdded() == true){
        	showPl(obj);
        }
        
    }
}
