package net.wisedog.android.whooing.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.engine.DataRepository.OnExpBudgetChangeListener;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.FragmentUtil;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ExpBudgetFragment extends SherlockFragmentActivity implements OnExpBudgetChangeListener{

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
	        repository.refreshExpBudget();
	        repository.registerObserver(this, DataRepository.EXP_BUDGET_MODE);
	    }
        
        
		//TODO 데이터가 없으면... Thread ON
		//있으면 DataRepository
/*		Bundle bundle = new Bundle();
		bundle.putString("start_date", WhooingCalendar.getTodayYYYYMM());
		bundle.putString("end_date", WhooingCalendar.getTodayYYYYMM());
		bundle.putString("account", "expenses");
		ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_BUDGET, bundle);
        thread.start();*/
		super.onResume();
	}



	/*protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BUDGET){
                    JSONObject obj = (JSONObject)msg.obj;
                    	showExpBudget(obj);
                }
               
            }
            super.handleMessage(msg);
        }
        
    };*/
    /*
     * {"error_parameters":[],"message":"","code":200,
     * "results":{"rows_type":"month",
     * "aggregate":{"total_steady":
     * {"money":0,"remains":0,"budget":0},
     * "total":{"money":0,"remains":0,"budget":0},
     * "total_floating":{"money":0,"remains":0,"budget":0},
     * "accounts":[{"account_id":"x50","money":0,"remains":0,"budget":0},{"account_id":"x51","money":0,"remains":0,"budget":0},{"account_id":"x52","money":0,"remains":0,"budget":0},{"account_id":"x53","money":0,"remains":0,"budget":0},{"account_id":"x54","money":0,"remains":0,"budget":0},{"account_id":"x55","money":0,"remains":0,"budget":0},{"account_id":"x56","money":0,"remains":0,"budget":0},{"account_id":"x57","money":0,"remains":0,"budget":0},{"account_id":"x58","money":0,"remains":0,"budget":0},{"account_id":"x59","money":0,"remains":0,"budget":0},{"account_id":"x60","money":0,"remains":0,"budget":0},{"account_id":"x77","money":0,"remains":0,"budget":0},{"account_id":"x78","money":0,"remains":0,"budget":0}],
     * "misc":{"today":{"money":0,"remains":0,"budget":0},"possibility":100,"weekly_remains":243054,"standard":255376,"daily_remains":34722}},"max":0,"rows":[{"total_steady":{"money":0,"remains":0,"budget":0},"total":{"money":0,"remains":416667,"budget":416667},"total_floating":{"money":0,"remains":0,"budget":0},"accounts":[{"account_id":"x50","money":0,"remains":0,"budget":0},{"account_id":"x51","money":0,"remains":0,"budget":0},{"account_id":"x52","money":0,"remains":0,"budget":0},{"account_id":"x53","money":0,"remains":0,"budget":0},{"account_id":"x54","money":0,"remains":0,"budget":0},{"account_id":"x55","money":0,"remains":0,"budget":0},{"account_id":"x56","money":0,"remains":0,"budget":0},{"account_id":"x57","money":0,"remains":0,"budget":0},{"account_id":"x58","money":0,"remains":0,"budget":0},{"account_id":"x59","money":0,"remains":0,"budget":0},{"account_id":"x60","money":0,"remains":0,"budget":0},{"account_id":"x77","money":0,"remains":0,"budget":0},{"account_id":"x78","money":0,"remains":0,"budget":0}],"date":201301,"misc":{"today":{"money":0,"remains":34722,"budget":34722},"possibility":100,"weekly_remains":243054,"standard":255376,"daily_remains":34722}}]},"rest_of_api":4988}
     * 
     * */
    private void showExpBudget(JSONObject obj){
    	//Log.i("wisedog", "Exp Budget Result : " + obj.toString());
        try{
        	JSONObject resultObj = obj.getJSONObject("results");
        	JSONObject resultAggregate = resultObj.getJSONObject("aggregate");
        }
        catch(JSONException e){
            return;
        }
    	//TODO aggregate 값으로 입력하기 
        TextView labelTotalAssetValue = (TextView)findViewById(R.id.exp_budget_total_value);
        
        TableLayout tl = (TableLayout) findViewById(R.id.exp_budget_table);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int secondColumnWidth = (int) (metrics.widthPixels * 0.6);
        Resources r = getResources();
        final int valueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95,
                r.getDisplayMetrics());
        final int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14,
                r.getDisplayMetrics());
        final int rightMargin4Dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                r.getDisplayMetrics());
        try {
            JSONObject objResult = obj.getJSONObject("results");
            JSONObject objAssets = objResult.getJSONObject("assets");   //FIXME 고칠것
            double totalAssetValue = objAssets.getDouble("total");
            if (labelTotalAssetValue != null) {
                labelTotalAssetValue.setText("" + objAssets.getInt("total"));
                View bar = (View) findViewById(R.id.bar_total_asset);
                int barWidth = FragmentUtil.getBarWidth(objAssets.getInt("total"), totalAssetValue,
                        secondColumnWidth, valueWidth);
                
                android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        barWidth, viewHeight);
                lParams.setMargins(0, 0, rightMargin4Dip, 0);
                lParams.gravity = Gravity.CENTER_VERTICAL;
                bar.setLayoutParams(lParams);
            }
            
            JSONArray objAssetAccounts = objAssets.getJSONArray("accounts");
            showBalanceEntities(objAssetAccounts, totalAssetValue, secondColumnWidth, 
                    valueWidth, tl, 0xFFC36FBC);
            
            JSONObject objLiabilities = objResult.getJSONObject("liabilities");
            JSONArray objLiabilitiesAccounts = objLiabilities.getJSONArray("accounts");
            TableLayout tableLiabilites = (TableLayout) findViewById(R.id.balance_liabilities_table);
            
            TextView labelTotalLiabilitiesValue = (TextView)findViewById(R.id.balance_total_liabilities_value);
            if(labelTotalLiabilitiesValue != null){
                labelTotalLiabilitiesValue.setText(""+objLiabilities.getInt("total"));
                View bar = (View)findViewById(R.id.bar_total_liabilities);
                int barWidth = FragmentUtil.getBarWidth(objLiabilities.getInt("total"), totalAssetValue, 
                        secondColumnWidth, valueWidth);
                android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        barWidth, viewHeight);
                lParams.setMargins(0, 0, rightMargin4Dip, 0);
                lParams.gravity = Gravity.CENTER_VERTICAL;
                bar.setLayoutParams(lParams);
            }
            
            showBalanceEntities(objLiabilitiesAccounts, totalAssetValue, secondColumnWidth, 
                    valueWidth, tableLiabilites, 0xFF5294FF);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    	
    }
    
    private void showBalanceEntities(JSONArray accounts, double totalAssetValue, 
            int secondColumnWidth, int labelWidth, TableLayout tl, int color) throws JSONException{
        if(accounts == null){
            return;
        }
        GeneralProcessor genericProcessor = new GeneralProcessor(this);
        for(int i = 0; i < accounts.length(); i++){
            JSONObject accountItem = (JSONObject) accounts.get(i);
            
            /* Create a new row to be added. */
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(1.0f);
            
            TextView accountText = new TextView(this);
            AccountsEntity entity = genericProcessor.findAccountById(accountItem.getString("account_id"));
            accountText.setText(entity.title);
            accountText.setLayoutParams(new LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, 0.4f));

            tr.addView(accountText);
            
            LinearLayout amountLayout = new LinearLayout(this);
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
            View barView = new View(this);
            barView.setBackgroundColor(color);
            int rightMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
            lParams.setMargins(0, 0, rightMargin, 0);
            lParams.gravity = Gravity.CENTER_VERTICAL;
            barView.setLayoutParams(lParams);
            
            //set up textview for showing amount
            TextView amountText = new TextView(this);
            amountText.setText(""+accountItem.getInt("money"));
            amountLayout.addView(barView);
            amountLayout.addView(amountText);
            tr.addView(amountLayout);
            
            /* Add row to TableLayout. */
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }



    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnExpBudgetChangeListener#onExpBudgetUpdate(org.json.JSONObject)
     */
    @Override
    public void onExpBudgetUpdate(JSONObject obj) {
        showExpBudget(obj);
        
    }
}
