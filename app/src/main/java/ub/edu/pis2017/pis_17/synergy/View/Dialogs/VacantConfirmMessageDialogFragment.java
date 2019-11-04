package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.ConfirmVacant.ConfirmVacantInAvailableProjectRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.DeclineVacant.DeclineVacantInAvailableProjectRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.SeeOwnAvailablePost.SeePostActivityOnConfirmedRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost.StartAvailablePostWithParticipantRunnable;

public class VacantConfirmMessageDialogFragment extends DialogFragment {

    private VacantConfirmMessage vacantConfirmMessage;
    private ProgressBarToggleable progressBarToggleable;
    private Class activityClass;
    private AvailableProject project;

    private SeePostActivityOnConfirmedRunnable seePostRunnable;
    private ConfirmVacantInAvailableProjectRunnable confirmVacantRunnable;
    private DeclineVacantInAvailableProjectRunnable declineVacantRunnable;

    private TextView contentTxtvw;
    private TextView titleTxtvw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_confirm_message, container, false);
        getDialog().setTitle("Simple Dialog");

        seePostRunnable = new SeePostActivityOnConfirmedRunnable(vacantConfirmMessage.getContext(), getActivity(), activityClass);
        confirmVacantRunnable = new ConfirmVacantInAvailableProjectRunnable(project, getActivity(), this, vacantConfirmMessage, progressBarToggleable);
        declineVacantRunnable = new DeclineVacantInAvailableProjectRunnable(project, getActivity(), this, vacantConfirmMessage, progressBarToggleable);

        Button confirmButton = rootView.findViewById(R.id.confirm_dialog_accept_btn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmVacantRunnable.run(vacantConfirmMessage.getFrom());
                dismiss();
            }
        });

        Button declineButton = rootView.findViewById(R.id.confirm_dialog_decline_btn);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineVacantRunnable.run();
            }
        });

        AppCompatImageView postButton = rootView.findViewById(R.id.confirm_dialog_post_btn);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seePostRunnable.run();
            }
        });

        contentTxtvw = rootView.findViewById(R.id.confirm_dialog_message_content_txtvw);
        contentTxtvw.setText(vacantConfirmMessage.getText());
        titleTxtvw = rootView.findViewById(R.id.confirm_dialog_title_txtvw);
        titleTxtvw.setText(vacantConfirmMessage.getTitle());

        ImageView iv = rootView.findViewById(R.id.dialogConfirm_senderimg);
        Presenter.getInstance().setUserImageToContext(vacantConfirmMessage.getFrom(),getActivity(),iv);

        TextView username = rootView.findViewById(R.id.dialogConfirm_sender);
        username.setText(vacantConfirmMessage.getFrom().getName());

        return rootView;
    }

    public void setVacantConfirmMessage(VacantConfirmMessage vacantConfirmMessage) {
        this.vacantConfirmMessage = vacantConfirmMessage;
    }

    public void setProgressBarToggleable(ProgressBarToggleable progressBarToggleable) {
        this.progressBarToggleable = progressBarToggleable;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public void setProject(AvailableProject project) {
        this.project = project;
    }

}
