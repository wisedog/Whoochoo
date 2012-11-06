/**
 * 
 */
package net.wisedog.android.whooing.test;

import net.wisedog.android.whooing.db.AccountsEntity;

import org.json.JSONException;
import org.json.JSONObject;

import junit.framework.TestCase;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class AccountsEntityTests extends TestCase {

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testConstructionAssets() throws JSONException{
        String exampleJSON = "{\"account_id\" : \"x1\",\"type\" : \"group\",\"title\" : \"유동자산\","
                + "\"memo\" : \"바로쓸 수 있는 것들\",\"open_date\" : 20090511,"
                +"\"close_date\" : 20160101,\"category\" : \"\"}";
        JSONObject obj = new JSONObject(exampleJSON);
        AccountsEntity entity = new AccountsEntity("assets", obj);
        assertEquals(entity.accountType, "assets");
        assertEquals(entity.account_id , "x1");
        assertEquals(entity.type, "group");
        assertEquals(entity.title, "유동자산");
        assertEquals(entity.memo, "바로쓸 수 있는 것들");
        assertEquals(entity.open_date, 20090511);
        assertEquals(entity.close_date, 20160101);
        assertEquals(entity.category, "");
    }
    
    public void testConstructionLiabilities() throws JSONException{
        String exampleJSON = "{\"account_id\" : \"x10\",\"type\" : \"account\",\"title\" :" +
        		" \"신한카드\",\"memo\" : \"월 목표 사용액 : 50만원\",\"open_date\" : 20110101" +
        		",\"close_date\" : 21000101,\"category\" : \"creditcard\",\"opt_use_date\" " +
        		": \"p1\",\"opt_pay_date\" : 25,\"opt_pay_account_id\" : \"x1\"}";
        JSONObject obj = new JSONObject(exampleJSON);
        AccountsEntity entity = new AccountsEntity("liabilities",obj);
        assertEquals(entity.accountType, "liabilities");
        assertEquals(entity.account_id , "x10");
        assertEquals(entity.type, "account");
        assertEquals(entity.title, "신한카드");
        assertEquals(entity.memo, "월 목표 사용액 : 50만원");
        assertEquals(entity.open_date, 20110101);
        assertEquals(entity.close_date, 21000101);
        assertEquals(entity.category, "creditcard");
        assertEquals(entity.opt_use_date, "p1");
        assertEquals(entity.opt_pay_date, 25);
        assertEquals(entity.opt_pay_account_id, "x1");
    }
    
    public void testConstructionCapital() throws JSONException{
        String exampleJSON = "{\"account_id\" : \"x8\",\"type\" : \"account\",\"title\" :" +
        		" \"초기설정\",\"memo\" : \"기초자금 설정 및 자본수정\",\"open_date\" " +
        		": 20100101,\"close_date\" : 20100101,\"category\" : \"\"}";
        JSONObject obj = new JSONObject(exampleJSON);
        AccountsEntity entity = new AccountsEntity("capital", obj);
        assertEquals(entity.accountType, "capital");
        assertEquals(entity.account_id , "x8");
        assertEquals(entity.type, "account");
        assertEquals(entity.title, "초기설정");
        assertEquals(entity.memo, "기초자금 설정 및 자본수정");
        assertEquals(entity.open_date, 20100101);
        assertEquals(entity.close_date, 20100101);
        assertEquals(entity.category, "");
    }
    
    public void testConstructionIncome() throws JSONException{
        String exampleJSON = "{\"account_id\" : \"x21\",\"type\" : \"account\",\"title\" :" +
        		" \"주수익\",\"memo\" : \"월급 및 기타소득\",\"open_date\" : 20010101,\"close_date\" :" +
        		" 21000101,\"category\" : \"steady\"}";
        JSONObject obj = new JSONObject(exampleJSON);
        AccountsEntity entity = new AccountsEntity("income", obj);
        assertEquals(entity.accountType, "income");
        assertEquals(entity.account_id , "x21");
        assertEquals(entity.type, "account");
        assertEquals(entity.title, "주수익");
        assertEquals(entity.memo, "월급 및 기타소득");
        assertEquals(entity.open_date, 20010101);
        assertEquals(entity.close_date, 21000101);
        assertEquals(entity.category, "steady");
    }
    
    public void testConstructionExpenses() throws JSONException{
        String exampleJSON = "{\"account_id\" : \"x23\",\"type\" : \"account\",\"title\" :" +
        		" \"식비\",\"memo\" : \"일반 생활식비\",\"open_date\" : 20010101,\"close_date\" :" +
        		" 21000101,\"category\" : \"steady\"}";
        JSONObject obj = new JSONObject(exampleJSON);
        AccountsEntity entity = new AccountsEntity("expenses", obj);
        assertEquals(entity.accountType, "expenses");
        assertEquals(entity.account_id , "x23");
        assertEquals(entity.type, "account");
        assertEquals(entity.title, "식비");
        assertEquals(entity.memo, "일반 생활식비");
        assertEquals(entity.open_date, 20010101);
        assertEquals(entity.close_date, 21000101);
        assertEquals(entity.category, "steady");
    }
    
    public void testConstructionFailTest(){
        String exampleJSON = "{\"type\" : \"account\",\"title\" :" +
                " \"식비\",\"memo\" : \"일반 생활식비\",\"open_date\" : 20010101,\"close_date\" :" +
                " 21000101,\"category\" : \"steady\"}";
        JSONObject obj;
        try {
            obj = new JSONObject(exampleJSON);
        } catch (JSONException e) {
            fail("JSON converting error!");
            return;
        }
        
        try {
            AccountsEntity entity = new AccountsEntity("expenses", obj);
        } catch (JSONException e) {
            assertTrue("Pass", true);
        }
    }

}
