package ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces;

import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 28/04/2018.
 */

public interface Done {

    /**
     * Getes the memoir of a done post
     * @return
     */
    public String getMemoir();

    /**
     * Sets the momoir to the done post
     * @param memoir
     */
    public void setMemoir(String memoir);

    /**
     * @return Id of the done post
     */
    public String getId();

}
