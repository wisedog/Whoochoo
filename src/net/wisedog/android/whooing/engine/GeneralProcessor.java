/**
 * 
 */
package net.wisedog.android.whooing.engine;

import net.wisedog.android.whooing.db.WhooingDbOpenHelper;
import android.app.Activity;
import android.content.Context;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class GeneralProcessor {
	Context mContext = null;
	
	public GeneralProcessor(Activity activity) {
		mContext = activity;
	}

	/**
	 * Check if exists accounts table
	 * @return		return true if it exists, else return false
	 * */
	public boolean checkingAccountsInfo() {
		WhooingDbOpenHelper dbHelper = new WhooingDbOpenHelper(mContext);
		if(dbHelper.getAccountsInfoCount() > 0){
			return true;
		}
		return false;
	}
}
