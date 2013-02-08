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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BbsListFragment extends SherlockListFragment implements OnScrollListener {
    public static final String LIST_FRAGMENT_TAG = "bbs_list_tag";
    
    protected ArrayList<BoardItem> mDataArray;
    protected View footerView;
    protected boolean loading = false;
    private int mPageNum = 1;
    protected Bundle mBundle = null;
    private int mBoardType = -1;
    protected BoardAdapter mAdapter;
    
    public void setData(Bundle b){
        mBundle = b;
    }
    
    public void setData(int boardType){
        mBoardType = boardType;
    }

    
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataArray = new ArrayList<BoardItem>();
        mAdapter = new BoardAdapter(getActivity(), mDataArray);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //BoardAdapter adapter = new BoardAdapter(getActivity(), mDataArray);
        footerView = ((LayoutInflater) getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        getListView().addFooterView(footerView, null, false);
        setListAdapter(mAdapter);
        getListView().removeFooterView(footerView);
        getListView().setOnScrollListener(this);
    }
    

    @Override
    public void onListItemClick(ListView parent, View view, int position, long id) {
        
        BoardItem item = (BoardItem)getListView().getItemAtPosition(position);
        ((BbsFragmentActivity)getActivity()).addArticleFragment(item);
    }
    
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BOARD){
                    mPageNum = mPageNum + 1;
                    JSONObject obj = (JSONObject)msg.obj;
                    //mBbsValue = obj;
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
        
        for(int i = 0; i < length; i++){
            JSONObject entity = array.getJSONObject(i);
            int id = entity.getInt("bbs_id");
            String subject = entity.getString("subject");
            int commentNum = entity.getInt("comments");
            int hits = entity.getInt("hits");
            String contents = entity.getString("contents");
            int recommandation = entity.getInt("recommandation");
            long timestamp = entity.getLong("timestamp");
            
            JSONObject objWriter = entity.getJSONObject("writer");
            String userName = objWriter.getString("username");
            String userImage = objWriter.getString("image_url");
            int userId = objWriter.getInt("user_id");
            int userLevel = objWriter.getInt("level");
            
                    
            BoardItem item = new BoardItem(id, userName, commentNum, subject);
            item.hits = hits;
            item.contents = contents;
            item.recommandation = recommandation;
            item.userId = userId;
            item.userLevel = userLevel;
            item.userImage = userImage;
            item.timestamp = timestamp;
            
            mDataArray.add(item);
        }
        HeaderViewListAdapter a1 = (HeaderViewListAdapter)getListView().getAdapter();
        BoardAdapter adapter = (BoardAdapter) a1.getWrappedAdapter();
        adapter.setData(mDataArray);
        adapter.notifyDataSetChanged();
        getListView().removeFooterView(footerView);
        loading = false;

    }

    /* (non-Javadoc)
     * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount -1;
        //Log.i("wisedog", "first : " + firstVisibleItem + " visible : " + visibleItemCount + " total : " + totalItemCount + " page : " + mPageNum);
        if (loadMore && !loading)
        {
            loading = true;
            getListView().addFooterView(footerView, null, false);
            Bundle b = new Bundle();
            b.putInt("board_type", mBoardType);
            b.putInt("page", mPageNum);
            b.putInt("limit", 20);
            ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_GET_BOARD, b);
            thread.start();
        }
        
    }

    /* (non-Javadoc)
     * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
     */
    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        ; //Do nothing        
    }
}
