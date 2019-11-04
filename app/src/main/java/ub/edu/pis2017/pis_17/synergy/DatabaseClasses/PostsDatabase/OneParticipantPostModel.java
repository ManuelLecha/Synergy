package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;

public class OneParticipantPostModel extends PostModel{

    public String participant;

    public OneParticipantPostModel(String id, String adminUid, Location location, String title, String description, PostType postType, String participant) {
        super(id, adminUid, location, title, description, postType);
        this.participant = participant;
    }

    public OneParticipantPostModel(String id, String admin_uid, Location location, String title, String description, PostType postType){
        super(id, admin_uid, location, title, description, postType);
        this.participant = null;
    }
}
