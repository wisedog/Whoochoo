package net.wisedog.android.whooing.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.api.GeneralApi;
import junit.framework.TestCase;

public class GeneralApiTests extends TestCase {
    protected String mToken;
    protected String mPIN;
    protected String mTokenSecret;
    protected String mUserId;
    protected String mAppSection;

    protected void setUp() throws Exception {
        mToken = "13165741351c21b2088c12706c1acd1d63cf7b49";
        mPIN = "992505";
        mTokenSecret = "e56d804b1a703625596ed3a1fd0f4c529fc2ff2c";
        mUserId = "8955";
        mAppSection = "s10550";
        super.setUp();
    }

    public void testGetInfo() throws JSONException {
        GeneralApi api = new GeneralApi();
        JSONObject obj = api.getInfo("https://whooing.com/api/accounts.json_array?section_id="+mAppSection, 
                Define.APP_ID, mToken,
                Define.APP_SECRET, mTokenSecret);
        assertNotNull(obj);
        JSONObject objResult = obj.getJSONObject("results");
        assertNotNull(objResult);
        JSONArray objLiabilities = objResult.getJSONArray("liabilities");
        assertNotNull(objLiabilities);
        if(objLiabilities.length() == 0){
            fail("No items in Liabilities");
        }
        JSONArray objIncome = objResult.getJSONArray("income");
        if(objIncome.length() == 0){
            fail("No items in Income");
        }
        
    }

}
