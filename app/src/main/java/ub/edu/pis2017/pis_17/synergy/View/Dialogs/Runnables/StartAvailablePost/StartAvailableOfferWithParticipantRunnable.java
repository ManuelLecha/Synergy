package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost;

import android.app.Activity;
import android.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class StartAvailableOfferWithParticipantRunnable extends StartAvailablePostWithParticipantRunnable<AvailableOffer> {

    private Activity parentActivity;

    public StartAvailableOfferWithParticipantRunnable(AvailableOffer offer, Activity parentActivity, ProgressBarToggleable progressBarToggleable, DialogFragment dialogFragment, Message message) {
        super(offer, dialogFragment, message, progressBarToggleable);
        this.parentActivity = parentActivity;
    }

    public void run(User user) {
        progressBarToggleable.setToLoading();
        Log.d("Inbox", "post is null?" + (post==null));

        Presenter.getInstance().startOffer(post, user).whenDone(() -> {
            Toast.makeText(parentActivity, "Offer started", Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
            dialogFragment.dismiss();
            parentActivity.finish();
        }).ifFails(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
        });

    }
}
