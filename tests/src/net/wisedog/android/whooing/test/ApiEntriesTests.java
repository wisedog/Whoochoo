/**
 * 
 */
package net.wisedog.android.whooing.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.api.Entries;
import net.wisedog.android.whooing.db.AccountsEntity;
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
    
    public void testGetSerializedObject() throws JSONException{
        String exampleJSON = "{\"account_id\" : \"x1\",\"type\" : \"group\",\"title\" : \"유동자산\","
                + "\"memo\" : \"바로쓸 수 있는 것들\",\"open_date\" : 20090511,"
                +"\"close_date\" : 20160101,\"category\" : \"\"}";
        JSONObject obj1 = new JSONObject(exampleJSON);
        AccountsEntity left = new AccountsEntity("assets", obj1);
        
        String exampleJSON1 = "{\"account_id\" : \"x10\",\"type\" : \"account\",\"title\" :" +
                " \"신한카드\",\"memo\" : \"월 목표 사용액 : 50만원\",\"open_date\" : 20110101" +
                ",\"close_date\" : 21000101,\"category\" : \"creditcard\",\"opt_use_date\" " +
                ": \"p1\",\"opt_pay_date\" : 25,\"opt_pay_account_id\" : \"x1\"}";
        JSONObject obj2 = new JSONObject(exampleJSON1);
        AccountsEntity right = new AccountsEntity("liabilities", obj2);
        Bundle bundle = new Bundle();
        int formattedDate = 20121111;
        bundle.putInt("entry_date", formattedDate);
        bundle.putParcelable("l_account", left);
        bundle.putParcelable("r_account", right);
        bundle.putString("item", "abcdef_()#$%^&");
        bundle.putDouble("money", 12345.67);
        bundle.putString("memo", "1235678");
        String result = mEntries.getSerializedObject(mAppSection, bundle); 
        assertNotNull(result);
        
    }
}
