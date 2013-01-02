package net.wisedog.android.whooing.activity;

import net.wisedog.android.whooing.R;
import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class ProfitLossFragment extends Fragment {
    public static ProfitLossFragment newInstance() {
        ProfitLossFragment fragment = new ProfitLossFragment();

        return fragment;
    }

	private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.profit_loss_fragment, null);
    	
        return view;
    }
    
    

    @Override
	public void onResume() {
		super.onResume();
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}
}
