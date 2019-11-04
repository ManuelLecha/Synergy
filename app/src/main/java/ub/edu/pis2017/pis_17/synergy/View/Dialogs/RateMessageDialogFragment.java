package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.DialogFragment;
import android.media.Rating;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.RateDoneUser.RateUserFromDonePostRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.SeeOwnAvailablePost.SeePostActivityOnConfirmedRunnable;

public class RateMessageDialogFragment extends DialogFragment{

    private RateMessage rateMessage;
    private Post postContext;
    private ProgressBarToggleable progressBarToggleable;
    private Class activityClass;
    private User toUser;

    private SeePostActivityOnConfirmedRunnable seePostRunnable;
    private RateUserFromDonePostRunnable rateUserRunnable;

    private RatingBar rtngBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_rate_message, container, false);
        getDialog().setTitle("Rate Message");

        seePostRunnable = new SeePostActivityOnConfirmedRunnable(postContext, getActivity(), activityClass);

        rateUserRunnable = new RateUserFromDonePostRunnable(toUser, progressBarToggleable, getActivity(), this, rateMessage);

        rtngBar = rootView.findViewById(R.id.rate_dialog_rtngbar);

        Button okButton = rootView.findViewById(R.id.rate_dialog_okay_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageView postButton = rootView.findViewById(R.id.rate_dialog_post_btn);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seePostRunnable.run();
            }
        });

        Button rateButton = rootView.findViewById(R.id.rate_dialog_rate_btn);

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = Math.round(rtngBar.getRating());
                rateUserRunnable.run(score);
                Presenter.getInstance().markMessageAsDone(rateMessage);
                dismiss();
            }
        });



        TextView titleTxtvw = rootView.findViewById(R.id.rate_dialog_title_txtvw);
        titleTxtvw.setText(rateMessage.getTitle());

        TextView contentTxtvw = rootView.findViewById(R.id.rate_dialog_message_content_txtvw);
        contentTxtvw.setText(rateMessage.getText());

        return rootView;

    }

    public void setRateMessage(RateMessage rateMessage) {
        this.rateMessage = rateMessage;
    }

    public void setPostContext(Post postContext) {
        this.postContext = postContext;
    }

    public void setProgressBarToggleable(ProgressBarToggleable progressBarToggleable) {
        this.progressBarToggleable = progressBarToggleable;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

}
