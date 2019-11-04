package ub.edu.pis2017.pis_17.synergy.View.Lists.ParticipantAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;

public class EditParticipantListViewAdapter extends ListViewAdapter<String> {

    private class EditParticipantListViewHolder extends ItemListViewHolder {
        public TextInputEditText nameTxtinpedttxt;
    }

    public EditParticipantListViewAdapter(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected View setHolder(View itemView, String str, int uiPosition) {
        //Inflate the itemView
        LayoutInflater inf = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inf.inflate(R.layout.list_item_edit_participant,null);

        //Create the viewHolder and fill its attributes (ALWAYS remember to give the holder its UI position)
        EditParticipantListViewHolder holder = new EditParticipantListViewHolder();
        holder.nameTxtinpedttxt = (TextInputEditText) itemView.findViewById(R.id.edit_participant_list_item_teammate_position_txtinpedttxt);
        holder.viewNum = uiPosition;

        holder.nameTxtinpedttxt.setText(str);
        holder.nameTxtinpedttxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                listItems.set(uiPosition, s.toString());
                Log.d("NEWVACANT", listItems.get(uiPosition));
            }
        });

        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mActivity.getResources().getDimension(R.dimen.list_item_participant_height)));

        itemView.setTag(holder);

        return itemView;
    }

    @Override
    public void removeLast() {
        int size = listItems.size();
        if(size != 1) {
            listItems.remove(size - 1);
        }
    }

    @Override
    public void addAll(List<String> listItems) {
        listItems.forEach(el -> this.add(el));
    }

    @Override
    public List<String> getList(){
        List<String> correctedList = new LinkedList<>();
        int i = 1;
        for(String str : listItems) {
            correctedList.add(String.valueOf(i) + " : " + str);
            i++;
        }
        return correctedList;
    }
}
