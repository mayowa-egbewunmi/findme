package com.rotimi.finder.main.publicreports;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.marcohc.toasteroid.Toasteroid;
import com.rotimi.finder.R;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.Utility;
import butterknife.ButterKnife;

public class CreateWanted extends AppCompatActivity {

    public final static String TAG = CreateWanted.class.getSimpleName();
    public final static String MENU_ID = "menu_id";

    private Utility utility;
    private String name;
    private Double price;
    private int menuID;

    Dialog dialog;

    public CreateWanted() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wanted);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        utility = new Utility(this);

        processIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    public boolean validate() {
        boolean valid = true;
//
//        name = nameView.getText().toString().trim();
//        if (name.isEmpty()) {
//            nameView.setError(getString(R.string.name_error));
//            valid = false;
//        } else {
//            nameView.setError(null);
//        }
//        if (TextUtils.isEmpty(String.valueOf(priceView.getText().toString()))) {
//            priceView.setError(getString(R.string.price_error));
//            valid = false;
//        } else {
//            priceView.setError(null);
//            if(!TextUtils.isDigitsOnly(priceView.getText().toString())){
//                priceView.setError(getString(R.string.price_not_number));
//                valid = false;
//            }else{
//                price = Double.valueOf(priceView.getText().toString());
//            }
//        }
        return valid;
    }

    public void handleSubmit(View v){

        Toasteroid.show(this, R.string.success_create_more, Toasteroid.STYLES.SUCCESS);
    }

    /**
     * Extract Data from the Intent
     */
    private void processIntent() {
        Bundle bundle = getIntent().getBundleExtra(Constants.BUNDLE);
        if (bundle!=null && bundle.containsKey(MENU_ID)) {
            menuID = bundle.getInt(MENU_ID);
        }
    }
}

