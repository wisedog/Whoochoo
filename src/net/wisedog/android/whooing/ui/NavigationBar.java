package net.wisedog.android.whooing.ui;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.R.layout;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class NavigationBar extends LinearLayout {

	public NavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.navigation_bar, this);
	}
	

}

