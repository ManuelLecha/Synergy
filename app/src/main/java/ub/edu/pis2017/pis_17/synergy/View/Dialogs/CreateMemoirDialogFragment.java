package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.FinishOwnOngoingPost.FinishOngoingPostWithMemoirRunnable;

public class CreateMemoirDialogFragment extends DialogFragment {

    private FinishOngoingPostWithMemoirRunnable runnable;

    private EditText memoirEdtxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_modify_memoir, container, false);
        getDialog().setTitle("Simple Dialog");

        memoirEdtxt = rootView.findViewById(R.id.modify_memoir_dialog_memoir_content_edtxt);

        TextView titleTxtvw = rootView.findViewById(R.id.modify_memoir_dialog_title_txtvw);
        titleTxtvw.setText("Create your memoir");

        Button updateBtn = rootView.findViewById(R.id.modify_memoir_dialog_update_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.run(memoirEdtxt.getText().toString());
            }
        });

        return rootView;
    }

    public void setFinishRunnable(FinishOngoingPostWithMemoirRunnable runnable) {
        this.runnable = runnable;
    }

}
