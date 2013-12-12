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

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.PostItItem;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Post it article fragment
 * @author Wisedog(me@wisedog.net)
 *
 */
public class PostItArticleFragment extends SherlockFragment {
    
    private PostItItem mItem = null;
    private boolean mInEditMode = false;
    private boolean mInAddMode = false;
    private boolean mIsConfirm = false;
    
    public void setData(PostItItem item, boolean addMode){
        this.mItem = item;
        this.mInAddMode = addMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_it_article, container, false);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        final EditText editText = (EditText)getActivity().findViewById(R.id.post_it_article_edit_text);
        final Button modifyBtn = (Button)getActivity().findViewById(R.id.post_it_article_btn_modify);
        final Button removeBtn = (Button)getActivity().findViewById(R.id.post_it_article_btn_remove);
        final Button confirmBtn = (Button)getActivity().findViewById(R.id.post_it_article_btn_confirm);
        
        if(editText == null || modifyBtn == null || removeBtn == null || confirmBtn == null){
            super.onActivityCreated(savedInstanceState);
            return;
        }
        
        if(mInAddMode){
            editText.setEnabled(true);
            editText.requestFocus();
            removeBtn.setVisibility(View.GONE);
            modifyBtn.setVisibility(View.GONE);
            editText.addTextChangedListener(new TextWatcher(){

                @Override
                public void afterTextChanged(Editable arg0) {
                    String content = editText.getText().toString();
                    if(content.compareTo("") == 0){
                        confirmBtn.setEnabled(false);
                    }else{
                        confirmBtn.setEnabled(true);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    ; //Do nothing
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                	; //Do nothing
                }
            });

			confirmBtn.setEnabled(false);
			confirmBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putString("page", "_main/insert");
					b.putString("contents", editText.getText().toString());
					((ProgressBar)getActivity().findViewById(R.id.post_it_article_progress)).setVisibility(View.VISIBLE);
					confirmBtn.setEnabled(false);
					ThreadRestAPI thread = new ThreadRestAPI(mHandler,
							Define.API_POST_POSTIT, b);
					thread.start();
				}
			});
        }else{  //
            if(mItem != null){
                editText.setText(mItem.content);
                editText.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						String content = editText.getText().toString();
                        if(content.compareTo(mItem.content) == 0){
                            modifyBtn.setEnabled(false);
                        }else{
                            modifyBtn.setEnabled(true);
                        }
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						;//Do nothing
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						;//Do nothing
					}
				});
                
            }
            modifyBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mInEditMode) {
                        editText.setEnabled(false);
                        modifyBtn.setText(getString(R.string.text_loading));
                        modifyBtn.setEnabled(false);
                        ((ProgressBar)getActivity().findViewById(R.id.post_it_article_progress))
                        .setVisibility(View.VISIBLE);
                        Bundle b = new Bundle();
                        b.putInt("post_it_id", mItem.id);
                        b.putString("contents", editText.getText().toString());
                        ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                                Define.API_PUT_POSTIT, b);
                        thread.start();
                    } else {
                        editText.setEnabled(true);
                        editText.requestFocus();
                        modifyBtn.setText(getString(R.string.text_confirm));
                        modifyBtn.setEnabled(false);
                        mInEditMode = true;
                    }
                }
            });
            removeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mIsConfirm){
                        removeBtn.setEnabled(false);
                        ((ProgressBar)getActivity().findViewById(R.id.post_it_article_progress))
                        .setVisibility(View.VISIBLE);
                        Bundle b = new Bundle();
                        b.putInt("post_it_id", mItem.id);
                        ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                                Define.API_DELETE_POSTIT, b);
                        thread.start();
                    }
                    else{
                        removeBtn.setText(getString(R.string.post_it_question_delete));
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        removeBtn.startAnimation(shake);
                        mIsConfirm = true;
                    }
                    
                }
            });
            confirmBtn.setVisibility(View.GONE);
        }
        
        
        super.onActivityCreated(savedInstanceState);
    }
    
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Define.MSG_API_OK) {
				if (msg.arg1 == Define.API_POST_POSTIT) {
					((ProgressBar) getActivity().findViewById(
							R.id.post_it_article_progress))
							.setVisibility(View.INVISIBLE);
					((PostItFragmentActivity) getActivity())
							.needToRefresh(true);
					getActivity().getSupportFragmentManager().popBackStack();

				} else if (msg.arg1 == Define.API_PUT_POSTIT) {
					Toast.makeText(getActivity(), getString(R.string.bbs_article_post_modified),
							Toast.LENGTH_SHORT).show();
					((PostItFragmentActivity) getActivity())
							.needToRefresh(true);
					getActivity().getSupportFragmentManager().popBackStack();
				} else if (msg.arg1 == Define.API_DELETE_POSTIT) {
					Toast.makeText(getActivity(), getString(R.string.bbs_article_post_deleted),
							Toast.LENGTH_SHORT).show();
					((PostItFragmentActivity) getActivity())
							.needToRefresh(true);
					getActivity().getSupportFragmentManager().popBackStack();
				}
			}
			super.handleMessage(msg);
		}
	};
    
    
    public boolean allowBackPressed(){
        final EditText editText = (EditText)getActivity().findViewById(R.id.post_it_article_edit_text);
        int dirtyFlag = editText.getText().toString().compareTo(mItem.content);
        return (mInEditMode && dirtyFlag == 0);
    }
    
    
}
