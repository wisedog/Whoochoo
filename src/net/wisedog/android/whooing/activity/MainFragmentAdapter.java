package net.wisedog.android.whooing.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainFragmentAdapter extends FragmentStatePagerAdapter {
    private static final String[] CONTENT = new String[] { "Dashboard", "Mountain", "Balance", "Profit/Loss"};
    private int mCount = CONTENT.length;

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment result = null;
        switch(position){
        case 0:
            result= DashboardFragment.newInstance(CONTENT[position % CONTENT.length]);
            break;
        case 1:
            result = TestFragment2.newInstance("ASDF");
            break;
        case 2:
            result = TestFragment2.newInstance("B");
            break;
        case 3:
            result = TestFragment2.newInstance("C");
            break;
        }
        return result;
    }

    @Override
    public int getCount() {
        return mCount;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position];
    }

}
