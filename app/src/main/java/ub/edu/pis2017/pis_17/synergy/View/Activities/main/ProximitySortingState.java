package ub.edu.pis2017.pis_17.synergy.View.Activities.main;

import android.location.Location;

import java.util.List;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;

/**
 * Created by kanales on 07/05/2018.
 */

public class ProximitySortingState implements SortingState<Post> {

    private final Location loc;

    public ProximitySortingState(Location loc) {
        this.loc = loc;
    }

    @Override
    public void sort(List<? extends Post> l) {
        l.sort((p,q) -> {
            float a = p.getLocation().distanceTo(loc);
            float b = q.getLocation().distanceTo(loc);
            return Float.compare(a,b);
        });
    }
}
