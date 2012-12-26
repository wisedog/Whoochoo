package net.wisedog.android.whooing.test;

import net.wisedog.android.whooing.views.WhooingGraph;

import org.json.JSONArray;

import android.util.Log;

import junit.framework.TestCase;

public class WhooingGraphTests extends TestCase {
    private JSONArray mTestData = null;
    private WhooingGraph mGraph = null;
    protected void setUp() throws Exception {
        super.setUp();
        mGraph = new WhooingGraph();
        mTestData = new JSONArray("[{\"liabilities\":0,\"assets\":0,\"date\":201206,\"capital\":0,\"goal\":null},{\"liabilities\":0,\"assets\":-123456,\"date\":201207,\"capital\":0,\"goal\":null},{\"liabilities\":0,\"assets\":0,\"date\":201208,\"capital\":0,\"goal\":null},{\"liabilities\":0,\"assets\":0,\"date\":201209,\"capital\":0,\"goal\":null},{\"liabilities\":567890,\"assets\":12395678,\"date\":201210,\"capital\":11827788,\"goal\":null},{\"liabilities\":694335,\"assets\":12408023,\"date\":201211,\"capital\":11713688,\"goal\":null},{\"liabilities\":814335,\"assets\":12408023,\"date\":201212,\"capital\":11593688,\"goal\":null}]");
    }
    
    public void testPreconditions(){
        assertNotNull(mGraph);
        assertNotNull(mTestData);
    }
    
    public void testMaxY(){
        mGraph.decodeMountainData(mTestData);
        assertEquals(0, Double.compare(12408023f, mGraph.getMtMaxY()));
    }
    public void testMinY(){
        mGraph.decodeMountainData(mTestData);
        assertEquals(0, Double.compare(-123456f, mGraph.getMtMinY()));
    }
    
    public void testdecodeMountainData(){
        
    }

}
