package net.wisedog.android.whooing.ui;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.BillMonthlyItem;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Bill 안에 있는 Monthly Item안의 CreditCard 아이템 전개
 * @author Wisedog(me@wisedog.net)
 * */
public class BillMonthlyCardItem extends LinearLayout{

	public BillMonthlyCardItem(Context context) {
		super(context);
	}

	public BillMonthlyCardItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setup(Context context, BillMonthlyItem item) {
		inflate(context, R.layout.bill_list_card_item, this);

		TextView textCardName = (TextView) findViewById(R.id.bill_listitem_label_card_name);
		if (textCardName != null) {
			textCardName.setText(item.accountName);
		}

		TextView textAmount = (TextView) findViewById(R.id.bill_listitem_label_payment_amount);
		if (textAmount != null) {
			textAmount.setText(String.valueOf(item.amount));
		}

		TextView textPaymentDate = (TextView) findViewById(R.id.bill_listitem_payment_date);
		if (textPaymentDate != null) {
			textPaymentDate.setText(String.valueOf(item.paymentDate));
		}
		TextView textPeriod = (TextView) findViewById(R.id.bill_listitem_period);
		if (textPeriod != null) {
			String str = item.startUseDate + " ~ " + item.endUseDate;
			textPeriod.setText(str);
		}
	}

}
