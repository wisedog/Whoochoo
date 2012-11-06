package net.wisedog.android.whooing.test;

import net.wisedog.android.whooing.WhooingMain;
import net.wisedog.android.whooing.ui.NavigationBar;
import net.wisedog.android.whooing.R;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class MainTests extends ActivityInstrumentationTestCase2<WhooingMain> {

    private WhooingMain mActivity;
    private NavigationBar mNavBar;

    public MainTests(){
        this("MainTests");
    }
    public MainTests(String name){
        super(WhooingMain.class);
    }

/*    public MainTests(Class<WhooingMain> activityClass) {
        super(WhooingMain.class);
        // TODO Auto-generated constructor stub
    }
    

    public MainTests(String pkg, Class<WhooingMain> activityClass) {
        super("net.wisedog.android.whooing.test", WhooingMain.class);
        // TODO Auto-generated constructor stub
    }*/


    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mNavBar = (NavigationBar)mActivity.findViewById(R.id.nav_bar);
    }
    
    public void testPreconditions(){
        assertNotNull(mActivity);
        assertNotNull(mNavBar);
    }
    
    public void testSetRestApi(){
        TextView textView = (TextView)mActivity.findViewById(R.id.navbar_textview_restapi);
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mNavBar.setRestApi(100);
            }
        });
        getInstrumentation().waitForIdleSync();
        assertEquals("API:100", textView.getText());
    }

}
