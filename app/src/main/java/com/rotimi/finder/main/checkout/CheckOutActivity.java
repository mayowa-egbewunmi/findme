package com.rotimi.finder.main.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yarata.ysell.R;
import com.yarata.ysell.miniyarata.db.models.Returns;

import java.util.List;

import butterknife.ButterKnife;

public class CheckOutActivity extends AppCompatActivity {

    public int orderId;
    public List<Returns> returns;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_check_out);
         ButterKnife.bind(this);

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         processIntent();

         FragmentManager fm = getSupportFragmentManager();
         FragmentTransaction fragmentTransaction = fm.beginTransaction();
         CheckOutFragment checkOutFragment = CheckOutFragment.newInstance(orderId);
         fragmentTransaction.add(R.id.checkout_frame, checkOutFragment, CheckOutFragment.TAG);
         fragmentTransaction.commit();
    }

    /**
     * Extract Data from the Intent
     */
    private void processIntent() {
        Intent intent = getIntent();

        if (intent!=null && intent.hasExtra(CheckOutFragment.ORDER_ID)) {
            orderId = intent.getIntExtra(CheckOutFragment.ORDER_ID, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:{
                finish();
                break;
            }
        }

        return true;
    }
}
