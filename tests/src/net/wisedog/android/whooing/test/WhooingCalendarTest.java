/**
 * 
 */
package net.wisedog.android.whooing.test;

import android.util.Log;
import net.wisedog.android.whooing.utils.WhooingCalendar;
import junit.framework.TestCase;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class WhooingCalendarTest extends TestCase {
    //Important! Check the value before test.
    private String mTodayStringYYYYMMDD = "20121224";
    private int mTodayIntYYYYMMDD = 20121224;
    private String mTodayStringYYYYMM = "201212";
    
    /**
     * Test method for {@link net.wisedog.android.whooing.utils.WhooingCalendar#getTodayYYYYMMDD()}.
     */
    public void testGetTodayYYYYMMDD() {
        assertEquals(mTodayStringYYYYMMDD, WhooingCalendar.getTodayYYYYMMDD());
    }
    
    public void testGetTodayYYYYMMDDint(){
        assertEquals(mTodayIntYYYYMMDD , WhooingCalendar.getTodayYYYYMMDDint());
    }
    
    public void testGetTodayYYYYMM(){
        assertEquals(mTodayStringYYYYMM , WhooingCalendar.getTodayYYYYMM());
    }
    
    public void testGetPreMonthYYYYMM(){
        assertEquals("201206" , WhooingCalendar.getPreMonthYYYYMM(6));
        assertEquals("201112" , WhooingCalendar.getPreMonthYYYYMM(12));
    }

}
