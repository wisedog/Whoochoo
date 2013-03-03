package net.wisedog.android.whooing.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.network.ThreadThumbnailLoader;
import net.wisedog.android.whooing.utils.DateUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BbsReplyEntity extends LinearLayout {

	private Context mContext;
	/**사용자가 답글의 댓글을 추가했을 때, 여기에 있는 정보를 사용한다.*/
	private JSONObject mObjParent = null;	

	public BbsReplyEntity(Context context) {
		super(context);
		mContext = context;
	}
	
	public void setupReply(final JSONObject obj, final JSONObject objResult) throws JSONException{
		inflate(mContext, R.layout.bbs_article_chunk, this);
		
		if(obj == null){	//objResult may be null, but it's acceptance
			return;
		}
		if(objResult != null){
			mObjParent = objResult;
		}
		
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
		
		if(Define.USER_ID == objWriter.getInt("user_id")){
			ImageButton delImg = (ImageButton)findViewById(R.id.bbs_article_chunk_delete);
			if(delImg != null){
				delImg.setVisibility(View.VISIBLE);
				delImg.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
			
			ImageButton modifyImg = (ImageButton)findViewById(R.id.bbs_article_chunk_modify);
			if(modifyImg != null){
				modifyImg.setVisibility(View.VISIBLE);
				modifyImg.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
			
		}
		
		
		
		TextView textDate = (TextView)findViewById(R.id.bbs_article_chunk_date);
		if(textDate != null){
			String dateString = DateUtil.getDateWithTimestamp(obj.getLong("timestamp") * 1000);
			textDate.setText(dateString);
		}
		TextView textContents = (TextView)findViewById(R.id.bbs_article_chunk_contents);
		if(textContents != null){
			textContents.setText(obj.getString("contents"));
		}
		
		TextView textComments = (TextView)findViewById(R.id.bbs_article_chunk_comment_num);
		if(textComments != null){
			textComments.setText(obj.getInt("additions") + " comments");
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
					// TODO Toast here
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
                    BbsCommentEntity entity = new BbsCommentEntity(mContext);
                    try {
                        entity.setup(obj.getJSONObject("results"));
                        ll.addView(entity, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // TODO toast
                    }

                } else if (msg.arg1 == Define.API_GET_BOARD_COMMENT) {
                    LinearLayout ll = (LinearLayout) findViewById(R.id.bbs_article_chunk_comment_container);
                    try {
                        JSONObject objResults = obj.getJSONObject("results");
                        JSONArray commentArray = objResults.getJSONArray("rows");
                        int len = commentArray.length();
                        for (int i = 0; i < len; i++) {
                            BbsCommentEntity entity = new BbsCommentEntity(mContext);
                            entity.setup(commentArray.getJSONObject(i));
                            ll.addView(entity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
