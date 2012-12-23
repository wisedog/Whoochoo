package net.wisedog.android.whooing;

import net.wisedog.android.whooing.activity.MainFragmentActivity;
import net.wisedog.android.whooing.engine.DataRepository;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


/**
 * Initial class that show Splash
 */
public class Whooing extends Activity {
	Context mContext = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //String locale = getResources().getConfiguration().locale.getDisplayName();
        //Toast.makeText(this, "Locale :"+ locale, Toast.LENGTH_SHORT).show();
        //TODO SharedPreference 확인해서 회원가입 UI 띄우기. 아니면 바로 WhooingMain으로 이동
        setContentView(R.layout.main);
        mContext = this;
        DataRepository repository = DataRepository.getInstance();
        repository.refreshAll();
        Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent(mContext, MainFragmentActivity.class);
				startActivityForResult(intent, 1);
			}			
		};
		handler.sendEmptyMessageDelayed(0, 500);
    }
    
    @Override
	public void onBackPressed() {
		// block back button event
	}
    
    
    @Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK){
			switch(resultCode){
				case Define.RESPONSE_EXIT:
				{
					setResult(Define.RESPONSE_EXIT);
					this.finish();
					break;
				}
			}
		}
		this.finish();
	}
}