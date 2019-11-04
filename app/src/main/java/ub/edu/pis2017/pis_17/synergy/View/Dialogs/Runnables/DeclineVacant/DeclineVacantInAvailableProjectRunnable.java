package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.DeclineVacant;
        import android.app.Activity;
        import android.app.DialogFragment;
        import android.widget.Toast;

        import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
        import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
        import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
        import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
        import ub.edu.pis2017.pis_17.synergy.Model.Posts.Project;
        import ub.edu.pis2017.pis_17.synergy.Model.User;
        import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
        import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
        import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class DeclineVacantInAvailableProjectRunnable {

    private Project project;
    private Activity parentActivity;
    private VacantConfirmMessage message;
    private ProgressBarToggleable progressBarToggleable;
    private DialogFragment dialogFragment;

    public DeclineVacantInAvailableProjectRunnable(Project project, Activity parentActivity, DialogFragment dialogFragment, VacantConfirmMessage message, ProgressBarToggleable progressBarToggleable) {
        this.project = project;
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
            mb.setContent("Admin " + project.getAdmin() + " has declined your participation in " + project.getTitle());
            mb.setToUser(message.getFrom());
            mb.setPostContext(project);
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