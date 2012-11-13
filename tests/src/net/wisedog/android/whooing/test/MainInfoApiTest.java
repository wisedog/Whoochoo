/**
 * 
 */
package net.wisedog.android.whooing.test;

import org.json.JSONArray;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.api.MainInfo;
import junit.framework.TestCase;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class MainInfoApiTest extends TestCase {
    protected String mToken;
    protected String mPIN;
    protected String mTokenSecret;
    protected String mUserId;
    protected String mAppSection;
    protected MainInfo mMainInfo;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        mMainInfo = new MainInfo();
        mToken = "13165741351c21b2088c12706c1acd1d63cf7b49";
        mPIN = "992505";
        mTokenSecret = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
        mUserId = "8955";
        mAppSection = "s10550";
        super.setUp();
    }
    
    public void testGetInfo() {
        JSONObject obj1 = mMainInfo.getInfo(mAppSection, Define.APP_ID, mToken, Define.APP_SECRET,
                mTokenSecret);
        try{
            JSONObject obj = (JSONObject)obj1.getJSONObject("results");
            JSONObject objBill = obj.getJSONObject("bill");
            assertNotNull(objBill);            
            JSONObject objAggregate = objBill.getJSONObject("aggregate");
            assertNotNull(objAggregate);
            JSONArray objAccountsArray = objAggregate.getJSONArray("accounts");
            assertNotNull(objAccountsArray);
            
            JSONObject objBudget = obj.getJSONObject("budget");
            assertNotNull(objBudget);
            JSONArray objRows = objBudget.getJSONArray("rows");
            assertNotNull(objRows);
            JSONObject objMountain = obj.getJSONObject("mountain");
            assertNotNull(objMountain);
            JSONObject objInOut = obj.getJSONObject("in_out");
            assertNotNull(objInOut);
        }
        catch(org.json.JSONException e){
            fail("JSONException!!!");
        }
    }

}
