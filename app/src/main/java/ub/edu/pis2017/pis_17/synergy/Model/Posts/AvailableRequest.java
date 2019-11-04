package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public class AvailableRequest extends Request implements Available {
    public AvailableRequest(String id, User admin, Location location, String title, String description) {
        super(id, admin, location, title, description);
    }

    @Override
    public void addParticipant(User u, Object context) throws IllegalArgumentException {

    }
}
