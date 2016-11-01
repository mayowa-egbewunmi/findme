package com.rotimi.finder.main.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.yarata.ysell.miniyarata.db.models.Customer;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import io.bloco.faker.Faker;


/**
 * Created by mayowa on 9/2/16.
 */
public class Seeder {

    private Faker fakeData;
    private DatabaseWrapper dbWrapper;

    private static final String LOG = Seeder.class.getName();

    public Seeder(DatabaseWrapper dbWrapper){
        this.dbWrapper = dbWrapper;
    }

    public void loadDB(){

        fakeData = new Faker();
        List<Class<? extends Model>> models = FlowManager.getDatabase(YarataDB.class).getModelClasses();

        for(Class<? extends Model> model : models){
            Log.d(LOG, "Loading "+model.getSimpleName()+".....");
            saveModel(model);
        }
    }

    public void saveModel(Class<? extends Model> tableModel){
        JSONObject modelObject;
        try {
            switch (tableModel.getSimpleName()) {
                case (Customer.TABLENAME): {
                    Cursor cursor =  SQLite.select().from(Customer.class).query();
                    //confirm no record exist in the Assessment model
                    if(cursor!=null && !cursor.moveToFirst()) {
                        cursor.close();
                        for (int i = 1; i <= 20; i++) {
                            modelObject = new JSONObject();
                        }
                    }else{
                        Log.d(LOG, "Record exist in model "+tableModel.getSimpleName());
                    }
                    break;
                }

            }
        } catch (Exception ignored) { }
    }

    public void runInsertQuery(String tableName, JSONObject contents){

        ContentValues values = new ContentValues();
        Iterator<String> keys = contents.keys();

        try {
            while (keys.hasNext()) {
                String key = keys.next();
                values.put(key, contents.getString(key));
            }

            dbWrapper.insertWithOnConflict(tableName, null, values, 0); //Last argument is irrelevant, since SDK is > FROYO

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

