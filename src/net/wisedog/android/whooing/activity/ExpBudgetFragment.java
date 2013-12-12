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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.api.GeneralApi;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.ui.TableRowExpBudgetItem;
import net.wisedog.android.whooing.utils.WhooingAlert;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;

public class ExpBudgetFragment extends SherlockFragment{
	
	public static ExpBudgetFragment newInstance(Bundle b) {
		ExpBudgetFragment fragment = new ExpBudgetFragment();
        fragment.setArguments(b);
        return fragment;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.exp_budget_fragment, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	    DataRepository repository = WhooingApplication.getInstance().getRepo();
	    if(repository.getExpBudgetValue() != null){
	        showExpBudget(repository.getExpBudgetValue());
	    }
	    else{
	    	ProgressBar progress = (ProgressBar) getView().findViewById(R.id.exp_budget_progress);
	    	if(progress != null){
	    		progress.setVisibility(View.VISIBLE);
	    	}
	        AsyncTask<Void, Integer, JSONObject> task = new AsyncTask<Void, Integer, JSONObject>(){
	        	
                @Override
                protected JSONObject doInBackground(Void... arg0) {
        			String requestUrl = "https://whooing.com/api/budget/expenses.json_array?section_id="
        					+ Define.APP_SECTION 
        					+"&start_date=" + WhooingCalendar.getPreMonthYYYYMMDD(1)
        					+ "&end_date=" + WhooingCalendar.getTodayYYYYMMDD();
        			GeneralApi expBudget = new GeneralApi();
        			JSONObject result = expBudget.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
        					Define.APP_SECRET, Define.TOKEN_SECRET);
        			expBudget = null;	//for gc
                    return result;
                }

                @Override
                protected void onPostExecute(JSONObject result) {
                    if(Define.DEBUG && result != null){
                        Log.d("wisedog", "API Call - ExpBudgetFragmentActivity : " + result.toString());
                    }
                    int returnCode = Define.RESULT_OK;
        			try {
        				returnCode = result.getInt("code");
        			} catch (JSONException e) {
        				e.printStackTrace();
        				return;
        			}
        	    	if(returnCode == Define.RESULT_INSUFFIENT_API && Define.SHOW_NO_API_INFORM == false){
        	    		Define.SHOW_NO_API_INFORM = true;
        	    		WhooingAlert.showNotEnoughApi(getSherlockActivity());
        	    	}
        	    	
        	    	if(returnCode == Define.RESULT_OK){
        	    		DataRepository repository = WhooingApplication.getInstance().getRepo();
        	    		repository.setExpBudgetValue(result);
        	    		showExpBudget(result);
        	    		ProgressBar progress = (ProgressBar) getView().findViewById(R.id.exp_budget_progress);
        	        	if(progress != null){
        	        		progress.setVisibility(View.GONE);
        	        	}
        	    	}
        	    	
                    super.onPostExecute(result);
                }
            };
            
            task.execute();            	

	    }
	    super.onActivityCreated(savedInstanceState);
	}

    private void showExpBudget(JSONObject obj){
        
        TableLayout tl = (TableLayout) getView().findViewById(R.id.exp_budget_table);
        DisplayMetrics metrics = new DisplayMetrics();
        getSherlockActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
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
        GeneralProcessor genericProcessor = new GeneralProcessor(getSherlockActivity());
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
        TableRow tr = new TableRow(getSherlockActivity());
        tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        tr.setWeightSum(1.0f);
        
        TextView accountText = new TextView(getSherlockActivity());
        accountText.setText(accountName);
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.3f);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.leftMargin = margin4Dip;
        accountText.setLayoutParams(params);
        tr.addView(accountText);
        
        TableRowExpBudgetItem layout = new TableRowExpBudgetItem(getSherlockActivity());
        layout.setupListItem(spent, budget, remains, maxColumnWidth, minColumnWidth, color);
        layout.setLayoutParams(new LayoutParams(
                0, LayoutParams.WRAP_CONTENT, 0.7f));
        layout.requestLayout();
        tr.addView(layout);
        return tr;
    }
}
