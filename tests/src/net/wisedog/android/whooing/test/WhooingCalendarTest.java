/**
 * 
 */
package net.wisedog.android.whooing.test;

import net.wisedog.android.whooing.utils.WhooingCalendar;
import junit.framework.TestCase;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class WhooingCalendarTest extends TestCase {
    //Important! Check the value before test.
    private String mTodayStringYYYYMMDD = "20121113";
    private int mTodayIntYYYYMMDD = 20121113;
    
    /**
     * Test method for {@link net.wisedog.android.whooing.utils.WhooingCalendar#getTodayYYYYMMDD()}.
     */
    public void testGetTodayYYYYMMDD() {
        assertEquals(mTodayStringYYYYMMDD, WhooingCalendar.getTodayYYYYMMDD());
    }
    
    public void testGetTodayYYYYMMDDint(){
        assertEquals(mTodayIntYYYYMMDD , WhooingCalendar.getTodayYYYYMMDDint());
    }

}
