package com.rotimi.finder.main.db.models;

import android.database.Cursor;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.yarata.ysell.miniyarata.db.YarataDB;
import com.yarata.ysell.util.Constants;

/**
 * Created by mayowa on 10/14/16.
 */
@Table(database = YarataDB.class)
@ModelContainer
public class Customer extends BaseModel {

    public static final String TABLENAME = "Customer";
    private static final String LOG = Customer.class.getName();

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String name;

    @Column
    public String phone;

    @Column
    public String created_at;

    @Column
    public String updated_at;

    @Column(defaultValue = "0")
    public int deleted;

    @Column
    public String deleted_at;

    public double getPurchaseWorth(){
        Cursor cursor = SQLite.select(Method.sum(CheckOut_Table.amount).as(Constants.SUM)).from(CheckOut.class).where(CheckOut_Table.customer_id.eq(id)).query();

        if(cursor!=null && cursor.moveToFirst()){
            return cursor.getDouble(cursor.getColumnIndex(Constants.SUM));
        }
        else{
            return 0;
        }
    }
}