package ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces;

import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public interface Available {
    /**
     * @param u
     * @param context
     */
    public void addParticipant(User u, Object context) throws IllegalArgumentException;

    /**
     * Returns id of the post
     * @return
     */
    public String getId();

    /**
     *
     * @return
     */
    public String getTitle();

}
