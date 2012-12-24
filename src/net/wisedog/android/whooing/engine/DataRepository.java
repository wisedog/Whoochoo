/**
 * 
 */
package net.wisedog.android.whooing.engine;

import java.util.ArrayList;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.network.ThreadRestAPI;
import net.wisedog.android.whooing.utils.WhooingCalendar;

import org.json.JSONArray;
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
    
    private JSONObject mBsValue = null;	//자산부채 - bs
    private JSONObject mPlValue = null;	//비용수익 - pl
    private JSONObject mMtValue = null; //Mountain
    private JSONArray mMountainArray = null;	//자산변동 - mountain
    private ArrayList<OnBsChangeListener> mBsObservers = new ArrayList<OnBsChangeListener>();
    private ArrayList<OnPlChangeListener> mPlObservers = new ArrayList<OnPlChangeListener>();
    private ArrayList<OnMountainChangeListener> mMtObservers = new ArrayList<OnMountainChangeListener>();
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
        //ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_BALANCE);
        //ThreadRestAPI thread1 = new ThreadRestAPI(mHandler, Define.API_GET_PL);
        Bundle bundle = new Bundle();
        bundle.putString("end_date", WhooingCalendar.getTodayYYYYMM());
        bundle.putString("start_date", WhooingCalendar.getPreMonthYYYYMM(6));
        ThreadRestAPI thread = new ThreadRestAPI(mHandler, Define.API_GET_MOUNTAIN, bundle);
        thread.start();
        //thread1.start();
    }
    
    /**
     * Get Mountain values
     * */
    public JSONArray getMountainValue(){
    	return mMountainArray;
    }
    
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Define.MSG_API_OK) {
                if (msg.arg1 == Define.API_GET_BALANCE) {
                    JSONObject obj = (JSONObject)msg.obj;
                    mBsValue = obj;
                    for (OnBsChangeListener observer : mBsObservers) {
                        observer.onBsUpdate(obj);
                    }
                }else if(msg.arg1 == Define.API_GET_PL){
                    JSONObject obj = (JSONObject)msg.obj;
                    mPlValue = obj;
                    for (OnPlChangeListener observer : mPlObservers) {
                        observer.onPlUpdate(obj);
                    }
                }
                else if(msg.arg1 == Define.API_GET_MOUNTAIN){
                    JSONObject obj = (JSONObject)msg.obj;
                    mMtValue = obj;
                    for (OnMountainChangeListener observer : mMtObservers) {
                        observer.onMountainUpdate(obj);
                    }
                }
            }
        }
    };
    
    public void registerObserver(DataChangeListener o, int observerMode){
        if(o == null){
            return;
        }
        if(observerMode == 1){
            mBsObservers.add((OnBsChangeListener) o);
        }else if(observerMode == 2){
            mPlObservers.add((OnPlChangeListener) o);
        }else if(observerMode == 3){
            mMtObservers.add((OnMountainChangeListener)o);
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
        if(observerMode == 1){
            int idx = mBsObservers.indexOf(o);
            if(idx > 0) {
                mBsObservers.remove(idx);
           }
        }else if(observerMode == 2){
            int idx = mPlObservers.indexOf(o);
            if(idx > 0) {
                mPlObservers.remove(idx);
           }
        }
        else if(observerMode == 3){
            int idx = mMtObservers.indexOf(o);
            if(idx > 0) {
                mMtObservers.remove(idx);
           }
        }
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
