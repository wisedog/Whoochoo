package net.wisedog.android.whooing.activity;

import net.wisedog.android.whooing.R;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainFragmentAdapter extends FragmentStatePagerAdapter {
    private String[] mIndicatorItems;

    public MainFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        Resources res = context.getResources();
        mIndicatorItems = res.getStringArray(R.array.main_fragment_items);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = null;
        switch(position){
        case 0:
            result= DashboardFragment.newInstance(mIndicatorItems[position % mIndicatorItems.length]);
            break;
        case 1:
            result = MountainFragment.newInstance("ASDF");
            break;
        case 2:
            result = TestFragment.newInstance("B");
            break;
        case 3:
            result = TestFragment.newInstance("C");
            break;
        }
        return result;
    }

    @Override
    public int getCount() {
        return mIndicatorItems.length;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return mIndicatorItems[position];
    }

}
