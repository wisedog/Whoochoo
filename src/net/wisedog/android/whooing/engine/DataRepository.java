/**
 * 
 */
package net.wisedog.android.whooing.engine;

import java.util.ArrayList;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.activity.MainFragmentActivity;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

/**
 * FragmentPager에서 Fragment가 DestroyView되기때문에 값을 가지고 있다. 
 * @author Wisedog(me@wisedog.net)
 */
public class DataRepository{
    static public final int BS_MODE = 0;
    static public final int PL_MODE = 1;
    static public final int MOUNTAIN_MODE = 2;
    static public final int EXP_BUDGET_MODE = 3;
    static public final int USER_MODE = 4;
    
    private JSONObject mBsValue = null;	//자산부채 - bs
    private JSONObject mPlValue = null;	//비용수익 - pl
    private JSONObject mMtValue = null; //Mountain
    private JSONObject mExpBudgetValue = null; //Budget
    private JSONObject mUserValue = null;	//User data
    /** Frequent item info*/
    private JSONObject mLastestItem = null;    
    
    private ArrayList<OnBsChangeListener> mBsObservers = new ArrayList<OnBsChangeListener>();
    private ArrayList<OnPlChangeListener> mPlObservers = new ArrayList<OnPlChangeListener>();
    private ArrayList<OnMountainChangeListener> mMtObservers = new ArrayList<OnMountainChangeListener>();
    private ArrayList<OnExpBudgetChangeListener> mExpBudgetObservers = new ArrayList<OnExpBudgetChangeListener>();
    private ArrayList<OnUserChangeListener> mUserObservers = new ArrayList<OnUserChangeListener>();
	private Context mContext;
	
	private int mRestApiNum = 0;
    
//    private ArrayList<DataChangeListener> mDataObservers = new ArrayList<DataChangeListener>();
    // 대쉬보드 - Asset/Doubt: bs,pl / Monthly Budget : pl/ Credit Card : bs 
    
    public interface DataChangeListener{
        ;
    }
    public static interface OnBsChangeListener extends DataChangeListener {
        public void onBsUpdate(JSONObject obj);
    }
    public static interface OnPlChangeListener extends DataChangeListener{
        public void onPlUpdate(JSONObject obj);
    }
    public static interface OnMountainChangeListener extends DataChangeListener{
        public void onMountainUpdate(JSONObject obj);
    }
    public static interface OnExpBudgetChangeListener extends DataChangeListener{
        public void onExpBudgetUpdate(JSONObject obj);
    }
    public static interface OnUserChangeListener extends DataChangeListener{
        public void onUserUpdate(JSONObject obj);
    }
    
    //using singleton
    private static DataRepository dataRepository = new DataRepository();
    
    public static synchronized DataRepository getInstance(){
        return dataRepository;
    }
    
    /**
     * Init here
     *
     * */
    private void init(){
        //FIXME 여기 릴리즈 전 고칠것
        /*Define.REAL_TOKEN = "13165741351c21b2088c12706c1acd1d63cf7b49";
        Define.PIN = "992505";
        Define.TOKEN_SECRET = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
        Define.USER_ID = "8955"; 
        Define.APP_SECTION = "s10550";*/
    }
    
	/**
	 * Refresh Dashboard infomation from server
	 * */
	public void refreshDashboardValue(Context context) {
	    mContext = context;
		init();
		Bundle bundle = new Bundle();
		bundle.putString("end_date", WhooingCalendar.getTodayYYYYMM());
		bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMM(6));
		ThreadRestAPI thread = new ThreadRestAPI(mHandler,
				Define.API_GET_MOUNTAIN, bundle);
		thread.start();

