package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.FinishOwnOngoingPost;

import android.app.DialogFragment;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Ongoing;

public abstract class FinishOngoingPostWithMemoirRunnable<PostType extends Ongoing> {

    protected PostType post;
    protected DialogFragment dialogFragment;

    public FinishOngoingPostWithMemoirRunnable(PostType post, DialogFragment dialogFragment) {
        this.post = post;
        this.dialogFragment = dialogFragment;
    }

    public abstract void run(String memoir);

}
