package net.wisedog.android.whooing.test;


import net.wisedog.android.whooing.WhooingMain;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/*
 * Tests the example application Spinner. Uses the instrumentation test class
 * ActivityInstrumentationTestCase2 as its base class. The tests include
 *   - test initial conditions
 *   - test the UI
 *   - state management - preserving state after the app is shut down and restarted, preserving
 *     state after the app is hidden (paused) and re-displayed (resumed)
 *
 * Demonstrates the use of JUnit setUp() and assert() methods.
 */
public class WhooingMainTest extends ActivityInstrumentationTestCase2<WhooingMain> {
	
	private WhooingMain mActivity;

	public WhooingMainTest(String pkg, Class<WhooingMain> activityClass) {
		super(pkg, activityClass);
	}
	
	public WhooingMainTest(){
		super("net.wisedog.android.whooing", WhooingMain.class);
	}
	

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		mActivity = getActivity();
	}
	
	public void testInitHandshake(){
		String result = mActivity.initHandshake();
		assertNotNull(result);
	}
}
