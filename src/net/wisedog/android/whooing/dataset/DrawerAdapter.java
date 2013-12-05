package net.wisedog.android.whooing.dataset;

import java.util.List;

import net.wisedog.android.whooing.R;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {
    private List<Object> mItems;
	private Activity mActivity;

    public DrawerAdapter(List<Object> items, Activity context) {
        mItems = items;
        mActivity = context;
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof DrawerItem ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof DrawerItem;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Object item = getItem(position);

        if (item instanceof DrawerCategory) {
            if (v == null) {
                v = mActivity.getLayoutInflater().inflate(R.layout.menu_row_category,
                        parent, false);
            }

            ((TextView) v).setText(((DrawerCategory) item).mTitle);

        } else {
            if (v == null) {
                v = mActivity.getLayoutInflater().inflate(R.layout.menu_row_item,
                        parent, false);
            }

            TextView tv = (TextView) v;
            tv.setText(((DrawerItem) item).mTitle);
            tv.setCompoundDrawablesWithIntrinsicBounds(
                    ((DrawerItem) item).mIconRes, 0, 0, 0);
        }
        return v;
    }
}
