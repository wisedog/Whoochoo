package net.wisedog.android.whooing.activity;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.db.SmsDbOpenHelper;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingAlert;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
/**
 * 첫 페이지(대쉬보드)Fragment
 * @author Wisedog(me@wisedog.net)
 * */
public class DashboardFragment extends SherlockFragment{
	
	public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    private ProgressDialog dialog;
	private AdView adView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.whooing_main, null);
		
		// adView 만들기
	    adView = new AdView(getSherlockActivity(), AdSize.SMART_BANNER, "a15147cd53daa26");
	    LinearLayout layout = (LinearLayout)view.findViewById(R.id.dashboard_ads);

	    // 찾은 LinearLayout에 adView를 추가
	    layout.addView(adView);

	    // 기본 요청을 시작하여 광고와 함께 요청을 로드
	    AdRequest adRequest = new AdRequest();
	    if(Define.DEBUG){
	    	adRequest.addTestDevice("65E3B8CB214707370B559D98093D74AA");
	    }
	    adView.loadAd(adRequest);
	    
		return view;
	}

    @Override
    public void onResume() {
        DataRepository repository = WhooingApplication.getInstance().getRepo();
        if(repository.getMtValue() == null || repository.getExpBudgetValue() == null){
        	Bundle bundle = new Bundle();
    		bundle.putString("end_date", WhooingCalendar.getTodayYYYYMM());
    		bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMM(6));
    		ThreadRestAPI thread = new ThreadRestAPI(mHandler,
    				Define.API_GET_MOUNTAIN, bundle);
    		thread.start();

    		Bundle bundleBudget = new Bundle();
    		bundleBudget.putString("account", "expenses");
    		bundleBudget.putString("end_date", WhooingCalendar.getTodayYYYYMM());
    		bundleBudget.putString("start_date", WhooingCalendar.getTodayYYYYMM());
    		ThreadRestAPI thread1 = new ThreadRestAPI(mHandler,
    				Define.API_GET_BUDGET, bundleBudget);
    		thread1.start();
        }else{
        	if(repository.getMtValue() != null){
                showMountainValue(repository.getMtValue());
            }
            if(repository.getExpBudgetValue() != null){
                showBudgetValue(repository.getExpBudgetValue());
            }
        }
        
        if(getView() != null){
        	WiTextView smsCount = (WiTextView)getView().findViewById(R.id.text_sms_count);
            if(smsCount != null){
            	SmsDbOpenHelper db = new SmsDbOpenHelper(getSherlockActivity());
            	int count = db.getCount();
            	smsCount.setText(String.valueOf(count));
            }
        }
        
        super.onResume();
    }

	/* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
        adView.destroy();
        super.onDestroyView();
    }
    
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	if(msg.what == Define.MSG_API_OK){
        		JSONObject obj = (JSONObject)msg.obj;
                try {
                    int returnCode = obj.getInt("code");
                    if(returnCode == Define.RESULT_INSUFFIENT_API && Define.SHOW_NO_API_INFORM == false){
                        Define.SHOW_NO_API_INFORM = true;
                        WhooingAlert.showNotEnoughApi(getSherlockActivity());
                        return;
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                    return;
                }
        		DataRepository repository = WhooingApplication.getInstance().getRepo();
        		try {
        		    int restApi = obj.getInt("rest_of_api");
                    repository.setRestApi(restApi);
                    repository.refreshRestApi(getSherlockActivity());
                } catch (JSONException e) {
                    // Just passing..... 
                    e.printStackTrace();
                }
        		
        		if(msg.arg1 == Define.API_GET_MOUNTAIN){
			        repository.setMtValue(obj);
			        if(isAdded() == true){
			            showMountainValue(obj);
			        }
			        
			        repository.refreshRestApi(getSherlockActivity());
        		}else if(msg.arg1 == Define.API_GET_BUDGET){
        			repository.setExpBudgetValue(obj);
        			showBudgetValue(obj);
        		}
        	}
        }
    };
    
    public void setErrorHandler(String errorMsg){
        if(dialog != null){
            dialog.dismiss();
        }	
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        if(Define.NEED_TO_REFRESH == false && bundle != null){
        	if(getView() != null){
        		WiTextView textView = (WiTextView)getView().findViewById(R.id.balance_num);
                textView.setText(bundle.getString("assets_value"));
                textView = (WiTextView)getView().findViewById(R.id.doubt_num);
                textView.setText(bundle.getString("doubt_value"));
        	}            
        }
        super.onActivityCreated(bundle);
    }
    
    /**
     * Mountain 값을 보여준다. DashBoard 1,3 번째
     * @param	obj		Data formatted in JSON
     * */
    private void showMountainValue(JSONObject obj){
    	View v = getView();
    	if(v == null){
    		return;
    	}
        WiTextView currentBalance = (WiTextView)v.findViewById(R.id.balance_num);
        if(currentBalance == null){
        	return;
        }
        WiTextView doubtValue = (WiTextView)v.findViewById(R.id.doubt_num);
        if(currentBalance == null || doubtValue == null){
        	return;
        }
        JSONObject objResult = null;
        try{
        	if(Define.DEBUG){
        		Log.i("wisedog", "Dashboard - showMountainValue - " + obj.toString());
        	}
            objResult = obj.getJSONObject("results");
        }
        catch(JSONException e){
            e.printStackTrace();
            return;
        }
        
        //Set Assets, Doubt value
        try{
            JSONObject objAggregate = objResult.getJSONObject("aggregate");
            double capital = objAggregate.getDouble("capital");
            double liabilities = objAggregate.getDouble("liabilities");
            currentBalance.setText(WhooingCurrency.getFormattedValue(capital, getSherlockActivity()));
            doubtValue.setText(WhooingCurrency.getFormattedValue(liabilities, getSherlockActivity()));
        }catch(JSONException e){
            setErrorHandler("Comm error! Err-MAIN2");
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        
        int diff = 0;
        //set 전월대비
        try {
            JSONArray objArray = objResult.getJSONArray("rows");
            JSONObject last = (JSONObject) objArray.get(objArray.length() - 1);
            JSONObject preLast = (JSONObject) objArray.get(objArray.length() - 2);
            double lastCapital = last.getDouble("capital");
            double preLastCapital = preLast.getDouble("capital");
            diff = (int)(lastCapital - preLastCapital);

        } catch (JSONException e) {
            setErrorHandler("Comm error! Err-MAIN2");
            e.printStackTrace();
        }
        setCompareArrow(diff);
        WiTextView compareValue = (WiTextView)getSherlockActivity().findViewById(R.id.text_compare_premonth_value);
        compareValue.setText(WhooingCurrency.getFormattedValue(diff, getSherlockActivity()));
    }
    
    /**
     * 전월대비 금액에 대한 화살표 설정
     * @param		diff		전월대비 금액
     * */
    private void setCompareArrow(int diff){
    	String locale = getResources().getConfiguration().locale.getDisplayName();
    	ImageView arrow = (ImageView)getActivity().findViewById(R.id.compare_arrow);
    	if(diff > 0){
        	if(locale.equals("kor") || locale.equals("jpn") || locale.equals("chn")){
        		arrow.setImageResource(R.drawable.arrow_up_red);
        	}else{
        		arrow.setImageResource(R.drawable.arrow_up_green);
        	}
        }else if(diff < 0){
        	if(locale.equals("kor") || locale.equals("jpn") || locale.equals("chn")){
        		arrow.setImageResource(R.drawable.arrow_dn_blue);
        	}else{
        		arrow.setImageResource(R.drawable.arrow_dn_red);
        	}
        }
        else{
        	arrow.setImageResource(R.drawable.arrow_none);
        }
    }
    
    /**
     * Budget값을 보여준다. 2번째.
     * @param budgetValue	data formatted in JSON
     */
    private void showBudgetValue(JSONObject budgetValue) {
    	View v = getView();
    	if(v == null){
    		return;
    	}
        WiTextView monthlyExpenseText = (WiTextView)v.findViewById(R.id.budget_monthly_expense_spent);
        if(monthlyExpenseText == null){
            return;
        }
        
        try {
            JSONObject obj = (JSONObject) budgetValue.getJSONObject("results").getJSONArray("rows").get(0);
            JSONObject totalObj = obj.getJSONObject("total");
            double budget = totalObj.getDouble("budget");
            double expenses = totalObj.getDouble("money");
            double possibility = obj.getJSONObject("misc").getDouble("possibility");
            showBudgetGraph(budget, expenses);

            ImageView possibleView = (ImageView)v.findViewById(R.id.dashboard_budget_possiblities);
            if(possibility >= 80){
            	possibleView.setImageResource(R.drawable.icon_sunny);
            }else if(possibility >= 60){
            	possibleView.setImageResource(R.drawable.icon_cloudy2);
            }else if(possibility >= 40){
            	possibleView.setImageResource(R.drawable.icon_cloudy);
            }else if(possibility >= 20){
            	possibleView.setImageResource(R.drawable.icon_rainy);
            }else{
            	possibleView.setImageResource(R.drawable.icon_stormy);
            }
            
        } catch (JSONException e) {
            setErrorHandler("Comm Error! Err-BDG1");
        }
    }
    
    /**
     * Calculate and create graph for showing budget and spent value
     * @param	budget		budget amount
     * @param	expenses	spent amount
     * */
    public void showBudgetGraph(double budget, double expenses){
    	View v = getView();
    	if(v == null){
    		return;
    	}
    	LinearLayout ll = (LinearLayout)v.findViewById(R.id.budget_monthly_layout);
    	WiTextView budgetText = (WiTextView)v.findViewById(R.id.budget_monthly_expense_budget);
    	WiTextView spentText = (WiTextView)v.findViewById(R.id.budget_monthly_expense_spent);
    	budgetText.setText(WhooingCurrency.getFormattedValue(budget, getSherlockActivity()));
    	spentText.setText(WhooingCurrency.getFormattedValue(expenses, getSherlockActivity()));
    	
    	//Calculate width
    	int totalWidth = ll.getMeasuredWidth();
    	budgetText.measure(0, 0);
    	int budgetWidth = budgetText.getMeasuredWidth();
    	spentText.measure(0, 0);
    	int spentWidth = spentText.getMeasuredWidth();
    	
    	int budgetGraphWidth = 0;
    	int spentGraphWidth = 0;
    	if(budget > expenses){
    	    budgetGraphWidth = totalWidth - budgetWidth;
    	    spentGraphWidth = (int) (budgetGraphWidth * (expenses / budget));
    	    
    	}
    	else if(budget < expenses){	//Over-spent
    	    spentGraphWidth = totalWidth - spentWidth;
    	    budgetGraphWidth = (int) (spentGraphWidth * (budget / expenses));
    	    spentText.setTextColor(Color.RED);
    	}
    	View budgetGraph = (View)v.findViewById(R.id.budget_monthly_expense_budget_graph);
    	View spentGraph = (View)v.findViewById(R.id.budget_monthly_expense_spent_graph);
    	
    	LayoutParams params1 = (LayoutParams) budgetGraph.getLayoutParams();
    	LayoutParams params2 = (LayoutParams) spentGraph.getLayoutParams();
    	
    	params1.width = budgetGraphWidth - 10; //add margin
    	params2.width = spentGraphWidth - 10; //add margin
    	budgetGraph.setLayoutParams(params1);
    	spentGraph.setLayoutParams(params2);
    }
}
