package ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.FinishOwnOngoingPost;

import android.app.DialogFragment;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingOffer;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewMessageBuilder;

public class FinishOfferWithMemoirRunnable extends FinishOngoingPostWithMemoirRunnable<OngoingOffer> {

    private OngoingOwnOfferActivity parentActivity;

    public FinishOfferWithMemoirRunnable(OngoingOffer offer, OngoingOwnOfferActivity parentActivity, DialogFragment dialogFragment) {
        super(offer, dialogFragment);
        this.parentActivity = parentActivity;
    }

    public void run(String memoir) {
        parentActivity.setToLoading();

        FutureResult updateResult = Presenter.getInstance().finishOngoingOffer(post, memoir);

        updateResult.whenDone(() -> {

            ViewMessageBuilder mb = new ViewMessageBuilder();

            mb.setTitle("Rate : " + post.getTitle());
            mb.setContent("Please rate the Admin of the Offer you have participated in");
            mb.setToUser(post.getParticipant());
            mb.setFromUser(post.getAdmin());
            mb.setPostContext(post);

            RateMessage mesAdmin = mb.buildRateMessage();

            FutureResult messageSendAdminResult = Presenter.getInstance().sendRateMessage(mesAdmin);

            messageSendAdminResult.whenDone(() -> {

                mb.setContent("Please rate the Participant of your Offer");
                mb.setToUser(post.getAdmin());
                mb.setFromUser(post.getParticipant());
                mb.setPostContext(post);
                RateMessage mesParticipant = mb.buildRateMessage();

                FutureResult messageSendParticipantResult = Presenter.getInstance().sendRateMessage(mesParticipant);

                messageSendParticipantResult.whenDone(() -> {
                    Toast.makeText(parentActivity, "Offer finished", Toast.LENGTH_LONG).show();
                    parentActivity.setToFree();
                    dialogFragment.dismiss();
                    parentActivity.finish();
                }).ifFails(e -> {
                    Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
                    parentActivity.setToFree();
                });

            }).ifFails(e -> {
                Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
                parentActivity.setToFree();
            });
        }).ifFails(e -> {
            Toast.makeText(parentActivity, "Error : " + e, Toast.LENGTH_LONG).show();
            parentActivity.setToFree();
        });

    }

}
