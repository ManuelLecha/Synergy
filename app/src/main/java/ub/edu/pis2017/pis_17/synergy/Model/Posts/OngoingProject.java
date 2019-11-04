package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import java.util.Collection;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Ongoing;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public class OngoingProject extends Project implements Ongoing {

    public OngoingProject(String id, User admin, Location location, String title, String description, Collection<Object> positions, Map<Object, User> positionUsers) {
        super(id, admin, location, title, description, positions, positionUsers);
    }

}
