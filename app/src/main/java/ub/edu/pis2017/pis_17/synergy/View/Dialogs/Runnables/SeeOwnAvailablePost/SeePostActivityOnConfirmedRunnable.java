package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.SeeOwnAvailablePost;

import android.app.Activity;
import android.content.Intent;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.own.AvailableOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.InboxActivity;

public class SeePostActivityOnConfirmedRunnable {

    private Post post;
    private Activity parentActivity;
    private Class activityClass;

    public SeePostActivityOnConfirmedRunnable(Post post, Activity parentActivity, Class activityClass) {
        this.post = post;
        this.parentActivity = parentActivity;
        this.activityClass = activityClass;
    }

    public void run() {
        Intent i = new Intent(parentActivity, activityClass);
        i.putExtra("PostID", post.getId());
        parentActivity.startActivity(i);
    }

}
