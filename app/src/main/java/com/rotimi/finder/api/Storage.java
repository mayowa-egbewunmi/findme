package com.rotimi.finder.api;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rotimi.finder.util.Constants;

/**
 * Created by mayowa on 11/2/16.
 */

public class Storage {

    public StorageReference storageReference;
    public Storage(){
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Constants.STORAGE_URL).child("findme");
    }
}
