/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.adapter.BoardAdapter;
import net.wisedog.android.whooing.dataset.BoardItem;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * A fragment for listing bbs items 
 *
 */
public class BbsListFragment extends SherlockListFragment implements OnScrollListener, OnItemClickListener {
	public static final int BOARD_TYPE_FREE = 0;
	public static final int BOARD_TYPE_MONEY_TALK = 1;
	public static final int BOARD_TYPE_COUNSELING = 2;
	public static final int BOARD_TYPE_WHOOING = 3;

	/** Container to store BBS List data */
	protected ArrayList<BoardItem> mDataArray;
	/** Footer view of Listview*/
	protected View footerView;
	/** Adapter of this Listview*/
	protected BoardAdapter mAdapter;
	/** listview loading flag */
	protected boolean loading = false;
	/** Page number to loading*/
	private int mPageNum = 1;
	/** Board type*/
	private int mBoardType = -1;
	
    
	/**
	 * Get new fragment instance
	 * @param	b	bundle instance to set argument to this fragment
	 * */
    public static BbsListFragment newInstance(Bundle b){
    	BbsListFragment f = new BbsListFragment();
    	f.setArguments(b);
    	return f;
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataArray = new ArrayList<BoardItem>();
        mAdapter = new BoardAdapter(getSherlockActivity(), mDataArray);
        mBoardType = getArguments().getInt("board_type");
        setHasOptionsMenu(true);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		footerView = getActivity().getLayoutInflater().inflate(R.layout.footer, null, false);
		getListView().addFooterView(footerView, null, false);
        
		getListView().setOnScrollListener(this);
		getListView().setOnItemClickListener(this);
		//getListView().removeFooterView(footerView);
		setListAdapter(mAdapter);
		super.onActivityCreated(savedInstanceState);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		BoardItem item = (BoardItem)(getListView().getItemAtPosition(position));
		BbsArticleFragment f = new BbsArticleFragment();
    	f.setData(mBoardType, item);
        getSherlockActivity().getSupportFragmentManager().beginTransaction()
        .addToBackStack(null)
        .replace(R.id.main_content, f)
        .commit();
	}
    
    
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	menu.clear();
    	inflater.inflate(R.menu.write_bbs_menus, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_action_bbs_write){
			((MainFragmentActivity)getActivity()).addBbsWriteFragment(BbsWriteFragment.MODE_WRITE_ARTICLE, 
					null, null, 0, null, mBoardType);
		}
		return super.onOptionsItemSelected(item);
	}



	@SuppressLint("HandlerLeak")
	protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BOARD){
                    mPageNum = mPageNum + 1;
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        fillListItems(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.handleMessage(msg);
        }
        
    };
    
    /**
     * Fill list item with data from server
     * @param	obj		The data formatted in JSON
     * @throws	JSONException
     * */
    protected void fillListItems(JSONObject obj) throws JSONException{    	
        JSONArray array = obj.getJSONArray("results");
        int length = array.length();
        
        //Clear data for refresh
        if(mPageNum == 2 && !mDataArray.isEmpty()){
            mDataArray.clear();
        }
        
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
            item.category = entity.getString("category");
            
            mDataArray.add(item);
        }
        
        getListView().removeFooterView(footerView);
        mAdapter.notifyDataSetChanged();
        loading = false;
    }

    @Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount - 1;
		if (loadMore && !loading) {
			loading = true;
			getMore();
		}

	}

    /**
     * Get more list items
     * */
    public void getMore(){
    	getListView().addFooterView(footerView, null, false);
        Bundle b = new Bundle();
        b.putInt("board_type", mBoardType);
        b.putInt("page", mPageNum);
        b.putInt("limit", 18);
        
        JSONObject obj = WhooingApplication.getInstance().getRepo().getUserValue();
        String language = Define.LOCALE_LANGUAGE;
        try {
            language = obj.getString("language");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
        	e.printStackTrace();
        	//TODO Get User Value when user value is null
        	return;
        }
        b.putString("language", language);
        ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                Define.API_GET_BOARD, b);
        thread.start();
    }

    /* (non-Javadoc)
     * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
     */
    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        ; //Do nothing        
    }
}
