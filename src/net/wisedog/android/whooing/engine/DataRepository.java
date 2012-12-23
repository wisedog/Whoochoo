/**
 * 
 */
package net.wisedog.android.whooing.engine;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * FragmentPager에서 Fragment가 DestroyView되기때문에 값을 가지고 있다. 
 * @author Wisedog(me@wisedog.net)
 */
public class DataRepository {
    private JSONObject mBsValue = null;	//자산부채 - bs
    private JSONObject mPlBalue = null;	//비용수익 - pl
    private JSONArray mMountainArray = null;	//자산변동 - mountain
    // 대쉬보드 - Asset/Doubt: bs,pl / Monthly Budget : pl/ Credit Card : bs 
    
    //using singleton
    private static DataRepository dataRepository = new DataRepository();
    
    public static synchronized DataRepository getInstance(){
        return dataRepository;
    }
    
    public void refreshAll(){
    	Log.i("wisedog", "Refresh All");
    }
    
    /**
     * Get Mountain values
     * */
    public JSONArray getMountainValue(){
    	return mMountainArray;
    }
}
