package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.HasParticipant;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Ongoing;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public class OngoingOffer extends Offer implements Ongoing, HasParticipant {
    private User participant;

    public OngoingOffer(String id, User admin, Location location, String title, String description, User participant) {
        super(id, admin, location, title, description);
        this.participant = participant;
    }

    @Override
    public User getParticipant() {
        return participant;
    }

    public boolean isParticipant(User u) {
        return this.getParticipant().equals(u);
    }
}
