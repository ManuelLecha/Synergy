package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.ConfirmVacant;

import android.app.Activity;
import android.app.DialogFragment;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class ConfirmVacantInAvailableProjectRunnable {

    private AvailableProject project;
    private Activity parentActivity;
    private VacantConfirmMessage message;
    private ProgressBarToggleable progressBarToggleable;
    private DialogFragment dialogFragment;

    public ConfirmVacantInAvailableProjectRunnable(AvailableProject project, Activity parentActivity, DialogFragment dialogFragment, VacantConfirmMessage message, ProgressBarToggleable progressBarToggleable) {
        this.project = project;
        this.parentActivity = parentActivity;
        this.message = message;
        this.progressBarToggleable = progressBarToggleable;
        this.dialogFragment = dialogFragment;
    }

    public void run(User user) {
        progressBarToggleable.setToLoading();
        Presenter.getInstance().fillVacancyWithUser(project,user,message.getVacant().toString()).whenDone(() -> {
            Toast.makeText(parentActivity, "Participant added", Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
            dialogFragment.dismiss();
        }).ifFails(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            progressBarToggleable.setToFree();
        });
    }

}
