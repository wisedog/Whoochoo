/**
 * 
 */
package net.wisedog.android.whooing.activity;

import com.actionbarsherlock.app.SherlockActivity;

import net.wisedog.android.whooing.R;
import android.os.Bundle;

/**
 * @author newmoni
 *
 */
public class Welcome extends SherlockActivity {

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }

}
