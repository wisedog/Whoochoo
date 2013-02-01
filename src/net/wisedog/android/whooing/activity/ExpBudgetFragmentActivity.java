package net.wisedog.android.whooing.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.engine.DataRepository.OnExpBudgetChangeListener;
import net.wisedog.android.whooing.ui.TableRowExpBudgetItem;
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
	    DataRepository repository = DataRepository.getInstance();
	    if(repository.getExpBudgetValue() != null){
	        showExpBudget(repository.getExpBudgetValue());
	        super.onResume();
           return;
	    }
	    else{
	        repository.refreshExpBudget(this);
	        repository.registerObserver(this, DataRepository.EXP_BUDGET_MODE);
	    }
		super.onResume();
	}

    /*
     * {"error_parameters":[],"message":"","code":200,
     * "results":{"rows_type":"month",
     * "aggregate":{"total_steady":
     * {"money":0,"remains":0,"budget":0},
     * "total":{"money":0,"remains":0,"budget":0},
     * "total_floating":{"money":0,"remains":0,"budget":0},
     * "accounts":[{"account_id":"x50","money":0,"remains":0,"budget":0},{"account_id":"x51","money":0,"remains":0,"budget":0},{"account_id":"x52","money":0,"remains":0,"budget":0},{"account_id":"x53","money":0,"remains":0,"budget":0},{"account_id":"x54","money":0,"remains":0,"budget":0},{"account_id":"x55","money":0,"remains":0,"budget":0},{"account_id":"x56","money":0,"remains":0,"budget":0},{"account_id":"x57","money":0,"remains":0,"budget":0},{"account_id":"x58","money":0,"remains":0,"budget":0},{"account_id":"x59","money":0,"remains":0,"budget":0},{"account_id":"x60","money":0,"remains":0,"budget":0},{"account_id":"x77","money":0,"remains":0,"budget":0},{"account_id":"x78","money":0,"remains":0,"budget":0}],
     * "misc":{"today":{"money":0,"remains":0,"budget":0},"possibility":100,"weekly_remains":243054,"standard":255376,"daily_remains":34722}},"max":0,
     * "rows":[
     * {"total_steady":{"money":0,"remains":0,"budget":0},
     * "total":{"money":0,"remains":416667,"budget":416667},
     * "total_floating":{"money":0,"remains":0,"budget":0},
     * "accounts":[{"account_id":"x50","money":0,"remains":0,"budget":0},{"account_id":"x51","money":0,"remains":0,"budget":0},{"account_id":"x52","money":0,"remains":0,"budget":0},{"account_id":"x53","money":0,"remains":0,"budget":0},{"account_id":"x54","money":0,"remains":0,"budget":0},{"account_id":"x55","money":0,"remains":0,"budget":0},{"account_id":"x56","money":0,"remains":0,"budget":0},{"account_id":"x57","money":0,"remains":0,"budget":0},{"account_id":"x58","money":0,"remains":0,"budget":0},{"account_id":"x59","money":0,"remains":0,"budget":0},{"account_id":"x60","money":0,"remains":0,"budget":0},{"account_id":"x77","money":0,"remains":0,"budget":0},{"account_id":"x78","money":0,"remains":0,"budget":0}],"date":201301,"misc":{"today":{"money":0,"remains":34722,"budget":34722},
     * "possibility":100,"weekly_remains":243054,"standard":255376,"daily_remains":34722}}]},"rest_of_api":4988}
     * 
     * */
    private void showExpBudget(JSONObject obj){
        
        TableLayout tl = (TableLayout) findViewById(R.id.exp_budget_table);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        final int secondColumnWidth = (int) (metrics.widthPixels * 0.7);
        /*Resources r = getResources();
        
        final int valueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95,
                r.getDisplayMetrics());
        final int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14,
                r.getDisplayMetrics());*/
        
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
        showExpBudget(obj);
        
    }



    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        DataRepository repository = DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.EXP_BUDGET_MODE);
        super.onDestroy();
    }
    
    
}
