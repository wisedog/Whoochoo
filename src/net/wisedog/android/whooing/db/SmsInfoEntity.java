package net.wisedog.android.whooing.db;

public class SmsInfoEntity {
	/** for unique id */
	public int id = 0;
	
	/** account ID like X1, X11*/
	public String account_id = null;
	/** Message */
	public String msg = null;
	
	/** Close date */
	public int use_date = 0;
	
	/** money amount */
	public int amount;
	
	public SmsInfoEntity(){
	    ; //Do nothing
	}
}
