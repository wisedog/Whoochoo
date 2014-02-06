/*
 * Copyright (C) 2013 Jongha Kim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wisedog.android.whooing.activity;

import java.util.ArrayList;
import java.util.List;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.DrawerAdapter;
import net.wisedog.android.whooing.dataset.DrawerCategory;
import net.wisedog.android.whooing.dataset.DrawerItem;
import net.wisedog.android.whooing.dialog.AboutDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class MainFragmentActivity extends SherlockFragmentActivity{
	
    //for left sliding menu
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    /** Adpater for left menu*/
    private DrawerAdapter mAdapter;
    
    public boolean mDirtyFlagModifyBbs = false;
    
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

	/** Swaps fragments in the main content view 
	 * @param	position	left menu position what the user selected
	 * */
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
        case 2: //Transaction entries fragment
        {
        	b.putString("title", getString(R.string.left_menu_item_history));
        	TransactionEntriesFragment f = TransactionEntriesFragment.newInstance(b);
            getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();
        	
			break;
        }
        case 3: //Exp. budget fragment
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
        
        case 6: //Bill
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
			b.putString("title", getString(R.string.left_menu_item_post_it));
			PostItListFragment f = PostItListFragment.newInstance(b);
			getSupportFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_content, f)
            .commit();
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
		}
        default:
    		break;
        }	
	}
	
	private void buildSlideMenu(List<Object> items)
	{
        items.add(new DrawerCategory(getString(R.string.left_menu_category_report)));
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
		MenuInflater inflater = getSupportMenuInflater();		
		inflater.inflate(R.menu.menus, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId())
		{
		case R.id.menu_action_account:
		{
			Intent intent = new Intent(this, AccountsSetting.class);
        	startActivityForResult(intent, 1);
			break;
		}
		case R.id.menu_action_user:
		{
			Intent intent = new Intent(this, UserInfoSetting.class);
        	intent.putExtra("from_menu", true);
        	startActivityForResult(intent, 1);
			break;
		}
		case R.id.menu_action_rating:
		{
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
			break;
		}
		case R.id.menu_action_about:
		{
			DialogFragment newFragment = AboutDialog.newInstance();
            newFragment.show(getSupportFragmentManager(), "dialog");
			break;
		}
		case R.id.menu_action_postit_write:
		{
			PostItWriteFragment f = new PostItWriteFragment();
	        getSupportFragmentManager().beginTransaction()
	        .addToBackStack(null)
	        .replace(R.id.main_content, f)
	        .commit();
			break;
		}
		
		// Do not handle menu_action_bbs_write here! 
		// There are various kind of bbs, so option select event should be handled in BbsListFragment stuff..
		case android.R.id.home:
		{
			if(mDrawerLayout.isDrawerOpen(mDrawerList) == true){
        		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        		mDrawerLayout.closeDrawer(mDrawerList);
        	}else{
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mDrawerLayout.openDrawer(mDrawerList);
            }
		}
		default:
			return false;
		}
        return super.onOptionsItemSelected(item);
    }
    
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
    
    /**
     * Add write bbs fragment
     * @param	mode	flag for modify or reply or new article
     * @param	subject	subject
     * @param	contents	contents
     * @param	bbs_id	bbs id
     * @param	comment_id	used for only modify reply
     * @param	boardType	board type	
     * */
	public void addBbsWriteFragment(int mode, String subject, String contents,
			int bbs_id, String comment_id, int boardType) {
		BbsWriteFragment f = new BbsWriteFragment();
		if (mode == BbsWriteFragment.MODE_MODIFY_ARTICLE) {
			f.setData(mode, boardType, subject, contents, bbs_id);
		} else if (mode == BbsWriteFragment.MODE_MODIFY_REPLY) {
			f.setData(mode, boardType, subject, contents, bbs_id,
					comment_id);
		} else {
			f.setData(mode, boardType, subject, contents, bbs_id);
		}
		getSupportFragmentManager().beginTransaction().addToBackStack(null)
				.replace(R.id.main_content, f).commit();
	}
}
