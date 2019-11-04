package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.PropertyResourceBundle;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.UserMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.SeeOwnAvailablePost.SeePostActivityOnConfirmedRunnable;

public class UserMessageDialogFragment extends DialogFragment {

    private UserMessage userMessage;
    private ProgressBarToggleable progressBarToggleable;
    private Class activityClass;

    private SeePostActivityOnConfirmedRunnable seePostRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_user_message, container, false);
        getDialog().setTitle("Simple Dialog");

        seePostRunnable = new SeePostActivityOnConfirmedRunnable(userMessage.getContext(), getActivity(), activityClass);

        Button okButton = rootView.findViewById(R.id.user_dialog_ok_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageView postButton = rootView.findViewById(R.id.user_dialog_post_btn);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seePostRunnable.run();
            }
        });

        Button replyButton = rootView.findViewById(R.id.user_dialog_reply_btn);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageReplyDialogFragment dialog = new MessageReplyDialogFragment();
                dialog.setToUser(userMessage.getFrom());
                dialog.setPostContext(userMessage.getContext());
                dialog.setProgressBarToggleable(progressBarToggleable);
                FragmentManager fm = getFragmentManager();
                dialog.show(fm, "Dialog");
            }
        });

        TextView contentTxtvw = rootView.findViewById(R.id.user_dialog_message_content_txtvw);
        contentTxtvw.setText(userMessage.getText());

        TextView usernameTxtvw = rootView.findViewById(R.id.user_dialog_username_txtvw);
        usernameTxtvw.setText(userMessage.getFrom().getName());

        TextView titleTxtvw = rootView.findViewById(R.id.user_dialog_title_txtvw);
        titleTxtvw.setText(userMessage.getTitle());

        ImageView toUserImgvw = rootView.findViewById(R.id.user_dialog_user_imgvw);
        Presenter.getInstance().setUserImageToContext(userMessage.getFrom(), getActivity(), toUserImgvw);

        return rootView;
    }

    public void setUserMessage(UserMessage userMessage) {
        this.userMessage = userMessage;
    }

    public void setProgressBarToggleable(ProgressBarToggleable progressBarToggleable) {
        this.progressBarToggleable = progressBarToggleable;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }
}
