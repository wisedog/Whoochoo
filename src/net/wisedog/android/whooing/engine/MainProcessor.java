/**
 * 
 */
package net.wisedog.android.whooing.engine;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.ui.NavigationBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class MainProcessor {
    private Activity mActivity;

    public MainProcessor(Activity activity){
        mActivity = activity;
    }
    
    public void refreshAll(){
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, mActivity, Define.API_GET_MAIN);
        thread.start();
    }
    
    //TODO [Whooing] Add handler for various error status
    public boolean checkValidResult(JSONObject obj){
        JSONObject result1 = obj;
        try{
            int code = result1.getInt("code");
            if(code != 200){
                return false;
            }
           final int rest = result1.getInt("rest_of_api");
          /* mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    NavigationBar navBar = (NavigationBar)mActivity.findViewById(R.id.nav_bar);
                    navBar.setRestApi(rest);
                }
            } );*/
        }
        catch(JSONException e){
            return false;
        }
        return false;
    }
    
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_MAIN){
                    //Monthly Budget
                    JSONObject obj = (JSONObject)msg.obj;
                    checkValidResult(obj);
                    JSONObject objResult = null;
                    try{
                        objResult = obj.getJSONObject("results");
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                        return;
                    }
                    
                    TextView monthlyExpenseText = (TextView)mActivity.findViewById(R.id.budget_monthly_expense);
                    if(monthlyExpenseText == null){
                        return; //When leave this fragment
                    }
                    TextView labelAssets = (TextView)mActivity.findViewById(R.id.label_asset);
                    Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Roboto-Light.ttf");
                    monthlyExpenseText.setTypeface(typeface, Typeface.BOLD);
                    labelAssets.setTypeface(typeface);
                    try {
                        JSONObject total = objResult.getJSONObject("budget").getJSONObject("aggregate")
                                .getJSONObject("total");
                        
                        int budget = total.getInt("budget");
                        int expenses = total.getInt("money");
                        if(budget < expenses){
                            monthlyExpenseText.setTextColor(Color.RED);
                        }
                        monthlyExpenseText.setText(expenses+" / " + budget);
                        
                    } catch (JSONException e) {
                        setErrorHandler("통신 오류! Err-MAIN1");
                        e.printStackTrace();
                    }
                    
                    //Balance
                    TextView currentBalance = (TextView)mActivity.findViewById(R.id.balance_num);
                    TextView inoutBalance = (TextView)mActivity.findViewById(R.id.doubt_num);
                    currentBalance.setTypeface(typeface);
                    inoutBalance.setTypeface(typeface);
                    try{
                        JSONObject obj1 = objResult.getJSONObject("mountain").getJSONObject("aggregate");
                        DecimalFormat df = new DecimalFormat("#,##0");
                        currentBalance.setText(df.format(obj1.getLong("capital")));
                        
                        inoutBalance.setText(df.format(obj1.getLong("liabilities")));                       
                        
                    }catch(JSONException e){
                        setErrorHandler("통신 오류! Err-MAIN2");
                        e.printStackTrace();
                    }catch(IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    
                    try {
                        JSONArray array = objResult.getJSONObject("bill").getJSONObject("aggregate")
                                .getJSONArray("accounts");
                        int[] creditNameArray = new int[]{R.id.label_credit1, R.id.label_credit2};
                        int[] creditAmountArray = new int[]{R.id.text_credit_card, R.id.text_credit_card1};
                        GeneralProcessor general = new GeneralProcessor(mActivity);
                        for(int i = 0; i< array.length(); i++){
                            JSONObject object =(JSONObject) array.get(i);
                            String accountName = object.getString("account_id");
                            long money = object.getLong("money");
                            TextView creditNameLabel = (TextView)mActivity.findViewById(creditNameArray[i]);
                            AccountsEntity entity = general.findAccountById(accountName);
                            creditNameLabel.setText(entity.title);
                            creditNameLabel.setTypeface(typeface);
                            TextView creditAmountLabel = (TextView)mActivity.findViewById(creditAmountArray[i]);
                            creditAmountLabel.setText(Long.toString(money));
                            creditAmountLabel.setTypeface(typeface);
                        }
                        
                    } catch (JSONException e) {
                        setErrorHandler("통신 오류! Err-MAIN3");
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    

    public void setErrorHandler(String errorMsg){
        /*if(dialog != null){
            dialog.dismiss();
        }*/
        Toast.makeText(mActivity, errorMsg, Toast.LENGTH_LONG).show();
    }
}
