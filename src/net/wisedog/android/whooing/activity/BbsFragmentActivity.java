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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

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
	private int mPageNum = 1;
	protected View footerView;
	protected boolean loading = false;
	
	protected ListView mListView = null;
	protected ArrayList<BoardItem> mDataArray;
	
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
		
		mListView = (ListView)findViewById(R.id.bbs_listview);
		mDataArray = new ArrayList<BoardItem>();
		BoardAdapter adapter = new BoardAdapter(this, mDataArray);
		footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
       mListView.addFooterView(footerView, null, false);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(mItemClickListener);
		
		
		mListView.setOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1)
            {
                // nothing here
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
                if (loadMore && !loading)
                {
                    loading = true;
                    mListView.addFooterView(footerView, null, false);
                    Bundle b = new Bundle();
                    b.putInt("board_type", mBoardType);
                    b.putInt("page", mPageNum);
                    b.putInt("limit", 20);
                    ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_GET_BOARD, b);
                    thread.start();
                }
            }
        });
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add("write").setIcon(R.drawable.icon_write)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        SubMenu subMenu1 = menu.addSubMenu("Lists");        
        subMenu1.add("Setting");
        subMenu1.add("Help");
        subMenu1.add("About");

        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("write")) {
            /*Intent intent = new Intent(this, TransactionAdd.class);
            intent.putExtra("title", getString(R.string.text_add_transaction));
            startActivityForResult(intent, 1);*/
            Toast.makeText(this, "Press Write button", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

	
	protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BOARD){
                    mPageNum = mPageNum + 1;
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
        
        //ArrayList<BoardItem> dataArray = ((BoardAdapter)mListView.getAdapter()).getData();
        
        for(int i = 0; i < length; i++){
            JSONObject entity = array.getJSONObject(i);
            int id = entity.getInt("bbs_id");
            String content = entity.getString("subject");
            int commentNum = entity.getInt("comments");
            JSONObject objWriter = entity.getJSONObject("writer");
            String userName = objWriter.getString("username");
                    
            BoardItem item = new BoardItem(id, userName, commentNum, content);
            mDataArray.add(item);
        }
        BoardAdapter adapter = (BoardAdapter) ((HeaderViewListAdapter)mListView.getAdapter()).getWrappedAdapter();
        adapter.setData(mDataArray);
        adapter.notifyDataSetChanged();
        mListView.removeFooterView(footerView);
        loading = false;

	}
	
	AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        /* (non-Javadoc)
         * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
         */
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            BoardItem item = (BoardItem)mListView.getItemAtPosition(position);
            Toast.makeText(BbsFragmentActivity.this, "position : " + position + 
                    "item content: " + item.content + "id : " + item.id, Toast.LENGTH_SHORT).show();            
        }
	    
    };
}
