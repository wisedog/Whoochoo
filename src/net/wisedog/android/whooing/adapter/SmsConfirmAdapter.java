package net.wisedog.android.whooing.adapter;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.db.SmsInfoEntity;
import net.wisedog.android.whooing.widget.WiButton;
import net.wisedog.android.whooing.widget.WiTextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

public class SmsConfirmAdapter extends BaseAdapter {
	
	private ArrayList<SmsInfoEntity> mDataArray;
	private ArrayList<AccountsEntity> mAccountDataArray;
	private LayoutInflater mInflater;
	private Context mContext;

	public SmsConfirmAdapter(Context context, ArrayList<SmsInfoEntity> array, 
			ArrayList<AccountsEntity> accountArray){
		mDataArray = array;
		mAccountDataArray = accountArray;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
	}

	@Override
	public int getCount() {
		int size = 0;
		if(mDataArray != null){
			size = mDataArray.size();
		}
		return size;
	}

	@Override
	public Object getItem(int position) {
		if(mDataArray == null){
			return null;			
		}
		return mDataArray.get(position);
	}

	@Override
	public long getItemId(int position) {
        return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
       final int pos = position;
       if(convertView == null){
           convertView = mInflater.inflate(R.layout.sms_confirm_listiem, parent, false);
       }
       SmsInfoEntity entity = mDataArray.get(pos);
       if(entity!= null){
    	   WiTextView textTitle = (WiTextView)convertView.findViewById(R.id.sms_list_account_title);
    	   String accountTitle = getAccountName(entity.account_id);
    	   if(accountTitle != null){
    		   textTitle.setText(accountTitle);
    	   }
    	   WiTextView textExpense = (WiTextView)convertView.findViewById(R.id.sms_list_expenses);
    	   textExpense.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 인터페이스로 받아와야할듯... 
				
			}
		});
    	   ImageButton discardBtn = (ImageButton)convertView.findViewById(R.id.sms_list_discard);
    	   discardBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDataArray.remove(pos);
				SmsConfirmAdapter.this.notifyDataSetChanged();				
			}
		});
    	   
    	   WiButton okBtn = (WiButton)convertView.findViewById(R.id.sms_list_ok);
    	   okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mDataArray.remove(pos);
				SmsConfirmAdapter.this.notifyDataSetChanged();				
			}
		});
    	   
       }
		return convertView;
	}
	
	public String getAccountName(String account_id){
		if(mAccountDataArray == null){
			return null;
		}
		for(AccountsEntity entity : mAccountDataArray){
			if(entity.account_id.compareTo(account_id) == 0 ){
				return entity.title;
			}
		}
		return null;
	}

}
