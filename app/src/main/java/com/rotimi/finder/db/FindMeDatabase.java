package com.rotimi.finder.db;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mayowa on 11/1/16.
 */

public class FindMeDatabase {

    public static final String TAG = FindMeDatabase.class.getSimpleName();
    public static DatabaseReference dbRef;
    private  Context context;

    private OnReportUpdatedListener listener;

    public FindMeDatabase(Context context){
        this.context = context;
        this.listener = (OnReportUpdatedListener) context;

        dbRef = FirebaseDatabase.getInstance().getReference();

        // Read from the database
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(listener!=null){
                    listener.reportUpdated(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public interface OnReportUpdatedListener{
        void reportUpdated(DataSnapshot dataSnapshot);
    }
}
