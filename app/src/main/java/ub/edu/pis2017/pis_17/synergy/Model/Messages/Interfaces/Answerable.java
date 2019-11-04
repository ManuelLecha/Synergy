package ub.edu.pis2017.pis_17.synergy.Model.Messages.Interfaces;

import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 24/04/2018.
 */

public interface Answerable {

    public User getAdressee();

    public void answer(String message);

}
