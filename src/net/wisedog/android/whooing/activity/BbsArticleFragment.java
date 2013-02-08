/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.BoardItem;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.network.ThreadThumbnailLoader;
import net.wisedog.android.whooing.ui.BbsReplyEntity;
import net.wisedog.android.whooing.utils.DateUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BbsArticleFragment extends SherlockFragment {

    private int mBoardType = -1;
    private BoardItem mItemData = null;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle b = new Bundle();
        b.putInt("bbs_id", mItemData.id);
        b.putInt("board_type", mBoardType);
        ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_GET_BOARD_ARTICLE, b);
        thread.start();
        super.onCreate(savedInstanceState);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bbs_article_fragment, container, false);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        
        super.onActivityCreated(savedInstanceState);
    }
    
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BOARD_ARTICLE){
                    JSONObject obj = (JSONObject)msg.obj;
                    try {
                        showArticle(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(msg.what == 0){
                ImageView image = 
                        (ImageView)getActivity().findViewById(R.id.bbs_article_profile_image);
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
    
    
    public void setData(int boardType, BoardItem item){
        mBoardType = boardType;
        mItemData = item;
    }
    
    protected void showArticle(JSONObject obj) throws JSONException{
        JSONObject objResult = obj.getJSONObject("results");
        JSONObject objWriter = objResult.getJSONObject("writer");

        TextView textSubject = (TextView)getActivity().findViewById(R.id.bbs_article_subject);
        if(textSubject != null){
            textSubject.setText(objResult.getString("subject"));
        }
        
        TextView textName = (TextView)getActivity().findViewById(R.id.bbs_article_text_name);
        if(textName != null){
            textName.setText(objWriter.getString("username"));
        }
        TextView textLevel = (TextView)getActivity().findViewById(R.id.bbs_article_text_level);
        if(textLevel != null){
            textLevel.setText("lv. " + objWriter.getInt("level"));
        }
        
        TextView textDate = (TextView)getActivity().findViewById(R.id.bbs_article_text_date);
        if(textDate != null){
            String dateString = DateUtil.getDateWithTimestamp(objResult.getLong("timestamp") * 1000);
            textDate.setText(dateString);
        }
        
        TextView textContents = (TextView)getActivity().findViewById(R.id.bbs_article_text_contents);
        if(textContents != null){
            textContents.setText(objResult.getString("contents"));
        }
        
        ImageView profileImage = (ImageView)getActivity().findViewById(R.id.bbs_article_profile_image);
        profileImage.setScaleType(ImageView.ScaleType.FIT_XY);
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
        
        //inflate reply
        JSONArray replyArray = objResult.getJSONArray("rows");
        int len = replyArray.length();
        LinearLayout ll = (LinearLayout)getActivity().findViewById(R.id.bbs_article_reply_container);
        for(int i = 0;i < len; i++){
        	BbsReplyEntity entity = new BbsReplyEntity(getActivity());
        	entity.setupReply((JSONObject) replyArray.get(i));
        	ll.addView(entity);
        }
        
    }
    
}
