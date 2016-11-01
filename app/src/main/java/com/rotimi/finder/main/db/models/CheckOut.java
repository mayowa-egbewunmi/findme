package com.rotimi.finder.main.db.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.yarata.ysell.miniyarata.db.YarataDB;

/**
 * Created by mayowa on 10/14/16.
 */
@Table(database =  YarataDB.class)
@ModelContainer
public class CheckOut extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public double amount;

    @Column
    @ForeignKey
    public ForeignKeyContainer<Order> order;

    @Column
    @ForeignKey
    public ForeignKeyContainer<Customer> customer;

    @Column
    public String created_at;

    @Column
    public String updated_at;

    @Column(defaultValue = "0")
    public int deleted;

    @Column
    public String deleted_at;

    public ForeignKeyContainer<Customer> getCustomer(int id) {
        Customer customer = SQLite.select().from(Customer.class).where(Customer_Table.id.eq(id)).querySingle();
        return FlowManager.getContainerAdapter(Customer.class)
                .toForeignKeyContainer(customer);
    }
}