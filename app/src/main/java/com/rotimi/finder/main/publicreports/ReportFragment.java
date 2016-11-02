package com.rotimi.finder.main.publicreports;

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

import com.google.firebase.database.DataSnapshot;
import com.marcohc.toasteroid.Toasteroid;
import com.rotimi.finder.R;
import com.rotimi.finder.db.FindMeDatabase;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.IClickListener;
import com.rotimi.finder.util.RecyclerTouchListener;
import com.rotimi.finder.util.Utility;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportFragment extends Fragment implements FindMeDatabase.OnReportUpdatedListener {

    private static final String LOG = ReportFragment.class.getName();
    private List<ReportItem> reports;
    private ReportAdapter reportAdapter;
    private Utility utils;

    @BindView(R.id.reports_recycler_view) RecyclerView reportsRecyclerView;
    @BindView(R.id.reports_empty) LinearLayout emptyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        utils = new Utility(getActivity());

        reports = new ArrayList<>(); //TODO: Do your API request here
        for(int i = 0; i < 6; i++){
            reports.add(new ReportItem());
        }
        showEmptyState();
        reportAdapter = new ReportAdapter(getActivity(), reports);

        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reportsRecyclerView.setAdapter(reportAdapter);

        reportsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), reportsRecyclerView, new IClickListener() {

            @Override
            public void onClick(View view, int position) {
                ReportItem reportItem = reports.get(position);
                //TODO: show reports details
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public ArrayList<ReportItem> getAllReports(){
        return null;
    }

//    @Override
//    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        searchView.setOnQueryTextListener(this);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.upload: {
//                try {
//                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                                Constants.MY_PERMISSIONS_REQUEST);
//                        return true;
//                    }
//                    //TODO: Do action here when permission is granted
//                    //uploadBulkMenu("");//TODO remove this
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onResume() {
        super.onResume();
        runSetUp();
    }

    public void runSetUp(){
        reports = new ArrayList<>(); //GET reports here
        reportAdapter.setData(reports);
        reportAdapter.notifyDataSetChanged();

        showEmptyState();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void showEmptyState() {
        if (reports.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            reportsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            reportsRecyclerView.setVisibility(View.VISIBLE);
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

    @Override
    public void reportUpdated(DataSnapshot dataSnapshot) {
        reports.clear();
        Iterator<DataSnapshot> reportItems = dataSnapshot.getChildren().iterator();

        while (reportItems.hasNext()){
            DataSnapshot ds = reportItems.next();
            reports.add((ReportItem) ds.getValue());
        }
        runSetUp();
    }

//    public void uploadBulkMenu(String filePath){
//        List<Menu> menus;
//        try {
//            menus = utils.readExcelNamePriceColumn(filePath);
//            for(Menu menu : menus){
//                Menu _menu = SQLite.select().from(Menu.class).where(Menu_Table.name.eq(menu.name)).querySingle();
//                if(_menu == null){
//                    //it's a new reports
//                    menu.save();
//                }else{
//                    //update existing reports
//                    _menu.name = menu.name;
//                    _menu.price = menu.price;
//                    _menu.created_at = Utility.getCurrentDate(false);
//                    _menu.updated_at = Utility.getCurrentDate(false);
//                    _menu.save();
//                }
//            }
//            Toasteroid.show(getActivity(), "Stock loaded", Toasteroid.STYLES.SUCCESS);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toasteroid.show(getActivity(), "Stock loaded", Toasteroid.STYLES.ERROR);
//        }
//        onResume(); //refresh view;
//    }
//
//    public void openFile(String[] types) {
//
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, types);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        // special intent for Samsung file manager
//        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
//        // if you want any file type, you can skip next line
//        sIntent.putExtra("CONTENT_TYPE", types);
//        sIntent.addCategory(Intent.CATEGORY_DEFAULT);
//
//        Intent chooserIntent;
//        if (getActivity().getPackageManager().resolveActivity(sIntent, 0) != null) {
//            // it is device with samsung file manager
//            chooserIntent = Intent.createChooser(sIntent, "Open file");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
//        } else {
//            chooserIntent = Intent.createChooser(intent, "Open file");
//        }
//
//        try {
//            // startActivityForResult(chooserIntent, Constants.UPLOAD_BULK_PRODUCTS);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toasteroid.show(getActivity(), R.string.no_file_manager, Toasteroid.STYLES.ERROR);
//        }
//    }

}
