package ub.edu.pis2017.pis_17.synergy.View.Lists.ParticipantAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.UserProfileActivity;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.Model.Participant;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.R;

public class ParticipantListViewAdapter extends ListViewAdapter<Participant> {
    
    private class ParticipantListViewHolder extends ItemListViewHolder {
        public TextView nameTxtvw;
        public TextView positionTxtvw;
        public ImageView profileImgvw;
    }
    
    public ParticipantListViewAdapter(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected View setHolder(View itemView, Participant participant, int uiPosition) {
        
        //Inflate the itemView
        LayoutInflater inf = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inf.inflate(R.layout.list_item_show_participant,null);

        //Create the viewHolder and fill its attributes (ALWAYS remember to give the holder its UI position)
        ParticipantListViewHolder holder = new ParticipantListViewHolder();
        holder.nameTxtvw = (TextView) itemView.findViewById(R.id.show_participant_list_item_username_txtvw);
        holder.positionTxtvw = (TextView) itemView.findViewById(R.id.show_participant_list_item_position_name_txtvw);
        holder.profileImgvw = (ImageView) itemView.findViewById(R.id.show_participant_list_item_profile_pic_imgvw);
        holder.viewNum = uiPosition;
        
        User user = participant.getUser();
        if(user == null) {
            holder.nameTxtvw.setText(R.string.vacant_label);
            holder.profileImgvw.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.logo));
        }
        else {
            holder.nameTxtvw.setText(user.getName());
            Presenter.getInstance().setUserImageToContext(user,mActivity, holder.profileImgvw);
        }
        
        holder.nameTxtvw.setTypeface(null, Typeface.BOLD);
        holder.positionTxtvw.setText(participant.getPosition());
        
        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mActivity.getResources().getDimension(R.dimen.list_item_participant_height)));

        itemView.setTag(holder);

        return itemView;
    }

}