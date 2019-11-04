package ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public interface HasMultipleParticipants {

    /**
     * @param context
     * @return
     */
    public User getParticipantFor(Object context);

    /**
     * @return map of participants and users
     */
    public Map<Object,User> getPositionUsers();

    public Collection<Object> getPositions();

    /**
     * @param u
     * @return
     */
    public boolean isParticipant(User u);

    public int getParticipantCount();

    public String getId();

}
