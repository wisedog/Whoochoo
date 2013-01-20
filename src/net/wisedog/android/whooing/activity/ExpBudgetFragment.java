package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ExpBudgetFragment extends SherlockFragmentActivity{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_budget_fragment);
        
        
	}
	
	
	
	@Override
	protected void onResume() {
		//TODO 데이터가 없으면... Thread ON
		//있으면 DataRepository
		Bundle bundle = new Bundle();
		bundle.putString("start_date", WhooingCalendar.getTodayYYYYMM());
		bundle.putString("end_date", WhooingCalendar.getTodayYYYYMM());
		bundle.putString("account", "expenses");
		ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_BUDGET, bundle);
        thread.start();
		super.onResume();
	}



	protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BUDGET){
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                    	showExpBudget(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               
            }
            super.handleMessage(msg);
        }
        
    };
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
    private void showExpBudget(JSONObject obj) throws JSONException{
    	//Log.i("wisedog", "Exp Budget Result : " + obj.toString());
    	JSONObject resultObj = obj.getJSONObject("results");
    	JSONObject resultAggregate = resultObj.getJSONObject("aggregate");
    	//TODO aggregate 값으로 입력하기 
    	
    	
    }
}
