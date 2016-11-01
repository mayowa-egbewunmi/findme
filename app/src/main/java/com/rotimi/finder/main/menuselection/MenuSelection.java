package com.rotimi.finder.main.menuselection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yarata.ysell.R;
import com.yarata.ysell.miniyarata.db.models.Menu;
import com.yarata.ysell.miniyarata.db.models.Menu_Table;
import com.yarata.ysell.miniyarata.db.models.Order;
import com.yarata.ysell.miniyarata.db.models.Returns;
import com.yarata.ysell.miniyarata.db.models.Sales;
import com.yarata.ysell.miniyarata.sales.CreateOrderDialog;
import com.yarata.ysell.util.Constants;
import com.yarata.ysell.util.SystemData;
import com.yarata.ysell.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuSelection extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String LOG = MenuSelection.class.getName();
    private static final String SALES_REF = "sales_ref";
    private static final String ORDER_ID = "order_id";
    public static final String FOR_CHECKOUT = "for_checkout";

    private MenuSelectionAdapter menuSelectionAdapter;
    private SystemData sharedPref;

    private List<Menu> menu;
    private int orderId;
    private boolean forCheckout = false;
    private String orderName;

    @BindView(R.id.menu_selection_recycler_view) RecyclerView menuSelectionRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_selection);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        processIntent();
        initSelection();

        menu = SQLite.select().from(Menu.class).orderBy(Menu_Table.name,true).queryList();

        menuSelectionAdapter = new MenuSelectionAdapter(this, menu);
        menuSelectionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuSelectionRecyclerView.setAdapter(menuSelectionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.selection, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_selected) {

            if(forCheckout){ //Return to checkout page
                saveReturns();
            }
            else{ //Return to sales page
                saveSales();
            }
            this.finish();
            return true;
        }
        // Respond to the action bar's Up/Home button
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initSelection(){
        sharedPref = new SystemData(this);
        sharedPref.writeString( "{}", Constants.JSON_SELECTED_MENU);
    }

    public void saveSales(){
        try {
            JSONObject jsonMenuSelection = new JSONObject(sharedPref.getString(Constants.JSON_SELECTED_MENU));
            Iterator<String> keys = jsonMenuSelection.keys();

            if(orderId == 0){ //it's a new order, create a new order ref!
                Order order  = new Order();
                order.name = orderName;
                order.ref = String.valueOf(UUID.randomUUID());
                order.created_at = Utility.getCurrentDate(false);
                order.updated_at = Utility.getCurrentDate(false);
                order.save();
                orderId = order.id;
            }
            while (keys.hasNext()){
                String menuId = keys.next();
                Menu menu = SQLite.select().from(Menu.class).where(Menu_Table.id.eq(Integer.valueOf(menuId))).querySingle();
                int count = jsonMenuSelection.getInt(menuId);
                if(count > 0){
                    Sales sales = new Sales();
                    sales.menu = sales.getMenu(menu.id);
                    sales.closed = 0;
                    sales.quantity = count;
                    sales.order = sales.getOrder(orderId);
                    sales.total_amount = menu.price * count;
                    sales.created_at =  Utility.getCurrentDate(false);
                    sales.updated_at = Utility.getCurrentDate(false);
                    sales.save();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveReturns(){
        try {
            JSONObject jsonMenuSelection = new JSONObject(sharedPref.getString(Constants.JSON_SELECTED_MENU));
            Iterator<String> keys = jsonMenuSelection.keys();

            while (keys.hasNext()){
                String menuId = keys.next();
                Menu menu = SQLite.select().from(Menu.class).where(Menu_Table.id.eq(Integer.valueOf(menuId))).querySingle();
                int count = jsonMenuSelection.getInt(menuId);
                if(count > 0){
                    Returns returns = new Returns();
                    returns.menu = returns.getMenu(menu.id);
                    returns.quantity = count;
                    returns.order = returns.getOrder(orderId);
                    returns.total_amount = menu.price * count;
                    returns.created_at =  Utility.getCurrentDate(false);
                    returns.updated_at = Utility.getCurrentDate(false);

                    returns.save();
                    Toast.makeText(this, R.string.success, Toast.LENGTH_LONG).show();
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Extract Data from the Intent
     */
    private void processIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(ORDER_ID)) {
            orderId = intent.getIntExtra(ORDER_ID, 0);
        }
        if (intent.hasExtra(FOR_CHECKOUT)) {
            forCheckout = intent.getBooleanExtra(FOR_CHECKOUT, false);
        }
        if(intent.hasExtra(CreateOrderDialog.NAME)){
            orderName = intent.getStringExtra(CreateOrderDialog.NAME);
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        updateMenuOnSearch(newText);
        return false;
    }

    public void updateMenuOnSearch(String query){

        if(TextUtils.isEmpty(query)){
            menu = SQLite.select().from(Menu.class).queryList();
        }else{
            menu = SQLite.select().from(Menu.class).where(Menu_Table.name.like("%"+query+"%")).queryList();
        }
        menuSelectionAdapter.setData(menu);
        menuSelectionAdapter.notifyDataSetChanged();
    }
}
