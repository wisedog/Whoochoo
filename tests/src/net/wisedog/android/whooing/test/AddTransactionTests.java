package net.wisedog.android.whooing.test;

import static android.test.ViewAsserts.assertOnScreen;
import net.wisedog.android.whooing.activity.TransactionAdd;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTransactionTests extends ActivityInstrumentationTestCase2<TransactionAdd> {
    
    private TransactionAdd mActivity;
    private EditText mEditItem;
    private EditText mEditAmount;
    private TextView mTextDate;
    private Button mBtnChangeDate;
    private Button mBtnGo;

    public AddTransactionTests(){
        super("net.wisedog.android.whooing.TransactionAdd", TransactionAdd.class);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mEditItem = (EditText)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_auto_complete);
        mEditAmount = (EditText)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_edit_amount);
        mTextDate = (TextView)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_text_date);
        mBtnChangeDate = (Button)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_change_btn);
        mBtnGo = (Button)mActivity.findViewById(net.wisedog.android.whooing.R.id.add_transaction_btn_go);
    }
    
    public void testPreconditions(){
        assertTrue(mActivity != null);
        assertTrue(mEditItem != null);
        assertTrue(mEditAmount != null);
        assertTrue(mTextDate != null);
        assertTrue(mBtnChangeDate != null);
        assertTrue(mBtnGo != null);
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
        assertOnScreen(origin, mBtnGo);
        assertOnScreen(origin, mBtnChangeDate);
        assertOnScreen(origin, mTextDate);
    }
    
    public void testGoButtonCoverEntireScreen(){
        final int expected = LayoutParams.MATCH_PARENT;
        final LayoutParams lp = mBtnGo.getLayoutParams();
        assertEquals(expected, lp.width);
    }
    
    public void testCheckValidation(){
        mActivity.runOnUiThread(new Runnable(){
            public void run() {
                mEditItem.setText("ASDFG");
            }
        });
        assertEquals(false, mActivity.checkValidation());
        assertNotSame(mEditItem, mActivity.getCurrentFocus());
        mActivity.runOnUiThread(new Runnable(){
            public void run() {
                //mEditAmount.setText("123456");
            }
        });
        assertEquals(false, mActivity.checkValidation());
    }
    
    public void testGetAllAccountsInfo(){
        assertEquals(true, mActivity.getAllAccountsInfo() > 0);
    }
    
    public void testLeftRightEntryIsMoney(){
        assertEquals(mActivity.getLeftAccounts().title, mActivity.getRightAccounts().title);
    }
    
    public void testGoButtonBehavior(){
        
    }
}
