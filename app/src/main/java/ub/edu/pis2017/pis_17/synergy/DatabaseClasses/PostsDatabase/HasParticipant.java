package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;

public abstract class HasParticipant extends PostModel {

    protected String participantUid;

    public HasParticipant(String id, String adminUid, Location location, String title, String description, PostType postType, String participantUid) {
        super(id, adminUid, location, title, description, postType);
        this.participantUid = participantUid;
    }

    public String getParticipantUid(){
        return this.participantUid;
    }

    public void setParticipantUid(String participantUid) {
        this.participantUid = participantUid;
    }

    public HasParticipant() {
        super();
    }
}
