package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.widget.MenuDrawer;
import net.simonvt.widget.MenuDrawerManager;
import net.wisedog.android.whooing.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class MainFragmentActivity extends SherlockFragmentActivity{
	MainFragmentAdapter mFragmentAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;
    private static final String STATE_MENUDRAWER = "net.simonvt.menudrawer.samples.ContentSample.menuDrawer";
    private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.ContentSample.activePosition";

    private MenuDrawerManager mMenuDrawer;

    private MenuAdapter mAdapter;
    private MenuListView mList;

    private int mActivePosition = -1;
    
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.whooing_tabs);
        if (savedInstanceState != null) {
            mActivePosition = savedInstanceState.getInt(STATE_ACTIVE_POSITION);
        }
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMenuDrawer = new MenuDrawerManager(this, MenuDrawer.MENU_DRAG_CONTENT);
        mMenuDrawer.setContentView(R.layout.whooing_tabs);

        List<Object> items = new ArrayList<Object>();
        items.add(new Category("Category 1"));
        items.add(new Item("거래내역", R.drawable.ic_action_refresh_dark));
        items.add(new Item("수익예산", R.drawable.ic_action_select_all_dark));
        items.add(new Item("신용카드", R.drawable.ic_action_select_all_dark));
        items.add(new Category("Category 2"));
        items.add(new Item("게시판", R.drawable.ic_action_refresh_dark));

        // A custom ListView is needed so the drawer can be notified when it's
        // scrolled. This is to update the position
        // of the arrow indicator.
        mList = new MenuListView(this);
        mAdapter = new MenuAdapter(items);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(mItemClickListener);

        mMenuDrawer.setMenuView(mList);
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), this);
        mFragmentAdapter.addTab(DashboardFragment.class, null);
        mFragmentAdapter.addTab(MountainFragment.class, null);
        mFragmentAdapter.addTab(BalanceFragment.class, null);
        mFragmentAdapter.addTab(TestFragment.class, null);
        
        mPager.setAdapter(mFragmentAdapter);
        mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mMenuDrawer.closeMenu();
        }
    };

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Plus").setIcon(R.drawable.menu_plus_button_white)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		SubMenu subMenu1 = menu.addSubMenu("Lists");		
		subMenu1.add("Setting");
		subMenu1.add("About");

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.menu_lists_button);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("wisedog", "ITEM : " + item.getTitle());
        if (item.getTitle().equals("Plus")) {
            Intent intent = new Intent(this, TransactionAdd.class);
            intent.putExtra("title", "거래추가");
            startActivityForResult(intent, 1);
        }
        else if(item.getTitle().equals("History")){
            Intent intent = new Intent(this, TransactionEntries.class);
            intent.putExtra("title", "History");
            startActivityForResult(intent, 1);
        }
        else if (item.getItemId() == android.R.id.home) {
            if(mMenuDrawer.isMenuVisible() == true){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mMenuDrawer.closeMenu();
            }else{
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mMenuDrawer.openMenu();
            }
            
        }

        return super.onOptionsItemSelected(item);
    }
    
    private static final class Item {

        String mTitle;
        int mIconRes;

        Item(String title, int iconRes) {
            mTitle = title;
            mIconRes = iconRes;
        }
    }

    private static final class Category {

        String mTitle;

        Category(String title) {
            mTitle = title;
        }
    }

    private class MenuAdapter extends BaseAdapter {

        private List<Object> mItems;

        MenuAdapter(List<Object> items) {
            mItems = items;
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
            return getItem(position) instanceof Item ? 0 : 1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isEnabled(int position) {
            return getItem(position) instanceof Item;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Object item = getItem(position);

            if (item instanceof Category) {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_category,
                            parent, false);
                }

                ((TextView) v).setText(((Category) item).mTitle);

            } else {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_item,
                            parent, false);
                }

                TextView tv = (TextView) v;
                tv.setText(((Item) item).mTitle);
                tv.setCompoundDrawablesWithIntrinsicBounds(
                        ((Item) item).mIconRes, 0, 0, 0);
            }

            v.setTag(R.id.mdActiveViewPosition, position);

            if (position == mActivePosition) {
                mMenuDrawer.setActiveView(v, position);
            }

            return v;
        }
    }

}
