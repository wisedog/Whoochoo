package net.wisedog.android.whooing.test;

import net.wisedog.android.whooing.utils.RegExUtil;
import junit.framework.TestCase;

public class RegExTest extends TestCase {

    public void testTakeParamPassTest(){
        String result = RegExUtil.takeParam("http://abc.com?pin=sdff1235", "pin");
        assertEquals("sdff1235", result);
        result = RegExUtil.takeParam("http://abc.com?pin=sdff1235&token=1235fggdf", "token");
        assertEquals("1235fggdf", result);
    }
    public void testTakeParamFailTest(){
        String result = RegExUtil.takeParam("http://abc.com?pin=123543fvf", "token");
        assertEquals(null, result);
    }
    
    public void testTakeParamNullTest(){
        String result = RegExUtil.takeParam(null, "pin");
        assertEquals(null, result);
        result = RegExUtil.takeParam("http://abc.com", null);
        assertEquals(null, result);
    }
}
