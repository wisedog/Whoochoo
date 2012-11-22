/**
 * 
 */
package net.wisedog.android.whooing.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class SmsFilter {
    static public final String KEY_ITEM = "item";
    static public final String KEY_CARDNAME = "cardname";
    static public final String KEY_DATE = "date";
    static public final String KEY_MONEY = "money";

    public Bundle filterMessage(String msg){
        
        //Get patterns from pattern DB
        //compare with patterns
        //if none exists, return null
        String account = null;
        String money = null;
        String item = null;
        Pattern p = Pattern.compile("\\[([^&]+)\\]([^&]+)님 ([^&]+)원 ([^&]+) 누적액");
        Matcher m = p.matcher(msg);
        if(m.find()){
            account = m.group(1);
            money = m.group(3);
            item = m.group(4);
        }
        Bundle b = null;
        if(account != null){
            b = new Bundle();
            b.putString(KEY_CARDNAME, account);
            b.putString(KEY_MONEY, money);
            b.putString(KEY_ITEM, item);
        }
        return b;
    }
    
    public Bundle filterMessages(String msg){
        
        return null;
    }
    
    
}
