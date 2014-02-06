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
import net.wisedog.android.whooing.dataset.BoardItem;

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
public class BoardAdapter extends BaseAdapter {
    private ArrayList<BoardItem> mDataArray;
    private LayoutInflater mInflater;
    
    public BoardAdapter(Context context, ArrayList<BoardItem> dataArray){
        mDataArray = dataArray;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public ArrayList<BoardItem> getData(){
        return mDataArray;
    }
    
    public void setData(ArrayList<BoardItem> array){
        mDataArray = array;
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
            convertView = mInflater.inflate(R.layout.bbs_list_item, parent, false);
        }
        
        BoardItem item = mDataArray.get(pos);
        if(item != null){
            TextView textContent = (TextView) convertView.findViewById(R.id.bbs_text_content);
            if(textContent != null){
                textContent.setText(item.subject);
            }
            TextView textCommentNum = (TextView) convertView.findViewById(R.id.bbs_text_comment_num);
            if(textCommentNum != null){
                textCommentNum.setText(String.valueOf(item.commentNum));
            }
            
            TextView textAuthor = (TextView) convertView.findViewById(R.id.bbs_text_author);
            if(textAuthor != null){
                textAuthor.setText(item.userName);
            }
        }
        return convertView;
    }

}
