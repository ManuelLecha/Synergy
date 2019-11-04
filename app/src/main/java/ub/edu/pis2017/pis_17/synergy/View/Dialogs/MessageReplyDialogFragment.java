package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.ReplyToUser.ReplyToUserWithMessageRunnable;

public class MessageReplyDialogFragment extends DialogFragment {

    private User toUser;
    private Post postContext;
    private ProgressBarToggleable progressBarToggleable;

    private ReplyToUserWithMessageRunnable replyToUserWithMessageRunnable;

    private EditText titleEdtx;
    private EditText contentEdtxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_message_send, container, false);
        getDialog().setTitle("Simple Dialog");

        replyToUserWithMessageRunnable = new ReplyToUserWithMessageRunnable(toUser, postContext, progressBarToggleable, getActivity(), this);

        titleEdtx = rootView.findViewById(R.id.message_send_dialog_title_edttxt);
        contentEdtxt = rootView.findViewById(R.id.message_send_dialog_content_edttxt);

        Button sendButton = rootView.findViewById(R.id.message_send_dialog_send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyToUserWithMessageRunnable.run(titleEdtx.getText().toString(), contentEdtxt.getText().toString());
                dismiss();
            }
        });

        Button cancelButton = rootView.findViewById(R.id.message_send_dialog_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageView toUserImgvw = rootView.findViewById(R.id.message_send_dialog_user_imgvw);
        Presenter.getInstance().setUserImageToContext(toUser, getActivity(), toUserImgvw);
        TextView usernameTxtvw = rootView.findViewById(R.id.message_send_dialog_username_txtvw);
        usernameTxtvw.setText("Message to " + toUser.getName());

        return rootView;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public void setPostContext(Post postContext) {
        this.postContext = postContext;
    }

    public void setProgressBarToggleable(ProgressBarToggleable progressBarToggleable) {
        this.progressBarToggleable = progressBarToggleable;
    }

}
