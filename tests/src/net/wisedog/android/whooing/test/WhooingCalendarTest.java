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
    private String mTodayStringYYYYMMDD = "20130103";
    private int mTodayIntYYYYMMDD = 20130103;
    private String mTodayStringYYYYMM = "201301";
    
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
        assertEquals("201207" , WhooingCalendar.getPreMonthYYYYMM(6));
        assertEquals("201201" , WhooingCalendar.getPreMonthYYYYMM(12));
    }
    
    public void testGetPreMonthYYYYMMDD(){
        assertEquals("20121203" , WhooingCalendar.getPreMonthYYYYMMDD(1));
    }

}
