/**
 * 
 */
package net.wisedog.android.whooing.test;

import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.api.Balance;
import junit.framework.TestCase;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BalanceApiTest extends TestCase {
    protected String mToken;
    protected String mPIN;
    protected String mTokenSecret;
    protected String mUserId;
    protected String mAppSection;
    protected Balance mBalance;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        mBalance = new Balance();
        mToken = "13165741351c21b2088c12706c1acd1d63cf7b49";
        mPIN = "992505";
        mTokenSecret = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
        mUserId = "8955";
        mAppSection = "s10550";
        super.setUp();
    }
    
    public void testGetBalance(){
        JSONObject obj1 = mBalance.getBalance(mAppSection, Define.APP_ID, 
                mToken, Define.APP_SECRET, mTokenSecret, null);
        try{
            JSONObject obj = obj1.getJSONObject("results");
            JSONObject objCapital = obj.getJSONObject("capital");
            if(objCapital == null){
                fail("Capital value is null!");
            }
            JSONObject obj2 = obj.getJSONObject("liabilities");
            if(obj2 == null){
                fail("Liabilities value is null");
            }
            JSONObject objAccounts = obj2.getJSONObject("accounts");
            if(objAccounts == null){
                fail("Accounts value is null");
            }
            
        }catch(org.json.JSONException e){
            fail("JSON Exception ! ");
        }
        assertNotNull(obj1);
    }

}
