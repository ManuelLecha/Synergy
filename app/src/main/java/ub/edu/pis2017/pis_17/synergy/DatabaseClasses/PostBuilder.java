package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.PostModel;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;

/**
 * Created by kanales on 07/05/2018.
 */

abstract class PostBuilder implements Builder<PostModel, Post> {
    protected PostModel postModel;

    public PostBuilder setModel(PostModel p) {
        this.postModel = p;
        return this;
    }

    abstract public Post build();
}
