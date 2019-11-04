package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.DeclineParticipant.DeclineParticipantAvailablePostRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.SeeOwnAvailablePost.SeePostActivityOnConfirmedRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost.StartAvailablePostWithParticipantRunnable;

public class PostConfirmMessageDialogFragment extends DialogFragment {

    private PostConfirmMessage postConfirmMessage;
    private ProgressBarToggleable progressBarToggleable;
    private Class activityClass;

    private SeePostActivityOnConfirmedRunnable seePostRunnable;
    private StartAvailablePostWithParticipantRunnable startPostRunnable;
    private DeclineParticipantAvailablePostRunnable declineParticipantRunnable;

    private TextView contentTxtvw;
    private TextView titleTxtvw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_confirm_message, container, false);
        getDialog().setTitle("Simple Dialog");

        seePostRunnable = new SeePostActivityOnConfirmedRunnable(postConfirmMessage.getContext(), getActivity(), activityClass);
        declineParticipantRunnable = new DeclineParticipantAvailablePostRunnable(postConfirmMessage.getContext(), getActivity(), this, postConfirmMessage, progressBarToggleable);

        Button confirmButton = rootView.findViewById(R.id.confirm_dialog_accept_btn);
        confirmButton.setOnClickListener(v -> {
            Log.d("Inbox", "participant is null?" + (postConfirmMessage.getFrom()==null));
            startPostRunnable.run(postConfirmMessage.getFrom());
            dismiss();
        });

        Button declineButton = rootView.findViewById(R.id.confirm_dialog_decline_btn);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineParticipantRunnable.run();
            }
        });

        ImageView postButton = rootView.findViewById(R.id.confirm_dialog_post_btn);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seePostRunnable.run();
            }
        });

        contentTxtvw = rootView.findViewById(R.id.confirm_dialog_message_content_txtvw);
        contentTxtvw.setText(postConfirmMessage.getText());
        titleTxtvw = rootView.findViewById(R.id.confirm_dialog_title_txtvw);
        titleTxtvw.setText(postConfirmMessage.getTitle());

        ImageView iv = rootView.findViewById(R.id.dialogConfirm_senderimg);
        Presenter.getInstance().setUserImageToContext(postConfirmMessage.getFrom(),getActivity(),iv);

        TextView username = rootView.findViewById(R.id.dialogConfirm_sender);
        username.setText(postConfirmMessage.getFrom().getName());

        return rootView;
    }

    public void setPostConfirmMessage(PostConfirmMessage postConfirmMessage) {
        this.postConfirmMessage = postConfirmMessage;
    }

    public void setProgressBarToggleable(ProgressBarToggleable progressBarToggleable) {
        this.progressBarToggleable = progressBarToggleable;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public void setStartAvailablePostWithParticipantRunnable(StartAvailablePostWithParticipantRunnable startPostRunnable) {
        this.startPostRunnable = startPostRunnable;
    }
}
