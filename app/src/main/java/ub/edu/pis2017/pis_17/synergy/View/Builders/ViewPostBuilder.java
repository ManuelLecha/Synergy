package ub.edu.pis2017.pis_17.synergy.View.Builders;

import android.location.Location;

import java.util.Collection;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.Model.User;

public class ViewPostBuilder {

    private String id;
    private User admin;
    private User participant;
    private Map<Object, User> users;
    private Collection<Object> positions;
    private Location location;
    private String title;
    private String description;
    private String memoir;

    public ViewPostBuilder() {
        this.id = "";
    }

    public void setId(String id) {this.id = id;}
    public void setAdmin(User admin) {this.admin = admin;}
    public void setParticipant(User participant) {this.participant = participant;}
    public void setPositions(Collection<Object> positions){this.positions = positions;}
    public void setUsers(Map<Object, User> users) {this.users = users;}
    public void setLocation(Location location) {this.location = location;}
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setMemoir(String memoir) {this.memoir = memoir;}

    public AvailableRequest buildAvailableRequest() {
        return new AvailableRequest(id, admin, location, title, description);
    }

    public OngoingRequest buildOngoingRequest() {
        return new OngoingRequest(id, admin, location, title, description, participant);
    }

    public DoneRequest buildDoneRequest() {
        return new DoneRequest(id, admin, location, title, description, memoir, participant);
    }

    public AvailableOffer buildAvailableOffer() {
        return new AvailableOffer(id, admin, location, title, description);
    }

    public OngoingOffer buildOngoingOffer() {
        return new OngoingOffer(id, admin, location, title, description, participant);
    }

    public DoneOffer buildDoneOffer() {
        return new DoneOffer(id, admin, location, title, description, memoir, participant);
    }

    public AvailableProject buildAvailableProject() {
        return new AvailableProject(id, admin, location, title, description, positions, users);
    }

    public OngoingProject buildOngoingProject() {
        return new OngoingProject(id, admin, location, title, description, positions, users);
    }

    public DoneProject buildDoneProject() {
        return new DoneProject(id, admin, location, title, description, positions, users, memoir);
    }

}
