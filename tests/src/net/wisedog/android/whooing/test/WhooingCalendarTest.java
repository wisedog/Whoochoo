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

    /**
     * Test method for {@link net.wisedog.android.whooing.utils.WhooingCalendar#getTodayYYYYMMDD()}.
     */
    public void testGetTodayYYYYMMDD() {
        assertEquals("Expected 20121030, but " + WhooingCalendar.getTodayYYYYMMDD(),
                "20121030", WhooingCalendar.getTodayYYYYMMDD());
    }

}
