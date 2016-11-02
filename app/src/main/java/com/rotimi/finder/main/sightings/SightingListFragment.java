package com.rotimi.finder.main.sightings;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import com.marcohc.toasteroid.Toasteroid;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.rotimi.finder.R;
import com.rotimi.finder.db.Reports;
import com.rotimi.finder.db.Sightings;
import com.rotimi.finder.db.Sightings_Table;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.IClickListener;
import com.rotimi.finder.util.RecyclerTouchListener;
import com.rotimi.finder.util.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SightingListFragment extends Fragment {

    private static final String LOG = SightingListFragment.class.getName();
    private List<Sightings> sightings;
    private SightingAdapter sightingAdapter;
    private Utility utils;

    private String reportID;

    @BindView(R.id.sightings_recycler_view) RecyclerView sightingsRecyclerView;
    @BindView(R.id.sightings_empty) LinearLayout emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        processIntent();
    }

    public static SightingListFragment newInstance(String reportId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.REPORT_ID, reportId);
        SightingListFragment fragment = new SightingListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sightings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        utils = new Utility(getActivity());

        sightings = new ArrayList<>(); //TODO: Do your API request here
        sightingAdapter = new SightingAdapter(getActivity(), sightings);
        sightingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sightingsRecyclerView.setAdapter(sightingAdapter);

        runSetUp();

        sightingsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), sightingsRecyclerView, new IClickListener() {

            @Override
            public void onClick(View view, int position) {
                //TODO: show sightings details
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(sightingReceiver, new IntentFilter(Constants.SIGHTING_UPDATED));
        runSetUp();
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(sightingReceiver);
    }

    public void runSetUp(){
        sightings = SQLite.select().from(Sightings.class).where(Sightings_Table.report_id.eq(reportID)).orderBy(Sightings_Table._id, false).queryList();

        if(sightings!=null) {
            sightingAdapter.setData(sightings);
            sightingAdapter.notifyDataSetChanged();
            showEmptyState();
        }
    }

    public void showEmptyState() {

        if (sightings.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            sightingsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            sightingsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG, "result on activity");
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case Constants.DUMMY: {
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

    public void processIntent(){
        Bundle bundle = getArguments();
        if(bundle.containsKey(Constants.REPORT_ID)){
            reportID = bundle.getString(Constants.REPORT_ID);
        }
    }

    BroadcastReceiver sightingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runSetUp();
        }
    };
}
