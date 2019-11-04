package ub.edu.pis2017.pis_17.synergy.View.Showers;

import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 30/04/2018.
 */

interface Shower<T> {
     public <B extends T> void show(B user);
}
