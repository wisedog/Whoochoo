/**
 * 
 */
package net.wisedog.android.whooing.engine;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class DataRepository {
    
    //using singleton
    private static DataRepository dataRepository = new DataRepository();
    
    public static synchronized DataRepository getInstance(){
        return dataRepository;
    }
    
}
