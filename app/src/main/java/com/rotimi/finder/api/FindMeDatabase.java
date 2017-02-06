package com.rotimi.finder.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.rotimi.finder.db.Reports;
import com.rotimi.finder.db.Reports_Table;
import com.rotimi.finder.db.Sightings;
import com.rotimi.finder.db.Sightings_Table;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.Utility;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by mayowa on 11/1/16.
 */

public class FindMeDatabase {

    public static final String TAG = FindMeDatabase.class.getSimpleName();
    public DatabaseReference dbRef;
    private  Context context;

    public FindMeDatabase(Context context){
        this.context = context;
        initDB();
    }

    public void initDB(){
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Read from the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot reportSnapShot = dataSnapshot.child(Constants.REPORTS);
                DataSnapshot sightingSnapShot = dataSnapshot.child(Constants.SIGHTINGS);
                if(reportSnapShot!=null){
                    Toast.makeText(context, "New Report available", Toast.LENGTH_LONG).show();
                    //Save in db
                    saveReport(reportSnapShot);
                    Intent itn = new Intent(Constants.REPORT_UPDATED);
                    context.sendBroadcast(itn);
                }

                if(sightingSnapShot!=null){
                    Toast.makeText(context, "New sighting available", Toast.LENGTH_LONG).show();
                    //Send Broadcast
                    saveSighting(sightingSnapShot);
                    Intent itn = new Intent(Constants.SIGHTING_UPDATED);
                    context.sendBroadcast(itn);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void saveSighting(DataSnapshot sightingSnapShot){
        Iterator<DataSnapshot> sightingItems = sightingSnapShot.getChildren().iterator();
        Sightings sighting;

        while (sightingItems.hasNext()){

            DataSnapshot ds = sightingItems.next();
            HashMap<String, String> sightingItem = (HashMap<String, String>) ds.getValue();

            sighting = SQLite.select().from(Sightings.class).where(Sightings_Table.id.eq(sightingItem.get(Constants.ID))).querySingle();
            if(sighting ==null){
                sighting = new Sightings();
            }
            sighting.id = sightingItem.get(Constants.ID);
            sighting.report = sightingItem.get(Constants.REPORT_ID);
            sighting.location = sightingItem.get(Constants.LOCATION);
            sighting.comment = sightingItem.get(Constants.COMMENT);
            sighting.created_at = sightingItem.get(Constants.DATE);
            sighting.updated_at = Utility.getCurrentDate(false);
            sighting.imageUrl = sightingItem.get(Constants.IMAGE_URL);

            sighting.save();
        }
    }


    public void saveReport(DataSnapshot reportSnapShot){
        Iterator<DataSnapshot> reportItems = reportSnapShot.getChildren().iterator();
        Reports report;

        while (reportItems.hasNext()){
            DataSnapshot ds = reportItems.next();
            HashMap<String, String> reportItem = (HashMap<String, String>) ds.getValue();

            report = SQLite.select().from(Reports.class).where(Reports_Table.id.eq(reportItem.get(Constants.ID))).querySingle();
            if(report ==null){
                report = new Reports();
            }

            report.id = reportItem.get(Constants.ID);
            report.age = reportItem.get(Constants.AGE);
            report.comment = reportItem.get(Constants.COMMENT);
            report.complexion = reportItem.get(Constants.COMPLEXION);
            report.found = reportItem.get(Constants._FOUND);
            report.height = reportItem.get(Constants.HEIGHT);
            report.imageUrl = reportItem.get(Constants.IMAGE_URL);
            report.mobile_number = reportItem.get(Constants.MOBILE_NUMBER);
            report.name = reportItem.get(Constants.NAME);
            report.type = reportItem.get(Constants.TYPE);
            report.sex = reportItem.get(Constants.SEX);
            report.police = reportItem.get(Constants.POLICE);

            report.created_at = Utility.getCurrentDate(false);
            report.updated_at = Utility.getCurrentDate(false);

            report.save();
        }
    }
}
