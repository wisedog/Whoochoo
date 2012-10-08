package net.wisedog.android.whooing.views;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MountainGraph extends View {
	float[] mAssetsData;
	float[] mLiabilitiesData;
	Paint mPaint;
	
	int mWidth = 0;
	int mHeight = 0;
	float mMax = 0;
	float mMin = 0;
	
	Context mContext;

	public MountainGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MountainGraph(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MountainGraph(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mContext = context;
	}
	
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		
	}

	/**
	 * 최근 3개월간의 순자산 및 부채 변동에 대한 데이터 입력이다.
	 * @param		assets		순자산 데이터값
	 * @param		liabilities	부채 데이터값
	 * */
	public void initAndDraw(float[] assets, float[] liabilities){
		//TODO Validate data integrity
		mAssetsData = assets;
		mLiabilitiesData = liabilities;
		
		if(getMax(assets) >= getMax(liabilities)){
			mMax = getMax(assets);
		}
		else{
			mMax = getMax(liabilities);
		}
		
		if(getMin(assets) <= getMin(liabilities)){
			mMin = getMin(assets);
		}
		else{
			mMin = getMin(liabilities);
		}
		
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(5);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawColor(Color.GRAY);
		
		float diff = mMax - mMin;
		//점찍을 X좌표들 
		float[] xPoints = new float[] {5.0f, (mWidth-10.0f)/2 , mWidth-5.0f};
		
		mPaint.setColor(Color.BLACK);
		canvas.drawLine(10, 20, 30, 50, mPaint) ;
		Toast.makeText(mContext, 
				"width:"+mWidth+"/height:"+mHeight+"Min:"+mMin+"Max:"+mMax
				, Toast.LENGTH_SHORT).show();
	}
	
	protected float getMax(float[] data){
		float max = data[0];

	    for (int i = 1; i < data.length; i++){
	    	if (data[i] > max){
		    	  max = data[i];
	    	}
	    }
	    return max;
	}
	
	protected float getMin(float[] data){
		float min = data[0];

	    for (int i = 1; i < data.length; i++){
	    	if (data[i] < min){
		    	  min = data[i];
	    	}
	    }
	    return min;
	}
}
