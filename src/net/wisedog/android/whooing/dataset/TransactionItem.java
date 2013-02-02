/**
 * 
 */
package net.wisedog.android.whooing.dataset;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class TransactionItem{

    public TransactionItem(String date, String item, String amount, String leftAccount,
            String rightAccount) {
        super();
        Date = date;
        Item = item;
        Amount = amount;
        LeftAccount = leftAccount;
        RightAccount = rightAccount;
    }
    public String Date;
    public String Item;
    public String Amount;
    public String LeftAccount;
    public String RightAccount;
    public long Entry_ID;
    
}
