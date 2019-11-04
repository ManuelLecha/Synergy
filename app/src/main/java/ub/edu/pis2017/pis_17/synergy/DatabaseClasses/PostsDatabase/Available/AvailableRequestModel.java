package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.PostModel;

public class AvailableRequestModel extends PostModel {
    public AvailableRequestModel(String id, String adminUid, Location location, String title, String description, PostType postType) {
        super(id, adminUid, location, title, description, postType);
    }

    public AvailableRequestModel() {
        super();
    }

    @Override
    public String getAdminUid() {
        return super.getAdminUid();
    }
}
