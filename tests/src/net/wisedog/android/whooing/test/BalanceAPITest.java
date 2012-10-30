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
public class BalanceAPITest extends TestCase {
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
        mToken = "ca01f5d4b108ae0fb14c60be98b24f353b57ba50";
        mPIN = "731251";
        mTokenSecret = "c753d2953d283694b378332d8f3919be155748b7";
        mUserId = "93aa0354c21629add8f373887b15f0e3";
        mAppSection = "s2128";
        super.setUp();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }
    
    public void testGetBalance(){
        JSONObject obj = mBalance.getBalance(mAppSection, Define.APP_ID, 
                mToken, Define.APP_SECRET, mTokenSecret, null);
        assertNotNull(obj);
    }

}
