package net.wisedog.android.whooing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.api.GeneralApi;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import net.wisedog.android.whooing.views.WhooingGraph;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
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
        	AsyncTask<Void, Integer, JSONObject> task = new AsyncTask<Void, Integer, JSONObject>(){

                @Override
                protected JSONObject doInBackground(Void... arg0) {
        		    String requestUrl = "https://whooing.com/api/mountain.json_array?section_id="+Define.APP_SECTION 
        		    		+ "&start_date=" + WhooingCalendar.getPreMonthYYYYMM(6) 
        		            + "&end_date=" + WhooingCalendar.getTodayYYYYMM(); 
        		    GeneralApi mountain = new GeneralApi();
        		    JSONObject result = mountain.getInfo(requestUrl, Define.APP_ID, Define.REAL_TOKEN,
                             Define.APP_SECRET, Define.TOKEN_SECRET);
                	mountain = null;
                    return result;
                }

                @Override
                protected void onPostExecute(JSONObject result) {
                    if(Define.DEBUG && result != null){
                        Log.d("wisedog", "API Call - Balance : " + result.toString());
                    }
                    DataRepository repository = WhooingApplication.getInstance().getRepo();
			        repository.setMtValue(result);
			        if(isAdded() == true){
			        	showMountainGraph(result);
			        }
                    super.onPostExecute(result);
                }
            };
            
            task.execute();            	
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
                wg.showGraph(getSherlockActivity(), llBody, resultObj.getJSONArray("rows"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
