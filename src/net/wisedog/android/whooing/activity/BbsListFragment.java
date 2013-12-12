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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BbsListFragment extends SherlockFragment implements OnScrollListener, OnItemClickListener {
	public static final String BBS_LIST_FRAGMENT_TAG = "bbs_list_tag";
	public static final int BOARD_TYPE_FREE = 0;
	public static final int BOARD_TYPE_MONEY_TALK = 1;
	public static final int BOARD_TYPE_COUNSELING = 2;
	public static final int BOARD_TYPE_WHOOING = 3;

	protected ListView mListView;
	protected ArrayList<BoardItem> mDataArray;
	protected View footerView;
	protected boolean loading = false;
	private int mPageNum = 1;
	private int mBoardType = -1;
	protected BoardAdapter mAdapter;
    
    public static BbsListFragment newInstance(Bundle b){
    	BbsListFragment f = new BbsListFragment();
    	f.setArguments(b);
    	return f;
    }

    
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataArray = new ArrayList<BoardItem>();
        mAdapter = new BoardAdapter(getSherlockActivity(), mDataArray);
        mBoardType = getArguments().getInt("board_type");
        setHasOptionsMenu(true);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bbs_fragment, container, false);
		if(view != null){
			mListView = (ListView)view.findViewById(R.id.bbs_listview);
			footerView = inflater.inflate(R.layout.footer, null, false);
	        mListView.addFooterView(footerView, null, false);
	        mListView.setAdapter(mAdapter);
	        mListView.setOnScrollListener(this);
	        mListView.setOnItemClickListener(this);
		}
		return view;
	}


	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        
        super.onActivityCreated(savedInstanceState);
    }
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		BoardItem item = (BoardItem)(mListView.getItemAtPosition(position));
        ((MainFragmentActivity)getSherlockActivity()).addArticle(mBoardType, item);
	}
    
    
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		Log.i("wisedog", "BbsListFragment - onCreateOptionMenu");
		menu.add("write").setIcon(R.drawable.icon_write)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		Log.i("wisedog", "BbsListFragment - onPrepareOptionMenu");
		//menu.clear();
/*		SubMenu subMenu1 = menu.addSubMenu("Lists");
		
        String[] menuItemsArray = getResources().getStringArray(R.array.main_actionbar_menuitem);
        for(int i = 0; i < menuItemsArray.length; i++){
        	subMenu1.add(menuItemsArray[i]);
        }	
        if(Define.DEBUG){
        	subMenu1.add("test");
        }

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.menu_lists_button);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		*/
		
		super.onPrepareOptionsMenu(menu);
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
    	//Gone progress bar. Note that progress
    	if(getView() != null){
    		ProgressBar progress = (ProgressBar)getView().findViewById(R.id.bbs_progress);
    		if(progress != null){
    			progress.setVisibility(View.GONE);
    		}
    	}
    	
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
        
        mListView.removeFooterView(footerView);
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
        mListView.addFooterView(footerView, null, false);
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

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onHiddenChanged(boolean)
     */
    @SuppressLint("NewApi")
    @Override
    public void onHiddenChanged(boolean hidden) {
    	
    	//TODO 이건 fragment hidden 할때 사용하는 것으로 현재 구조에서는 필요없다. 
    	// 삭제할것. 
        if(!hidden){
            ((BbsFragmentActivity)getActivity()).mItemVisible = true;
            getSherlockActivity().invalidateOptionsMenu();
        }
        BbsFragmentActivity activity = (BbsFragmentActivity)getActivity();
        if(activity.getListNeedRefresh()){
            activity.setListRefreshFlag(false);
            mPageNum = 1;
            getMore();
        }
        super.onHiddenChanged(hidden);
    }


}
