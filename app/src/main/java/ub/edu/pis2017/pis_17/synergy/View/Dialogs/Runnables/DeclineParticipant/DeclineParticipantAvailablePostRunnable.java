package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.DeclineParticipant;

import android.app.Activity;
import android.app.DialogFragment;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Project;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class DeclineParticipantAvailablePostRunnable {

    private Post post;
    private Activity parentActivity;
    private PostConfirmMessage message;
    private ProgressBarToggleable progressBarToggleable;
    private DialogFragment dialogFragment;

    public DeclineParticipantAvailablePostRunnable(Post post, Activity parentActivity, DialogFragment dialogFragment, PostConfirmMessage message, ProgressBarToggleable progressBarToggleable) {
        this.post = post;
        this.parentActivity = parentActivity;
        this.message = message;
        this.progressBarToggleable = progressBarToggleable;
        this.dialogFragment = dialogFragment;
    }

    public void run() {
        progressBarToggleable.setToLoading();
        Presenter.getInstance().markMessageAsDone(message).whenDone(() -> {
            ViewMessageBuilder mb = new ViewMessageBuilder();
            mb.setId("");
            mb.setTitle("Participation declined");
            mb.setContent("Admin " + post.getAdmin() + " has declined your participation in " + post.getTitle());
            mb.setToUser(message.getFrom());
            mb.setPostContext(post);
            SystemMessage sm = mb.buildSystemMessage();
            Presenter.getInstance().sendSystemMessage(sm).whenDone(() -> {
                Toast.makeText(parentActivity, "Participant declined", Toast.LENGTH_LONG).show();
                progressBarToggleable.setToFree();
                dialogFragment.dismiss();
            }).ifFails(e -> {
                Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
                progressBarToggleable.setToFree();
            });
        }).ifFails(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
        });
    }

}