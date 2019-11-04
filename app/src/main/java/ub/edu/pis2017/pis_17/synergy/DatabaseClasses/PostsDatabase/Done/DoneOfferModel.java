package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.HasParticipant;

public class DoneOfferModel extends HasParticipant implements DoneModel {

    private String memoir;

    public DoneOfferModel(String id, String adminUid, Location location, String title, String description, PostType postType, String participant_uid, String memoir) {
        super(id, adminUid, location, title, description, postType, participant_uid);
        this.memoir = memoir;
    }

    @Override
    public String getMemoir() {
        return this.memoir;
    }

    public DoneOfferModel() {
        super();
    }
}
