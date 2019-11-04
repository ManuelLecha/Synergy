package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase;

import android.location.Location;

import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;

public class ManyParticipantPostModel extends PostModel{

    private Map<String,String> vacants;

    public ManyParticipantPostModel(String id, String adminUid, Location location, String title, String description, PostType postType, Map<String, String> vacants) {
        super(id, adminUid, location, title, description, postType);
        this.vacants = vacants;
    }

    public Map<String, String> getVacants() {
        return vacants;
    }

    public void setVacants(Map<String, String> vacants) {
        this.vacants = vacants;
    }
}
