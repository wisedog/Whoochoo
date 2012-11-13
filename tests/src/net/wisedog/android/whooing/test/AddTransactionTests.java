package net.wisedog.android.whooing.test;

import static android.test.ViewAsserts.assertOnScreen;
import net.wisedog.android.whooing.TransactionAdd;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTransactionTests extends ActivityInstrumentationTestCase2<TransactionAdd> {
    
    private TransactionAdd mActivity;
    private EditText mEditItem;
    private EditText mEditAmount;
    private TextView mTextDate;
    private Button mBtnChangeDate;

    public AddTransactionTests(){
        super("net.wisedog.android.whooing.TransactionAdd", TransactionAdd.class);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mEditItem = (EditText)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_edit_item);
        mEditAmount = (EditText)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_edit_amount);
        mTextDate = (TextView)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_text_date);
        mBtnChangeDate = (Button)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_change_btn);
    }
    
    public void testPreconditions(){
        assertTrue(mActivity != null);
        assertTrue(mEditItem != null);
        assertTrue(mEditAmount != null);
        assertTrue(mTextDate != null);
        assertTrue(mBtnChangeDate != null);
    }
    
    public void testInputFieldsShouldStartEmpty(){
        assertEquals("", mEditItem.getText().toString());
        assertEquals("", mEditAmount.getText().toString());
    }
    
    public void testFieldsOnScreen(){
        final Window window = mActivity.getWindow();
        final View origin = window.getDecorView();
        assertOnScreen(origin, mEditItem);
        assertOnScreen(origin, mEditAmount);
    }
    
    public void testTest(){
        assertEquals("2012.03.09", mTextDate.getText().toString());
    }

}
