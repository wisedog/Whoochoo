package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.List;

//import net.simonvt.widget.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer;
import net.wisedog.android.whooing.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
    private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.ContentSample.activePosition";

    private MenuDrawer mMenuDrawer;

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

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
        mMenuDrawer.setContentView(R.layout.whooing_tabs);

        List<Object> items = new ArrayList<Object>();
        items.add(new Category(getString(R.string.left_menu_category_report)));
        items.add(new Item(getString(R.string.left_menu_item_history), R.drawable.left_menu_entries));
        items.add(new Item(getString(R.string.left_menu_item_exp_budget), R.drawable.left_menu_budget));
        items.add(new Item(getString(R.string.left_menu_item_credit), R.drawable.left_menu_bill));
        items.add(new Category(getString(R.string.left_menu_category_etc)));
        items.add(new Item("게시판", R.drawable.ic_action_refresh_dark));

        // A custom ListView is needed so the drawer can be notified when it's
        // scrolled. This is to update the position
        // of the arrow indicator.
        mList = new MenuListView(this);
        mAdapter = new MenuAdapter(items);
        
        mList.setCacheColorHint(Color.TRANSPARENT);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(mItemClickListener);
        
        mMenuDrawer.setMenuView(mList);
        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mMenuDrawer.invalidate();                
            }
        });
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), this);
        mFragmentAdapter.addTab(DashboardFragment.class, null);
        mFragmentAdapter.addTab(MountainFragment.class, null);
        mFragmentAdapter.addTab(BalanceFragment.class, null);
        mFragmentAdapter.addTab(ProfitLossFragment.class, null);
        
        mPager.setAdapter(mFragmentAdapter);
        mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mMenuDrawer.closeMenu();
            if(position == 1){
                Intent intent = new Intent(MainFragmentActivity.this, TransactionEntries.class);
                intent.putExtra("title", "History");
                startActivityForResult(intent, 1);
            }
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
        if (item.getTitle().equals("Plus")) {
            Intent intent = new Intent(this, TransactionAdd.class);
            intent.putExtra("title", getString(R.string.text_add_transaction));
            startActivityForResult(intent, 1);
        }
        else if(item.getTitle().equals("History")){
            Intent intent = new Intent(this, TransactionEntries.class);
            intent.putExtra("title", getString(R.string.left_menu_item_history));
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
    
    
    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        if(mMenuDrawer.isMenuVisible() == true){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mMenuDrawer.closeMenu();
            return;
        }
        super.onBackPressed();
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
