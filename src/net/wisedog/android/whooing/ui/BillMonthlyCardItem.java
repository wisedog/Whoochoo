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
import net.wisedog.android.whooing.dataset.BillMonthlyItem;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
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
	
	public void setup(Context context, BillMonthlyItem item, int maxWidth, double totalAmount) {
		inflate(context, R.layout.bill_list_card_item, this);
		
		View graphView = (View)findViewById(R.id.credit_graph);
		if(graphView != null){
			graphView.setBackgroundColor(0xFF77D644);
			if(Double.compare(0.0f, item.amount) != 0){
				double ratio = item.amount / totalAmount;
				Resources r = getResources();
		        final int valueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90,
		                r.getDisplayMetrics());
				double barWidth = valueWidth * ratio;
				
				android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) graphView
						.getLayoutParams();
				params.width = (int) barWidth;
				graphView.setLayoutParams(params);
				
			}
		}

		TextView textCardName = (TextView) findViewById(R.id.bill_listitem_label_card_name);
		if (textCardName != null) {
			textCardName.setText(item.accountName);
		}

		TextView textAmount = (TextView) findViewById(R.id.bill_listitem_label_payment_amount);
		if (textAmount != null) {
			textAmount.setText(WhooingCurrency.getFormattedValue(item.amount, context));
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
		this.requestLayout();
	}

}
