/**
 * 
 */
package net.wisedog.android.whooing.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.db.AccountsEntity;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.engine.DataRepository.OnBsChangeListener;
import net.wisedog.android.whooing.engine.GeneralProcessor;
import net.wisedog.android.whooing.utils.FragmentUtil;
import net.wisedog.android.whooing.utils.WhooingCurrency;
import net.wisedog.android.whooing.widget.WiTextView;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BalanceFragment extends SherlockFragment implements OnBsChangeListener{

    public static BalanceFragment newInstance() {
        BalanceFragment fragment = new BalanceFragment();
        return fragment;
    }

    private Activity mActivity;
	private AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balance_fragment, null);
        // adView 만들기
	    adView = new AdView(getSherlockActivity(), AdSize.SMART_BANNER, "a15147cd53daa26");
	    LinearLayout layout = (LinearLayout)view.findViewById(R.id.balance_ads);

	    // 찾은 LinearLayout에 adView를 추가
	    layout.addView(adView);

	    // 기본 요청을 시작하여 광고와 함께 요청을 로드
	    AdRequest adRequest = new AdRequest();
	    if(Define.DEBUG){
	    	adRequest.addTestDevice("65E3B8CB214707370B559D98093D74AA");
	    }
	    adView.loadAd(adRequest);
		return view;
    }
    
    

    @Override
    public void onResume() {
        TableLayout tl = (TableLayout) mActivity
                .findViewById(R.id.balance_asset_table);
        if(tl.getChildCount() <= 2){
        	DataRepository repository = DataRepository.getInstance();
            if(repository.getBsValue() != null){
                showBalance(repository.getBsValue());
            }
            else{
                repository.refreshBsValue(getSherlockActivity());
                repository.registerObserver(this, DataRepository.BS_MODE);
            }
        }
        super.onResume();
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
        adView.destroy();
        super.onDestroyView();
    }
    
	/**
	 * Show Balance
	 * @param	obj		JSON formatted balance data
	 * */
	public void showBalance(JSONObject obj) {
	    WiTextView labelTotalAssetValue = (WiTextView)mActivity.findViewById(R.id.balance_total_asset_value);
		
		TableLayout tl = (TableLayout) mActivity
				.findViewById(R.id.balance_asset_table);
		DisplayMetrics metrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		final int secondColumnWidth = (int) (metrics.widthPixels * 0.6);
		Resources r = getResources();
		final int valueWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95,
                r.getDisplayMetrics());
		final int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14,
                r.getDisplayMetrics());
		final int rightMargin4Dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                r.getDisplayMetrics());
		try {
			JSONObject objResult = obj.getJSONObject("results");
			JSONObject objAssets = objResult.getJSONObject("assets");
			double totalAssetValue = objAssets.getDouble("total");
            if (labelTotalAssetValue != null) {
            	double totalAssetValue1 = objAssets.getDouble("total");
                labelTotalAssetValue.setText(WhooingCurrency.getFormattedValue(totalAssetValue1));
                View bar = (View) mActivity.findViewById(R.id.bar_total_asset);
                int barWidth = FragmentUtil.getBarWidth(objAssets.getInt("total"), totalAssetValue,
                        secondColumnWidth, valueWidth);
                
                android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        barWidth, viewHeight);
                lParams.setMargins(0, 0, rightMargin4Dip, 0);
                lParams.gravity = Gravity.CENTER_VERTICAL;
                bar.setLayoutParams(lParams);
            }
			
			JSONArray objAssetAccounts = objAssets.getJSONArray("accounts");
			showBalanceEntities(objAssetAccounts, totalAssetValue, secondColumnWidth, 
			        valueWidth, tl, 0xFFC36FBC);
			
			JSONObject objLiabilities = objResult.getJSONObject("liabilities");
			JSONArray objLiabilitiesAccounts = objLiabilities.getJSONArray("accounts");
			TableLayout tableLiabilites = (TableLayout) mActivity
	                .findViewById(R.id.balance_liabilities_table);
			
			WiTextView labelTotalLiabilitiesValue = (WiTextView)mActivity.findViewById(R.id.balance_total_liabilities_value);
			if(labelTotalLiabilitiesValue != null){
				double totalLiabilities = objLiabilities.getDouble("total");
			    labelTotalLiabilitiesValue.setText(WhooingCurrency.getFormattedValue(totalLiabilities));
                View bar = (View)mActivity.findViewById(R.id.bar_total_liabilities);
                int barWidth = FragmentUtil.getBarWidth(objLiabilities.getInt("total"), totalAssetValue, 
                        secondColumnWidth, valueWidth);
                android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                        barWidth, viewHeight);
                lParams.setMargins(0, 0, rightMargin4Dip, 0);
                lParams.gravity = Gravity.CENTER_VERTICAL;
                bar.setLayoutParams(lParams);
            }
			
			showBalanceEntities(objLiabilitiesAccounts, totalAssetValue, secondColumnWidth, 
			        valueWidth, tableLiabilites, 0xFF5294FF);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void showBalanceEntities(JSONArray accounts, double totalAssetValue, 
	        int secondColumnWidth, int labelWidth, TableLayout tl, int color) throws JSONException{
	    if(accounts == null){
	        return;
	    }
	    GeneralProcessor genericProcessor = new GeneralProcessor(mActivity);
	    for(int i = 0; i < accounts.length(); i++){
	        JSONObject accountItem = (JSONObject) accounts.get(i);
            
            /* Create a new row to be added. */
            TableRow tr = new TableRow(mActivity);
            tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tr.setWeightSum(1.0f);
            
            WiTextView accountText = new WiTextView(mActivity);
            AccountsEntity entity = genericProcessor.findAccountById(accountItem.getString("account_id"));
            accountText.setText(entity.title);
            accountText.setLayoutParams(new LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, 0.4f));

            tr.addView(accountText);
            
            LinearLayout amountLayout = new LinearLayout(mActivity);
            amountLayout.setLayoutParams(new TableRow.LayoutParams(
                    0, 
                    LayoutParams.WRAP_CONTENT,0.6f));

            int barWidth = FragmentUtil.getBarWidth(accountItem.getDouble("money"), totalAssetValue, 
                    secondColumnWidth, labelWidth);
            Resources r = getResources();
            int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, r.getDisplayMetrics());
            android.widget.LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                    barWidth, px);
            
            //set up view for horizontally bar graph 
            View barView = new View(mActivity);
            barView.setBackgroundColor(color);
            int rightMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());
            lParams.setMargins(0, 0, rightMargin, 0);
            lParams.gravity = Gravity.CENTER_VERTICAL;
            barView.setLayoutParams(lParams);
            
            //set up textview for showing amount
            WiTextView amountText = new WiTextView(mActivity);
            double money = accountItem.getDouble("money");
            amountText.setText(WhooingCurrency.getFormattedValue(money));
            amountLayout.addView(barView);
            amountLayout.addView(amountText);
            tr.addView(amountLayout);
            
            /* Add row to TableLayout. */
            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
	    }
	}
	

    /* (non-Javadoc)
     * @see net.wisedog.android.whooing.engine.DataRepository.OnBsChangeListener#onBsUpdate(org.json.JSONObject)
     */
    public void onBsUpdate(JSONObject obj) {
        showBalance(obj);
    }
}
