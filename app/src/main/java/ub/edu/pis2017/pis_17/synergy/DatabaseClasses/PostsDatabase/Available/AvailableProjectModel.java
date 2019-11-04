package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available;

import android.location.Location;

import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.HasManyParticipantModel;

public class AvailableProjectModel extends HasManyParticipantModel {

    public AvailableProjectModel(String id, String adminUid, Location location, String title, String description, PostType postType, Map<String, String> vacants) {
        super(id, adminUid, location, title, description, postType, vacants);
    }

    public AvailableProjectModel() {
        super();
    }
}
