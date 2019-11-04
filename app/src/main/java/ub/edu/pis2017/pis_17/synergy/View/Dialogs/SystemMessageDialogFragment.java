package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.SeeOwnAvailablePost.SeePostActivityOnConfirmedRunnable;

public class SystemMessageDialogFragment extends DialogFragment {

    private SystemMessage systemMessage;
    private Class activityClass;

    private SeePostActivityOnConfirmedRunnable seePostRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_system_message, container, false);
        getDialog().setTitle("Simple Dialog");

        seePostRunnable = new SeePostActivityOnConfirmedRunnable(systemMessage.getContext(), getActivity(), activityClass);

        Button okButton = rootView.findViewById(R.id.system_dialog_okay_btn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button postButton = rootView.findViewById(R.id.system_dialog_post_btn);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seePostRunnable.run();
            }
        });

        TextView contentTxtvw = rootView.findViewById(R.id.system_dialog_message_content_txtvw);
        contentTxtvw.setText(systemMessage.getText());

        TextView titleTxtvw = rootView.findViewById(R.id.system_dialog_title_txtvw);
        titleTxtvw.setText(systemMessage.getTitle());

        return rootView;
    }

    public void setSystemMessage(SystemMessage systemMessage) {
        this.systemMessage = systemMessage;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }

}
