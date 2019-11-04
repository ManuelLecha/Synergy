package ub.edu.pis2017.pis_17.synergy.Presenter.Storage;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.User;

public class FirebaseStorageInteractor implements StorageInteractor {

    private final String DefaultPhotoRef = "logo.png";
    private FirebaseStorage storage;

    public FirebaseStorageInteractor() {
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public StorageReference getUserImageReference(User user) {
        String userimagePath = user.getImagePath();
        return storage.getReference().child(userimagePath);
    }

    @Override
    public FutureBox<String> addNewUserImage(User user, Uri file) {
        FutureBox<String> futureImagePath = FutureBox.empty();
        StorageReference oldPhotoRef = storage.getReference().child(user.getImagePath());
        if(!oldPhotoRef.equals(DefaultPhotoRef)){oldPhotoRef.delete();}
        String newPhotoPath = user.getUid() + file.getLastPathSegment();
        StorageReference newPhotoRef = storage.getReference().child(newPhotoPath);
        UploadTask task = newPhotoRef.putFile(file);
        task.addOnSuccessListener(t -> {
            futureImagePath.give(newPhotoPath);
        }).addOnFailureListener(t -> {
            futureImagePath.error("Could not add user with uid " + user.getUid() + " the new profile image");
        });
        return futureImagePath;
    }


}
