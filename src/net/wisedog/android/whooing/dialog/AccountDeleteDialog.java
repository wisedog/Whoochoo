package net.wisedog.android.whooing.dialog;

import net.wisedog.android.whooing.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AccountDeleteDialog extends SherlockDialogFragment {
    
    static public AccountDeleteDialog newInstance(String accountId, String accountType) {
        AccountDeleteDialog f = new AccountDeleteDialog();
        
        //Setting bundle
        Bundle args = new Bundle();
        args.putString("account_id", accountId);
        args.putString("account_type", accountType);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_setting_del_account, container, true);
        return v;//super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
       
    }    
    

}
