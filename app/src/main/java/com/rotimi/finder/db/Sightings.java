package com.rotimi.finder.db;

import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;
import com.rotimi.finder.util.Constants;

/**
 * Created by mayowa on 11/2/16.
 */

@Table(database =  FindMeDB.class)
@ModelContainer
public class Sightings extends BaseModel {

    public static final String TABLENAME = Sightings.class.getSimpleName();

    @Column
    @PrimaryKey(autoincrement = true)
    public int _id;

    @Column
    @Unique
    public String id;

    @Column
    public String location;

    @Column
    public String comment;

    @Column
    public String imageUrl;

    @Column
    @ForeignKey(tableClass = Reports.class, references = {@ForeignKeyReference(columnName = Constants.REPORT_ID, columnType = String.class, foreignKeyColumnName = Constants.ID)})
    public String report;

    @Column
    public String created_at;

    @Column
    public String updated_at;

    public ForeignKeyContainer<Reports> getReport(String id) {
        Reports report = SQLite.select().from(Reports.class).where(Reports_Table.id.eq(id)).querySingle();
        if(report!=null){
            Log.d("REPORT", "Report not null");
        }else{
            Log.d("REPORT", "Report is null");
        }
        return FlowManager.getContainerAdapter(Reports.class)
                .toForeignKeyContainer(report);
    }
}