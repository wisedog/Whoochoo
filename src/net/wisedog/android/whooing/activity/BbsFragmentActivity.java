/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.adapter.BoardAdapter;
import net.wisedog.android.whooing.dataset.BoardItem;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BbsFragmentActivity extends SherlockFragmentActivity {
	public static final int BOARD_TYPE_FREE = 0;
	public static final int BOARD_TYPE_MONEY_TALK = 1;
	public static final int BOARD_TYPE_COUNSELING = 2;
	public static final int BOARD_TYPE_WHOOING = 3;
	
	private JSONObject mBbsValue = null;
	private int mBoardType = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Styled);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_fragment);
		setTitle(getIntent().getStringExtra("title"));
		mBoardType = getIntent().getIntExtra("board_type", -1);
		if(mBoardType == -1){
			Toast.makeText(this, "Error on board", Toast.LENGTH_LONG).show();
			return;
		}
	}

	@Override
	protected void onResume() {
		if(mBbsValue == null){
			Bundle b = new Bundle();
			b.putInt("board_type", mBoardType);
            ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_GET_BOARD, b);
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
                if(msg.arg1 == Define.API_GET_BOARD){
                    JSONObject obj = (JSONObject)msg.obj;
                    mBbsValue = obj;
                    try {
                        showBoard(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.handleMessage(msg);
        }
        
    };

	protected void showBoard(JSONObject obj) throws JSONException{
		JSONArray array = obj.getJSONArray("results");
        int length = array.length();
        
        ArrayList<BoardItem> dataArray = new ArrayList<BoardItem>();
        
        for(int i = 0; i < length; i++){
            JSONObject entity = array.getJSONObject(i);
            int id = entity.getInt("bbs_id");
            String content = entity.getString("subject");
            BoardItem item = new BoardItem(id, "", "", content);
            dataArray.add(item);
        }
        ListView boardList = (ListView)findViewById(R.id.bbs_listview);
        BoardAdapter adapter = new BoardAdapter(this, dataArray);
        boardList.setAdapter(adapter);
	}

}
