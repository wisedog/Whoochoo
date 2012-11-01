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
import net.wisedog.android.whooing.network.ThreadRestAPI;
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
    
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_MAIN){
                    //Monthly Budget
                    JSONObject obj = (JSONObject)msg.obj;
                    TextView monthlyExpenseText = (TextView)mActivity.findViewById(R.id.budget_monthly_expense);
                    TextView labelAssets = (TextView)mActivity.findViewById(R.id.label_asset);
                    Typeface typeface = Typeface.createFromAsset(mActivity.getAssets(), "fonts/Roboto-Light.ttf");
                    monthlyExpenseText.setTypeface(typeface, Typeface.BOLD);
                    labelAssets.setTypeface(typeface);
                    try {
                        JSONObject total = obj.getJSONObject("budget").getJSONObject("aggregate")
                                .getJSONObject("total");
                        
                        int budget = total.getInt("budget");
                        int expenses = total.getInt("money");
                        if(budget < expenses){
                            monthlyExpenseText.setTextColor(Color.RED);
                        }
                        //monthlyBudgetText.setText("예산:"+budget);
                        monthlyExpenseText.setText(expenses+" / " + budget);
                        
                    } catch (JSONException e) {
                        setErrorHandler("통신 오류! Err-MAIN1");
                        e.printStackTrace();
                    }
                    
                    //Balance
                    TextView currentBalance = (TextView)mActivity.findViewById(R.id.balance_num);
                    TextView inoutBalance = (TextView)mActivity.findViewById(R.id.inout_num);
                    currentBalance.setTypeface(typeface);
                    inoutBalance.setTypeface(typeface);
                    try{
                        JSONObject obj1 = obj.getJSONObject("mountain").getJSONObject("aggregate");
                        DecimalFormat df = new DecimalFormat("#,##0");
                        currentBalance.setText(df.format(obj1.getLong("capital")));
                        
                        inoutBalance.setText(df.format(obj1.getLong("liabilities")));                       
                        
                    }catch(JSONException e){
                        setErrorHandler("통신 오류! Err-MAIN2");
                        e.printStackTrace();
                    }catch(IllegalArgumentException e){
                        e.printStackTrace();
                    }
                    
                    TextView creditCard = (TextView)mActivity.findViewById(R.id.text_credit_card);
                    creditCard.setTypeface(typeface);
                    try {
                        JSONArray array = obj.getJSONObject("bill").getJSONObject("aggregate")
                                .getJSONArray("accounts");
/*                      JSONObject total = obj.getJSONObject("bill").getJSONObject("aggregate")
                                .getJSONObject("total");*/
                        
                        String fullString = "";
                        for(int i = 0; i< array.length(); i++){
                            JSONObject object =(JSONObject) array.get(i);
                            String accountName = object.getString("account_id");
                            long money = object.getLong("money");
                            fullString = fullString + accountName + " : " + money+ "\n";
                        }
                        creditCard.setText(fullString);
                        
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
