package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.adapter.PostItAdapter;
import net.wisedog.android.whooing.dataset.PostItItem;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Fragment Activity for Post it!
 * @author wisedog(me@wisedog.net)
 * */
public class PostItFragmentActivity extends SherlockFragmentActivity {

    private JSONObject mPostItValue = null;
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_it_fragment);
    }
    
    @Override
    protected void onResume() {
        if(mPostItValue == null){
            ThreadRestAPI thread = new ThreadRestAPI(mHandler, this, Define.API_GET_POST_IT);
            thread.start();
        }
        else{
            //show Postit 
        }
        super.onResume();
    }
    
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_POST_IT){
                    JSONObject obj = (JSONObject)msg.obj;
                    mPostItValue = obj;
                    try {
                        showPostIt(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.handleMessage(msg);
        }
        
    };
    
    /**
     * Show post it
     * @param obj   JSON formatted data
     */
    protected void showPostIt(JSONObject obj) throws JSONException{
        JSONArray array = obj.getJSONArray("results");
        int length = array.length();
        
        ArrayList<PostItItem> dataArray = new ArrayList<PostItItem>();
        
        for(int i = 0; i < length; i++){
            JSONObject entity = array.getJSONObject(i);
            int id = entity.getInt("post_it_id");
            String page = entity.getString("page");
            String everywhere = entity.getString("everywhere");
            String content = entity.getString("contents");
            PostItItem item = new PostItItem(id, page, everywhere, content);
            dataArray.add(item);
        }
        ListView postItList = (ListView)findViewById(R.id.post_it_listview);
        PostItAdapter adapter = new PostItAdapter(this, dataArray);
        postItList.setAdapter(adapter);
    }
}
