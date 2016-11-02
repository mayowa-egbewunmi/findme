package com.rotimi.finder.main.sightings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marcohc.toasteroid.Toasteroid;
import com.rotimi.finder.R;
import com.rotimi.finder.db.FindMeDatabase;
import com.rotimi.finder.main.DialogListener;
import com.rotimi.finder.main.UserDialog;
import com.rotimi.finder.util.Constants;
import com.rotimi.finder.util.SystemData;
import com.rotimi.finder.util.Utility;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SightingCreateFragment extends Fragment implements DialogListener{

    private Utility utility;
    private SystemData systemData;

    @BindView(R.id.create_sighting_comment) TextView commentView;
    @BindView(R.id.create_sighting_location) TextView locationView;
    @BindView(R.id.create_sighting_upload) TextView uploadView;
    @BindView(R.id.create_sighting_upload_picture) TextView uploadPictureView;
    @BindView(R.id.create_sighting_picture) ImageView pictureView;

    private String comment, location, imageUrl;
    private SightingItem sightingItem;

    private boolean pictureUploaded = false;
    private Uri imagePath;
    private Bitmap profilePicture;

    private String reportKey;

    public static SightingCreateFragment newInstance(String param1, String param2) {
        SightingCreateFragment fragment = new SightingCreateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sighting_create, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        utility = new Utility(getActivity());
        systemData = new SystemData(getActivity());

        sightingItem = new SightingItem();

        uploadPictureView.setOnClickListener(v -> showFileChooser());

        uploadView.setOnClickListener(v -> {
            if(validate()){
                if(utility.isRegistered()){
                    uploadSighting();
                }else{
                    //Show dialog to register user
                    FragmentManager fragmentManager = getChildFragmentManager();
                    UserDialog.newInstance().show(fragmentManager, UserDialog.TAG);
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        location = locationView.getText().toString().trim();
        comment = commentView.getText().toString().trim();

        if (location.isEmpty()) {
            locationView.setError(getString(R.string.empty_error));
            valid = false;
        } else {
            locationView.setError(null);
        }
        if(comment.isEmpty()){
            commentView.setError(getString(R.string.empty_error));
            valid = false;
        }else{
            commentView.setError(null);
        }
        if(!pictureUploaded){
            Toasteroid.show(getActivity(), getString(R.string.picture_not_loaded), Toasteroid.STYLES.ERROR);
            valid = false;
        }
        return valid;
    }

    public void setValue() {
        sightingItem.id = UUID.randomUUID().toString();
        sightingItem.location = location;
        sightingItem.comment = comment;
        sightingItem.imageUrl = imageUrl;
    }

    public void submitReport(){
        setValue();
        String key = FindMeDatabase.dbRef.child(Constants.REPORTS).child(reportKey).child(Constants.SIGHTINGS).push().getKey();
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/reports/"+reportKey+"/sightings/"+key, sightingItem);

        FindMeDatabase.dbRef.updateChildren(childUpdate);
    }

    private void uploadSighting(){
        //getting the actual path of the image
        String path = getPath(imagePath);
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getActivity(), uploadId, Constants.END_POINT+"teacher")
                    .addFileToUpload(path, Constants.FILE) //Adding file
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                //If the user has denied the permission previously your code will come to this block
                //Here you can explain why you need this permission
                //Explain here why you need this permission
            }
            //And finally ask for the permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, Constants.MY_PERMISSIONS_REQUEST);
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
                Toasteroid.show(getActivity(), R.string.storage_permitted, Toasteroid.STYLES.SUCCESS);
            } else {
                //Displaying another toast if permission is not granted
                Toasteroid.show(getActivity(), R.string.no_permission, Toasteroid.STYLES.ERROR);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imagePath = data.getData();
            try {
                pictureUploaded = true;
                profilePicture = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
                pictureView.setImageBitmap(profilePicture);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
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
            uploadSighting();
        }
    }


}
