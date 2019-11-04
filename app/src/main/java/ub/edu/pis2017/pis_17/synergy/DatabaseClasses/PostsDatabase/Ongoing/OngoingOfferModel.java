package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.HasParticipant;

public class OngoingOfferModel extends HasParticipant {
    public OngoingOfferModel(String id, String adminUid, Location location, String title, String description, PostType postType, String participant_uid) {
        super(id, adminUid, location, title, description, postType, participant_uid);
    }

    public OngoingOfferModel() {
        super();
    }
}
