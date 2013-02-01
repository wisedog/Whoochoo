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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class MountainFragment extends SherlockFragment implements OnMountainChangeListener {
    private Activity mActivity;

    public static MountainFragment newInstance(String content) {
        MountainFragment fragment = new MountainFragment();
        return fragment;
    }

	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
