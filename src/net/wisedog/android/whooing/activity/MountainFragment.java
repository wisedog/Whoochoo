package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.OnMountainChangeListener;
import net.wisedog.android.whooing.views.WhooingGraph;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class MountainFragment extends SherlockFragment implements OnMountainChangeListener {

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
    	View view = inflater.inflate(R.layout.mountain_fragment, null);
    	
        return view;
    }
    
    @Override
    public void onResume() {
        DataRepository repository = WhooingApplication.getInstance().getRepo();
        if(repository.getMtValue() != null){
           showMountainGraph(repository.getMtValue());
        }
        else{
            repository.registerObserver(this, DataRepository.MOUNTAIN_MODE);
            repository.refreshMtValue(getSherlockActivity());
        }
        
        super.onResume();
        
    }
    
    @Override
    public void onDestroyView() {
        DataRepository repository = WhooingApplication.getInstance().getRepo(); //DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.MOUNTAIN_MODE);
        super.onDestroyView();
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
            LinearLayout llBody = (LinearLayout) getSherlockActivity().findViewById(R.id.mountain_fragment_container);
            if(llBody != null){
                boolean result = wg.showGraph(getSherlockActivity(), llBody, resultObj.getJSONArray("rows"));
                if(result == false){
                    DataRepository repository = WhooingApplication.getInstance().getRepo();
                    Object observer = repository.findObserver(DataRepository.MOUNTAIN_MODE, (Object)this);
                    if(observer == null)
                    {
                        repository.registerObserver(this, DataRepository.MOUNTAIN_MODE);
                    }
                    repository.refreshMtValue(getSherlockActivity());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
