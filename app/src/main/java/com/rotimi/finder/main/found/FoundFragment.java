package com.rotimi.finder.main.found;

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

public class FoundFragment extends Fragment implements FindMeDatabase.OnReportUpdatedListener{
    private static final String LOG = FoundFragment.class.getName();
    private List<ReportItem> found;
    private FoundAdapter foundAdapter;
    private Utility utils;
    
    @BindView(R.id.found_recycler_view) RecyclerView foundRecyclerView;
    @BindView(R.id.found_empty) LinearLayout emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_found, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        utils = new Utility(getActivity());

        found = new ArrayList<>(); //TODO: Do your API request here
        for(int i = 0; i < 6; i++){
            found.add(new ReportItem());
        }

        showEmptyState();

        foundAdapter = new FoundAdapter(getActivity(), found);
        foundRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foundRecyclerView.setAdapter(foundAdapter);

        foundRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), foundRecyclerView, new IClickListener() {

            @Override
            public void onClick(View view, int position) {
                ReportItem FoundItem = found.get(position);
                //TODO: show wanted details
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        
    }

    @Override
    public void onResume() {
        super.onResume();

        runSetUp();
    }

    public void runSetUp(){
        found = new ArrayList<>(); //GET reports here
        foundAdapter.setData(found);
        foundAdapter.notifyDataSetChanged();

        showEmptyState();
    }

    public void showEmptyState() {

        if (found.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            foundRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            foundRecyclerView.setVisibility(View.VISIBLE);
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
        found.clear();
        Iterator<DataSnapshot> reportItems = dataSnapshot.getChildren().iterator();

        while (reportItems.hasNext()){

            //TODO: Filter out founds

            DataSnapshot ds = reportItems.next();
            found.add((ReportItem) ds.getValue());
        }
        runSetUp();
    }
}
