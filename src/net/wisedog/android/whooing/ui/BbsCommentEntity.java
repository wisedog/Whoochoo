package net.wisedog.android.whooing.ui;

import java.util.Date;
import java.util.Locale;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.network.ThreadRestAPI;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BbsCommentEntity extends LinearLayout {

	private Context mContext;
	private int mBoardType;
	private int mBbsId;
	private String mCommentId;
	private ProgressDialog mProgress;
	
	

	public BbsCommentEntity(Context context, int boardType, int bbsId, String commentId) {
		super(context);
		mContext = context;
		mBoardType = boardType;
		mBbsId = bbsId;
		mCommentId = commentId;
	}
	
	public void setup(final JSONObject obj) throws JSONException{
		inflate(mContext, R.layout.bbs_comment_item, this);
		JSONObject objWriter = obj.getJSONObject("writer");
		
		TextView textContent = (TextView)findViewById(R.id.bbs_comment_contents);
		textContent.setText(obj.getString("contents"));
		
		TextView textDate = (TextView) findViewById(R.id.bbs_comment_date);
		if (textDate != null) {
			if (objWriter.getInt("user_id") == Define.USER_ID) {
				textDate.setText("X");
				textDate.setOnClickListener(new OnClickListener(){
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
		                        b.putString("comment_id", mCommentId);
		                        
		                        try {
		                        	b.putInt("addition_id", obj.getInt("addition_id"));
		                        } catch (JSONException e) {
		                            e.printStackTrace();
		                            Toast.makeText(mContext, "Error - Comment-01", Toast.LENGTH_LONG).show();
		                            return;
		                        }
		                        mProgress = ProgressDialog.show(mContext, "", 
                                        mContext.getString(R.string.text_deleting));
		                        ThreadRestAPI thread = new ThreadRestAPI(mHandler,
		                                Define.API_DELETE_BOARD_COMMENT, b);
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

			} else {
				Date date = new Date(obj.getLong("timestamp") * 1000);
				Locale locale = new Locale(Define.LOCALE_LANGUAGE, Define.COUNTRY_CODE);
				java.text.DateFormat df = java.text.DateFormat.getDateInstance(
						java.text.DateFormat.SHORT, locale);
				String date1 = df.format(date).toString();
				textDate.setText(date1);
			}
		}
		
		TextView textName = (TextView)findViewById(R.id.bbs_comment_name);
		textName.setText(objWriter.getString("username"));
	}
	
	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Define.MSG_API_OK) {
				JSONObject obj = (JSONObject) msg.obj;
                if (msg.arg1 == Define.API_DELETE_BOARD_COMMENT) {
                	mProgress.dismiss();
                	if(Define.DEBUG){
                        Log.i("wisedog", "API_DELETE_BOARD_COMMENT : " + obj.toString());
                    }
                	int resultCode;
					try {
						resultCode = obj.getInt("code");
						if(resultCode == 200){
	                		BbsCommentEntity.this.setVisibility(View.GONE);
	                	}
					} catch (JSONException e) {
						e.printStackTrace();
					}
                	
                }
			}
			super.handleMessage(msg);
		}
		
	};

}
