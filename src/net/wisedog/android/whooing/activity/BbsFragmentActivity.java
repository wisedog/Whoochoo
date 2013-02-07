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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Styled);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bbs_fragment1);
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
                    list,BbsListFragment.LIST_FRAGMENT_TAG).commit();
		 }
	}
	
	/**
	 * Add Article Fragment with given Item
	 * @param  item    item info what the user selected        
	 * */
	public void addArticleFragment(BoardItem item){
	    Fragment fr0 = (Fragment) getSupportFragmentManager().findFragmentByTag(BbsListFragment.LIST_FRAGMENT_TAG);
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BbsArticleFragment fragment = new BbsArticleFragment();        
        ft.hide(fr0);
        ft.add(R.id.bbs_fragment_container, fragment, "abc");
        ft.show(fragment);
        ft.addToBackStack(null);
        ft.commit();
        
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
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("write")) {
            /*Intent intent = new Intent(this, TransactionAdd.class);
            intent.putExtra("title", getString(R.string.text_add_transaction));
            startActivityForResult(intent, 1);*/
            Toast.makeText(this, "Press Write button", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
	/*
	
	protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == Define.MSG_API_OK){
                if(msg.arg1 == Define.API_GET_BOARD){
                    mPageNum = mPageNum + 1;
                    JSONObject obj = (JSONObject)msg.obj;
                    mBbsValue = obj;
                    try {
                        showBoard(obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.handleMessage(msg);
        }
        
    };

	protected void showBoard(JSONObject obj) throws JSONException{
		JSONArray array = obj.getJSONArray("results");
        int length = array.length();
        
        //ArrayList<BoardItem> dataArray = ((BoardAdapter)mListView.getAdapter()).getData();
        
        for(int i = 0; i < length; i++){
            JSONObject entity = array.getJSONObject(i);
            int id = entity.getInt("bbs_id");
            String content = entity.getString("subject");
            int commentNum = entity.getInt("comments");
            JSONObject objWriter = entity.getJSONObject("writer");
            String userName = objWriter.getString("username");
                    
            BoardItem item = new BoardItem(id, userName, commentNum, content);
            mDataArray.add(item);
        }
        BoardAdapter adapter = (BoardAdapter) ((HeaderViewListAdapter)mListView.getAdapter()).getWrappedAdapter();
        adapter.setData(mDataArray);
        adapter.notifyDataSetChanged();
        mListView.removeFooterView(footerView);
        loading = false;

	}*/
}
