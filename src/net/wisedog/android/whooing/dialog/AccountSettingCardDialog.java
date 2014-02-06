/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.dialog;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.activity.AccountsModify;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.widget.WiButton;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
        void onFinishingSetting(String useDate, int payDate, String accountId);
    }
    
    static public AccountSettingCardDialog newInstance(AccountsEntity entity, ArrayList<AccountsEntity> list){
        AccountSettingCardDialog f = new AccountSettingCardDialog();

        // Setting bundle
        Bundle args = new Bundle();
        args.putParcelableArrayList("account_list", list);    
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
        View v = inflater.inflate(R.layout.account_setting_card, container, true);
        
        //Setting account select spinner
        ArrayList<AccountsEntity> list = getArguments().getParcelableArrayList("account_list");
        AccountsEntity modifyEntity = getArguments().getParcelable("account_entity");
        mAccountsTitleList = new ArrayList<String>();
        int accountPos = -1;
        
        for(int i = 0; i < list.size(); i++){
            AccountsEntity entity = list.get(i);
            if(entity != null && entity.accountType.compareTo("assets") == 0){
                mAccountsTitleList.add(entity.title);
                mIdList.add(entity.account_id);
                //To modified card account, set account value from modified account id 
                if(modifyEntity != null && entity.account_id.compareTo(modifyEntity.account_id) == 0){
                    accountPos = i;
                }
            }
        }
        final Spinner accountsSpinner = (Spinner) v
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
        if(accountPos != -1){
            accountsSpinner.setSelection(accountPos);
        }else{
            accountsSpinner.setSelection(0);
        }        

        final Spinner payDateSpinner = (Spinner) v.findViewById(R.id.account_setting_card_spinner_date);
        ArrayAdapter<Integer> dateAdapter = new ArrayAdapter<Integer>(
                getSherlockActivity(), android.R.layout.select_dialog_item, DATE_NUMBER) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }
        };
        payDateSpinner.setAdapter(dateAdapter);
        
        if(modifyEntity != null){   //Modify case
            payDateSpinner.setSelection(modifyEntity.opt_pay_date - 1);
        }else{
            payDateSpinner.setSelection(0);
        }
        
        
        final Spinner periodSpinner = (Spinner) v.findViewById(R.id.account_setting_card_spinner_period);
        Resources res = getResources();
        String[] startDate = res.getStringArray(R.array.card_start_date_array);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(getSherlockActivity(), 
                android.R.layout.select_dialog_item, startDate){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }
        };
        
        periodSpinner.setAdapter(periodAdapter);        
        if(modifyEntity != null){   //Modify case
            int useDate = modifyEntity.getUseDateInt();
            if(useDate > 0){
                periodSpinner.setSelection(useDate + 30);
            }else{
                periodSpinner.setSelection(Math.abs(useDate));
            }            
        }
        else{
            periodSpinner.setSelection(31); //Set pre-month 1st day
        }
        
        WiButton cancelBtn = (WiButton)v.findViewById(R.id.account_setting_card_cancel);
        cancelBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        
        WiButton confirmBtn = (WiButton)v.findViewById(R.id.account_setting_card_confirm);
        confirmBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String useDate = null;
                int payDate = -1;
                String accountId = null;
                if(accountsSpinner != null){
                    int pos = accountsSpinner.getSelectedItemPosition();
                    if(pos != Spinner.INVALID_POSITION){
                        accountId = mIdList.get(pos);
                    }
                }
                
                if(payDateSpinner != null){
                    int pos = payDateSpinner.getSelectedItemPosition();
                    if(pos != Spinner.INVALID_POSITION){
                        payDate = DATE_NUMBER[pos];
                    }
                }
                
                if(periodSpinner != null){
                    int pos = periodSpinner.getSelectedItemPosition();
                    if(pos != Spinner.INVALID_POSITION){
                        if(pos >= 0 && pos <= 30){
                            useDate = "pp" + (pos + 1);
                        }
                        else{
                            useDate = "p" + (pos - 30);
                        }
                    }
                }
                if(useDate != null && payDate != -1 && accountId != null){
                    AccountsModify activity = (AccountsModify) getActivity();
                    activity.onFinishingSetting(useDate, payDate, accountId);            
                }
                dismiss();
                
            }
        });
        
        return v;
    }
    
    
    public void onClickConfirmCreditCard(View v){
        String useDate = null;
        int payDate = -1;
        String accountId = null;
        Spinner accountsSpinner = (Spinner) getActivity().findViewById(R.id.account_setting_card_spinner_accounts);
        if(accountsSpinner != null){
            int pos = accountsSpinner.getSelectedItemPosition();
            if(pos != Spinner.INVALID_POSITION){
                accountId = mIdList.get(pos);
            }
        }
        
        Spinner payDateSpinner = (Spinner) getActivity().findViewById(R.id.account_setting_card_spinner_date);
        if(payDateSpinner != null){
            int pos = payDateSpinner.getSelectedItemPosition();
            if(pos != Spinner.INVALID_POSITION){
                payDate = DATE_NUMBER[pos];
            }
        }
        
        Spinner periodSpinner = (Spinner) getActivity().findViewById(R.id.account_setting_card_spinner_period);
        if(periodSpinner != null){
            int pos = periodSpinner.getSelectedItemPosition();
            if(pos != Spinner.INVALID_POSITION){
                if(pos >= 0 && pos <= 30){
                    useDate = "pp" + (pos + 1);
                }
                else{
                    useDate = "p" + (pos - 30);
                }
            }
        }
        if(useDate != null && payDate != -1 && accountId != null){
            AccountsModify activity = (AccountsModify) getActivity();
            activity.onFinishingSetting(useDate, payDate, accountId);            
        }
        this.dismiss();
    }
    
    public void onClickCancelCreditCard(View v){
        this.dismiss();
    }
}
