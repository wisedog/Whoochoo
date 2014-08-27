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
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;



public class AccountSettingCheckcardDialog extends DialogFragment {
    private ArrayList<String> mAccountsTitleList = new ArrayList<String>();
    private ArrayList<String> mIdList = new ArrayList<String>();
    
    public interface AccountCheckCardSettingListener {
        /**
         * Interface for checkcard dialog.
         * @param   accountId   account id what the user selected
         * */
        void onFinishingChoosing(String accountId);
    }
    
    static public AccountSettingCheckcardDialog newInstance(ArrayList<AccountsEntity> list){
        AccountSettingCheckcardDialog f = new AccountSettingCheckcardDialog();

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
        View v = inflater.inflate(R.layout.account_setting_checkcard,
                container, true);
        ArrayList<AccountsEntity> list = getArguments().getParcelableArrayList(
                "account_list");
        mAccountsTitleList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            AccountsEntity entity = list.get(i);
            if (entity != null && entity.accountType.compareTo("assets") == 0) {
                mAccountsTitleList.add(entity.title);
                mIdList.add(entity.account_id);
            }
        }
        final Spinner accountsSpinner = (Spinner) v
                .findViewById(R.id.account_setting_checkcard_spinner);
        ArrayAdapter<String> accountsAdapter = new ArrayAdapter<String>(
        		getActivity(), android.R.layout.select_dialog_item,
                mAccountsTitleList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(Color.rgb(0x33, 0x33, 0x33));
                return v;
            }
        };
        accountsSpinner.setAdapter(accountsAdapter);
        accountsSpinner.setSelection(0);
        
        WiButton cancelBtn = (WiButton)v.findViewById(R.id.account_setting_checkcard_btn_cancel);
        if(cancelBtn != null){
            cancelBtn.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    dismiss();
                    
                }
            });
        }
        WiButton confirmBtn = (WiButton)v.findViewById(R.id.account_setting_checkcard_btn_confirm);
        if(confirmBtn != null){
            confirmBtn.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    AccountsModify activity = (AccountsModify) getActivity();
                    int pos = accountsSpinner.getSelectedItemPosition();
                    if(pos != Spinner.INVALID_POSITION){
                        activity.onFinishingChoosing(mIdList.get(pos));
                    }
                     
                    dismiss();                    
                }
            });
        }
        

        return v;
    }
}
