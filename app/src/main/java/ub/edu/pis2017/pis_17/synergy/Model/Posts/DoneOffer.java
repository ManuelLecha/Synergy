package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Done;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.HasParticipant;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public class DoneOffer extends Offer implements Done, HasParticipant {
    private String memoir;
    private User participant;

    public DoneOffer(String id, User admin, Location location, String title, String description, String memoir, User participant) {
        super(id, admin, location, title, description);
        this.memoir = memoir;
        this.participant = participant;
    }

    @Override
    public User getParticipant() {
        return participant;
    }

    @Override
    public boolean isParticipant(User u) {
        return false;
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
