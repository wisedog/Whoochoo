package net.wisedog.android.whooing.activity;

import net.wisedog.android.whooing.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class DashboardFragment extends SherlockFragment {
	private static final String KEY_TAB_NUM = "key.tab.num";
	
	public static DashboardFragment newInstance(String text) {
        DashboardFragment fragment = new DashboardFragment();
        
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(KEY_TAB_NUM, text);
        fragment.setArguments(args);

        
        return fragment;
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.whooing_main2, null);
        
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
