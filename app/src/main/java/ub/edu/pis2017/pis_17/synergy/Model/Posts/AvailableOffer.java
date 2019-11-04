package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import java.util.Collection;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public class AvailableOffer extends Offer implements Available {
    public AvailableOffer(String id, User admin, Location location, String title, String description) {
        super(id, admin, location, title, description);
    }

    @Override
    public void addParticipant(User u, Object context) throws IllegalArgumentException {
        // TODO call to db
    }

}
