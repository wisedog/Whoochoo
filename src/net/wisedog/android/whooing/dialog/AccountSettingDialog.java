package net.wisedog.android.whooing.dialog;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AccountSettingDialog extends SherlockDialogFragment {
    public interface AccountSettingListener {
        void onFinishingSetting(AccountsEntity entity);
    }
    
    static public AccountSettingDialog newInstance(String accountType){
        AccountSettingDialog f = new AccountSettingDialog();

        // Setting bundle
        Bundle args = new Bundle();
        args.putString("account_type", accountType);      
        f.setArguments(args);
        return f;
    } 

    static public AccountSettingDialog newInstance(AccountsEntity entity){
        AccountSettingDialog f = new AccountSettingDialog();

        // Setting bundle
        Bundle args = new Bundle();
        args.putParcelable("account_entity", entity);        
        f.setArguments(args);
        return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_modify, container, true);
        AccountsEntity entity = getArguments().getParcelable("account_entity");
        String accountType = getArguments().getString("account_type");
        
        if(accountType == null && entity == null){  //abnormal condition
            return v;
        }
        
        if(accountType != null){
            setupUi(v, accountType);
        }else {
            setupUi(v, entity.accountType);
        }
        
        if(entity == null){ //Modify
            
        }else if(accountType == null)   //Newly added
        {
            
        }
        
        //setupUi(v, accountType);
        return v;
    }

    private void setupUi(View v, String accountType) {
        Toast.makeText(this.getSherlockActivity(), accountType, Toast.LENGTH_SHORT).show();
        if(accountType.compareTo("assets") == 0){
            
        }
        else if(accountType.compareTo("expenses") == 0){
            
        }
        else if(accountType.compareTo("capital") == 0){
            
        }
        else if(accountType.compareTo("liabilities") == 0){
            
        }
        else if(accountType.compareTo("income") == 0){
            
        }
        
    }
}
