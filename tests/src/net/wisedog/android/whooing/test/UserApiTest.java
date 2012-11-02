/**
 * 
 */
package net.wisedog.android.whooing.test;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.api.UserAPI;
import junit.framework.TestCase;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class UserApiTest extends TestCase {
    protected String mToken;
    protected String mPIN;
    protected String mTokenSecret;
    protected String mUserId;
    protected String mAppSection;
    
    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        mToken = "13165741351c21b2088c12706c1acd1d63cf7b49";
        mPIN = "992505";
        mTokenSecret = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
        mUserId = "8955";
        mAppSection = "s10550";
        super.setUp();
    }

    /**
     * Test method for
     * {@link net.wisedog.android.whooing.api.MainInfo#getInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    public void testGetInfo() {
        UserAPI mUserApi = new UserAPI();
        JSONObject obj = mUserApi.getUserInfo(Define.APP_ID, mToken, Define.APP_SECRET,
                mTokenSecret);
        try{
            JSONObject objResult = obj.getJSONObject("results");
            assertNotNull(objResult);
            assertEquals("8955", objResult.getString("user_id"));
            assertEquals("Wisedog", objResult.getString("username"));
            assertEquals("KRW", objResult.getString("currency"));
            assertEquals("KR", objResult.getString("country"));
            assertEquals("ko", objResult.getString("language"));
        }
        catch(JSONException e){
            fail("JSONException in User GetInfo");
        }
    }

}
