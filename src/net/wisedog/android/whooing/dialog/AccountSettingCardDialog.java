package net.wisedog.android.whooing.dialog;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class AccountSettingCardDialog extends SherlockDialogFragment {
    
    private static final Integer[] DATE_NUMBER = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
    private ArrayList<String> mAccountsTitleList = new ArrayList<String>();
    private ArrayList<String> mIdList = new ArrayList<String>();
    
    public interface AccountCardSettingListener {
        void onFinishingSetting(AccountsEntity entity);
    }
    
    static public AccountSettingCardDialog newInstance(ArrayList<AccountsEntity> list){
        AccountSettingCardDialog f = new AccountSettingCardDialog();

        // Setting bundle
        Bundle args = new Bundle();
        args.putParcelableArrayList("account_list", list);    
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
        View v = inflater.inflate(R.layout.account_setting_card, container, true);
        
        
        ArrayList<AccountsEntity> list = getArguments().getParcelableArrayList("account_list");
        mAccountsTitleList = new ArrayList<String>();
        for(int i = 0; i < list.size(); i++){
            AccountsEntity entity = list.get(i);
            if(entity != null && entity.accountType.compareTo("liabilities") == 0){
                mAccountsTitleList.add(entity.title);
                mIdList.add(entity.account_id);
            }
        }
        Spinner accountsSpinner = (Spinner) v
                .findViewById(R.id.account_setting_card_spinner_accounts);
        ArrayAdapter<String> accountsAdapter = new ArrayAdapter<String>(
                getSherlockActivity(), android.R.layout.select_dialog_item, mAccountsTitleList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }
        };
        accountsSpinner.setAdapter(accountsAdapter);
        accountsSpinner.setSelection(0);

        Spinner dateSpinner = (Spinner) v.findViewById(R.id.account_setting_card_spinner_date);
        ArrayAdapter<Integer> dateAdapter = new ArrayAdapter<Integer>(
                getSherlockActivity(), android.R.layout.select_dialog_item, DATE_NUMBER) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }
        };
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setSelection(0);
        
        Spinner periodSpinner = (Spinner) v.findViewById(R.id.account_setting_card_spinner_period);
        Resources res = getResources();
        String[] startDate = res.getStringArray(R.array.card_start_date_array);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(getSherlockActivity(),
             R.array.card_start_date_array, startDate){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }
        };
             
        periodSpinner.setAdapter(periodAdapter);
        periodSpinner.setSelection(31);
        return v;
    }

}
