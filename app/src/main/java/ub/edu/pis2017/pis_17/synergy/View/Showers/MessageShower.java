package ub.edu.pis2017.pis_17.synergy.View.Showers;

import android.app.Activity;
import android.app.FragmentManager;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.UserMessage;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.PostConfirmMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.RateMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.SystemMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.UserMessageDialogFragment;

/**
 * Created by kanales on 29/04/2018.
 */

public class MessageShower implements Shower<Message> {
    private Activity activity;

    public MessageShower(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void show(Message msg) {
        FragmentManager fm = activity.getFragmentManager();
        android.app.DialogFragment dialog;

        if (msg instanceof SystemMessage) {
            dialog = new SystemMessageDialogFragment();
            dialog.show(fm, "Dialog");
        }
        else if (msg instanceof PostConfirmMessage) {
            dialog = new PostConfirmMessageDialogFragment();
            dialog.show(fm, "Dialog");
        }
        else if (msg instanceof UserMessage) {
            dialog = new UserMessageDialogFragment();
            dialog.show(fm, "Dialog");
        }
        else if (msg instanceof RateMessage) {
            dialog = new RateMessageDialogFragment();
            dialog.show(fm, "Dialog");
        }
    }
}