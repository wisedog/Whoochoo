/**
 * 
 */
package net.wisedog.android.whooing.activity;

import net.wisedog.android.whooing.R;
import net.wisedog.android.whooing.dataset.BoardItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

/**
 * @author Wisedog(me@wisedog.net)
 *
 */
public class BbsFragmentActivity extends SherlockFragmentActivity {
	public static final int BOARD_TYPE_FREE = 0;
	public static final int BOARD_TYPE_MONEY_TALK = 1;
	public static final int BOARD_TYPE_COUNSELING = 2;
	public static final int BOARD_TYPE_WHOOING = 3;

	private int mBoardType = -1;
	public boolean mItemVisible = true;
	
	protected boolean mRefreshListFlag = false;
	public boolean mRefreshArticleFlag = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Styled);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_fragment);
		setTitle(getIntent().getStringExtra("title"));
		
		mBoardType = getIntent().getIntExtra("board_type", -1);
		if(mBoardType == -1){
			Toast.makeText(this, "Error on board", Toast.LENGTH_LONG).show();
			return;
		}
		
		 if (getSupportFragmentManager().findFragmentById(R.id.bbs_fragment_container) == null) {
		     BbsListFragment list = new BbsListFragment();
		     list.setData(mBoardType);
            getSupportFragmentManager().beginTransaction().add(R.id.bbs_fragment_container, 
                    list,BbsListFragment.BBS_LIST_FRAGMENT_TAG).commit();
		 }
	}
	
	/**
	 * Add Article Fragment with given Item
	 * @param  item    item info what the user selected        
	 * */
	public void addArticleFragment(BoardItem item){
	    Fragment fr0 = (Fragment) getSupportFragmentManager().findFragmentByTag(BbsListFragment.BBS_LIST_FRAGMENT_TAG);
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BbsArticleFragment fragment = new BbsArticleFragment();
        fragment.setData(mBoardType, item);
        ft.hide(fr0);
        ft.add(R.id.bbs_fragment_container, fragment, BbsArticleFragment.BBS_ARTICLE_FRAGMENT_TAG);
        ft.show(fragment);
        ft.addToBackStack(null);
        ft.commit();
        
	}
	
	public void addWriteFragment(int mode, String subject, String contents, int bbs_id){
	    Fragment fr0 = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BbsWriteFragment fragment = new BbsWriteFragment();
        if(mode == BbsWriteFragment.MODE_MODIFY_ARTICLE || mode == BbsWriteFragment.MODE_MODIFY_REPLY){
        	fr0 = (Fragment)getSupportFragmentManager().findFragmentByTag(BbsArticleFragment.BBS_ARTICLE_FRAGMENT_TAG);
        }
        else{
        	fr0 = (Fragment) getSupportFragmentManager().findFragmentByTag(BbsListFragment.BBS_LIST_FRAGMENT_TAG);
        }
        fragment.setData(mode, mBoardType, subject, contents, bbs_id);
        ft.hide(fr0);
        ft.add(R.id.bbs_fragment_container, fragment, BbsWriteFragment.BBS_WRITE_FRAGMENT_TAG);
        ft.show(fragment);
        ft.addToBackStack(null);
        ft.commit();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mItemVisible) {
            menu.add("write").setIcon(R.drawable.icon_write)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("write")) {
            addWriteFragment(BbsWriteFragment.MODE_WRITE_ARTICLE, null, null, 0);
        }

        return super.onOptionsItemSelected(item);
    }
	
	public void setListRefreshFlag(boolean b){
	    mRefreshListFlag = b;
	}
	
	public boolean getListNeedRefresh(){
	    return mRefreshListFlag;
	}
}
