package ub.edu.pis2017.pis_17.synergy.View.Activities.main;

import java.util.List;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;

/**
 * Created by kanales on 07/05/2018.
 */

public class ScoreSortingState implements SortingState<Post> {
    @Override
    public void sort(List<? extends Post> l) {
        l.sort((p,q) -> -Float.compare(p.getAdmin().getRating(),q.getAdmin().getRating()));
    }
}
