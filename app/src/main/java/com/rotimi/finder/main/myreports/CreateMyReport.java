package com.rotimi.finder.main.myreports;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.marcohc.toasteroid.Toasteroid;
import com.rotimi.finder.R;
import com.rotimi.finder.api.FindMeDatabase;
import com.rotimi.finder.api.Storage;
import com.rotimi.finder.main.DialogListener;
import com.rotimi.finder.main.UserDialog;
import com.rotimi.finder.main.publicreports.ReportItem;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.SystemData;
import com.rotimi.finder.util.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class CreateMyReport extends AppCompatActivity implements DialogListener {
    public final static String TAG = CreateMyReport.class.getSimpleName();
    public final static String MENU_ID = "menu_id";

    private Utility utility;

    @BindView(R.id.myreport_age) TextView ageView;
    @BindView(R.id.myreport_comment) TextView commentView;
    @BindViews({R.id.myreport_complexion_brown, R.id.myreport_complexion_dark, R.id.myreport_complexion_fair}) List<RadioButton> complexionViews;
    @BindViews({R.id.myreport_type_kidnapped, R.id.myreport_type_missing, R.id.myreport_type_wanted}) List<RadioButton> typeViews;
    @BindViews({R.id.myreport_sex_female, R.id.myreport_sex_male }) List<RadioButton> sexViews;
    @BindView(R.id.myreport_complexion) RadioGroup complexionGroupView;
    @BindView(R.id.myreport_sex) RadioGroup sexGroupView;
    @BindView(R.id.myreport_type) RadioGroup typeGroupView;
    @BindView(R.id.myreport_height) TextView heightView;
    @BindView(R.id.myreport_picture) ImageView pictureView;
    @BindView(R.id.myreport_police_station) Spinner policeView;
    @BindView(R.id.myreport_name) TextView nameView;
    @BindView(R.id.myreport_upload_picture) TextView uploadPictureView;
    @BindView(R.id.myreport_upload) TextView uploadView;

    private String name, comment, police, age, height;
    private String sex, complexion, type, imageUrl;

    private boolean pictureUploaded = false;
    private Uri imagePath;
    private Bitmap profilePicture;
    private ReportItem myReportItem;

    private Storage storage;
    private FindMeDatabase findmeDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_myreport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        utility = new Utility(this);
        myReportItem = new ReportItem();

        storage = new Storage();
        findmeDatabase = new FindMeDatabase(this);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.state_pros, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        policeView.setAdapter(staticAdapter);

        policeView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                police = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        sexGroupView.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.myreport_sex_female){
                sex = Constants.FEMALE;
            }else if(checkedId == R.id.myreport_sex_male){
                sex = Constants.MALE;
            }
        });

        complexionGroupView.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.myreport_complexion_brown){
                complexion = Constants.BROWN;
            }
            else if(checkedId == R.id.myreport_complexion_dark){
                complexion = Constants.DARK;
            }
            else if(checkedId == R.id.myreport_complexion_fair){
                complexion = Constants.FAIR;
            }
        });

        typeGroupView.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.myreport_type_kidnapped){
                type = Constants.KIDNAPPED;
            }
            else if(checkedId == R.id.myreport_type_wanted){
                type = Constants.WANTED;
            }
            else if(checkedId == R.id.myreport_type_missing){
                type = Constants.MISSING;
            }
        });
        uploadPictureView.setOnClickListener(v -> showFileChooser());

        uploadView.setOnClickListener(v -> {
            if(validate()){
                if(utility.isRegistered()){
                    if(utility.isConnected()) {
                        startUploading(getPath(imagePath));
                    }else{
                        Toasteroid.show(this, R.string.connection_error, Toasteroid.STYLES.ERROR);
                    }
                }else{
                    //Show dialog to register user
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    UserDialog.newInstance().show(fragmentManager, UserDialog.TAG);
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        name = nameView.getText().toString().trim();
        comment = commentView.getText().toString().trim();
        age = ageView.getText().toString().trim();
        height = heightView.getText().toString().trim();

        if (name.isEmpty()) {
            nameView.setError(getString(R.string.name_error));
            valid = false;
        } else {
            nameView.setError(null);
        }

        if(age.isEmpty()){
            ageView.setError(getString(R.string.empty_error));
            valid = false;
        }else{
            ageView.setError(null);
        }
        if(height.isEmpty()) {
            heightView.setError(getString(R.string.empty_error));
            valid = false;
        }else{
            heightView.setError(null);
        }
        if(!pictureUploaded){
            Toasteroid.show(this, getString(R.string.picture_not_loaded), Toasteroid.STYLES.ERROR);
            valid = false;
        }
        return valid;
    }
    public void showProgress(){
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.show();
    }

    public void submitReport(){
        myReportItem.name = name;
        myReportItem.age = age;
        myReportItem.comment = comment;
        myReportItem.complexion = complexion;
        myReportItem.height = height;
        myReportItem.imageUrl = imageUrl;
        myReportItem.police = police;
        myReportItem.sex = sex;
        myReportItem.type = type;
        myReportItem.mobile_number = new SystemData(this).getString(Constants.PHONE);
        myReportItem.id = UUID.randomUUID().toString();

        findmeDatabase.dbRef.child(Constants.REPORTS).child(myReportItem.id).setValue(myReportItem);

//        String key = FindMeDatabase.dbRef.child(Constants.REPORTS).push().getKey();
//        Map<String, Object> childUpdate = new HashMap<>();
//        childUpdate.put("/reports/"+key, myReportItem);
//        FindMeDatabase.dbRef.updateChildren(childUpdate);
    }

    public void startUploading(String filePath){
        showProgress();
        progressDialog.setMessage(getString(R.string.uploading_picture));
        String name = filePath.substring(filePath.lastIndexOf("/"));

        Log.d(TAG, name+" ===== ");
        StorageReference mountainsRef = storage.storageReference.child(name);

        try {
            InputStream stream = new FileInputStream(new File(filePath));

            UploadTask uploadTask = mountainsRef.putStream(stream);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Toasteroid.show(CreateMyReport.this, R.string.failed, Toasteroid.STYLES.ERROR);
                exception.printStackTrace();

            }).addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, downloadUrl.getPath());
                progressDialog.setMessage(getString(R.string.sending_report));

                imageUrl = downloadUrl.getPath();
                new Async().execute();
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }
            //And finally ask for the permission
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, Constants.MY_PERMISSIONS_REQUEST);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Constants.PICK_IMAGE_REQUEST);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toasteroid.show(this, R.string.storage_permitted, Toasteroid.STYLES.SUCCESS);
            } else {
                //Displaying another toast if permission is not granted
                Toasteroid.show(this, R.string.no_permission, Toasteroid.STYLES.ERROR);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == this.RESULT_OK && data != null && data.getData() != null) {
            imagePath = data.getData();
            try {
                pictureUploaded = true;
                profilePicture = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                pictureView.setImageBitmap(profilePicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);

        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    public void onReturn(String tag) {

        if(tag == UserDialog.TAG){
            if(utility.isConnected()) {
                startUploading(getPath(imagePath));
            }else{
                Toasteroid.show(this, R.string.connection_error, Toasteroid.STYLES.ERROR);
            }
        }
    }

    private class Async extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            submitReport();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toasteroid.show(CreateMyReport.this, R.string.success_create_more, Toasteroid.STYLES.SUCCESS);
        }
    }
}

