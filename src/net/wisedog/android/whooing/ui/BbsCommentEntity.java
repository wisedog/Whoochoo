package net.wisedog.android.whooing.ui;

import java.util.Date;
import java.util.Locale;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BbsCommentEntity extends LinearLayout {

	private Context mContext;

	public BbsCommentEntity(Context context) {
		super(context);
		mContext = context;
	}
	
	public void setup(JSONObject obj) throws JSONException{
		inflate(mContext, R.layout.bbs_comment_item, this);
		JSONObject objWriter = obj.getJSONObject("writer");
		
		
		TextView textContent = (TextView)findViewById(R.id.bbs_comment_contents);
		textContent.setText(obj.getString("contents"));
		
		TextView textDate = (TextView)findViewById(R.id.bbs_comment_date);
		//String dateString = DateUtil.getDateWithTimestamp(obj.getLong("timestamp") * 1000);
		Date date = new Date(obj.getLong("timestamp") * 1000);
		Locale locale = new Locale("en","US");//TODO change for localize
       java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, locale);
       String date1 = df.format(date).toString();
		textDate.setText(date1);
		
		TextView textName = (TextView)findViewById(R.id.bbs_comment_name);
		textName.setText(objWriter.getString("username"));
	}

}
