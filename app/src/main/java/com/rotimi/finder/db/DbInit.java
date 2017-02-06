package com.rotimi.finder.db;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by mayowa on 8/30/16.
 */
public class DbInit {

    public static final String LOG = DbInit.class.getName();

    private Context context;

    public DbInit(Context context){
        this.context = context;
        FlowManager.init(new FlowConfig.Builder(context).build());
    }
}
