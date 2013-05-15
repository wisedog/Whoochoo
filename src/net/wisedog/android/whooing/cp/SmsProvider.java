package net.wisedog.android.whooing.cp;

import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.db.SmsDbOpenHelper;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SmsProvider extends ContentProvider {
    static final int ALL_CARD = 1;
    
    private AccountsDbOpenHelper mAccountDb;
    private SmsDbOpenHelper mSmsDb;
    
    private static final String AUTHORITY = "net.wisedog.whooing.contentprovider";

    private static final String BASE_PATH = "sms";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
        + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
        + "/sms";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
        + "/smsitem";

    
    static final UriMatcher Matcher;
    static{
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI(AUTHORITY, "account", ALL_CARD);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Do not support delete feature
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        if(Matcher.match(uri) == ALL_CARD) {
            return "vnd." + AUTHORITY + "/card";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mSmsDb.addMessage(values);
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
        String sql = "SELECT * FROM " + AccountsDbOpenHelper.TABLE_ACCOUNTS;
        if(Matcher.match(uri) == ALL_CARD){
            sql += " WHERE account_type='liabilities' AND (category='creditcard' OR category='checkcard');";
        }
        SQLiteDatabase db = mAccountDb.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        // Do not support update feature
        return 0;
    }

}
