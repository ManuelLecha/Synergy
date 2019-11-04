package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

/**
 * Created by kanales on 07/05/2018.
 */

interface Builder<F,T> {
    public Builder setModel(F o);
    public T build();
}
