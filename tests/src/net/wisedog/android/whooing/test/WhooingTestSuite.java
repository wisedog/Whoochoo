package net.wisedog.android.whooing.test;

import android.test.suitebuilder.TestSuiteBuilder;
import junit.framework.Test;
import junit.framework.TestSuite;

public class WhooingTestSuite extends TestSuite {
		public static Test suite() {
		    return new TestSuiteBuilder(WhooingTestSuite.class)
		            .includeAllPackagesUnderHere().build();
		}
	}
