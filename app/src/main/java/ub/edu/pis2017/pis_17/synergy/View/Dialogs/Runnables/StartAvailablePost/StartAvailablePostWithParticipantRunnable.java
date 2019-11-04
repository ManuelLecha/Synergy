package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost;

import android.app.DialogFragment;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Ongoing;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;

public abstract class StartAvailablePostWithParticipantRunnable<PostType extends Available> {

    protected PostType post;
    protected DialogFragment dialogFragment;
    protected Message message;
    protected ProgressBarToggleable progressBarToggleable;

    public StartAvailablePostWithParticipantRunnable(PostType post, DialogFragment dialogFragment, Message message, ProgressBarToggleable progressBarToggleable) {
        this.post = post;
        this.dialogFragment = dialogFragment;
        this.message = message;
        this.progressBarToggleable = progressBarToggleable;
    }

    public abstract void run(User user);

}
