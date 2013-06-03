package net.wisedog.android.whooing.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.api.Entries;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.db.SmsDbOpenHelper;
import net.wisedog.android.whooing.db.SmsInfoEntity;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import net.wisedog.android.whooing.widget.WiButton;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SmsConfirmAdapter extends BaseAdapter {
	
	private ArrayList<SmsInfoEntity> mDataArray;
	private ArrayList<AccountsEntity> mAccountDataArray;
	private ArrayList<ExpEntity> mExpAccountArray;
	private ArrayList<String> mExpAccountTitleArray;
	private LayoutInflater mInflater;
	private Context mContext;
	
	
	private class ExpEntity{
	    private String account_id;
	    private String title;
	    public ExpEntity(String account_id, String title){
	        this.account_id = account_id;
	        this.title = title;
	    }
	    public String getAccountId(){
	        return account_id;
	    }
	}

	public SmsConfirmAdapter(Context context, ArrayList<SmsInfoEntity> array, 
			ArrayList<AccountsEntity> accountArray){
		mDataArray = array;
		mAccountDataArray = accountArray;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		mExpAccountArray = new ArrayList<ExpEntity>();
		mExpAccountTitleArray = new ArrayList<String>();
		mExpAccountArray.add(new ExpEntity("",""));
		mExpAccountTitleArray.add(context.getString(R.string.sms_set_expense));
		
		for(AccountsEntity entity : accountArray){
		    if(entity.accountType.compareTo("expenses") == 0 && 
		            entity.type.compareTo("group") != 0 &&
		            entity.close_date > WhooingCalendar.getTodayYYYYMMDDint()){
		        ExpEntity expEntity = new ExpEntity(entity.account_id, entity.title);
		        mExpAccountArray.add(expEntity);
		        mExpAccountTitleArray.add(entity.title);
		    }
		}
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
    	   WiTextView textAmount = (WiTextView)convertView.findViewById(R.id.sms_list_expense_amount);
    	   String amount = String.valueOf(entity.amount);
    	   if(amount != null){
    	       textAmount.setText(amount);
    	   }
    	   
    	   WiTextView textDate = (WiTextView)convertView.findViewById(R.id.sms_list_expense_date);
    	   String date = WhooingCalendar.getLocaleDateInt(entity.use_date);
    	   if(date != null){
    	       textDate.setText(date);
    	   }
    	   
    	   WiTextView textItem = (WiTextView)convertView.findViewById(R.id.sms_list_expense_item);
           if(entity.item != null){
               textItem.setText(entity.item);
           }
    	   
           // Setting Spinner
    	   final Spinner spinner = (Spinner)convertView.findViewById(R.id.sms_list_spinner_expense);
    	   String[] expTitleArray = new String[mExpAccountTitleArray.size()];
    	   expTitleArray = mExpAccountTitleArray.toArray(expTitleArray);
    	   ArrayAdapter<String> expAccountAdapter = new ArrayAdapter<String>(mContext,
                   android.R.layout.select_dialog_item, expTitleArray);
    	   spinner.setAdapter(expAccountAdapter);
    	   spinner.setSelection(0);
    	   spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int spinnerPos, long id) {
                if(spinnerPos == 0){
                    return;
                }
                
                SmsInfoEntity entity = mDataArray.get(pos);
                entity.targetAccountId = mExpAccountArray.get(spinnerPos).getAccountId();
                mDataArray.set(pos, entity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ; //Do nothing
                
            }
        });
    	   
    	   ImageButton discardBtn = (ImageButton)convertView.findViewById(R.id.sms_list_discard);
    	   discardBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    SmsInfoEntity entity  = mDataArray.get(pos);
			    SmsDbOpenHelper db = new SmsDbOpenHelper(mContext);
			    db.deleteItem(entity.id);
				mDataArray.remove(pos);
				
				SmsConfirmAdapter.this.notifyDataSetChanged();
			}
		});
    	   
    	   WiButton okBtn = (WiButton)convertView.findViewById(R.id.sms_list_ok);
    	   okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    if(spinner.getSelectedItemPosition() == 0){
			        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                    v.startAnimation(shake);
                    Toast.makeText(mContext, mContext.getString(R.string.sms_toast_no_set_expense), Toast.LENGTH_LONG).show();
			        return;
			    }
			    final ProgressDialog progressDialog = new ProgressDialog(mContext);
			    final SmsInfoEntity entity  = mDataArray.get(pos);
			    AsyncTask<Void, Integer, JSONObject> task = new AsyncTask<Void, Integer, JSONObject>(){

			        
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog.setIndeterminate(true);
                        progressDialog.setTitle(mContext.getString(R.string.text_loading));
                        progressDialog.show();                        
                    }
                    @Override
                    protected JSONObject doInBackground(Void... params) {
                        GeneralProcessor processor = new GeneralProcessor(mContext);
                        ExpEntity expEntity = mExpAccountArray.get(spinner.getSelectedItemPosition());
                        AccountsEntity leftAccount = processor.findAccountById(expEntity.account_id);
                        AccountsEntity rightAccount = processor.findAccountById(entity.account_id);
                        
                        final Bundle bundle = new Bundle();
                        bundle.putInt("entry_date", entity.use_date);
                        bundle.putParcelable("l_account", leftAccount);
                        bundle.putParcelable("r_account", rightAccount);
                        bundle.putString("item", entity.item);
                        bundle.putDouble("money", entity.amount);
                        bundle.putString("memo", "");
                        
                        Entries entryInsert = new Entries();
                        JSONObject result = entryInsert.insertEntry(Define.APP_SECTION, Define.APP_ID, 
                                Define.REAL_TOKEN, Define.APP_SECRET, Define.TOKEN_SECRET, bundle);
                        return result;
                    }
                    @Override
                    protected void onPostExecute(JSONObject result) {
                        int resultCode = 200;
                        try{
                            resultCode = result.getInt("code");
                        }catch(JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(mContext, "Error-SMS-02", Toast.LENGTH_LONG).show();
                            return;
                        }
                        
                        if(resultCode != 200){
                            Toast.makeText(mContext, "Error-SMS-03", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            return;
                        }
                        
                        SmsDbOpenHelper db = new SmsDbOpenHelper(mContext);
                        db.deleteItem(entity.id);
                        mDataArray.remove(pos);
                        progressDialog.dismiss();
                        SmsConfirmAdapter.this.notifyDataSetChanged();
                    }			        
			    };
			    task.execute();
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
