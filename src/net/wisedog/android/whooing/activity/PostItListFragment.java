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
import net.wisedog.android.whooing.adapter.PostItAdapter;
import net.wisedog.android.whooing.dataset.PostItItem;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * A fragment for showing Post it! item list
 * TODO PostItFragmentActivity to here
 */
public class PostItListFragment extends SherlockListFragment{
    
    protected ArrayList<PostItItem> mDataArray;
    protected View footerView;
    protected PostItAdapter mAdapter;
    
    static public PostItListFragment newInstance(Bundle b){
    	PostItListFragment f = new PostItListFragment();
    	f.setArguments(b);
    	return f;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataArray = new ArrayList<PostItItem>();
        mAdapter = new PostItAdapter(getActivity(), mDataArray);
        setHasOptionsMenu(true);
    }

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        footerView = ((LayoutInflater) getActivity()
        		.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        		.inflate(R.layout.footer, null, false);
        getListView().addFooterView(footerView, null, false);
        setListAdapter(mAdapter);
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_POST_IT);
        thread.start();
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.write_postit_menus, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}
	


	@Override
    public void onListItemClick(ListView parent, View view, int position, long id) {
        PostItItem item = (PostItItem)getListView().getItemAtPosition(position);
        PostItArticleFragment f = new PostItArticleFragment();
        f.setData(item);
        getSherlockActivity().getSupportFragmentManager().beginTransaction()
        .addToBackStack(null)
        .replace(R.id.main_content, f)
        .commit();
        //((MainFragmentActivity)getSherlockActivity()).addPostItArticleFragment(item);
    }
    
    @SuppressLint("HandlerLeak")
	protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_POST_IT){
                    JSONObject obj = (JSONObject)msg.obj;
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
     * Show list items from data
     * @param	obj		post-it data formatted in JSON
     * @throws	JSONException 
     * */
    protected void showPostIt(JSONObject obj) throws JSONException{
        JSONArray array = obj.getJSONArray("results");
        int length = array.length();
        mDataArray.clear();
        
        for(int i = 0; i < length; i++){
            JSONObject entity = array.getJSONObject(i);
            int id = entity.getInt("post_it_id");
            String page = entity.getString("page");
            String everywhere = entity.getString("everywhere");
            String content = entity.getString("contents");
            PostItItem item = new PostItItem(id, page, everywhere, content);
            mDataArray.add(item);
        }
        getListView().removeFooterView(footerView);
        mAdapter.notifyDataSetChanged();
    }
}
