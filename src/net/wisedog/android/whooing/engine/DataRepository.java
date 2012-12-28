/**
 * 
 */
package net.wisedog.android.whooing.engine;

import java.util.ArrayList;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCalendar;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * FragmentPager에서 Fragment가 DestroyView되기때문에 값을 가지고 있다. 
 * @author Wisedog(me@wisedog.net)
 */
public class DataRepository{
    static public final int BS_MODE = 0;
    static public final int PL_MODE = 1;
    static public final int MOUNTAIN_MODE = 2;
    static public final int BUDGET_MODE = 3;
    private JSONObject mBsValue = null;	//자산부채 - bs
    private JSONObject mPlValue = null;	//비용수익 - pl
    private JSONObject mMtValue = null; //Mountain
    private JSONObject mBudgetValue = null; //Budget
    private ArrayList<OnBsChangeListener> mBsObservers = new ArrayList<OnBsChangeListener>();
    private ArrayList<OnPlChangeListener> mPlObservers = new ArrayList<OnPlChangeListener>();
    private ArrayList<OnMountainChangeListener> mMtObservers = new ArrayList<OnMountainChangeListener>();
    private ArrayList<OnBudgetChangeListener> mBudgetObservers = new ArrayList<OnBudgetChangeListener>();
    
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
    public static interface OnBudgetChangeListener extends DataChangeListener{
        public void onBudgetUpdate(JSONObject obj);
    }
    
    //using singleton
    private static DataRepository dataRepository = new DataRepository();
    
    public static synchronized DataRepository getInstance(){
        return dataRepository;
    }
    
    public void init(){
        Define.REAL_TOKEN = "13165741351c21b2088c12706c1acd1d63cf7b49";
        Define.PIN = "992505";
        Define.TOKEN_SECRET = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
        Define.USER_ID = "8955"; 
        Define.APP_SECTION = "s10550";
    }
    
    public void refreshAll(){
    	Log.i("wisedog", "Refresh All");
    }
    
    public void refreshDashboardValue(){
        init();
        Bundle bundle = new Bundle();
        bundle.putString("end_date", WhooingCalendar.getTodayYYYYMM());
        bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMM(6));
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_MOUNTAIN, bundle);
        thread.start();
        
        Bundle bundleBudget = new Bundle();
        bundleBudget.putString("account", "expenses");
        bundleBudget.putString("end_date", WhooingCalendar.getTodayYYYYMM());
        bundleBudget.putString("start_date", WhooingCalendar.getTodayYYYYMM());
        ThreadRestAPI thread1 = new ThreadRestAPI(mHandler, Define.API_GET_BUDGET, bundleBudget);
        thread1.start();
    }
    
    public void refreshBsValue(){
        init();
        Bundle bundle = new Bundle();
        bundle.putString("end_date", WhooingCalendar.getTodayYYYYMMDD());
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_BALANCE, bundle);
        thread.start();
    }
    
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Define.MSG_API_OK) {
                JSONObject obj = (JSONObject)msg.obj;
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
                    mBudgetValue = obj;
                    for (OnBudgetChangeListener observer : mBudgetObservers) {
                        observer.onBudgetUpdate(obj);
                    }
                }
            }
        }
    };
    
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
        else if(observerMode == BUDGET_MODE){
            mBudgetObservers.add((OnBudgetChangeListener)o);
        }
        
    }
/*
    public void registerBsObserver(OnBsChangeListener o) {
        if(o != null){
            mBsObservers.add(o);
        }
    }
    
    public void registerPlObserver(OnPlChangeListener o){
        if(o != null){
            mPlObservers.add(o);
        }
    }*/

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
        else if(observerMode == BUDGET_MODE){
            int idx = mBudgetObservers.indexOf(o);
            if(idx > 0) {
                mBudgetObservers.remove(idx);
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
    public JSONObject getBudgetValue(){
        return mBudgetValue;
    }
    
    public JSONObject getBsValue(){
        return mBsValue;
    }
    
    /*public void removeBsObserver(OnBsChangeListener o) {
        int idx = mBsObservers.indexOf(o);
        if(idx > 0) {
            mBsObservers.remove(idx);
       }
    }
    
    public void removePlObserver(OnPlChangeListener o) {
        int idx = mPlObservers.indexOf(o);
        if(idx > 0) {
            mPlObservers.remove(idx);
       }
    }*/
/*
    public void notifyObservers() {
        for (OnBsChangeListener observer : mBsObservers) {
            observer.onBsUpdate();
        }
        for (OnPlChangeListener observer : mPlObservers) {
            observer.onPlUpdate();
        }
    }*/

}