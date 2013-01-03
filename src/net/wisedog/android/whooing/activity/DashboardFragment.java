package net.wisedog.android.whooing.activity;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingAuth;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.OnBudgetChangeListener;
import net.wisedog.android.whooing.engine.DataRepository.OnMountainChangeListener;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.engine.MainProcessor;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
/**
 * 첫 페이지(대쉬보드)Fragment
 * @author Wisedog(me@wisedog.net)
 * */
public class DashboardFragment extends SherlockFragment implements OnMountainChangeListener, OnBudgetChangeListener{
    private static final String KEY_TAB_NUM = "key.tab.num";
	
	public static DashboardFragment newInstance(String text) {
        DashboardFragment fragment = new DashboardFragment();
        
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(KEY_TAB_NUM, text);
        fragment.setArguments(args);

        
        return fragment;
    }

    private Activity mActivity;
    private ProgressDialog dialog;
    private boolean isFirstCalling = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.whooing_main2, null);
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        DataRepository repository = DataRepository.getInstance();
        repository.registerObserver(this, DataRepository.MOUNTAIN_MODE);
        repository.registerObserver(this, DataRepository.BUDGET_MODE);
        if(repository.getMtValue() != null){
            showMountainValue(repository.getMtValue());
        }
        if(repository.getBudgetValue() != null){
            showBudgetValue(repository.getBudgetValue());
        }
        
       /* // TODO 전월대비를 넣어보자
        if(isFirstCalling == true || Define.NEED_TO_REFRESH == true){
            // For Debug
               Define.REAL_TOKEN = "13165741351c21b2088c12706c1acd1d63cf7b49";
               Define.PIN = "992505";
               Define.TOKEN_SECRET = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
               Define.USER_ID = "8955";
               Define.APP_SECTION = "s10550";
               if (mActivity != null) {
                   mActivity.deleteDatabase(AccountsDbOpenHelper.DATABASE_NAME);
               }
               if (Define.PIN == null || Define.REAL_TOKEN == null) {
                   ThreadHandshake thread = new ThreadHandshake(mHandler, mActivity, false);
                   thread.start();
                   dialog = ProgressDialog.show(mActivity, "", getString(R.string.authorizing), true);
                   dialog.setCancelable(true);
               } else {
                   GeneralProcessor generalProcessor = new GeneralProcessor(mActivity);
                   if (generalProcessor.checkingAccountsInfo()) {
                       MainProcessor mainProcessor = new MainProcessor(mActivity);
                       mainProcessor.refreshAll();
                   } else {
                       Toast.makeText(mActivity, "Getting accounts information", Toast.LENGTH_LONG).show();
                       ThreadRestAPI thread = new ThreadRestAPI(mGeneralHandler, mActivity,
                               Define.API_GET_ACCOUNTS);
                       thread.start();
                   }
               }
               isFirstCalling = false;
               Define.NEED_TO_REFRESH = false;
               
               DataRepository repository = DataRepository.getInstance();
               repository.registerObserver(this, DataRepository.MOUNTAIN_MODE);
           }*/
        super.onResume();
    }
    

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView() {
        DataRepository repository = DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.MOUNTAIN_MODE);
        repository.removeObserver(this, DataRepository.BUDGET_MODE);
        
        super.onDestroyView();
    }

    public void refreshAll() {
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, mActivity, Define.API_GET_MAIN);
        thread.start();
    }
    
    Handler mGeneralHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_FAIL){
                dialog.dismiss();
                Toast.makeText(mActivity, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
            }
            else if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_ACCOUNTS){
                    JSONObject result = (JSONObject)msg.obj;
                    try {
                        JSONObject objResult = result.getJSONObject("results");
                        GeneralProcessor general = new GeneralProcessor(mActivity);
                        general.fillAccountsTable(objResult);
                        Toast.makeText(mActivity, "Complete", Toast.LENGTH_LONG).show();
                        MainProcessor mainProcessor = new MainProcessor(mActivity);
                      mainProcessor.refreshAll();
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                        Toast.makeText(mActivity, "Exception", Toast.LENGTH_LONG).show();
                    }
                }
            }
            super.handleMessage(msg);
        }
        
    };
    
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_FAIL){
                dialog.dismiss();
                Toast.makeText(mActivity, getString(R.string.msg_auth_fail), Toast.LENGTH_LONG).show();
            }
            else if(msg.what == Define.MSG_REQ_AUTH){
                Intent intent = new Intent(mActivity, WhooingAuth.class);
                intent.putExtra("first_token", (String)msg.obj);
                
                startActivityForResult(intent, Define.REQUEST_AUTH);
            }
            else if(msg.what == Define.MSG_AUTH_DONE){
                ThreadRestAPI thread = new ThreadRestAPI(mHandler, mActivity, Define.API_GET_SECTIONS);
                thread.start();
            }
            else if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_SECTIONS){
                    JSONObject result = (JSONObject)msg.obj;                    
                    try {
                        JSONArray array = result.getJSONArray("results");
                        JSONObject obj = (JSONObject) array.get(0);
                        String section = obj.getString("section_id");
                        if(section != null){
                            Define.APP_SECTION = section;
                            Log.d("whooing", "APP SECTION:"+ Define.APP_SECTION);
                            SharedPreferences prefs = mActivity.getSharedPreferences(Define.SHARED_PREFERENCE,
                                    Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(Define.KEY_SHARED_SECTION_ID, section);
                            editor.commit();
                            dialog.dismiss();
                            Toast.makeText(mActivity, getString(R.string.msg_auth_success),
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            throw new JSONException("Error in getting section id");
                        }
                    } catch (JSONException e) {
                        setErrorHandler("통신 오류! Err-SCT1");
                        e.printStackTrace();
                    }
                }
                else if(msg.arg1 == Define.API_GET_BUDGET){
                    TextView monthlyBudgetText = (TextView)mActivity.findViewById(R.id.label_monthly);
                    TextView monthlyExpenseText = (TextView)mActivity.findViewById(R.id.budget_monthly_expense);
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        int budget = obj.getInt("budget");
                        int expenses = obj.getInt("money");
                        if(budget < expenses){
                            monthlyExpenseText.setTextColor(Color.RED);
                        }
                        monthlyBudgetText.setText("예산:"+budget);
                        monthlyExpenseText.setText("지출 : "+expenses);
                        
                    } catch (JSONException e) {
                        setErrorHandler("통신 오류! Err-BDG1");
                    }
                    
                }
                else if(msg.arg1 == Define.API_GET_BALANCE){
                    TextView currentBalance = (TextView)mActivity.findViewById(R.id.balance_num);
                    TextView inoutBalance = (TextView)mActivity.findViewById(R.id.doubt_num);
                    JSONObject obj = (JSONObject)msg.obj;
                    try{
                        JSONObject obj1 = obj.getJSONObject("assets");
                        DecimalFormat df = new DecimalFormat("#,##0");
                        currentBalance.setText(df.format(obj1.getLong("total")));
                        
                        JSONObject obj2 = obj.getJSONObject("liabilities");
                        inoutBalance.setText(df.format(obj2.getLong("total")));                     
                        
                    }catch(JSONException e){
                        setErrorHandler("통신 오류! Err-BNC1");
                        e.printStackTrace();
                    }catch(IllegalArgumentException e){
                        setErrorHandler("통신 오류! Err-BNC2");
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    
    public void setErrorHandler(String errorMsg){
        if(dialog != null){
            dialog.dismiss();
        }	
        Toast.makeText(mActivity, errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        
        if(Define.NEED_TO_REFRESH == false && bundle != null){
            TextView textView = (TextView)mActivity.findViewById(R.id.balance_num);
            textView.setText(bundle.getString("assets_value"));
            textView = (TextView)mActivity.findViewById(R.id.doubt_num);
            textView.setText(bundle.getString("doubt_value"));
            isFirstCalling = bundle.getBoolean("first_calling");
        }
        super.onActivityCreated(bundle);
    }
    
    /**
     * Mountain 값을 보여준다. DashBoard 1,3 번째
     * @param	obj		Data formatted in JSON
     * */
    private void showMountainValue(JSONObject obj){
        TextView currentBalance = (TextView)mActivity.findViewById(R.id.balance_num);
        TextView doubtValue = (TextView)mActivity.findViewById(R.id.doubt_num);
        Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Roboto-Light.ttf");
        if(currentBalance == null || doubtValue == null){
        	return;
        }
        currentBalance.setTypeface(typeface);
        doubtValue.setTypeface(typeface);
        JSONObject objResult = null;
        try{
            objResult = obj.getJSONObject("results");
        }
        catch(JSONException e){
            e.printStackTrace();
            return;
        }
        
        //Set Assets, Doubt value
        try{
            JSONObject objAggregate = objResult.getJSONObject("aggregate");
            
            DecimalFormat df = new DecimalFormat("#,##0");  //TODO apply Localization
            currentBalance.setText(df.format(objAggregate.getDouble("capital")));
            doubtValue.setText(df.format(objAggregate.getDouble("liabilities")));                       
            
        }catch(JSONException e){
            setErrorHandler("통신 오류! Err-MAIN2");
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        
        //set 전월대비
        try {
            JSONArray objArray = objResult.getJSONArray("rows");
            JSONObject last = (JSONObject) objArray.get(objArray.length() - 1);
            JSONObject preLast = (JSONObject) objArray.get(objArray.length() - 2);
            double lastCapital = last.getDouble("capital");
            double preLastCapital = preLast.getDouble("capital");
            int diff = (int)(lastCapital - preLastCapital);
            TextView compareValue = (TextView)mActivity.findViewById(R.id.text_compare_premonth_value);
            compareValue.setTypeface(typeface);
            compareValue.setText(String.valueOf(diff));
            
        } catch (JSONException e) {
            setErrorHandler("통신 오류! Err-MAIN2");
            e.printStackTrace();
        }
    }
    
    /**
     * Budget값을 보여준다. 2번째.
     * @param budgetValue	data formatted in JSON
     */
    private void showBudgetValue(JSONObject budgetValue) {
        TextView monthlyExpenseText = (TextView)mActivity.findViewById(R.id.budget_monthly_expense);
        if(monthlyExpenseText == null){
            return;
        }
        
        try {
            JSONObject obj = (JSONObject) budgetValue.getJSONObject("results").getJSONArray("rows").get(0);
            JSONObject totalObj = obj.getJSONObject("total");
            double budget = totalObj.getDouble("budget");
            double expenses = totalObj.getDouble("money");
            if(budget < expenses){
                monthlyExpenseText.setTextColor(Color.RED);
            }
            monthlyExpenseText.setText(budget + " / " + expenses);
            
        } catch (JSONException e) {
            setErrorHandler("통신 오류! Err-BDG1");
        }
        
    }

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnMountainChangeListener#onMountainUpdate(org.json.JSONObject)
     */
    public void onMountainUpdate(JSONObject obj) {
        //여기서 Dashboard의 Asset, Doubt, 전월대비 설정한다. 
        showMountainValue(obj);
    }

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnBudgetChangeListener#onBudgetUpdate(org.json.JSONObject)
     */
    public void onBudgetUpdate(JSONObject obj) {
       showBudgetValue(obj);
        
    }
}
