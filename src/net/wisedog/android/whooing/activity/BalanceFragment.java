/**
 * 
 */
package net.wisedog.android.whooing.activity;

import org.json.JSONObject;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.OnBsChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BalanceFragment extends SherlockFragment implements OnBsChangeListener{

    private static final String KEY_CONTENT = "TestFragment:Content";

    public static BalanceFragment newInstance(String content) {
        BalanceFragment fragment = new BalanceFragment();

        return fragment;
    }

    private String mContent = "???";
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balance_fragment, null);
        return view;
    }
    
    

    @Override
    public void onResume() {
        DataRepository repository = DataRepository.getInstance();
        if(repository.getBsValue() != null){
            showBalance(repository.getBsValue());
           super.onResume();
           return;
        }
        else{
            repository.refreshBsValue();
            repository.registerObserver(this, DataRepository.BS_MODE);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
    
    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }
    
    @Override
    public void onDestroyView() {
        DataRepository repository = DataRepository.getInstance();
        repository.removeObserver(this, DataRepository.BS_MODE);
        super.onDestroyView();
    }
    
    public void showBalance(JSONObject obj){
        TableLayout tl = (TableLayout)mActivity.findViewById(R.id.balance_asset_table);
        
        /* Create a new row to be added. */
        TableRow tr = new TableRow(mActivity);
        tr.setLayoutParams(new LayoutParams(
                       LayoutParams.MATCH_PARENT,
                       LayoutParams.WRAP_CONTENT));
             /* Create a Button to be the row-content. */
             Button b = new Button(mActivity);
             b.setText("Dynamic Button");
             b.setLayoutParams(new LayoutParams(
                       LayoutParams.MATCH_PARENT,
                       LayoutParams.WRAP_CONTENT));
             /* Add Button to row. */
             tr.addView(b);
       /* Add row to TableLayout. */
       tl.addView(tr,new TableLayout.LayoutParams(
                 LayoutParams.MATCH_PARENT,
                 LayoutParams.WRAP_CONTENT));
    }

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnBsChangeListener#onBsUpdate(org.json.JSONObject)
     */
    public void onBsUpdate(JSONObject obj) {
    /*{"error_parameters":[],"message":"","code":200,
     * "results":
     * {"capital":{"total":11593688},
     * "liabilities":{"total":814335,"accounts":{"x21":643090,"x76":158900,"x20":0,"x22":12345}},
     * "assets":{"total":12408023,
     * "accounts":{"x2":0,"x1":12408023}}},
     * "rest_of_api":4935}
*/
        showBalance(obj);
    }
}
