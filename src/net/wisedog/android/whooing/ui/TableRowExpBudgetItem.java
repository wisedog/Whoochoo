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
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class TableRowExpBudgetItem extends RelativeLayout{

    private Context mContext;

    /**
     * @param context
     */
    public TableRowExpBudgetItem(Context context) {
        super(context);
        mContext = context;
    }
    
    public void setupListItem(double spent, double budget, double remains, int maxWidth, int minWidth, int color){
        inflate(mContext, R.layout.exp_budget_row_item, this);
        
        View barView = findViewById(R.id.exp_budget_spent_graph);
        LayoutParams params = (LayoutParams) barView.getLayoutParams();
        double ratio = spent / budget;
        int barWidth = (int)(maxWidth * ratio);
        if(barWidth < minWidth){
            barWidth = minWidth;
        }
        params.width = barWidth;
        barView.setBackgroundColor(color);
        barView.setLayoutParams(params);
        
        
        TextView remainText = (TextView) findViewById(R.id.exp_budget_remains_text);
        if(remainText != null){
            remainText.setText(String.valueOf(remains));
        }
        
        TextView spentBudgetText = (TextView) findViewById(R.id.exp_budget_spent_budget_text);
        if(spentBudgetText != null){
            spentBudgetText.setText(String.valueOf(spent) + " / " + String.valueOf(budget));
        }
        this.requestLayout();
    }

}
