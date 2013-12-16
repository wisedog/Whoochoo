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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

/**
 * A fragment for writing Post it!
 */
public class PostItWriteFragment extends SherlockFragment {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	final View view = inflater.inflate(R.layout.post_it_write_article, container, false);
    	if(view != null){
    		final EditText editText = (EditText)view.findViewById(R.id.post_it_write_edit_text);
            final Button confirmBtn = (Button)view.findViewById(R.id.post_it_write_btn_confirm);
            final Button cancelBtn = (Button)view.findViewById(R.id.post_it_write_btn_cancel);
            editText.setText("");
            editText.requestFocus();
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
            
			confirmBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putString("page", "_main/insert");	// TODO Now only support attaching to main
					b.putString("contents", editText.getText().toString());
					((ProgressBar)view.findViewById(R.id.post_it_write_progress)).setVisibility(View.VISIBLE);
					confirmBtn.setEnabled(false);
					ThreadRestAPI thread = new ThreadRestAPI(mHandler,
							Define.API_POST_POSTIT, b);
					thread.start();
				}
			});
			
			cancelBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getActivity().getSupportFragmentManager().popBackStack();
				}
			});
    	}
        return view; 
    }
    
    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	menu.clear();
		inflater.inflate(R.menu.submenus_only, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}
    
    
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Define.MSG_API_OK) {
				if (msg.arg1 == Define.API_POST_POSTIT) {
					((ProgressBar) getView().findViewById(
							R.id.post_it_write_progress))
							.setVisibility(View.INVISIBLE);
					getActivity().getSupportFragmentManager().popBackStack();
				}
			}
			super.handleMessage(msg);
		}
	};
}
