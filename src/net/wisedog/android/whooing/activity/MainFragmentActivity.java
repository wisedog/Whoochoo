package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.List;

import net.wisedog.android.whooing.Define;
import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.WhooingApplication;
import net.wisedog.android.whooing.dataset.BoardItem;
import net.wisedog.android.whooing.dataset.DrawerAdapter;
import net.wisedog.android.whooing.dataset.DrawerCategory;
import net.wisedog.android.whooing.dataset.DrawerItem;
import net.wisedog.android.whooing.dialog.AboutDialog;
import net.wisedog.android.whooing.engine.DataRepository;
import net.wisedog.android.whooing.utils.WhooingAlert;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;


public class MainFragmentActivity extends SherlockFragmentActivity{
	
    //for left sliding menu
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    /** Adpater for left menu*/
    private DrawerAdapter mAdapter;
    
    private TextView mRestApiText = null;
    
    public static final int API_MENUITEM_ID = 100000;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.whooing_tabs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        
        List<Object> items = new ArrayList<Object>();
        buildSlideMenu(items);
        
        mAdapter = new DrawerAdapter(items, this);
        mDrawerList.setAdapter(mAdapter);
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        TransactionAddFragment f = new TransactionAddFragment();
        getSupportFragmentManager().beginTransaction()
        .addToBackStack(null)
        .replace(R.id.main_content, f)
        .commit();
                
    }
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    mDrawerList.setItemChecked(position, true);
	    mDrawerLayout.closeDrawer(mDrawerList);
	    
    	Bundle b = new Bundle();	//bundle for all cases
    	
		switch(position){
        case 1: //Home
        {
        	getSupportFragmentManager().popBackStack(0, 0);
            break;
        }
        case 2: //Transaction entries fragment activity
        {
        	b.putString("title", getString(R.string.left_menu_item_history));
        	TransactionEntriesFragment f = TransactionEntriesFragment.newInstance(b);
            getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();
        	
			break;
        }
        case 3: //Exp. budget fragment activity
        {
        	b.putString("title", getString(R.string.left_menu_item_balance));
        	ExpBudgetFragment f = ExpBudgetFragment.newInstance(b);
            getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();	
            break;
        }
        case 4: //Balance
        {
        	b.putString("title", getString(R.string.left_menu_item_balance));
        	BalanceFragment f = BalanceFragment.newInstance(b);
            getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();	
            break;
        }   
        case 5: //Profit/Loss
        {
        	b.putString("title", getString(R.string.left_menu_item_profit_loss));
        	ProfitLossFragment f = ProfitLossFragment.newInstance(b);
            getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();	
            break;
        }
        
        case 6: //Bill fragment activity
        {
        	b.putString("title", getString(R.string.left_menu_item_credit));
        	BillFragment f = BillFragment.newInstance(b);
            getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();	
            break;
        }
        case 7: //Mountain 
        {
        	b.putString("title", getString(R.string.left_menu_item_mountain));
        	MountainFragment f = MountainFragment.newInstance(b);
            getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();	
            break;        
        }

        case 9://Postit
        {
            Intent intentPostIt = new Intent(MainFragmentActivity.this, PostItFragmentActivity.class);
            intentPostIt.putExtra("title", getString(R.string.text_post_it));
            startActivityForResult(intentPostIt, 1);
            break;
        }
		case 11: // Free board
		{
			b.putInt("board_type", BbsListFragment.BOARD_TYPE_FREE);
			b.putString("title", getString(R.string.text_free_board));
			BbsListFragment f = BbsListFragment.newInstance(b);
			getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();
			/*Intent intentBbsFree = new Intent(MainFragmentActivity.this,
					BbsFragmentActivity.class);
			intentBbsFree
					.putExtra("title", getString(R.string.text_free_board));
			intentBbsFree.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_FREE);
			startActivityForResult(intentBbsFree, 1);*/
            break;
		}
		case 12: // Finance board
		{
			b.putInt("board_type", BbsListFragment.BOARD_TYPE_MONEY_TALK);
			b.putString("title", getString(R.string.text_free_board));
			BbsListFragment f = BbsListFragment.newInstance(b);
			getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();
			break;
            /*Intent intentBbsFinance = new Intent(MainFragmentActivity.this,
                    BbsFragmentActivity.class);
            intentBbsFinance
                    .putExtra("title", getString(R.string.text_free_finance));
            intentBbsFinance.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_MONEY_TALK);
            startActivityForResult(intentBbsFinance, 1);
            break;*/
		}
		case 13: // Counseling board
		{
			b.putInt("board_type", BbsListFragment.BOARD_TYPE_COUNSELING);
			b.putString("title", getString(R.string.text_free_board));
			BbsListFragment f = BbsListFragment.newInstance(b);
			getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();
			break;
            /*Intent intentBbsCounseling = new Intent(MainFragmentActivity.this,
                    BbsFragmentActivity.class);
            intentBbsCounseling
                    .putExtra("title", getString(R.string.text_free_counseling));
            intentBbsCounseling.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_COUNSELING);
            startActivityForResult(intentBbsCounseling, 1);
            break;*/
		}
		case 14: // Support Board
		{
			b.putInt("board_type", BbsListFragment.BOARD_TYPE_WHOOING);
			b.putString("title", getString(R.string.text_free_board));
			BbsListFragment f = BbsListFragment.newInstance(b);
			getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();
			break;
            /*Intent intentBbsSupport = new Intent(MainFragmentActivity.this,
                    BbsFragmentActivity.class);
            intentBbsSupport
                    .putExtra("title", getString(R.string.text_free_support));
            intentBbsSupport.putExtra("board_type", BbsFragmentActivity.BOARD_TYPE_WHOOING);
            startActivityForResult(intentBbsSupport, 1);
            break;*/
		}
        default:
    		break;
        }	
	}
	
	private void buildSlideMenu(List<Object> items)
	{
        items.add(new DrawerCategory(getString(R.string.left_menu_category_report)));
        //items.add(new DrawerItem(getString(R.string.left_menu_item_dashboard), R.drawable.left_menu_dashboard));
        items.add(new DrawerItem(getString(R.string.left_menu_item_home), R.drawable.left_menu_dashboard));	//TODO
        items.add(new DrawerItem(getString(R.string.left_menu_item_history), R.drawable.left_menu_entries));
        items.add(new DrawerItem(getString(R.string.left_menu_item_exp_budget), R.drawable.left_menu_budget));
        items.add(new DrawerItem(getString(R.string.left_menu_item_balance), R.drawable.left_menu_bs));
        items.add(new DrawerItem(getString(R.string.left_menu_item_profit_loss), R.drawable.left_menu_pl));        
        items.add(new DrawerItem(getString(R.string.left_menu_item_credit), R.drawable.left_menu_bill));
        items.add(new DrawerItem(getString(R.string.left_menu_item_mountain), R.drawable.left_menu_mountain));
        items.add(new DrawerCategory(getString(R.string.left_menu_category_tool)));
        items.add(new DrawerItem(getString(R.string.left_menu_item_post_it), R.drawable.left_menu_post_it));
        items.add(new DrawerCategory(getString(R.string.left_menu_category_comm)));
        items.add(new DrawerItem(getString(R.string.left_menu_item_board_free), R.drawable.left_menu_bbs_free));
        items.add(new DrawerItem(getString(R.string.left_menu_item_board_finance), R.drawable.left_menu_bbs_moneytalk));
        items.add(new DrawerItem(getString(R.string.left_menu_item_board_counseling), R.drawable.left_menu_bbs_counseling));
        items.add(new DrawerItem(getString(R.string.left_menu_item_board_support), R.drawable.left_menu_bbs_whooing));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.i("wisedog", "onCreateOptionMenu.... ");
	    mRestApiText = new TextView(this);
	    mRestApiText.setId(API_MENUITEM_ID);
	    DataRepository repository =  WhooingApplication.getInstance().getRepo();
	    mRestApiText.setText("Api\r\n "+ repository.getRestApi());
	    mRestApiText.setClickable(true);
	    mRestApiText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
	    mRestApiText.setTextColor(Color.WHITE);
	    menu.add("Api").setActionView(mRestApiText).setShowAsAction(android.support.v4.view.MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
	    
	    if(repository.getRestApi() == 0 && Define.SHOW_NO_API_INFORM == false){
	    	Define.SHOW_NO_API_INFORM = true;
	    	WhooingAlert.showNotEnoughApi(this);
	    }
	    
	    addSubMenuItem(menu);

		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	Log.i("wisedog", "onPrepareOptionsMenu.... ");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String[] menuItemsArray = getResources().getStringArray(R.array.main_actionbar_menuitem);
        if(item.getTitle().equals(menuItemsArray[0])){
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
        
        /*else if(item.getTitle().equals(menuItemsArray[3])){
        	Define.REAL_TOKEN = null;
        	Define.TOKEN_SECRET = null;
        	Define.PIN = null;
        	setResult(RESULT_OK);
        	finish();
        }*/
        else if(item.getTitle().equals(menuItemsArray[3])){
            DialogFragment newFragment = AboutDialog.newInstance();
            newFragment.show(getSupportFragmentManager(), "dialog");
        }
        else if (item.getItemId() == android.R.id.home) {
        	if(mDrawerLayout.isDrawerOpen(mDrawerList) == true){
        		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        		mDrawerLayout.closeDrawer(mDrawerList);
        	}else{
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        /*else if(item.getTitle().toString().compareTo("test") == 0){
        	SmsDbOpenHelper helper = new SmsDbOpenHelper(this);
        	
        	ContentValues values = new ContentValues();
        	values.put(AccountsDbOpenHelper.KEY_ID, 0);
        	values.put(AccountsDbOpenHelper.KEY_ACCOUNT_ID, "x37");
        	values.put(AccountsDbOpenHelper.KEY_DATE, 20130515);
        	values.put(AccountsDbOpenHelper.KEY_AMOUNT, 12345);
        	values.put(AccountsDbOpenHelper.KEY_SMS_ITEM, "asdfasdf");
        	values.put(AccountsDbOpenHelper.KEY_MSG, "blahblah");
        	long result = helper.addMessage(values);
        	//Log.i("wisedog", "DB insert is " + result);
        }*/
        return super.onOptionsItemSelected(item);
    }
    
    
    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
    	if(mDrawerLayout.isDrawerOpen(mDrawerList) == true){
    		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    		mDrawerLayout.closeDrawer(mDrawerList);
    	}
    	else{
    		int count = getSupportFragmentManager().getBackStackEntryCount();
    		if(count > 1){
    			getSupportFragmentManager().popBackStack();
    		}
    		else{
    			this.setResult(RESULT_CANCELED);
    	        finish();
    		}
    	}
    }
    
    public void addSubMenuItem(Menu menu){
    	SubMenu subMenu1 = menu.addSubMenu("Lists");
    	
        String[] menuItemsArray = getResources().getStringArray(R.array.main_actionbar_menuitem);
        for(int i = 0; i < menuItemsArray.length; i++){
        	subMenu1.add(menuItemsArray[i]);
        }	
        if(Define.DEBUG){
        	subMenu1.add("test");
        }

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.menu_lists_button);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }
    
    public void addArticle(int boardType, BoardItem item){
    	
    	//TODO set actionbar icon - write button, menu tree
    	BbsArticleFragment f = new BbsArticleFragment();
    	f.setData(boardType, item);
        getSupportFragmentManager().beginTransaction()
        .addToBackStack(null)
        .replace(R.id.main_content, f)
        .commit();
        
        /*
    	Fragment fr0 = (Fragment) getSupportFragmentManager().findFragmentByTag(BbsListFragment.BBS_LIST_FRAGMENT_TAG);
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BbsArticleFragment fragment = new BbsArticleFragment();
        fragment.setData(mBoardType, item);
        ft.hide(fr0);
        ft.add(R.id.bbs_fragment_container, fragment, BbsArticleFragment.BBS_ARTICLE_FRAGMENT_TAG);
        ft.show(fragment);
        ft.addToBackStack(null);
        ft.commit();*/
    }
    
	public void addWriteFragment(int mode, String subject, String contents,
			int bbs_id, String comment_id, int boardType) {
		//TODO set actionbar icon - write button, menu tree
		BbsWriteFragment f = new BbsWriteFragment();
		if (mode == BbsWriteFragment.MODE_MODIFY_ARTICLE) {
			/*f = (Fragment) getSupportFragmentManager().findFragmentByTag(
					BbsArticleFragment.BBS_ARTICLE_FRAGMENT_TAG);*/
			f.setData(mode, boardType, subject, contents, bbs_id);
		} else if (mode == BbsWriteFragment.MODE_MODIFY_REPLY) {
			/*f = (Fragment) getSupportFragmentManager().findFragmentByTag(
					BbsArticleFragment.BBS_ARTICLE_FRAGMENT_TAG);*/
			f.setData(mode, boardType, subject, contents, bbs_id,
					comment_id);
		} else {
			/*f = (Fragment) getSupportFragmentManager().findFragmentByTag(
					BbsListFragment.BBS_LIST_FRAGMENT_TAG);*/
			f.setData(mode, boardType, subject, contents, bbs_id);
		}
		getSupportFragmentManager().beginTransaction().addToBackStack(null)
				.replace(R.id.main_content, f).commit();
		// fragment.setData(mode, mBoardType, subject, contents, bbs_id);
		/*
		 * ft.hide(f); ft.add(R.id.bbs_fragment_container, fragment,
		 * BbsWriteFragment.BBS_WRITE_FRAGMENT_TAG); ft.show(fragment);
		 * ft.addToBackStack(null); ft.commit();
		 */
	}
}
