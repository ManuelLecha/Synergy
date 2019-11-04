package ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces;

import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public interface HasParticipant {
    /**
     * @return
     */
    public User getParticipant();

    /**
     * @return
     */
    public boolean isParticipant(User u);

    public String getId();
}
