package net.wisedog.android.whooing.test;

import net.wisedog.android.whooing.WhooingTransactionAdd;
import android.test.ActivityInstrumentationTestCase2;

public class AddTransactionTests extends ActivityInstrumentationTestCase2<WhooingTransactionAdd> {
    
    public AddTransactionTests(){
        this("AddTransactionTests");
    }
    public AddTransactionTests(String name){
        super(WhooingTransactionAdd.class);
    }
    protected void setUp() throws Exception {
        super.setUp();
    }

}
