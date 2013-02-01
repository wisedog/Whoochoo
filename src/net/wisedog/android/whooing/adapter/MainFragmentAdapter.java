package net.wisedog.android.whooing.adapter;

import java.util.ArrayList;

import net.wisedog.android.whooing.R;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainFragmentAdapter extends FragmentStatePagerAdapter {
    private String[] mIndicatorItems;
    
    private final Context mContext;
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

    static final class TabInfo {
        private final Class<?> mClss;
        private final Bundle mArgs;

        TabInfo(Class<?> aClass, Bundle args) {
            mClss = aClass;
            mArgs = args;
        }
    }

    public MainFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        Resources res = context.getResources();
        mIndicatorItems = res.getStringArray(R.array.main_fragment_items);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.mClss.getName(),
                info.mArgs);
    }

    @Override
    public int getCount() {
        return mIndicatorItems.length;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return mIndicatorItems[position];
    }
    
    public void addTab(Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(clss, args);
        mTabs.add(info);
        notifyDataSetChanged();
    }

}
