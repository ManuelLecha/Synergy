package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing;

import android.location.Location;

import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.HasManyParticipantModel;

public class OngoingProjectModel extends HasManyParticipantModel {
    public OngoingProjectModel(String id, String adminUid, Location location, String title, String description, PostType postType, Map<String, String> vacants) {
        super(id, adminUid, location, title, description, postType, vacants);
    }

    public OngoingProjectModel() {
        super();
    }
}
