package net.wisedog.android.whooing.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.network.ThreadThumbnailLoader;
import net.wisedog.android.whooing.utils.DateUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BbsReplyEntity extends LinearLayout {

	private Context mContext;

	public BbsReplyEntity(Context context) {
		super(context);
		mContext = context;
	}
	
	public void setupReply(JSONObject obj) throws JSONException{
		inflate(mContext, R.layout.bbs_article_chunk, this);
		if(obj == null){
			return;
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
		
		Button confirmBtn = (Button)findViewById(R.id.bbs_article_chunk_comment_confirm_btn);
		confirmBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Disable edittext, btn, turn on progressbar
				// TODO run thread
				
			}
		});

		LinearLayout ll = (LinearLayout)findViewById(R.id.bbs_article_chunk_comment_container);
		
		JSONArray commentArray = obj.getJSONArray("rows");
		int len = commentArray.length();
		for(int i = 0; i < len; i++){
			BbsCommentEntity entity = new BbsCommentEntity(mContext);
			entity.setup(commentArray.getJSONObject(i));
			ll.addView(entity);
		}
	}
	
	protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	if(msg.what == Define.MSG_API_OK){
        		if(msg.arg1 == Define.API_POST_BOARD_COMMENT){
        			JSONObject obj = (JSONObject)msg.obj;
        			//TODO turn off progress
        			LinearLayout ll = (LinearLayout)findViewById(R.id.bbs_article_chunk_comment_container);
        			BbsCommentEntity entity = new BbsCommentEntity(mContext);
        			try {
						entity.setup(obj);
						ll.addView(entity);
					} catch (JSONException e) {
						e.printStackTrace();
						//TODO toast
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
