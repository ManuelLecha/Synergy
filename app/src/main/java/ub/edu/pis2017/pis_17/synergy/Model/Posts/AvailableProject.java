package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import java.util.Collection;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public class AvailableProject extends Project implements Available {


    public AvailableProject(String id, User admin, Location location, String title, String description, Collection<Object> positions, Map<Object, User> positionUsers) {
        super(id, admin, location, title, description, positions, positionUsers);
    }

    @Override
    public void addParticipant(User u, Object context) throws IllegalArgumentException {
        super.fillVacancy(u,context);
    }
}
