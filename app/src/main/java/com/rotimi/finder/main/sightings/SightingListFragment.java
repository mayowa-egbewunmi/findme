package com.rotimi.finder.main.sightings;

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

import com.marcohc.toasteroid.Toasteroid;
import com.rotimi.finder.R;
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
    private List<SightingItem> sightings;
    private SightingAdapter sightingAdapter;
    private Utility utils;

    @BindView(R.id.sightings_recycler_view) RecyclerView sightingsRecyclerView;
    @BindView(R.id.sightings_empty) LinearLayout emptyView;
    @BindView(R.id.sightings_card_frame) CardView frameView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        for(int i = 0; i < 6; i++){
            sightings.add(new SightingItem());
        }
        
        showEmptyState();

        sightingAdapter = new SightingAdapter(getActivity(), sightings);

        sightingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sightingsRecyclerView.setAdapter(sightingAdapter);

        sightingsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), sightingsRecyclerView, new IClickListener() {

            @Override
            public void onClick(View view, int position) {
                SightingItem sightingItem = sightings.get(position);
                //TODO: show sightings details
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.upload: {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                Constants.MY_PERMISSIONS_REQUEST);
                        return true;
                    }
                    //TODO: Do action here when permission is granted
                    //uploadBulkMenu("");//TODO remove this
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        sightings = new ArrayList<>(); //GET sightings here
        sightingAdapter.setData(sightings);
        sightingAdapter.notifyDataSetChanged();

        showEmptyState();
    }

    public void showEmptyState() {

        if (sightings.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            frameView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            frameView.setVisibility(View.VISIBLE);
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
}
