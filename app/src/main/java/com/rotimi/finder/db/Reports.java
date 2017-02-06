package com.rotimi.finder.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by mayowa on 11/2/16.
 */

@Table(database =  FindMeDB.class)
@ModelContainer
public class Reports extends BaseModel{

    public static final String TABLENAME = Reports.class.getSimpleName();

    @Column
    @PrimaryKey(autoincrement = true)
    public int _id;

    @Column
    @Unique
    public String id;

    @Column
    public String name;

    @Column
    public String comment;

    @Column
    public String police;

    @Column
    public String age;

    @Column
    public String height;

    @Column
    public String sex;

    @Column
    public String complexion;

    @Column
    public String type;

    @Column
    public String imageUrl;

    @Column
    public String found;

    @Column
    public String mobile_number;

    @Column
    public String created_at;

    @Column
    public String updated_at;

}
