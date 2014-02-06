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
package net.wisedog.android.whooing.adapter;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.PostItItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class PostItAdapter extends BaseAdapter {
    private ArrayList<PostItItem> mDataArray;
    private LayoutInflater mInflater;
    
    public PostItAdapter(Context context, ArrayList<PostItItem> dataArray){
        mDataArray = dataArray;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void setData(ArrayList<PostItItem> dataArray){
        mDataArray = dataArray;
    }

    public int getCount() {
        return mDataArray.size();
    }

    public Object getItem(int position) {
        return mDataArray.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public void clearAdapter()
    {
    	mDataArray.clear();
        notifyDataSetChanged();
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.postit_list_item, parent, false);
        }
        TextView textContent = (TextView) convertView.findViewById(R.id.post_it_text);
        PostItItem item = mDataArray.get(pos);
        
        if(item != null && textContent != null){
            textContent.setText(item.content);
        }
        return convertView;
    }

}
