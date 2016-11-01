package com.rotimi.finder.main.db.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.yarata.ysell.miniyarata.db.YarataDB;

/**
 * Created by mayowa on 8/29/16.
 */
@Table(database = YarataDB.class)
@ModelContainer
public class Menu extends BaseModel {

    public static final String TABLENAME = "Menu";
    private static final String LOG = Menu.class.getName();

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String name;

    @Column
    public double price;

    @Column
    public String created_at;

    @Column
    public String updated_at;

    @Column(defaultValue = "0")
    public int deleted;

    @Column
    public String deleted_at;
}