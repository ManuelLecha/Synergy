package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.FinishOwnOngoingPost;

import android.app.DialogFragment;
import android.provider.Telephony;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class FinishProjectWithMemoirRunnable extends FinishOngoingPostWithMemoirRunnable<OngoingProject> {

    private OngoingOwnProjectActivity parentActivity;

    public FinishProjectWithMemoirRunnable(OngoingProject project, OngoingOwnProjectActivity parentActivity, DialogFragment dialogFragment) {
        super(project, dialogFragment);
        this.parentActivity = parentActivity;
    }

    public void run(String memoir) {
        parentActivity.setToLoading();

        FutureResult updateResult = Presenter.getInstance().finishOngoingProject(post, memoir);

        updateResult.whenDone(() -> {

            ViewMessageBuilder mb = new ViewMessageBuilder();
            RateMessage rmes;
            mb.setTitle("Rate : " + post.getTitle());
            mb.setPostContext(post);
            for(Object position : ((OngoingProject)post).getPositions()) {
                User u = ((OngoingProject)post).getPositionUsers().getOrDefault(position, null);
                if(u != null) {
                    mb.setToUser(u);
                    mb.setFromUser(post.getAdmin());
                    mb.setContent("Please rate the Admin of the Project you have participated in");
                    rmes = mb.buildRateMessage();
                    Presenter.getInstance().sendRateMessage(rmes);
                    mb.setToUser(((OngoingProject)post).getAdmin());
                    mb.setFromUser(u);
                    mb.setContent("Please rate " + u.getName() + "'s participation on your project");
                    rmes = mb.buildRateMessage();
                    Presenter.getInstance().sendRateMessage(rmes);
                }
            }
            Toast.makeText(parentActivity, "Request finished", Toast.LENGTH_LONG).show();
            parentActivity.setToFree();
            dialogFragment.dismiss();
            parentActivity.finish();
        }).ifFails(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            parentActivity.setToFree();
        });
    }

}
