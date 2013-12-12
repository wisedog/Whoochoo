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

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.PostItItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

/**
 * Fragment Activity for Post it!
 * @author wisedog(me@wisedog.net)
 * */
public class PostItFragmentActivity extends SherlockFragmentActivity {
	protected boolean mRefreshFlag = false;
	//See http://developer.android.com/training/basics/fragments/communicating.html
	//아무래도 interface 로 통신해서 해야할듯 

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Styled);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_it_fragment);
/*        setTitle(getIntent().getStringExtra("title"));
        if (getSupportFragmentManager().findFragmentById(R.id.bbs_fragment_container) == null) {
            PostItListFragment list = new PostItListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.post_it_fragment_container, list, PostItListFragment.LIST_FRAGMENT_TAG)
                    .commit();
        }*/
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("write").setIcon(R.drawable.icon_write)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        SubMenu subMenu1 = menu.addSubMenu("Lists");        
        subMenu1.add("Setting");
        subMenu1.add("Help");
        subMenu1.add("About");

        return super.onCreateOptionsMenu(menu);
    }
    
    /**
     * Add Article Fragment with given Item
     * @param  item    item info what the user selected        
     * */
    public void addArticleFragment(PostItItem item, boolean isAdd){
        Fragment fr0 = (Fragment) getSupportFragmentManager().findFragmentByTag(PostItListFragment.LIST_FRAGMENT_TAG);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PostItArticleFragment fragment = new PostItArticleFragment();
        fragment.setData(item, isAdd);
        ft.hide(fr0);
        ft.add(R.id.post_it_fragment_container, fragment, "abc");
        ft.show(fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("write")) {
            addArticleFragment(null, true);
        }

        return super.onOptionsItemSelected(item);
    }

	public void needToRefresh(boolean b) {
		mRefreshFlag = b;
		
	}
	
	public boolean getNeedRefresh(){
		return mRefreshFlag;
	}
}
