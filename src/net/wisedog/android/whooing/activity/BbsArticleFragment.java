/**
 * 
 */
package net.wisedog.android.whooing.activity;

import java.net.MalformedURLException;
import java.net.URL;

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
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * This fragment is for show bbs list
 * @author Wisedog(me@wisedog.net)
 */
public class BbsArticleFragment extends SherlockFragment {
    public static final String BBS_ARTICLE_FRAGMENT_TAG = "bbs_article_tag";

    private int mBoardType = -1;
    private BoardItem mItemData = null;
    private ProgressDialog mProgress = null;

	private AdView adView;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        refreshArticle(false);
        super.onCreate(savedInstanceState);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.bbs_article_fragment, container, false);
        // adView 만들기
	    adView = new AdView(getSherlockActivity(), AdSize.SMART_BANNER, "a15147cd53daa26");
	    LinearLayout layout = (LinearLayout)view.findViewById(R.id.bbs_article_ads);

	    // 찾은 LinearLayout에 adView를 추가
	    layout.addView(adView);

	    // 기본 요청을 시작하여 광고와 함께 요청을 로드
	    AdRequest adRequest = new AdRequest();
	    if(Define.DEBUG){
	    	adRequest.addTestDevice("65E3B8CB214707370B559D98093D74AA");
	    }
	    adView.loadAd(adRequest);
        return view;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	ProgressBar progress = (ProgressBar)getActivity().findViewById(R.id.bbs_article_progress);
        if(progress != null){
        	progress.setVisibility(View.VISIBLE);
        }
        
        Button confirmButton = (Button)getActivity().findViewById(R.id.bbs_article_post_reply_btn);
        if(confirmButton != null){
        	confirmButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					EditText text = (EditText)getActivity().findViewById(R.id.bbs_article_post_reply_box);
			    	if(text != null){
			    		String str = text.getText().toString();
			    		if(str == null || str.equals("")){
			    			Toast.makeText(getActivity(), getString(R.string.bbs_article_inform_fill_contents)
			    					, Toast.LENGTH_SHORT).show();
			    			return;
			    		}
			    		Bundle b = new Bundle();
			    		b.putInt("bbs_id", mItemData.id);
			    		b.putString("contents", str);
			    		b.putString("category", mItemData.category);
			    		ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_POST_BOARD_REPLY, b);
			           thread.start();
			           setEnableStatus(false);
			    	}
				}
			});
        }
        
        
        super.onActivityCreated(savedInstanceState);
    }
    
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                JSONObject obj = (JSONObject)msg.obj;
                if(msg.arg1 == Define.API_GET_BOARD_ARTICLE){
                    try {
                        showArticle(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(msg.arg1 == Define.API_POST_BOARD_REPLY){
                	setEnableStatus(true);
                	((EditText)getActivity().findViewById(R.id.bbs_article_post_reply_box)).setText("");
                	
                	int result = 0;
                	try {
						result = obj.getInt("code");
					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(getSherlockActivity(), "Error - BBS-03", Toast.LENGTH_LONG).show();
						return;
					}
                	if(result == Define.RESULT_OK){
                		LinearLayout ll = (LinearLayout)getActivity().findViewById(R.id.bbs_article_reply_container);
                		BbsReplyEntity entity = new BbsReplyEntity(getActivity());
                    	try {
							entity.setupReply(obj.getJSONObject("results"), null);
							ll.addView(entity, 0);
						} catch (JSONException e) {
							e.printStackTrace();
						}
                	}
                	if(Define.DEBUG){
                	    Log.d("wisedog", "BOARD_REPLY : " + obj.toString());
                	}
                }
                else if(msg.arg1 == Define.API_DELETE_BOARD_ARTICLE){
                    if(Define.DEBUG){
                        Log.d("wisedog", "BOARD_DELETE : " + obj.toString());
                    }
                    int result = 0;
                    try {
                        result = obj.getInt("code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getSherlockActivity(), "Error - BBS-02", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(result == Define.RESULT_OK){
                        mProgress.dismiss();
                        Toast.makeText(getActivity(), getString(R.string.bbs_deleted), Toast.LENGTH_LONG).show();
                        ((BbsFragmentActivity)getActivity()).setListRefreshFlag(true);
                        getActivity().getSupportFragmentManager().popBackStack();                        
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
    
    public void setEnableStatus(boolean enable){
    	((EditText)getActivity().findViewById(R.id.bbs_article_post_reply_box)).setEnabled(enable);
    	((Button)getActivity().findViewById(R.id.bbs_article_post_reply_btn)).setEnabled(enable);
    	
    	if(enable){
    		((ProgressBar)getActivity().findViewById(R.id.bbs_article_post_reply_progress)).setVisibility(View.INVISIBLE);
    	}else{
    		((ProgressBar)getActivity().findViewById(R.id.bbs_article_post_reply_progress)).setVisibility(View.VISIBLE);
    	}
    }
    
    /**
     * Show article data
     * @param		Bbs data formatted in JSON
     * */
    protected void showArticle(JSONObject obj) throws JSONException{
        JSONObject objResult = obj.getJSONObject("results");
        JSONObject objWriter = objResult.getJSONObject("writer");
        
        ProgressBar progress = (ProgressBar)getActivity().findViewById(R.id.bbs_article_progress);
        if(progress != null){
        	progress.setVisibility(View.GONE);
        }

        final TextView textSubject = (TextView)getActivity().findViewById(R.id.bbs_article_subject);
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
        
        final TextView textContents = (TextView)getActivity().findViewById(R.id.bbs_article_text_contents);
        if(textContents != null){
            textContents.setText(objResult.getString("contents"));
        }
        
        if(Define.USER_ID == objWriter.getInt("user_id")){
            ImageButton btnDelete = (ImageButton)getActivity().findViewById(R.id.bbs_article_delete);
            ImageButton btnModify = (ImageButton)getActivity().findViewById(R.id.bbs_article_modify);
            if(btnDelete != null && btnModify != null){
                btnDelete.setVisibility(View.VISIBLE);
                btnModify.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(new OnClickListener() { 
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle(getString(R.string.bbs_delete_alert_title));
                        alertDialogBuilder.setMessage(getString(R.string.bbs_delete_alert_message))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.text_yes), new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProgress = ProgressDialog.show(getActivity(), "", 
                                        getString(R.string.text_deleting));
                                Bundle b = new Bundle();
                                b.putInt("board_type", mBoardType);
                                b.putInt("bbs_id", mItemData.id);
                                ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_DELETE_BOARD_ARTICLE, b);
                                thread.start();
                            }
                        })
                        .setNegativeButton(getString(R.string.text_no), new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();                                
                            }
                        });
                        
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
                
                btnModify.setOnClickListener(new OnClickListener(){
					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						String subject = textSubject.getText().toString();
						String content = textContents.getText().toString();
						BbsFragmentActivity activity = (BbsFragmentActivity) getActivity();
						activity.addWriteFragment(BbsWriteFragment.MODE_MODIFY_ARTICLE, subject,
								content, mItemData.id, null);
						activity.mItemVisible = false;
						activity.invalidateOptionsMenu();
					}
                });
            }
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
        	BbsReplyEntity entity = new BbsReplyEntity(getActivity(), this, mItemData.id, this.mBoardType);
        	entity.setupReply((JSONObject) replyArray.get(i), objResult);
        	ll.addView(entity);
        }
        
    }

    @SuppressLint("NewApi")
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            ((BbsFragmentActivity) getActivity()).mItemVisible = true;
            getSherlockActivity().invalidateOptionsMenu();
        }
        BbsFragmentActivity activity = (BbsFragmentActivity) getActivity();
        if (activity.mRefreshArticleFlag) {
            Toast.makeText(getActivity(), "TEST1", Toast.LENGTH_SHORT).show();
            activity.mRefreshArticleFlag = false;
            refreshArticle(true);
        }
        super.onHiddenChanged(hidden);
    }
	
	public void refreshArticle(boolean clear){
	    clearContent();
	    Bundle b = new Bundle();
        b.putInt("bbs_id", mItemData.id);
        b.putInt("board_type", mBoardType);
        ThreadRestAPI thread = new ThreadRestAPI(mHandler,Define.API_GET_BOARD_ARTICLE, b);
        thread.start();
	}
	
	protected void clearContent(){
	    LinearLayout ll = (LinearLayout)getActivity().findViewById(R.id.bbs_article_reply_container);
	    if(ll != null){
	        ll.removeAllViews();
	    }
	    ProgressBar progress = (ProgressBar)getActivity().findViewById(R.id.bbs_article_progress);
        if(progress != null){
            progress.setVisibility(View.VISIBLE);
        }

        final TextView textSubject = (TextView)getActivity().findViewById(R.id.bbs_article_subject);
        if(textSubject != null){
            textSubject.setText("");
        }
        
        TextView textName = (TextView)getActivity().findViewById(R.id.bbs_article_text_name);
        if(textName != null){
            textName.setText("");
        }
        TextView textLevel = (TextView)getActivity().findViewById(R.id.bbs_article_text_level);
        if(textLevel != null){
            textLevel.setText("");
        }
        
        TextView textDate = (TextView)getActivity().findViewById(R.id.bbs_article_text_date);
        if(textDate != null){
            textDate.setText("");
        }
        
        final TextView textContents = (TextView)getActivity().findViewById(R.id.bbs_article_text_contents);
        if(textContents != null){
            textContents.setText("");
        }
	}
}
