package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.HasParticipant;

public class DoneRequestModel extends HasParticipant implements DoneModel {

    private String memoir;

    public DoneRequestModel(String id, String adminUid, Location location, String title, String description, PostType postType, String participant_uid, String memoir) {
        super(id, adminUid, location, title, description, postType, participant_uid);
        this.memoir = memoir;
    }

    @Override
    public String getMemoir() {
        return memoir;
    }

    public DoneRequestModel() {
        super();
    }
}
