package net.wisedog.android.whooing.cp;

import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.db.SmsDbOpenHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SmsProvider extends ContentProvider {
    static final int TYPE_ACCOUNT = 100;
    static final int TYPE_SMS = 200;
    
    private AccountsDbOpenHelper mAccountDb;
    private SmsDbOpenHelper mSmsDb;
    
    private static final String AUTHORITY = "net.wisedog.android.whooing.contentprovider";
    
    static final UriMatcher Matcher;
    static{
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI(AUTHORITY, "account", TYPE_ACCOUNT);
        Matcher.addURI(AUTHORITY, "sms", TYPE_SMS);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Do not support delete feature
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        if(Matcher.match(uri) == TYPE_ACCOUNT) {
            return "vnd." + AUTHORITY + "/card";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    	if(Matcher.match(uri) == TYPE_SMS){
    		mSmsDb.addMessage(values);
    		//TODO Notification ... 
    	}
        
        return null;
    }

    @Override
    public boolean onCreate() {
        mAccountDb = new AccountsDbOpenHelper(getContext());
        mSmsDb = new SmsDbOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        if(Matcher.match(uri) == TYPE_ACCOUNT){
        	String sql = "SELECT * FROM " + AccountsDbOpenHelper.TABLE_ACCOUNTS;
            sql += " WHERE (account_type='liabilities' AND type='account') " +
            		"AND (category='creditcard' OR category='checkcard');";

            SQLiteDatabase db = mAccountDb.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            return cursor;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // Do not support update feature
        return 0;
    }

}
