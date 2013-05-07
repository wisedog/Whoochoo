package net.wisedog.android.whooing.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.engine.DataRepository.OnExpBudgetChangeListener;
import net.wisedog.android.whooing.ui.TableRowExpBudgetItem;
import net.wisedog.android.whooing.utils.WhooingAlert;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ExpBudgetFragmentActivity extends SherlockFragmentActivity implements OnExpBudgetChangeListener{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_budget_fragment);
	}
	
	
	
	@Override
	protected void onResume() {
	    DataRepository repository = WhooingApplication.getInstance().getRepo(); //DataRepository.getInstance();
	    if(repository.getExpBudgetValue() != null){
	        showExpBudget(repository.getExpBudgetValue());
	    }
	    else{
	        repository.refreshExpBudget(this);
	        repository.registerObserver(this, DataRepository.EXP_BUDGET_MODE);
	    }
		super.onResume();
	}

    private void showExpBudget(JSONObject obj){
        
        TableLayout tl = (TableLayout) findViewById(R.id.exp_budget_table);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        final int secondColumnWidth = (int) (metrics.widthPixels * 0.7);
        
        try {
            JSONObject objResult = obj.getJSONObject("results");
            JSONArray objRows = objResult.getJSONArray("rows");
            JSONObject objRow1 = (JSONObject) objRows.get(0);
            JSONObject objTotalExpBudget = objRow1.getJSONObject("total");
            
            double totalExpBudgetValue = objTotalExpBudget.getDouble("budget");
            double totalSpent = objTotalExpBudget.getDouble("money");
            double totalRemains = objTotalExpBudget.getDouble("remains");
            
            TableRow tr = addOneRowItem(getString(R.string.total_expenses), totalSpent, totalExpBudgetValue, totalRemains, 
                    0xFFC36FBC, secondColumnWidth, 5);
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            
            showExpBudgetEntries(objRow1.getJSONArray("accounts"), tl, 0x885294FF, secondColumnWidth, 5);
            

        } catch (JSONException e) {
            e.printStackTrace();
        }
    	
    }
    
    private void showExpBudgetEntries(JSONArray accounts, TableLayout tl, int color, 
            int maxColumnWidth, int minColumnWidth) throws JSONException{
        if(accounts == null){
            return;
        }
        GeneralProcessor genericProcessor = new GeneralProcessor(this);
        for(int i = 0; i < accounts.length(); i++){
            JSONObject accountItem = (JSONObject) accounts.get(i);
            AccountsEntity entity = genericProcessor.findAccountById(accountItem.getString("account_id"));
            
            double spent = accountItem.getDouble("money");
            double budget = accountItem.getDouble("budget");
            double remains = accountItem.getDouble("remains");
            
            TableRow tr = addOneRowItem(entity.title, spent, budget, remains, color, maxColumnWidth, minColumnWidth);
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
        
    }
    
    private TableRow addOneRowItem(String accountName, double spent, double budget, double remains, 
            int color, int maxColumnWidth, int minColumnWidth){
        Resources r = getResources();
        final int margin4Dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                r.getDisplayMetrics());
        /* Create a new row to be added. */
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        tr.setWeightSum(1.0f);
        
        TextView accountText = new TextView(this);
        accountText.setText(accountName);
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.3f);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = margin4Dip;
        accountText.setLayoutParams(params);
        tr.addView(accountText);
        
        TableRowExpBudgetItem layout = new TableRowExpBudgetItem(this);
        layout.setupListItem(spent, budget, remains, maxColumnWidth, minColumnWidth, color);
        layout.setLayoutParams(new LayoutParams(
                0, LayoutParams.WRAP_CONTENT, 0.7f));
        layout.requestLayout();
        tr.addView(layout);
        return tr;
    }

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnExpBudgetChangeListener#onExpBudgetUpdate(org.json.JSONObject)
     */
    @Override
    public void onExpBudgetUpdate(JSONObject obj) {
    	int returnCode = Define.RESULT_OK;
		try {
			returnCode = obj.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		}
    	if(returnCode == Define.RESULT_INSUFFIENT_API && Define.SHOW_NO_API_INFORM == false){
    		Define.SHOW_NO_API_INFORM = true;
    		WhooingAlert.showNotEnoughApi(this);
    	}
        showExpBudget(obj);
        
    }



    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        DataRepository repository = WhooingApplication.getInstance().getRepo(); //DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.EXP_BUDGET_MODE);
        super.onDestroy();
    }
    
    
}
