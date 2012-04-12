package net.wisedog.android.whooing.test;


import net.wisedog.android.whooing.WhooingMain;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
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

	public WhooingMainTest(Class<WhooingMain> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

}
