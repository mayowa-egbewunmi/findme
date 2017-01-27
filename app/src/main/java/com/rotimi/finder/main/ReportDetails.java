package com.rotimi.finder.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marcohc.toasteroid.Toasteroid;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.rotimi.finder.MainActivity;
import com.rotimi.finder.R;
import com.rotimi.finder.db.Reports;
import com.rotimi.finder.db.Reports_Table;
import com.rotimi.finder.main.myreports.CreateMyReport;
import com.rotimi.finder.main.publicreports.ReportItem;
import com.rotimi.finder.main.sightings.SightingActivity;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.SystemData;
import com.rotimi.finder.util.Utility;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportDetails extends AppCompatActivity {

    @BindView(R.id.report_details_age) TextView ageView;
    @BindView(R.id.report_details_complexion) TextView complexionView;
    @BindView(R.id.report_details_height) TextView heightView;
    @BindView(R.id.report_details_name) TextView nameView;
    @BindView(R.id.report_details_picture) ImageView pictureView;
    @BindView(R.id.report_details_police_station) TextView policeView;
    @BindView(R.id.report_details_sex) TextView sexView;
    @BindView(R.id.report_details_type) TextView typeView;
    @BindView(R.id.report_details_comment) TextView commentView;
    @BindView(R.id.report_details_report_found) TextView reportFoundView;


    private String reportID;
    private Reports reportItem;
    private ProgressDialog progressDialog;

    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        ButterKnife.bind(this);

        utility = new Utility(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        processIntent();
        runSetUp();
    }

    public void displayReport(){
        ageView.setText(reportItem.age);
        complexionView.setText(reportItem.complexion);
        heightView.setText(reportItem.height);
        nameView.setText(reportItem.name);

        Picasso.with(this).load(reportItem.imageUrl)
                .placeholder(R.drawable.empty_profile2)
                .error(R.drawable.empty_profile2)
                .into(pictureView);

        policeView.setText(reportItem.police);
        sexView.setText(reportItem.sex);
        typeView.setText(reportItem.type);
        commentView.setText(reportItem.comment);

        //If the reporter
        if(reportItem.mobile_number.equalsIgnoreCase(new SystemData(this).getString(Constants.PHONE))){
            reportFoundView.setVisibility(View.VISIBLE);
        }else{
            reportFoundView.setVisibility(View.GONE);
        }

        reportFoundView.setOnClickListener(v -> {
            if(utility.isConnected())
                new AsyncFound().execute();
            else{
                Toasteroid.show(this, R.string.connection_error, Toasteroid.STYLES.ERROR);
            }
        });
    }

    public void runSetUp(){
        reportItem = SQLite.select().from(Reports.class).where(Reports_Table.id.eq(reportID)).querySingle();
        if(reportItem!= null)
            displayReport();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.report_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.sightings:{
                Intent intent = new Intent(this, SightingActivity.class);
                intent.putExtra(Constants.REPORT_ID, reportItem.id);
                startActivity(intent);
                break;
            }
            case R.id.call_reporting_line:{
                //Extract mobile number and do make call here;
                String helpLine = reportItem.police.substring(reportItem.police.indexOf(":")+1).trim();
                makeCall(helpLine);
            }
        }

        return true;
    }

    public void makeCall(String helpLine) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }
            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CALL_PHONE }, Constants.MY_PERMISSIONS_REQUEST);
            return;
        }
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + helpLine));
        startActivity(callIntent);
    }

    private void processIntent(){
        Intent intent = getIntent();
        if(intent.hasExtra(Constants.REPORT_ID)){
            reportID = intent.getStringExtra(Constants.REPORT_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Checking the request code of our request
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toasteroid.show(this, R.string.permission_granted, Toasteroid.STYLES.SUCCESS);
            } else {
                //Displaying another toast if permission is not granted
                Toasteroid.show(this, R.string.no_permission, Toasteroid.STYLES.ERROR);
            }
        }
    }

    public void showProgress(){
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.show();
    }

    private class AsyncFound extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity.findMeDatabase.dbRef.child(Constants.REPORTS).child(reportID).child("found").setValue("1");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toasteroid.show(ReportDetails.this, R.string.success, Toasteroid.STYLES.SUCCESS);
        }
    }

}
