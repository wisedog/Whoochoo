package net.wisedog.android.whooing.activity;

import net.wisedog.android.whooing.R;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class MainFragmentActivity extends SherlockFragmentActivity {
	
	
	
	MainFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.whooing_tabs);
        
        mAdapter = new MainFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
        //TODO +, 메뉴, API 버튼, Progressive 버튼 추가
    }
}
