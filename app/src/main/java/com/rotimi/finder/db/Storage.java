package com.rotimi.finder.db;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rotimi.finder.util.Constants;

/**
 * Created by mayowa on 11/2/16.
 */

public class Storage {

    public static StorageReference storageReference;
    public Storage(){
        storageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_URL).child("findme");
    }
}
