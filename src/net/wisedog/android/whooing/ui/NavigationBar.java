package net.wisedog.android.whooing.ui;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.activity.TransactionAdd;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationBar extends LinearLayout {
    public static interface OnNavigationClick {
        public void onHeadButton(final String type);
    }
	private Context mContext;
	protected OnNavigationClick mListener = null;
	protected String   mTitleText  = "";

    public NavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.navigation_bar, this);
		mContext = context;
	}
    
    public NavigationBar(Context context) {
        super(context);
        inflate(context, R.layout.navigation_bar, this);
        mContext = context;
    }

    public void setNavigationListener(OnNavigationClick listener) {
        mListener = listener;
    }
	
	public void setupNavbar(){
	    findViewById(R.id.nav_btn_head).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.onHeadButton("");
            }
            }
        });
	    findViewById(R.id.nav_btn_plus).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TransactionAdd.class);
                intent.putExtra("title", "거래추가");
                ((Activity)mContext).startActivityForResult(intent, 1);
            }
        });
	}
	
	public void setRestApi(int number){
	    TextView textView = (TextView)findViewById(R.id.navbar_textview_restapi);
	    if(textView != null){
	        textView.setText("API:"+number);
	    }
	}
	
	/**
	 * Set navigation title
	 * @param      title       String to set title
	 * */
	public void setTitle(String title){
	    if(title == null){
	        return;
	    }
	    TextView titleText = (TextView)findViewById(R.id.nav_text_title);
	    if(titleText != null){
	        titleText.setText(title);
	    }
	}
	
	public void showPlusButton(boolean flag){
	    TextView plusText = (TextView)findViewById(R.id.nav_btn_plus);
	    if(plusText == null){
	        return;
	    }
	        
	    if(flag){
	        plusText.setVisibility(View.VISIBLE);
	    }else{
	        plusText.setVisibility(View.GONE);
	    }
	}

}

