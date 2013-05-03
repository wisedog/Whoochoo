package net.wisedog.android.whooing.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.activity.AccountsSetting;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.widget.WiButton;
import net.wisedog.android.whooing.widget.WiTextView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AccountDeleteConfirmDialog extends SherlockDialogFragment {
    public interface AccountDeleteListener {
        void onFinishingDeleting();
    }
    
    private boolean isExistEntries = false;
    

    static public AccountDeleteConfirmDialog newInstance(String accountId, String accountType) {
        AccountDeleteConfirmDialog f = new AccountDeleteConfirmDialog();
        
        //Setting bundle
        Bundle args = new Bundle();
        args.putString("account_id", accountId);
        args.putString("account_type", accountType);
        f.setArguments(args);
        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
       
    }    
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_setting_del_account, container, true);
        v.findViewById(R.id.account_setting_del_layout).setVisibility(View.INVISIBLE);
        
        WiButton confirmBtn = (WiButton) v.findViewById(R.id.account_setting_del_btn_confirm);
        final RadioGroup group = (RadioGroup)v.findViewById(R.id.account_setting_del_radio_group);
        
        if(confirmBtn != null){
            confirmBtn.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    boolean isDelete = false;
                    if(isExistEntries){
                        if(group.getCheckedRadioButtonId() == R.id.account_setting_del_radio1){ //Deactivate
                            isDelete = false;
                        }else{  //Delete case
                            isDelete = true;
                        }
                        //Check Radio buttons                
                    }
                    else{   //Delete case
                        isDelete = true;
                    }
                    
                    if(isDelete){
                        Bundle b = new Bundle();
                        b.putString("account_type", getArguments().getString("account_type"));
                        b.putString("account_id", getArguments().getString("account_id"));
                        
                        ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_DELETE_ACCOUNTS, b);
                        thread.start();
                    }
                    else{   //Deactivate case
                        Bundle b = new Bundle();
                        b.putString("account_type", getArguments().getString("account_type"));
                        b.putString("account_id", getArguments().getString("account_id"));
                        b.putString("is_close", "y");
                        
                        ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_PUT_ACCOUNTS, b);
                        thread.start();
                    }
                }
            });
        }
        
        WiButton cancelBtn = (WiButton) v.findViewById(R.id.account_setting_del_btn_cancel);
        if(cancelBtn != null){
            this.dismiss();
        }
        
     // Check transactions
        Bundle b = new Bundle();
        b.putString("account_type", getArguments().getString("account_type"));
        b.putString("account_id", getArguments().getString("account_id"));
        ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_GET_ACCOUNT_EXISTS_ENTRIES, b);
        thread.start();
        
        return v;
    }
    

    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                JSONObject obj = (JSONObject)msg.obj;
                int result = 0;
                try {
                    result = obj.getInt("code");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                    //TODO Toast
                }
                if(result != Define.RESULT_OK){
                    //TODO Toast
                    return;
                }
                if(msg.arg1 == Define.API_GET_ACCOUNT_EXISTS_ENTRIES){
                    try {
                        JSONObject resultObj = obj.getJSONObject("results");
                        int count = resultObj.getInt("count");
                        String lastOne = resultObj.getString("last_one");
                        WiTextView loadingText = (WiTextView)getActivity().findViewById(R.id.account_setting_del_loading);
                        if(loadingText != null){
                            loadingText.setVisibility(View.GONE);
                        }
                        ProgressBar progress = (ProgressBar)getActivity().findViewById(R.id.account_setting_del_progress);
                        if(progress != null){
                            progress.setVisibility(View.GONE);
                        }
                        
                        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.account_setting_del_layout);
                        ll.setVisibility(View.VISIBLE);
                        WiTextView msgText = (WiTextView)getActivity().findViewById(R.id.account_setting_del_text_msg);
                        if(lastOne.compareTo("y") == 0){
                            //TODO change msg board that "not delete. only this account is in account type"
                            getActivity().findViewById(R.id.account_setting_del_btn_confirm).setEnabled(false);
                            getActivity().findViewById(R.id.account_setting_del_radio1).setVisibility(View.GONE);
                            getActivity().findViewById(R.id.account_setting_del_radio2).setVisibility(View.GONE);
                            return;
                        }
                        if(count == 0){
                            msgText.setText(getActivity().getString(R.string.account_setting_del_msg_really_delete));
                            getActivity().findViewById(R.id.account_setting_del_radio1).setVisibility(View.GONE);
                            getActivity().findViewById(R.id.account_setting_del_radio2).setVisibility(View.GONE);
                            isExistEntries = false;
                        }
                        else{
                            isExistEntries = true;
                        }                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }                        
                }
                //Deactivate cases
                else if(msg.arg1 == Define.API_PUT_ACCOUNTS){   
                    AccountsSetting activity = (AccountsSetting)getActivity();
                    activity.onFinishingDeleting();
                }
                else if(msg.arg1 == Define.API_DELETE_ACCOUNTS){
                    AccountsSetting activity = (AccountsSetting)getActivity();
                    activity.onFinishingDeleting();
                }
                super.handleMessage(msg);
            }
        }
    };
}
