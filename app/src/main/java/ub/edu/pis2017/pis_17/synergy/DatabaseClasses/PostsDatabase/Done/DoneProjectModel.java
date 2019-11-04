package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done;

import android.location.Location;

import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.HasManyParticipantModel;

public class DoneProjectModel extends HasManyParticipantModel implements DoneModel {

    private String memoir;

    public DoneProjectModel(String id, String adminUid, Location location, String title, String description, PostType postType, Map<String, String> vacants, String memoir) {
        super(id, adminUid, location, title, description, postType, vacants);
        this.memoir = memoir;
    }

    @Override
    public String getMemoir() {
        return this.memoir;
    }

    public DoneProjectModel() {
        super();
    }
}
