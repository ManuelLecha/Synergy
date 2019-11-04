package ub.edu.pis2017.pis_17.synergy.View.Activities.main;

import java.util.List;

/**
 * Created by kanales on 07/05/2018.
 */

public interface SortingState<Item> {
    public void sort(List<? extends Item> list);
}
