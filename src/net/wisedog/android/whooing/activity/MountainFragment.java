package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import net.wisedog.android.whooing.views.WhooingGraph;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public final class MountainFragment extends SherlockFragment{

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
        	Bundle bundle = new Bundle();
            bundle.putString("end_date", WhooingCalendar.getTodayYYYYMM());
            bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMM(6));
            ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                    Define.API_GET_MOUNTAIN, bundle);
            thread.start();
        }
        super.onResume();
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == Define.MSG_API_OK){
				JSONObject obj = (JSONObject) msg.obj;
				if(msg.arg1 == Define.API_GET_MOUNTAIN){
			        DataRepository repository = WhooingApplication.getInstance().getRepo();
			        repository.setMtValue(obj);
			        if(isAdded() == true){
			        	showMountainGraph(obj);
			        }
				}
			}
			super.handleMessage(msg);
		}
		
	};

}
