package net.wisedog.android.whooing;

import net.wisedog.android.whooing.engine.DataRepository;
import android.app.Application;
import android.util.Log;

public class WhooingApplication extends Application {
    
    private static WhooingApplication sInstance;
    //using singleton
    private static DataRepository dataRepository = null;
    
    public static WhooingApplication getInstance(){
        return sInstance;
    }
    
    public synchronized DataRepository getRepo(){
        if(dataRepository == null){
            Log.d("wisedog", "DataRepository is just created");
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }
    
    @Override
    public void onCreate() {
      super.onCreate();  
      sInstance = this;
      sInstance.initializeInstance();
    }

    private void initializeInstance() {
        dataRepository = new DataRepository();        
    }

}
