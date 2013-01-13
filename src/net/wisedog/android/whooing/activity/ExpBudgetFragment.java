package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ExpBudgetFragment extends SherlockFragmentActivity {

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
		ThreadRestAPI thread = new ThreadRestAPI(mHandler, this, Define.API_GET_BUDGET);
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
    
    private void showExpBudget(JSONObject obj) throws JSONException{
    	
    }
}
