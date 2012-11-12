package net.wisedog.android.whooing.test;



import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.db.WhooingDbOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

public class WhooingDbOpenerTests extends AndroidTestCase {
    private WhooingDbOpenHelper mDb = null;
    private String exampleJSON1 = "{\"account_id\" : \"x1\",\"type\" : \"group\",\"title\" : \"유동자산\","
            + "\"memo\" : \"바로쓸 수 있는 것들\",\"open_date\" : 20090511,"
            +"\"close_date\" : 20160101,\"category\" : \"\"}";
    private String exampleJSON2 = "{\"account_id\" : \"x10\",\"type\" : \"account\",\"title\" :" +
            " \"신한카드\",\"memo\" : \"월 목표 사용액 : 50만원\",\"open_date\" : 20110101" +
            ",\"close_date\" : 21000101,\"category\" : \"creditcard\",\"opt_use_date\" " +
            ": \"p1\",\"opt_pay_date\" : 25,\"opt_pay_account_id\" : \"x1\"}";
    
    protected void setUp() throws Exception {
        super.setUp();
        mContext.deleteDatabase(WhooingDbOpenHelper.DATABASE_NAME);
        mDb = new WhooingDbOpenHelper(mContext);
    }
    
    
    
    /* (non-Javadoc)
     * @see android.test.AndroidTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        SQLiteDatabase db = mDb.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + WhooingDbOpenHelper.TABLE_ACCOUNTS);
        db.close();
        mContext.deleteDatabase(WhooingDbOpenHelper.DATABASE_NAME);
        super.tearDown();
    }



    public void testPreconditions(){
        assertNotNull(mDb);
    }
    
    public void testOnCreate(){
        SQLiteDatabase db = mDb.getReadableDatabase();
        if(db == null){
            fail("DB is not created!");
        }
        
        String tableName = SQLiteDatabase.findEditTable(WhooingDbOpenHelper.TABLE_ACCOUNTS); 
        if(tableName == null){
            fail("Table name'"+ WhooingDbOpenHelper.TABLE_ACCOUNTS + "' cannot be found!");
        }
        
    }

    public void testAddAccountEntity() throws JSONException {
        JSONObject obj = new JSONObject(exampleJSON1);
        AccountsEntity info = new AccountsEntity("assets", obj);
        assertEquals(true, mDb.addAccountEntity(info));
        assertEquals(1,mDb.getAccountsInfoCount());
        mDb.clearTable();
    }

    public void testGetAllAccountsInfo() throws JSONException {
        assertEquals(0, mDb.getAccountsInfoCount());
        JSONObject obj = new JSONObject(exampleJSON1);
        AccountsEntity info = new AccountsEntity("assets", obj);
        assertEquals(true, mDb.addAccountEntity(info));
        obj = new JSONObject(exampleJSON2);
        info = new AccountsEntity("liabilities", obj);
        assertEquals(true, mDb.addAccountEntity(info));
        List<AccountsEntity> list = mDb.getAllAccountsInfo();
        assertEquals(2, list.size());
        AccountsEntity entity = list.get(0);
        assertEquals("x1", entity.account_id);
    }

    public void testGetLastUpdateDate() {
        //fail("Not yet implemented");
    }

    public void testClearTable() throws JSONException {
        JSONObject obj = new JSONObject(exampleJSON1);
        AccountsEntity info = new AccountsEntity("assets", obj);
        assertEquals(true, mDb.addAccountEntity(info));
        assertEquals(1,mDb.getAccountsInfoCount());
        mDb.clearTable();
        assertEquals(0, mDb.getAccountsInfoCount());
    }
    
    public void testGetAccountById() throws JSONException{
        mDb.clearTable();
        JSONObject obj = new JSONObject(exampleJSON1);
        AccountsEntity info = new AccountsEntity("assets", obj);
        assertEquals(true, mDb.addAccountEntity(info));
        AccountsEntity info1 = mDb.getAccountById("x1");
        assertNotNull(info1);
        if(info.account_id.equals("x1") != true){
            fail("Not same value");
        }
    }

}
