package com.rotimi.finder;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rotimi.finder.api.FindMeDatabase;
import com.rotimi.finder.api.Storage;
import com.rotimi.finder.db.DbInit;
import com.rotimi.finder.main.BaseController;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static FindMeDatabase findMeDatabase;
    public static Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.setDebug(true);
        ButterKnife.bind(this);

        new DbInit(this); //set up tables
        findMeDatabase = new FindMeDatabase(this); //set up firebase
        storage = new Storage();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.fcontainer, BaseController.newInstance());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
