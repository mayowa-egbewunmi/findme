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
@Table(database = YarataDB.class)
@ModelContainer
public class Sales extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    @ForeignKey
    public ForeignKeyContainer<Order> order;

    @Column
    public int quantity;

    @Column
    public int closed;

    @Column
    public double total_amount;

    @Column
    @ForeignKey
    public ForeignKeyContainer<Menu> menu;

    @Column
    @ForeignKey
    public ForeignKeyContainer<CheckOut> checkout;

    @Column
    public String created_at;

    @Column
    public String updated_at;

    @Column(defaultValue = "0")
    public int deleted;

    @Column
    public String deleted_at;

    public ForeignKeyContainer<Menu> getMenu(int id) {
        Menu menu = SQLite.select().from(Menu.class).where(Menu_Table.id.eq(id)).querySingle();
        return FlowManager.getContainerAdapter(Menu.class)
                .toForeignKeyContainer(menu);
    }

    public ForeignKeyContainer<CheckOut> getCheckout(int id) {
        CheckOut checkout = SQLite.select().from(CheckOut.class).where(CheckOut_Table.id.eq(id)).querySingle();
        return FlowManager.getContainerAdapter(CheckOut.class)
                .toForeignKeyContainer(checkout);
    }

    public ForeignKeyContainer<Order> getOrder(int id) {
        Order order = SQLite.select().from(Order.class).where(Order_Table.id.eq(id)).querySingle();
        return FlowManager.getContainerAdapter(Order.class)
                .toForeignKeyContainer(order);
    }
}
