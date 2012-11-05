package net.wisedog.android.whooing.ui;

import net.wisedog.android.whooing.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	}
	
	public void setRestApi(int number){
	    TextView textView = (TextView)findViewById(R.id.navbar_textview_restapi);
	    if(textView != null){
	        textView.setText("API:"+number);
	    }
	}

}

