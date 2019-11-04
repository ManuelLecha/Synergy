package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase;

import android.location.Location;

import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;

public abstract class HasManyParticipantModel extends PostModel {

    protected Map<String,String> vacants;

    public HasManyParticipantModel(String id, String adminUid, Location location, String title, String description, PostType postType, Map<String, String> vacants) {
        super(id, adminUid, location, title, description, postType);
        this.vacants = vacants;
    }

    /**
     * Checks if there is a participant
     * @return
     */
    public boolean completedParticipants(){
        return !vacants.containsValue(null);
    }

    public Map<String,String> getVacants(){
        return this.vacants;
    }

    public HasManyParticipantModel() {
        super();
    }
}
