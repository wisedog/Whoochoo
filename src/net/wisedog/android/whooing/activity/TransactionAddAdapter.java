/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author newmoni
 *
 */
public class TransactionAddAdapter extends BaseAdapter {
    private ArrayList<TransactionItem> mDataArray;
    private LayoutInflater mInflater;
    
    public TransactionAddAdapter(Context context, ArrayList<TransactionItem> dataArray){
        mDataArray = dataArray;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mDataArray.size();
    }

    public Object getItem(int position) {
        return "AAAA";
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
       
       GeneralProcessor generic = null;
       if(parent != null){
    	   generic = new GeneralProcessor(parent.getContext());
       }
       
       TransactionItem item = mDataArray.get(pos);
       AccountsEntity entityLeft = generic.findAccountById(item.LeftAccount);
       AccountsEntity entityRight = generic.findAccountById(item.RightAccount);
       
       
       textDate.setText(item.Date);
       textItem.setText(item.Item);
       textAmount.setText(item.Amount);
       textLeft.setText(entityLeft.title);
       textRight.setText(entityRight.title);
       
        return convertView;
    }

}
