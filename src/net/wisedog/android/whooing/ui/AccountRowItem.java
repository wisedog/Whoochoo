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
