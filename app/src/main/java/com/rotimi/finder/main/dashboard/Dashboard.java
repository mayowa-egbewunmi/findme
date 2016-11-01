package com.rotimi.finder.main.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.yarata.ysell.R;
import com.yarata.ysell.miniyarata.db.YarataDB;
import com.yarata.ysell.miniyarata.db.models.CheckOut;
import com.yarata.ysell.miniyarata.db.models.Sales;
import com.yarata.ysell.miniyarata.db.models.Sales_Table;
import com.yarata.ysell.util.Constants;
import com.yarata.ysell.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Dashboard extends AppCompatActivity {

    @BindView(R.id.dashboard_sales_open)
    TextView openSalesView;
    @BindView(R.id.dashboard_sales_month)
    TextView monthSalesView;
    @BindView(R.id.dashboard_sales_today)
    TextView todaySalesView;
    @BindView(R.id.dashboard_sales_week)
    TextView weekSalesView;
    @BindView(R.id.dashboard_sales_year)
    TextView yearSalesView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        displaySalesSummary(Utility.getCurrentDate(false));
    }

    public void displaySalesSummary(String date){

        String checkOutTable = CheckOut.class.getSimpleName();

        String THIS_YEAR_SALES = "this_year_sales";
        String THIS_MONTH_SALES = "this_month_sales";
        String THIS_WEEK_SALES = "this_week_sales";
        String TODAY_SALES = "today_sales";
        String OPEN_SALES = "open_sales";

        //this query gets sales for month, week day and year
        String query = "SELECT " + THIS_YEAR_SALES +", "+ THIS_MONTH_SALES+", " +THIS_WEEK_SALES+", "+TODAY_SALES+" FROM " +
                " (SELECT TOTAL("+ Constants.AMOUNT+") as this_year_sales FROM "+ checkOutTable+
                " WHERE strftime('%Y', "+Constants.CREATED_AT+") = strftime('%Y', '"+date+"')) as ys " +
                " CROSS JOIN (SELECT TOTAL("+Constants.AMOUNT+") as this_month_sales FROM "+checkOutTable+
                " WHERE strftime('%m-%Y', "+Constants.CREATED_AT+") = strftime('%m-%Y', '"+date+"')) as ms " +
                " CROSS JOIN (SELECT TOTAL("+Constants.AMOUNT+") as this_week_sales FROM "+checkOutTable+
                " WHERE strftime('%W-%Y', "+Constants.CREATED_AT+") = strftime('%W-%Y', '"+date+"')) as ws " +
                " CROSS JOIN (SELECT TOTAL("+Constants.AMOUNT+") as today_sales FROM "+checkOutTable+
                " WHERE date("+Constants.CREATED_AT+") = date('"+date+"')) as ts; ";

        FlowManager.getDatabase(YarataDB.class).executeTransaction(databaseWrapper -> {
            Cursor cursor = databaseWrapper.rawQuery(query, null);

            if(cursor!=null && cursor.moveToFirst()){
                todaySalesView.setText(formatValue(cursor.getString(cursor.getColumnIndex(TODAY_SALES))));
                weekSalesView.setText(formatValue(cursor.getString(cursor.getColumnIndex(THIS_WEEK_SALES))));
                monthSalesView.setText(formatValue(cursor.getString(cursor.getColumnIndex(THIS_MONTH_SALES))));
                yearSalesView.setText(formatValue(cursor.getString(cursor.getColumnIndex(THIS_YEAR_SALES))));
                cursor.close();
            }
        });

        Cursor cursor = SQLite.select(Method.sum(Sales_Table.total_amount).as(OPEN_SALES)).from(Sales.class).where(Sales_Table.closed.eq(0)).query();

        if(cursor!=null && cursor.moveToFirst()){
            openSalesView.setText(formatValue(String.valueOf(cursor.getInt(cursor.getColumnIndex(OPEN_SALES)))));
        }
    }

    String formatValue(String val){
        return "#"+val;
    }

}
