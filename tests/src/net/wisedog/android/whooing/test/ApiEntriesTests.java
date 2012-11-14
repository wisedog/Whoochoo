/**
 * 
 */
package net.wisedog.android.whooing.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.api.Entries;
import junit.framework.TestCase;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class ApiEntriesTests extends TestCase {
    protected String mToken;
    protected String mPIN;
    protected String mTokenSecret;
    protected String mUserId;
    protected String mAppSection;
    protected Entries mEntries;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        mEntries = new Entries();
        mToken = "13165741351c21b2088c12706c1acd1d63cf7b49";
        mPIN = "992505";
        mTokenSecret = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
        mUserId = "8955";
        mAppSection = "s10550";
        super.setUp();
    }
    
    public void testPreconditions(){
        assertNotNull(mEntries);
    }
    
    public void testGetLatest() throws JSONException{
        JSONObject obj = mEntries.getLatest(mAppSection, Define.APP_ID, 
                mToken, Define.APP_SECRET, mTokenSecret,5);
        assertNotNull(obj);
        JSONArray objResult = obj.getJSONArray("results");
        assertTrue(objResult.length() > 0);
    }

}
