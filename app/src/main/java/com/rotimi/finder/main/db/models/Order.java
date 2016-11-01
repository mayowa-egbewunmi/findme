package com.rotimi.finder.main.db.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.yarata.ysell.miniyarata.db.YarataDB;

/**
 * Created by mayowa on 10/20/16.
 */

@Table(database = YarataDB.class)
@ModelContainer
public class Order extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String name;

    @Column
    public String ref;

    @Column(defaultValue = "0")
    public int closed;

    @Column
    public String created_at;

    @Column
    public String updated_at;

}
