/**
 * 
 */
package net.wisedog.android.whooing.dataset;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BillMonthlyItem{

	/*Date like this : 
	 {"end_use_date":20130131,"money":0,"pay_date":28,"account_id":"x76",
	     * "start_use_date":20130101}*/
    public BillMonthlyItem(int startUseDate, int endUseDate, 
    		String accountName, int paymentDate, double amount) {
        super();
        this.startUseDate = startUseDate;
        this.endUseDate = endUseDate;
        this.accountName = accountName;
        this.paymentDate = paymentDate;
        this.amount = amount;
    }
    public int startUseDate;
    public int endUseDate;
    public String accountName;
    public int paymentDate;
    public double amount;    
}
