/**
 * 
 */
package net.wisedog.android.whooing.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.BillMonthlyItem;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BillMonthlyEntity extends RelativeLayout{

    private Context mContext;

    /**
     * @param context
     */
    public BillMonthlyEntity(Context context) {
        super(context);
        mContext = context;
    }
    
    /**
     * @param   objRowItem  
     * */
    public void setupMonthlyCard(JSONObject objRowItem) throws JSONException{
    	inflate(mContext, R.layout.bill_item_month, this);
    	if(objRowItem == null){
    		return;
    	}
    	
    	double total = objRowItem.getDouble("total");
    	TextView totalAmount = (TextView)findViewById(R.id.bill_label_total_value);
    	if(totalAmount != null){
    		totalAmount.setText(WhooingCurrency.getFormattedValue(total));
    	}
    	
    	int date = objRowItem.getInt("date");
    	TextView monthText = (TextView)findViewById(R.id.bill_label_month);
    	if(monthText != null){
    		String dateStr = String.valueOf(date);
    		String dateYear = dateStr.substring(0, 4);
    		String dateMonth = dateStr.substring(4, 6); 
    		monthText.setText(dateYear + " " + dateMonth);
    	}
    	
    	//TODO Bar graph
    	//1. R.id.bar_total_bill 의 width 측정
    	//2. itemCard.setup에 같이 넘겨주기
    	//기타 : credit_graph색 변경
    	
    	View barView = (View)findViewById(R.id.bar_total_bill);
    	int width = 0;
    	if(barView != null){
//    		barView.measure(0, 0);
    		width = barView.getMeasuredWidth();//TODO 고치자. 다 0으로 들어온다.  
    	}
    	
    	
    	JSONArray array = objRowItem.getJSONArray("accounts");
    	GeneralProcessor generic = new GeneralProcessor(mContext);
    	LinearLayout baseLayout = (LinearLayout)findViewById(R.id.bill_month_card_item);
    	ArrayList<BillMonthlyItem> creditArray = new ArrayList<BillMonthlyItem>();
    	
    	int length = array.length();
    	for(int i = 0; i < length; i++){
    		JSONObject entity = (JSONObject) array.get(i);
    		AccountsEntity accountEntity = generic.findAccountById(entity.getString("account_id"));
    		
    		BillMonthlyItem item = new BillMonthlyItem(entity.getInt("start_use_date"), 
    				entity.getInt("end_use_date"), accountEntity.title, 
    				entity.getInt("pay_date"), entity.getDouble("money"));
    		creditArray.add(item);
    		
    		//card payment info in the monthly credit card section
           BillMonthlyCardItem itemCard = new BillMonthlyCardItem(mContext);
           itemCard.setup(mContext, item, width, total);
           baseLayout.addView(itemCard);
    	}
    	
    	//아래의 결제일 및 결제액 정보에 대한 처리
    	TextView paymentDateText = (TextView)findViewById(R.id.bill_month_duedate);
    	double[] paymentDateArray = new double[31];
    	if(paymentDateText != null){
    		for(int i = 0; i < creditArray.size(); i ++){
    			BillMonthlyItem item = creditArray.get(i);
    			
    			double amount = paymentDateArray[item.paymentDate];
    			paymentDateArray[item.paymentDate] = amount + item.amount;
    		}
    	}
    	
    	String paymentInfo = "";
    	for(int i = 0; i < 31; i++){
    		Double d = paymentDateArray[i];
    		if(d > 0.0f){
    			paymentInfo = paymentInfo + i + " : " + WhooingCurrency.getFormattedValue(d) + "   ";
    		}
    	}
    	
    	paymentDateText.setText(paymentInfo);

    	this.requestLayout();
    }
}
