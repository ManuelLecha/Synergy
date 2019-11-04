package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.ReplyToUser;

import android.app.Activity;
import android.app.DialogFragment;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.MessageBuilder;
import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.UserMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class ReplyToUserWithMessageRunnable {

    private User toUser;
    private Post postContext;
    private ProgressBarToggleable progressBarToggleable;
    private Activity parentActivity;
    private DialogFragment dialogFragment;

    public ReplyToUserWithMessageRunnable(User toUser, Post postContext, ProgressBarToggleable progressBarToggleable, Activity parentActivity, DialogFragment dialogFragment) {
        this.toUser = toUser;
        this.postContext = postContext;
        this.progressBarToggleable = progressBarToggleable;
        this.parentActivity = parentActivity;
        this.dialogFragment = dialogFragment;
    }

    public void run(String title, String content) {
        progressBarToggleable.setToLoading();

        ViewMessageBuilder mb = new ViewMessageBuilder();
        mb.setId("");
        mb.setTitle(title);
        mb.setContent(content);
        mb.setToUser(toUser);
        mb.setPostContext(postContext);

        FutureBox<User> actualUserBox = Presenter.getInstance().getCurrentUser();
        actualUserBox.whenFull(user -> {
            mb.setToUser(user);
            FutureResult sendResult = Presenter.getInstance().sendUserMessage(mb.buildUserMessage());
            sendResult.whenDone(() -> {
                Toast.makeText(parentActivity, "Message sent", Toast.LENGTH_LONG).show();
                progressBarToggleable.setToFree();
                dialogFragment.dismiss();
            }).ifFails(e -> {
                Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
                progressBarToggleable.setToFree();
            });
        }).onCancellation(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
        });
    }

}
