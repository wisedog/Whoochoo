package net.wisedog.android.whooing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * An utility class for String
 * @author Wisedog(me@wisedog.net)
 * */
public class StringUtil {
	public static String convertStreamToString(InputStream is) {
        /**
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e(StringUtil.class.toString(), "String Util convertStreamToSting failed - IO Exception");
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            	Log.e(StringUtil.class.toString(), "String Util convertStreamToSting failed - InputStream close fail");
            	return null;
            }
        }
        return sb.toString();
    }
}