		Bundle bundleBudget = new Bundle();
		bundleBudget.putString("account", "expenses");
		bundleBudget.putString("end_date", WhooingCalendar.getTodayYYYYMM());
		bundleBudget.putString("start_date", WhooingCalendar.getTodayYYYYMM());
		ThreadRestAPI thread1 = new ThreadRestAPI(mHandler,
				Define.API_GET_BUDGET, bundleBudget);
		thread1.start();
	}
    
    /**
     * Refresh Balance infomation from server
     * */
    public void refreshBsValue(Context context){
        mContext = context;
		init();
		Bundle bundle = new Bundle();
		bundle.putString("end_date", WhooingCalendar.getTodayYYYYMMDD());
		ThreadRestAPI thread = new ThreadRestAPI(mHandler,
				Define.API_GET_BALANCE, bundle);
		thread.start();
    }
    
    /**
     * Refresh Profit/Loss infomation from server
     * */
    public void refreshPlValue(Context context){
        mContext = context;
        init();
        Bundle bundle = new Bundle();
        bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMMDD(1));
        bundle.putString("end_date", WhooingCalendar.getTodayYYYYMMDD());
        ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                Define.API_GET_PL, bundle);
        thread.start();
    }
    
    /**
     * Refresh account infomation from server
     * @param		context		Context that is needed for accessing database
     * */
    public void refreshAccount(Context context) {
    	mContext = context;
    	ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                Define.API_GET_ACCOUNTS);
        thread.start();
	}
    
    public void refreshExpBudget(Context context){
        mContext = context;
        init();
        Bundle bundle = new Bundle();
        bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMMDD(1));
        bundle.putString("end_date", WhooingCalendar.getTodayYYYYMMDD());
        ThreadRestAPI thread = new ThreadRestAPI(mHandler,
                Define.API_GET_PL, bundle);
        thread.start();
    }
    
	public void refreshUserInfo(Context context) {
		mContext = context;
		init();
		ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_USER_INFO);
		thread.start();
	}
	
	public void refreshLastestItems(Context context){
	    init();
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_ENTRIES_LATEST_ITEMS);
        thread.start();
	}
    
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Define.MSG_API_OK) {
                JSONObject obj = (JSONObject)msg.obj;
                if(obj != null){
                    int returnCode = 200;
                    try{                        
                        mRestApiNum = obj.getInt("rest_of_api");
                        setRestApi(mContext, mRestApiNum);
                        returnCode = obj.getInt("code");
                    }catch(JSONException e){
                        setRestApi(mContext, 0);
                    }
                    if(setCodeHandling(returnCode) == false){
                        return;
                    }
                }
                
                if (msg.arg1 == Define.API_GET_BALANCE) {
                    mBsValue = obj;
                    for (OnBsChangeListener observer : mBsObservers) {
                        observer.onBsUpdate(obj);
                    }
                }else if(msg.arg1 == Define.API_GET_PL){
                    mPlValue = obj;
                    for (OnPlChangeListener observer : mPlObservers) {
                        observer.onPlUpdate(obj);
                    }
                }
                else if(msg.arg1 == Define.API_GET_MOUNTAIN){
                    mMtValue = obj;
                    for (OnMountainChangeListener observer : mMtObservers) {
                        observer.onMountainUpdate(obj);
                    }
                }
                else if(msg.arg1 == Define.API_GET_BUDGET){
                    mExpBudgetValue = obj;
                    for (OnExpBudgetChangeListener observer : mExpBudgetObservers) {
                        observer.onExpBudgetUpdate(obj);
                    }
                }
                else if(msg.arg1 == Define.API_GET_ACCOUNTS){
                	if(mContext != null){
                		GeneralProcessor general = new GeneralProcessor(mContext);
                		try {
                			JSONObject objResult = obj.getJSONObject("results");
							general.fillAccountsTable(objResult);
						} catch (JSONException e) {
							e.printStackTrace();
						}
                		}
                }
                else if(msg.arg1 == Define.API_GET_USER_INFO){
                	if(mContext != null){
                		mUserValue = obj;
                        for (OnUserChangeListener observer : mUserObservers) {
                            observer.onUserUpdate(obj);
                        }
                	}
                }
                else if(msg.arg1 == Define.API_GET_ENTRIES_LATEST_ITEMS){
                    mLastestItem = obj;
                    Log.i("wisedog", "lastest items : " + obj.toString());
                }
                else if(msg.arg1 == Define.API_GET_FREQUENT_ITEM){
                    mLastestItem = obj;
                    Log.i("wisedog", "Frequent Item " + obj.toString());
                }
            }
        }
    };
    
    protected void setRestApi(Context context, final int apiNum){
        if(context != null){
            TextView textView = (TextView) ((Activity)context).findViewById(MainFragmentActivity.API_MENUITEM_ID);
            if(textView != null){
                if(apiNum == 0){
                    textView.setText("Api\r\n -");
                }else{
                    textView.setText("Api\r\n "+String.valueOf(apiNum));
                }
            }
        }
    }
    
    public void refreshRestApi(Context context){
        setRestApi(context, mRestApiNum);
    }
    
    /**
     * @param returnCode
     * @return
     */
    protected boolean setCodeHandling(int returnCode) {
        // TODO Auto-generated method stub
        return true;
    }

    public void registerObserver(DataChangeListener o, int observerMode){
        if(o == null){
            return;
        }
        if(observerMode == BS_MODE){
            mBsObservers.add((OnBsChangeListener) o);
        }
        else if(observerMode == PL_MODE){
            mPlObservers.add((OnPlChangeListener) o);
        }
        else if(observerMode == MOUNTAIN_MODE){
            mMtObservers.add((OnMountainChangeListener)o);
        }
        else if(observerMode == EXP_BUDGET_MODE){
            mExpBudgetObservers.add((OnExpBudgetChangeListener)o);
        }
        else if(observerMode == USER_MODE){
            mUserObservers.add((OnUserChangeListener)o);
        }
        
    }

    public void removeObserver(DataChangeListener o, int observerMode) {
        if(observerMode == BS_MODE){
            int idx = mBsObservers.indexOf(o);
            if(idx > 0) {
                mBsObservers.remove(idx);
           }
        }else if(observerMode == PL_MODE){
            int idx = mPlObservers.indexOf(o);
            if(idx > 0) {
                mPlObservers.remove(idx);
           }
        }
        else if(observerMode == MOUNTAIN_MODE){
            int idx = mMtObservers.indexOf(o);
            if(idx > 0) {
                mMtObservers.remove(idx);
           }
        }
        else if(observerMode == EXP_BUDGET_MODE){
            int idx = mExpBudgetObservers.indexOf(o);
            if(idx > 0) {
                mExpBudgetObservers.remove(idx);
           }
        }
        else if(observerMode == USER_MODE){
        	int idx = mUserObservers.indexOf(o);
        	if(idx > 0){
        		mUserObservers.remove(idx);
        	}
        }
    }
    
    /**
     * @return  Return saved mountain value
     * */
    public JSONObject getMtValue(){
        return mMtValue;
    }
    
    /**
     * @return  Return saved budget value
     * */
    public JSONObject getExpBudgetValue(){
        return mExpBudgetValue;
    }
    
    public JSONObject getBsValue(){
        return mBsValue;
    }
    
    public JSONObject getPlValue(){
        return mPlValue;
    }
    
    public JSONObject getUserValue(){
    	return mUserValue;
    }
    
    public JSONObject getLastestItems(){
        return mLastestItem;
    }
}
