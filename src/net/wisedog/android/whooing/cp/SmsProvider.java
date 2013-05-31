/*
  Copyright 2013 Jongha Kim(me@wisedog.net)
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package net.wisedog.android.whooing.cp;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.Whooing;
import net.wisedog.android.whooing.db.AccountsDbOpenHelper;
import net.wisedog.android.whooing.db.SmsDbOpenHelper;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

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
    		Toast.makeText(getContext(), "Receiving a message.", 
                    Toast.LENGTH_LONG).show();
    		
    		mSmsDb.addMessage(values);
    		Context context = getContext();
    		
    		NotificationCompat.Builder mBuilder =
    		        new NotificationCompat.Builder(context)
    		        .setSmallIcon(R.drawable.ic_launcher)
    		        .setContentTitle(context.getString(
    		        		net.wisedog.android.whooing.R.string.app_name))
    		        .setContentText("1건의 카드거래가 입력되었습니다");
    		// Creates an explicit intent for an Activity in your app
    		Intent resultIntent = new Intent(context, Whooing.class);
    		
    		// The stack builder object will contain an artificial back stack for the
    		// started Activity.
    		// This ensures that navigating backward from the Activity leads out of
    		// your application to the Home screen.
    		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    		// Adds the back stack for the Intent (but not the Intent itself)
    		stackBuilder.addParentStack(Whooing.class);
    		// Adds the Intent that starts the Activity to the top of the stack
    		stackBuilder.addNextIntent(resultIntent);
    		PendingIntent resultPendingIntent =
    		        stackBuilder.getPendingIntent(
    		            0,
    		            PendingIntent.FLAG_UPDATE_CURRENT
    		        );
    		mBuilder.setContentIntent(resultPendingIntent);
    		NotificationManager mNotificationManager =
    		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    		// mId allows you to update the notification later on.
    		mNotificationManager.notify(0, mBuilder.build());
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
