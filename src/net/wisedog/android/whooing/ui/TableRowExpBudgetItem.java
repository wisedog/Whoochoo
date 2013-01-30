/**
 * 
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
