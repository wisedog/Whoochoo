/**
 * 
 */
package net.wisedog.android.whooing.test;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * @author newmoni
 *
 */
public class GeneralProcessorTests extends AndroidTestCase {
    private String exampleJSON1 = "{\"account_id\" : \"x1\",\"type\" : \"group\",\"title\" : \"유동자산\","
            + "\"memo\" : \"바로쓸 수 있는 것들\",\"open_date\" : 20090511,"
            +"\"close_date\" : 20160101,\"category\" : \"\"}";

    /* (non-Javadoc)
     * @see android.test.AndroidTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /* (non-Javadoc)
     * @see android.test.AndroidTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + AccountsDbOpenHelper.TABLE_ACCOUNTS);
        db.close();
        mContext.deleteDatabase(AccountsDbOpenHelper.DATABASE_NAME);
        super.tearDown();
    }
    
    public void testCheckingAccountsInfoSuccessCase() throws JSONException{
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        GeneralProcessor general = new GeneralProcessor(mContext);
        JSONObject obj = new JSONObject(exampleJSON1);
        AccountsEntity info = new AccountsEntity("assets", obj);
        assertEquals(true, dbHelper.addAccountEntity(info));
        assertEquals(true, general.checkingAccountsInfo());
    }
    
    @SuppressWarnings("unused")
    public void testCheckingAccountsInfoFailCase(){
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        mContext.deleteDatabase(AccountsDbOpenHelper.DATABASE_NAME);
        GeneralProcessor general = new GeneralProcessor(mContext);
        assertEquals(false, general.checkingAccountsInfo());
    }
    
    public void testFillAccountsTable(){
        mContext.deleteDatabase(AccountsDbOpenHelper.DATABASE_NAME);
        String str = "{\"liabilities\":[{\"category\":\"normal\",\"title\":\"갚을돈\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x20\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"creditcard\",\"title\":\"신용카드\",\"opt_pay_account_id\":\"x2\",\"memo\":\"\",\"account_id\":\"x21\",\"open_date\":20100101,\"opt_use_date\":\"p1\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":25},{\"category\":\"checkcard\",\"title\":\"국민체크카드\",\"opt_pay_account_id\":\"x2\",\"memo\":\"\",\"account_id\":\"x22\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"}],\"assets\":[{\"category\":\"normal\",\"title\":\"현금\",\"opt_pay_account_id\":\"\",\"memo\":\"기본적으로 있는 자산항목\",\"account_id\":\"x1\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"normal\",\"title\":\"우리은행\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x2\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"}],\"capital\":[{\"category\":\"normal\",\"title\":\"기초잔액\",\"opt_pay_account_id\":\"\",\"memo\":\"기초잔액 설정시 이용\",\"account_id\":\"x40\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"normal\",\"title\":\"자본조정\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x75\",\"open_date\":20121029,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"}],\"income\":[{\"category\":\"steady\",\"title\":\"월급\",\"opt_pay_account_id\":\"\",\"memo\":\"월 정기급여\",\"account_id\":\"x70\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"floating\",\"title\":\"보너스\",\"opt_pay_account_id\":\"\",\"memo\":\"월 정기급여 외 상여금\",\"account_id\":\"x71\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"steady\",\"title\":\"이자수익\",\"opt_pay_account_id\":\"\",\"memo\":\"CMA, 적금이자 등\",\"account_id\":\"x72\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"floating\",\"title\":\"펀드,주식\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x73\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"floating\",\"title\":\"할인수익\",\"opt_pay_account_id\":\"\",\"memo\":\"각종 포인트, 카드할인 등\",\"account_id\":\"x74\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"}],\"expenses\":[{\"category\":\"steady\",\"title\":\"식비\",\"opt_pay_account_id\":\"\",\"memo\":\"주식,간식 등\",\"account_id\":\"x50\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"steady\",\"title\":\"교통비\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x51\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"steady\",\"title\":\"주거,통신\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x52\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"floating\",\"title\":\"생활용품\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x53\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"floating\",\"title\":\"경조사비\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x54\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"steady\",\"title\":\"지식,문화\",\"opt_pay_account_id\":\"\",\"memo\":\"책,공연,콘서트 등\",\"account_id\":\"x55\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"},{\"category\":\"floating\",\"title\":\"의복,미용\",\"opt_pay_account_id\":\"\",\"memo\":\"\",\"account_id\":\"x56\",\"open_date\":20100101,\"opt_use_date\":\"\",\"close_date\":29991231,\"type\":\"account\",\"opt_pay_date\":\"\"}]}";
        JSONObject obj = null;
        try {
            obj = new JSONObject(str);
        } catch (JSONException e) {
            fail("Error on creating JSONObject");
            return;
        }
        
        GeneralProcessor general = new GeneralProcessor(mContext);
        try {
            assertEquals(true, general.fillAccountsTable(obj));
        } catch (JSONException e) {
            e.printStackTrace();
            fail("Error on inserting JSONObject");
        }
        AccountsDbOpenHelper dbHelper = new AccountsDbOpenHelper(mContext);
        Log.i("wisedog", "DB records count : " + dbHelper.getAccountsInfoCount());
    }

}
