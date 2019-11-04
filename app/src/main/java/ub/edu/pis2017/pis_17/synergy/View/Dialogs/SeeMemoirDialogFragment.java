package ub.edu.pis2017.pis_17.synergy.View.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.R;

public class SeeMemoirDialogFragment extends DialogFragment {

    private String title;
    private String memoir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_see_memoir, container, false);
        getDialog().setTitle("Simple Dialog");

        TextView titleTxtvw = rootView.findViewById(R.id.see_memoir_dialog_title_txtvw);
        titleTxtvw.setText(title);

        TextView memoirTxtvw = rootView.findViewById(R.id.see_memoir_dialog_memoir_content_txtvw);
        memoirTxtvw.setText(memoir);

        return rootView;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMemoir(String memoir) {
        this.memoir = memoir;
    }



}
