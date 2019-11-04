package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import java.util.Collection;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Done;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public class DoneProject extends Project implements Done {
    String memoir;

    public DoneProject(String id, User admin, Location location, String title, String description, Collection<Object> positions, Map<Object, User> positionUsers, String memoir) {
        super(id, admin, location, title, description, positions, positionUsers);
        this.memoir = memoir;
    }

    @Override
    public String getMemoir() {
        return memoir;
    }

    @Override
    public void setMemoir(String memoir) {
        //TODO db call
    }
}
