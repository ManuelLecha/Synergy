package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost;

import android.app.Activity;
import android.app.DialogFragment;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class StartAvailableRequestWithParticipantRunnable extends StartAvailablePostWithParticipantRunnable<AvailableRequest> {

    private Activity parentActivity;

    public StartAvailableRequestWithParticipantRunnable(AvailableRequest request, Activity parentActivity, ProgressBarToggleable progressBarToggleable, DialogFragment dialogFragment, Message message) {
        super(request, dialogFragment, message, progressBarToggleable);
        this.parentActivity = parentActivity;
    }

    public void run(User user) {
        progressBarToggleable.setToLoading();
        Presenter.getInstance().startRequest(post, user).whenDone(() -> {
            Toast.makeText(parentActivity, "Request started", Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
            dialogFragment.dismiss();
            parentActivity.finish();
        }).ifFails(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
        });

    }

}
