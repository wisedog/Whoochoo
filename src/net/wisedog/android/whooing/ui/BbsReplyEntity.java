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
package net.wisedog.android.whooing.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.activity.BbsWriteFragment;
import net.wisedog.android.whooing.activity.MainFragmentActivity;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.network.ThreadThumbnailLoader;
import net.wisedog.android.whooing.utils.DateUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BbsReplyEntity extends LinearLayout {

	private Context mContext;
    private int mBbsId;    
    /** Board type(free, counseling ...) */
    private int mBoardType;	
    /** This is reply ID. Reply is Comment in Whooing */
    private String mCommentId;	
    private ProgressDialog mProgress;

	public BbsReplyEntity(Context context) {
		super(context);
		mContext = context;
	}
	
	/**
     * @param activity
     * @param bbsArticleFragment
     * @param id
     * @param mBoardType
     */
    public BbsReplyEntity(Context context, Fragment bbsArticleFragment, int bbs_id,
            int boardType) {
        super(context);
        mContext = context;
        mBbsId = bbs_id;
        mBoardType = boardType; 
    }

    public void setupReply(final JSONObject obj, final JSONObject objResult) throws JSONException{
		inflate(mContext, R.layout.bbs_article_chunk, this);
		
		if(obj == null){	//objResult may be null, but it's acceptance
			return;
		}
		
		mCommentId = obj.getString("comment_id");
		
		JSONObject objWriter = obj.getJSONObject("writer");
		ImageView profileImage = (ImageView)findViewById(R.id.bbs_article_chunk_img);
		String profileUrl = objWriter.getString("image_url");
        if(profileImage != null){
            URL url = null;
            try {
                url = new URL(profileUrl);
            } catch (MalformedURLException e) {
                profileImage.setImageResource(R.drawable.profile_anonymous);                 
                e.printStackTrace();
                Toast.makeText(mContext, "Error - Reply-04", Toast.LENGTH_LONG).show();
                return;
            }
            
            ThreadThumbnailLoader thread = new ThreadThumbnailLoader(mHandler, url);
            try{
                thread.start();
            }
            catch(IllegalThreadStateException e){
                profileImage.setImageResource(R.drawable.profile_anonymous);
            }
        }
		
		TextView textName = (TextView)findViewById(R.id.bbs_article_chunk_name);
		if(textName != null){
			textName.setText(objWriter.getString("username"));
		}
		
		TextView textLabel = (TextView)findViewById(R.id.bbs_article_chunk_level);
		if(textLabel != null){
			textLabel.setText(String.valueOf(objWriter.getInt("level")));
		}
		
		TextView textDate = (TextView)findViewById(R.id.bbs_article_chunk_date);
		if(textDate != null){
			String dateString = DateUtil.getDateWithTimestamp(obj.getLong("timestamp") * 1000);
			textDate.setText(dateString);
		}
		
		final TextView textContents = (TextView)findViewById(R.id.bbs_article_chunk_contents);
		if(textContents != null){
			textContents.setText(obj.getString("contents"));
		}
		
		TextView textComments = (TextView)findViewById(R.id.bbs_article_chunk_comment_num);
		if(textComments != null){
			textComments.setText(obj.getInt("additions") + " comments");
		}
		
		if(Define.USER_ID == objWriter.getInt("user_id")){
			ImageButton delImg = (ImageButton)findViewById(R.id.bbs_article_chunk_delete);
			if(delImg != null){
				delImg.setVisibility(View.VISIBLE);
				delImg.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
					    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		                alertDialogBuilder.setTitle(mContext.getString(R.string.bbs_delete_alert_title));
		                alertDialogBuilder.setMessage(mContext.getString(R.string.bbs_delete_alert_message))
		                .setCancelable(true)
		                .setPositiveButton(mContext.getString(R.string.text_yes), new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                        Bundle b = new Bundle();
		                        b.putInt("board_type", mBoardType);
		                        b.putInt("bbs_id", mBbsId);
		                        try {
		                            b.putString("comment_id", obj.getString("comment_id"));
		                            
		                        } catch (JSONException e) {
		                            e.printStackTrace();
		                            Toast.makeText(mContext, "Error - Reply-03", Toast.LENGTH_LONG).show();
		                            return;
		                        }
		                        mProgress = ProgressDialog.show(mContext, "", 
                                        mContext.getString(R.string.text_deleting));
		                        ThreadRestAPI thread = new ThreadRestAPI(mHandler,
		                                Define.API_DELETE_BOARD_REPLY, b);
		                       thread.start();
		                    }
		                })
		                .setNegativeButton(mContext.getString(R.string.text_no), new DialogInterface.OnClickListener() {
		                    
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                        dialog.cancel();                                
		                    }
		                });
		                
		                AlertDialog alertDialog = alertDialogBuilder.create();
		                alertDialog.show();
					}
					
				});
			}
			
			ImageButton modifyImg = (ImageButton)findViewById(R.id.bbs_article_chunk_modify);
			if(modifyImg != null){
				modifyImg.setVisibility(View.VISIBLE);
				modifyImg.setOnClickListener(new OnClickListener(){

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						MainFragmentActivity activity = (MainFragmentActivity)mContext;
						try {
							activity.addBbsWriteFragment(BbsWriteFragment.MODE_MODIFY_REPLY, null,
									textContents.getText().toString(), mBbsId, obj.getString("comment_id"), 0);
						} catch (JSONException e) {
							e.printStackTrace();
							return;
						}
					}
				});
			}
		}

		
		
		Button confirmBtn = (Button) findViewById(R.id.bbs_article_chunk_comment_confirm_btn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setLoadingStatus(true);
				EditText editText = (EditText)findViewById(R.id.bbs_article_chunk_comment_edittext);
				Bundle b = new Bundle();
				
				try {
					b.putString("category", objResult.getString("category"));
					b.putInt("bbs_id", objResult.getInt("bbs_id"));
					b.putString("comment_id", obj.getString("comment_id"));
				} catch (JSONException e) {
				    Toast.makeText(mContext, "Error - Reply-01", Toast.LENGTH_LONG).show();
					e.printStackTrace();
					return;
				}
				b.putString("contents", editText.getText().toString());
				ThreadRestAPI thread = new ThreadRestAPI(mHandler,
						Define.API_POST_BOARD_COMMENT, b);
				thread.start();

			}
		});

		if(obj.getInt("additions") > 0){
		    Bundle b = new Bundle();
		    try {
                b.putString("category", objResult.getString("category"));
                b.putInt("bbs_id", objResult.getInt("bbs_id"));
                b.putString("comment_id", obj.getString("comment_id"));
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
		    ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                    Define.API_GET_BOARD_COMMENT, b);
            thread.start();
		}
		
	}
	
	public void setLoadingStatus(boolean loading){
		Button confirmBtn = (Button)findViewById(R.id.bbs_article_chunk_comment_confirm_btn);
		confirmBtn.setEnabled(!loading);
		EditText editText = (EditText)findViewById(R.id.bbs_article_chunk_comment_edittext);
		editText.setEnabled(!loading);
		ProgressBar progress = (ProgressBar)findViewById(R.id.bbs_article_chunk_comment_progress);
		if(loading){
			progress.setVisibility(View.VISIBLE);
		}else{
			progress.setVisibility(View.INVISIBLE);
		}
	}
	
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Define.MSG_API_OK) {
                JSONObject obj = (JSONObject) msg.obj;
                if (msg.arg1 == Define.API_POST_BOARD_COMMENT) {
                    setLoadingStatus(false);
                    LinearLayout ll = (LinearLayout) findViewById(R.id.bbs_article_chunk_comment_container);
                    BbsCommentEntity entity = new BbsCommentEntity(mContext, mBoardType, mBbsId, mCommentId);
                    try {
                        entity.setup(obj.getJSONObject("results"));
                        ll.addView(entity, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "Error - Reply-02", Toast.LENGTH_LONG).show();
                    }
                    EditText editText = (EditText)findViewById(R.id.bbs_article_chunk_comment_edittext);
                    if(editText != null){
                    	editText.setText("");
                    }

                } else if (msg.arg1 == Define.API_GET_BOARD_COMMENT) {
                    LinearLayout ll = (LinearLayout) findViewById(R.id.bbs_article_chunk_comment_container);
                    try {
                        JSONObject objResults = obj.getJSONObject("results");
                        JSONArray commentArray = objResults.getJSONArray("rows");
                        int len = commentArray.length();
                        for (int i = 0; i < len; i++) {
                            BbsCommentEntity entity = new BbsCommentEntity(mContext, mBoardType, mBbsId, mCommentId);
                            entity.setup(commentArray.getJSONObject(i));
                            ll.addView(entity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(msg.arg1 == Define.API_DELETE_BOARD_REPLY){
                    if(Define.DEBUG){
                        Log.d("wisedog", "API_DELETE_BOARD_REPLY : " + obj.toString());
                    }
                    mProgress.dismiss();
                }
            }
        	else if(msg.what == 0){
                ImageView image = 
                        (ImageView)findViewById(R.id.bbs_article_chunk_img);
                if(msg.obj == null){
                    image.setImageResource(R.drawable.profile_anonymous);
                }
                else{
                    image.setImageBitmap((Bitmap)msg.obj);              
                }           
            }
            super.handleMessage(msg);
        }      
    };
}
