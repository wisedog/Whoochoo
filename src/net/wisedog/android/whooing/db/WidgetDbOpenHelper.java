/**
 * 
 */
package net.wisedog.android.whooing.db;

import java.util.ArrayList;
import java.util.List;

import net.wisedog.android.whooing.utils.WhooingCalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author Wisedog
 *
 */
public class WidgetDbOpenHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "Movie";
 
    // Contacts table name
    private static final String TABLE_MOVIE_RECENT = "recent";
 
    // Contacts Table Columns names
    private static final String KEY_ENTITY_ID = "entity_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENRE = "genre";
    private static final String KEY_SCORE = "score";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_DATE_DIFF = "date_diff";
    private static final String KEY_AGE_LIMIT = "age_limit";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_RUNTIME = "runtime";
    private static final String KEY_BOOKING_RATE = "booking_rate";
    private static final String KEY_UPDATE_DATE = "update_date";
    
    public WidgetDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public WidgetDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MOVIE_RECENT + "("
                + KEY_ENTITY_ID + " INTEGER PRIMARY KEY," 
                + KEY_NAME + " TEXT,"
                + KEY_GENRE + " TEXT," 
                + KEY_SCORE + " INTEGER,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT,"
                + KEY_DATE_DIFF + " TEXT,"
                + KEY_AGE_LIMIT + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_RUNTIME + " TEXT,"
                + KEY_BOOKING_RATE + " TEXT,"
                + KEY_UPDATE_DATE + "INTEGER"
                +")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /* (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_RECENT);

        // Create tables again
        onCreate(db);
    }
    
    
    
    /**
     * 영화정보 레코드 추가
    * @param       info     DataClass of Recent Movie info         
     * */
    public void addMovieEntity(RecentMovieInfo info){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ENTITY_ID, info.getEntityId());
        values.put(KEY_NAME, info.getName());
        values.put(KEY_GENRE, info.getGenre());
        values.put(KEY_SCORE, info.getScore());
        values.put(KEY_IMAGE_URL, info.getImageUrl());
        values.put(KEY_IMAGE_PATH, info.getImagePath());
        values.put(KEY_DATE_DIFF, info.getDateDiff());
        values.put(KEY_AGE_LIMIT, info.getAgeLimit());
        values.put(KEY_COUNTRY, info.getCountry());
        values.put(KEY_RUNTIME, info.getRuntime());
        values.put(KEY_BOOKING_RATE, info.getBookingRate());
        values.put(KEY_UPDATE_DATE, WhooingCalendar.getTodayYYYYMMDDint());
        long result = db.insert(TABLE_MOVIE_RECENT, null, values);
        if(result == -1){
            Log.e(this.getClass().toString(), "DB Insert Error");
        }
    }

    public List<RecentMovieInfo> getAllMovieInfo() {
        List<RecentMovieInfo> movieList = new ArrayList<RecentMovieInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE_RECENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecentMovieInfo movieInfo = new RecentMovieInfo();
                movieInfo.setEntityId(Integer.parseInt(cursor.getString(0)));
                movieInfo.setName(cursor.getString(1));
                movieInfo.setGenre(cursor.getString(2));
                movieInfo.setScore(Integer.parseInt(cursor.getString(3)));
                movieInfo.setImageUrl(cursor.getString(4));
                movieInfo.setImagePath(cursor.getString(5));
                movieInfo.setDateDiff(cursor.getString(6));
                movieInfo.setAgeLimit(cursor.getString(7));
                movieInfo.setCountry(cursor.getString(8));
                movieInfo.setRuntime(cursor.getString(9));
                movieInfo.setBookingRate(cursor.getString(10));
                movieInfo.setUpdateDate(Integer.parseInt(cursor.getString(11)));
                // Adding contact to list
                movieList.add(movieInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        
        if(movieList.isEmpty() == true){
            return null;
        }
        // return contact list
        return movieList;
    }

    /**
     * 최근영화정보 테이블의 레코드 수를 리턴한다. 
     * @return  레코드수 리턴
     * */
    public int getMovieInfoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MOVIE_RECENT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    /**
     * 마지막 업데이트 날짜를 리턴한다.
     * @return 데이터가 없는 등 문제가 생기면 0을 리턴, 그렇지 않으면 20121030과 같은 정수값을 리턴
     * */
    public int getLastUpdateDate(){
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIE_RECENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            return Integer.parseInt(cursor.getString(11));
        }
        else{
            return 0;
        }
    }

    public void clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_RECENT);

        // Create tables again
        onCreate(db);
    }

}
