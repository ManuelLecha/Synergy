package ub.edu.pis2017.pis_17.synergy.Presenter.Storage;

import android.media.Image;
import android.net.Uri;

import com.google.firebase.storage.StorageReference;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.User;

public interface StorageInteractor {

    /**
     * Gettign the user storage reference from a user
     * @param user
     */
    public StorageReference getUserImageReference(User user);

    /**
     * Errases the previous image and deletes the new one
     * @param file
     * @return User path image
     */
    public FutureBox<String> addNewUserImage(User user, Uri file);

}
