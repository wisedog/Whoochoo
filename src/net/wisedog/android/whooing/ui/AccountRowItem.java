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
package net.wisedog.android.whooing.ui;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.widget.WiTextView;
import android.content.Context;
import android.widget.RelativeLayout;

public class AccountRowItem extends RelativeLayout {
    private Context mContext;

    public AccountRowItem(Context context) {
        super(context);
        this.mContext = context;
    }
    public void setupListItem(AccountsEntity entity){
        inflate(mContext, R.layout.account_setting_item, this);
        
        WiTextView accountName = (WiTextView) findViewById(R.id.account_setting_item_text);
        if(accountName != null){
            accountName.setText(entity.title);
        } 
        requestLayout();
    }
}
