package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.adapter.MainFragmentAdapter;
import net.wisedog.android.whooing.db.SmsDbOpenHelper;
import net.wisedog.android.whooing.dialog.AboutDialog;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.utils.WhooingAlert;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
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
	private MainFragmentAdapter mFragmentAdapter;
    private ViewPager mPager;
    private PageIndicator mIndicator;

    /**Left sliding menu drawer*/
    private MenuDrawer mMenuDrawer;

    /** Adpater for left menu*/
    private MenuAdapter mAdapter;
    private MenuListView mList;
    
    private TextView mRestApiText = null;

    private int mActivePosition = -1;
    
    public static final int API_MENUITEM_ID = 100000;
    

	public interface IShowedFragment {
	
	    public void onShowedFragment();
	}
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
        mMenuDrawer.setContentView(R.layout.whooing_tabs);

        List<Object> items = new ArrayList<Object>();
        items.add(new Category(getString(R.string.left_menu_category_report)));
        items.add(new Item(getString(R.string.left_menu_item_dashboard), R.drawable.left_menu_dashboard));
        items.add(new Item(getString(R.string.left_menu_item_history), R.drawable.left_menu_entries));
        items.add(new Item(getString(R.string.left_menu_item_exp_budget), R.drawable.left_menu_budget));
        items.add(new Item(getString(R.string.left_menu_item_balance), R.drawable.left_menu_bs));
        items.add(new Item(getString(R.string.left_menu_item_profit_loss), R.drawable.left_menu_pl));        
        items.add(new Item(getString(R.string.left_menu_item_credit), R.drawable.left_menu_bill));
        items.add(new Item(getString(R.string.left_menu_item_mountain), R.drawable.left_menu_mountain));
        items.add(new Category(getString(R.string.left_menu_category_tool)));
        items.add(new Item(getString(R.string.left_menu_item_post_it), R.drawable.left_menu_post_it));
        items.add(new Category(getString(R.string.left_menu_category_comm)));
        items.add(new Item(getString(R.string.left_menu_item_board_free), R.drawable.left_menu_bbs_free));
        items.add(new Item(getString(R.string.left_menu_item_board_finance), R.drawable.left_menu_bbs_moneytalk));
        items.add(new Item(getString(R.string.left_menu_item_board_counseling), R.drawable.left_menu_bbs_counseling));
        items.add(new Item(getString(R.string.left_menu_item_board_support), R.drawable.left_menu_bbs_whooing));        
        

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
        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				Fragment fragment = (Fragment) mFragmentAdapter.instantiateItem(
						mPager, position);
				if (fragment instanceof IShowedFragment) {
					((IShowedFragment) fragment).onShowedFragment();
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {	
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
        
    }
	
	private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mMenuDrawer.closeMenu();
            switch(position){
            case 1: //Dashboard
                mPager.setCurrentItem(0);
                break;
            case 2: //Transaction entries fragment activity
            	Intent intent = new Intent(MainFragmentActivity.this, TransactionEntries.class);
				intent.putExtra("title", getString(R.string.left_menu_item_history));
				startActivityForResult(intent, 1);
				break;
            case 3: //Exp. budget fragment activity
            	Intent intentBudget = new Intent(MainFragmentActivity.this, ExpBudgetFragmentActivity.class);
            	intentBudget.putExtra("title", getString(R.string.text_expenses_budget));
            	startActivityForResult(intentBudget, 1);
            	break;
            case 4: //Balance
                mPager.setCurrentItem(2);
                break;
            case 5: //Profit/Loss
                mPager.setCurrentItem(3);
                break;
            case 6: //Bill fragment activity
                Intent intentBill = new Intent(MainFragmentActivity.this, BillFragmentActivity.class);
                intentBill.putExtra("title", getString(R.string.text_bill));
                startActivityForResult(intentBill, 1);
                break;
            case 7: //Mountain 
                mPager.setCurrentItem(1);
                break;
            case 9://Postit
                Intent intentPostIt = new Intent(MainFragmentActivity.this, PostItFragmentActivity.class);
                intentPostIt.putExtra("title", getString(R.string.text_post_it));
                startActivityForResult(intentPostIt, 1);
                break;
			case 11: // Free board
				Intent intentBbsFree = new Intent(MainFragmentActivity.this,
						BbsFragmentActivity.class);
				intentBbsFree
						.putExtra("title", getString(R.string.text_free_board));
				intentBbsFree.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_FREE);
				startActivityForResult(intentBbsFree, 1);
                break;
			case 12: // Finance board
                Intent intentBbsFinance = new Intent(MainFragmentActivity.this,
                        BbsFragmentActivity.class);
                intentBbsFinance
                        .putExtra("title", getString(R.string.text_free_finance));
                intentBbsFinance.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_MONEY_TALK);
                startActivityForResult(intentBbsFinance, 1);
                break;
			case 13: // Counseling board
                Intent intentBbsCounseling = new Intent(MainFragmentActivity.this,
                        BbsFragmentActivity.class);
                intentBbsCounseling
                        .putExtra("title", getString(R.string.text_free_counseling));
                intentBbsCounseling.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_COUNSELING);
                startActivityForResult(intentBbsCounseling, 1);
                break;
			case 14: // Support Board
                Intent intentBbsSupport = new Intent(MainFragmentActivity.this,
                        BbsFragmentActivity.class);
                intentBbsSupport
                        .putExtra("title", getString(R.string.text_free_support));
                intentBbsSupport.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_WHOOING);
                startActivityForResult(intentBbsSupport, 1);
                break;
            default:
        		break;
            }
        }
    };

    public void onClickBudgetMore(View v){
        Intent intentBudget = new Intent(MainFragmentActivity.this, ExpBudgetFragmentActivity.class);
        intentBudget.putExtra("title", getString(R.string.text_expenses_budget));
        startActivityForResult(intentBudget, 1);
    }
    
    public void onClickBalanceMore(View v){
        mPager.setCurrentItem(2);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    mRestApiText = new TextView(this);
	    mRestApiText.setId(API_MENUITEM_ID);
	    DataRepository repository =  WhooingApplication.getInstance().getRepo();//DataRepository.getInstance();
	    mRestApiText.setText("Api\r\n "+ repository.getRestApi());
	    mRestApiText.setClickable(true);
	    mRestApiText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
	    mRestApiText.setTextColor(Color.WHITE);
	    menu.add("Api").setActionView(mRestApiText).setShowAsAction(android.support.v4.view.MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
	    
	    if(repository.getRestApi() == 0 && Define.SHOW_NO_API_INFORM == false){
	    	Define.SHOW_NO_API_INFORM = true;
	    	WhooingAlert.showNotEnoughApi(this);
	    }
	    
		menu.add("Plus").setIcon(R.drawable.menu_plus_button_white)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		SubMenu subMenu1 = menu.addSubMenu("Lists");
	
        String[] menuItemsArray = getResources().getStringArray(R.array.main_actionbar_menuitem);
        for(int i = 0; i < menuItemsArray.length; i++){
        	subMenu1.add(menuItemsArray[i]);
        }	
        subMenu1.add("test");

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.menu_lists_button);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String[] menuItemsArray = getResources().getStringArray(R.array.main_actionbar_menuitem);
        if (item.getTitle().equals("Plus")) {
            Intent intent = new Intent(this, TransactionAdd.class);
            intent.putExtra("title", getString(R.string.text_add_transaction));
            startActivityForResult(intent, 1);
        }
        else if(item.getTitle().equals(menuItemsArray[0])){
        	Intent intent = new Intent(this, AccountsSetting.class);
        	startActivityForResult(intent, 1);
        }
        else if(item.getTitle().equals(menuItemsArray[1])){
        	Intent intent = new Intent(this, UserInfoSetting.class);
        	intent.putExtra("from_menu", true);
        	startActivityForResult(intent, 1);
        }
        else if(item.getTitle().equals(menuItemsArray[2])){
        	final String appName = "net.wisedog.android.whooing";
        	try {
        	    startActivity(
        	    		new Intent(Intent.ACTION_VIEW, 
        	    				Uri.parse("market://details?id="+appName)));
        	} catch (android.content.ActivityNotFoundException anfe) {
        	    startActivity(
        	    		new Intent(Intent.ACTION_VIEW, 
        	    				Uri.parse("http://play.google.com/store/apps/details?id="+appName)));
        	}
        }
        
        else if(item.getTitle().equals(menuItemsArray[3])){
        	Define.REAL_TOKEN = null;
        	Define.TOKEN_SECRET = null;
        	Define.PIN = null;
        	setResult(RESULT_OK);
        	finish();
        }
        else if(item.getTitle().equals(menuItemsArray[4])){
            DialogFragment newFragment = AboutDialog.newInstance();
            newFragment.show(getSupportFragmentManager(), "dialog");
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
        else if(item.getTitle().toString().compareTo("test") == 0){
        	SmsDbOpenHelper helper = new SmsDbOpenHelper(this);
        	
        	ContentValues values = new ContentValues();
        	values.put(SmsDbOpenHelper.KEY_ID, 0);
        	values.put(SmsDbOpenHelper.KEY_ACCOUNT_ID, "x12");
        	values.put(SmsDbOpenHelper.KEY_DATE, 20130515);
        	values.put(SmsDbOpenHelper.KEY_AMOUNT, 12345);
        	values.put(SmsDbOpenHelper.KEY_MSG, "blahblah");
        	long result = helper.addMessage(values);
        	Log.i("wisedog", "DB insert is " + result);
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
        this.setResult(RESULT_CANCELED);
        finish();
    }
    
    public void onClickTest(View v){
    	Intent intent = new Intent(MainFragmentActivity.this, SmsConfirmList.class);
		startActivityForResult(intent, 1);
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
