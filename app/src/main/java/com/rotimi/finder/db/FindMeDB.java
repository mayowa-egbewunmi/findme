package com.rotimi.finder.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by mayowa on 8/29/16.
 */
@Database(name = FindMeDB.NAME, version = FindMeDB.VERSION)
public class FindMeDB {

    public static final String NAME = "findme";

    public static final int VERSION = 1;
}