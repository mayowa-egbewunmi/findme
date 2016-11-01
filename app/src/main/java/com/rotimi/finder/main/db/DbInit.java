package com.rotimi.finder.main.db;

import android.content.Context;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by mayowa on 8/30/16.
 */
public class DbInit {

    public static final String LOG = DbInit.class.getName();

    private setupListener listener;
    private Context context;

    public DbInit(Context context){
        this.context = context;
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    public void populateDB(){

        this.listener = (setupListener) context;

        FlowManager.getDatabase(YarataDB.class)
                .beginTransactionAsync(databaseWrapper -> {

                    Log.d(LOG, "Populate DB");
                    Seeder seeder = new Seeder(databaseWrapper);
                    seeder.loadDB();
                })
                .error((transaction, error) -> {
                    //cache set up not complete here...
                    Log.d(LOG, "DB Transaction failed");
                    listener.onSetUpFailed();
                })
                .success(transaction -> {


                }).build().execute();
    }

    public interface setupListener{

        void onReady();

        void onSetUpFailed();
    }
}
