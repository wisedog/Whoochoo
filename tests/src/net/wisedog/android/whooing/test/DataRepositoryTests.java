package net.wisedog.android.whooing.test;

import net.wisedog.android.whooing.engine.DataRepository;
import junit.framework.TestCase;

public class DataRepositoryTests extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPreconditions(){
		if(DataRepository.getInstance() == null){
			fail("DataRepository getInstance failed");
		}
	}

}
