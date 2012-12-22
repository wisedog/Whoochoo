package net.wisedog.android.whooing.activity;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.views.WhooingGraph;
import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class MountainFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    public static MountainFragment newInstance(String content) {
        MountainFragment fragment = new MountainFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        return fragment;
    }

    private String mContent = "???";
	private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.mountain, null);
    	
        return view;
    }
    
    

    @Override
	public void onResume() {
    	WhooingGraph wg = new WhooingGraph();
    	wg.showGraph(mActivity);
		super.onResume();
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
    
    @Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}
}
