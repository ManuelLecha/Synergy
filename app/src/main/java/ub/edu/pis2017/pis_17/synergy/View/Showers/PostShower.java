package ub.edu.pis2017.pis_17.synergy.View.Showers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Offer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Project;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien.AvailableAlienOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien.AvailableAlienProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien.AvailableAlienRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.own.AvailableOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.own.AvailableOwnProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.own.AvailableOwnRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.alien.DoneAlienOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.alien.DoneAlienProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.alien.DoneAlienRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.own.DoneOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.own.DoneOwnProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.own.DoneOwnRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.alien.OngoingAlienOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.alien.OngoingAlienProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.alien.OngoingAlienRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnRequestActivity;

/**
 * Created by kanales on 29/04/2018.
 */

public class PostShower implements Shower<Post> {

    private Context context;

    public PostShower(Context context) {
        this.context = context;
    }

    @Override
    public void show(Post p) {
        FutureBox<User> currentUserBox = Presenter.getInstance().getCurrentUser();
        currentUserBox.whenFull(user -> {
            Intent intent = null;
            if(user.equals(p.getAdmin())) {
                if (p instanceof AvailableOffer) {
                    intent = new Intent(context, AvailableOwnOfferActivity.class);
                } else if (p instanceof AvailableRequest) {
                    intent = new Intent(context, AvailableOwnRequestActivity.class);
                } else if (p instanceof AvailableProject) {
                    intent = new Intent(context, AvailableOwnProjectActivity.class);
                } else if (p instanceof OngoingOffer) {
                    intent = new Intent(context, OngoingOwnOfferActivity.class);
                } else if (p instanceof OngoingRequest) {
                    intent = new Intent(context, OngoingOwnRequestActivity.class);
                } else if (p instanceof OngoingProject) {
                    intent = new Intent(context, OngoingOwnProjectActivity.class);
                } else if (p instanceof DoneOffer) {
                    intent = new Intent(context, DoneOwnOfferActivity.class);
                } else if (p instanceof DoneRequest) {
                    intent = new Intent(context, DoneOwnRequestActivity.class);
                } else if (p instanceof DoneProject) {
                    intent = new Intent(context, DoneOwnProjectActivity.class);
                }
            }
            else {
                if (p instanceof AvailableOffer) {
                    intent = new Intent(context, AvailableAlienOfferActivity.class);
                } else if (p instanceof AvailableRequest) {
                    intent = new Intent(context, AvailableAlienRequestActivity.class);
                } else if (p instanceof AvailableProject) {
                    intent = new Intent(context, AvailableAlienProjectActivity.class);
                } else if (p instanceof OngoingOffer) {
                    intent = new Intent(context, OngoingAlienOfferActivity.class);
                } else if (p instanceof OngoingRequest) {
                    intent = new Intent(context, OngoingAlienRequestActivity.class);
                } else if (p instanceof OngoingProject) {
                    intent = new Intent(context, OngoingAlienProjectActivity.class);
                } else if (p instanceof DoneOffer) {
                    intent = new Intent(context, DoneAlienOfferActivity.class);
                } else if (p instanceof DoneRequest) {
                    intent = new Intent(context, DoneAlienRequestActivity.class);
                } else if (p instanceof DoneProject) {
                    intent = new Intent(context, DoneAlienProjectActivity.class);
                }
            }
            if(intent != null) {
                intent.putExtra("PostID", p.getId());
                context.startActivity(intent);
            }
        });

    }

}
