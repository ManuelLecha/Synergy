package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.RateDoneUser;

import android.app.Activity;
import android.app.DialogFragment;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class RateUserFromDonePostRunnable {

    private User toUser;
    private User fromUser;
    private ProgressBarToggleable progressBarToggleable;
    private Activity parentActivity;
    private DialogFragment dialogFragment;
    private Message message;

    public RateUserFromDonePostRunnable(User toUser, ProgressBarToggleable progressBarToggleable, Activity parentActivity, DialogFragment dialogFragment, RateMessage message) {
        this.toUser = toUser;
        this.progressBarToggleable = progressBarToggleable;
        this.parentActivity = parentActivity;
        this.dialogFragment = dialogFragment;
        this.message = message;
        fromUser = message.getFrom();
    }

    public void run(int rate) {
        progressBarToggleable.setToLoading();

        FutureResult rateResult = Presenter.getInstance().rateUser(fromUser.getUid(), rate);
        rateResult.whenDone(() -> {
            FutureResult markResult = Presenter.getInstance().markMessageAsDone(message);
            markResult.whenDone(() -> {
                ViewMessageBuilder mb = new ViewMessageBuilder();
                mb.setTitle("Rated");
                mb.setContent("You have been given a score of " + String.valueOf(rate) + " points on " + message.getContext().getTitle());
                mb.setToUser(toUser);
                mb.setPostContext(message.getContext());
                SystemMessage message = mb.buildSystemMessage();
                FutureResult sendMessageResult = Presenter.getInstance().sendSystemMessage(message);
                sendMessageResult.whenDone(() -> {
                    Toast.makeText(parentActivity, "User rated", Toast.LENGTH_LONG).show();
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
        }).ifFails(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
        });
    }

}
