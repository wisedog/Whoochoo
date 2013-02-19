/**
 * 
 */
package net.wisedog.android.whooing.adapter;

import java.util.ArrayList;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.TransactionItem;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.utils.WhooingCurrency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class TransactionAddAdapter extends BaseAdapter {
    private ArrayList<TransactionItem> mDataArray;
    private LayoutInflater mInflater;
	private Context mContext;
    
    public TransactionAddAdapter(Context context, ArrayList<TransactionItem> dataArray){
        mDataArray = dataArray;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    public int getCount() {
        return mDataArray.size();
    }

    public Object getItem(int position) {
        return mDataArray.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public void clearAdapter()
    {
    	mDataArray.clear();
        notifyDataSetChanged();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
       final int pos = position;
       if(convertView == null){
           convertView = mInflater.inflate(R.layout.transaction_list_item, parent, false);
       }
       TextView textDate = (TextView)convertView.findViewById(R.id.transaction_listitem_date);
       TextView textItem = (TextView)convertView.findViewById(R.id.transaction_listitem_item);
       TextView textAmount = (TextView)convertView.findViewById(R.id.transaction_listitem_amount);
       TextView textLeft = (TextView)convertView.findViewById(R.id.transaction_listitem_left);
       TextView textRight = (TextView)convertView.findViewById(R.id.transaction_listitem_right);
       TextView textHead = (TextView)convertView.findViewById(R.id.transaction_listitem_head);
       textHead.setTypeface(Define.ROBOFONT);
       
       GeneralProcessor generic = null;
       if(parent != null){
    	   generic = new GeneralProcessor(parent.getContext());
       }
       
       TransactionItem item = mDataArray.get(pos);
       AccountsEntity entityLeft = generic.findAccountById(item.l_account_id);
       AccountsEntity entityRight = generic.findAccountById(item.r_account_id);
       
       if(entityRight != null && entityLeft != null){
    	   if(entityRight.accountType.equals("income")){
    		   textHead.setText(mContext.getString(R.string.text_income));
    	   }
    	   else if(entityLeft.accountType.equals("expenses")){
    		   textHead.setText(mContext.getString(R.string.text_expenses));
    	   }
    	   else{
    		   textHead.setText(mContext.getString(R.string.text_etc));
    	   }
       }
       textDate.setText(item.date.subSequence(0, 8));
       textDate.setTypeface(Define.ROBOFONT);
       textItem.setText(item.item);
       textItem.setTypeface(Define.ROBOFONT);
       textAmount.setText(WhooingCurrency.getFormattedValue(item.money));
       textAmount.setTypeface(Define.ROBOFONT);
       textLeft.setText(entityLeft.title);
       textLeft.setTypeface(Define.ROBOFONT);
       textRight.setText(entityRight.title);
       textRight.setTypeface(Define.ROBOFONT);
       
        return convertView;
    }

    /**
     * @param dataArray     set data array
     */
    public void setData(ArrayList<TransactionItem> dataArray) {
        mDataArray = dataArray;
    }

}
