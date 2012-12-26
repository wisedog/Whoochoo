package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.OnMountainChangeListener;
import net.wisedog.android.whooing.views.WhooingGraph;
import android.app.Activity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class MountainFragment extends SherlockFragment implements OnMountainChangeListener {
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onDestroyView()
     */

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
        /*
         * WhooingGraph wg = new WhooingGraph(); wg.showGraph(mActivity);
         */
        DataRepository repository = DataRepository.getInstance();
        if(repository.getMtValue() != null){
           showMountainGraph(repository.getMtValue());
           super.onResume();
           return;
        }
        repository.registerObserver(this, DataRepository.MOUNTAIN_MODE);
        super.onResume();
        
    }
    
    @Override
    public void onDestroyView() {
        DataRepository repository = DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.MOUNTAIN_MODE);
        super.onDestroyView();
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

    /**
     * implements for OnMountainChangeListener
     * */
    public void onMountainUpdate(JSONObject obj) {
        showMountainGraph(obj);
    }
    
    /**
     * @param   obj     graph data formatted JSON
     * */
    private void showMountainGraph(JSONObject obj){
        try {
            JSONObject resultObj = obj.getJSONObject("results");
            WhooingGraph wg = new WhooingGraph();
            LinearLayout llBody = (LinearLayout) mActivity.findViewById(R.id.test1);
            if(llBody != null){
                wg.showGraph(mActivity, llBody, resultObj.getJSONArray("rows"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
