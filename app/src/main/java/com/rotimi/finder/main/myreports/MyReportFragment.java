package com.rotimi.finder.main.myreports;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.marcohc.toasteroid.Toasteroid;
import com.rotimi.finder.R;
import com.rotimi.finder.db.FindMeDatabase;
import com.rotimi.finder.main.publicreports.ReportFragment;
import com.rotimi.finder.main.publicreports.ReportItem;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.IClickListener;
import com.rotimi.finder.util.RecyclerTouchListener;
import com.rotimi.finder.util.Utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyReportFragment extends Fragment implements FindMeDatabase.OnReportUpdatedListener{


    private static final String LOG = ReportFragment.class.getName();
    private List<ReportItem> myreports;
    private MyReportAdapter myreportsAdapter;
    private Utility utils;

    @BindView(R.id.myreports_recycler_view) RecyclerView myReportRecyclerView;
    @BindView(R.id.myreports_empty) LinearLayout emptyView;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myreports, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        utils = new Utility(getActivity());

        //runSetUp

        myreports = new ArrayList<>(); //TODO: Do your API request here
        for(int i = 0; i < 6; i++){
            myreports.add(new ReportItem());
        }

        showEmptyState();

        myreportsAdapter = new MyReportAdapter(getActivity(), myreports);
        myReportRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myReportRecyclerView.setAdapter(myreportsAdapter);

        myReportRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), myReportRecyclerView, new IClickListener() {

            @Override
            public void onClick(View view, int position) {
                ReportItem ReportItem = myreports.get(position);
                //TODO: show wanted details
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fab.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CreateMyReport.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        
        runSetUp();
    }

    public void runSetUp(){
        myreports = new ArrayList<>(); //GET reports here
        myreportsAdapter.setData(myreports);
        myreportsAdapter.notifyDataSetChanged();

        showEmptyState();
    }

    public void showEmptyState() {

        if (myreports.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            myReportRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            myReportRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG, "result on activity");
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case Constants.DUMMY: {
                    //uploadBulkMenu(data.getData().getPath());
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toasteroid.show(getActivity(), R.string.permission_granted, Toasteroid.STYLES.SUCCESS);
                break;
            }
        }
    }

    @Override
    public void reportUpdated(DataSnapshot dataSnapshot) {
        myreports.clear();
        Iterator<DataSnapshot> reportItems = dataSnapshot.getChildren().iterator();
        while (reportItems.hasNext()){
            //TODO: Filter out my reports
            DataSnapshot ds = reportItems.next();
            myreports.add((ReportItem) ds.getValue());
        }
        runSetUp();
    }
}
