/**
 * 
 */
package net.wisedog.android.whooing.ui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.BillMonthlyItem;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import android.content.Context;
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
    
    
    /*
     {"total":200000,"date":201302,
     * "accounts":[
     * {"end_use_date":20130131,"money":200000,"pay_date":25,
     * "account_id":"x21","start_use_date":20130101},
     * {"end_use_date":20130131,"money":0,"pay_date":28,"account_id":"x76",
     * "start_use_date":20130101}
     * ]}
     * */
    public void setupMonthlyCard(JSONObject objRowItem) throws JSONException{
    	inflate(mContext, R.layout.bill_item_month, this);
    	if(objRowItem == null){
    		return;
    	}
    	
    	double total = objRowItem.getDouble("total");
    	TextView totalAmount = (TextView)findViewById(R.id.bill_label_total_value);
    	if(totalAmount != null){
    		totalAmount.setText(String.valueOf(total));
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
    	
    	JSONArray array = objRowItem.getJSONArray("accounts");
    	int length = array.length();
    	GeneralProcessor generic = new GeneralProcessor(mContext);
    	LinearLayout baseLayout = (LinearLayout)findViewById(R.id.bill_month_card_item);
    	for(int i = 0; i < length; i++){
    		JSONObject entity = (JSONObject) array.get(i);
    		AccountsEntity accountEntity = generic.findAccountById(entity.getString("account_id"));
    		
    		BillMonthlyItem item = new BillMonthlyItem(entity.getInt("start_use_date"), 
    				entity.getInt("end_use_date"), accountEntity.title, 
    				entity.getInt("pay_date"), entity.getDouble("money"));
           BillMonthlyCardItem itemCard = new BillMonthlyCardItem(mContext);
           itemCard.setup(mContext, item);
           baseLayout.addView(itemCard);
    	}

    	this.requestLayout();
    }
}
