package ub.edu.pis2017.pis_17.synergy.View.Lists.MessageAdapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ParticipantAdapters.EditParticipantListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Showers.MessageShower;

public class MessageListViewAdapter extends ListViewAdapter<Message> {

    private class MessageListViewHolder extends ItemListViewHolder {
        public ImageView messageImgvw;
        public TextView titleTxtvw;
    }

    public MessageListViewAdapter(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected View setHolder(View itemView, Message message, int uiPosition) {
        //Inflate the itemView
        LayoutInflater inf = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inf.inflate(R.layout.list_item_message,null);

        //Create the viewHolder and fill its attributes (ALWAYS remember to give the holder its UI position)
        MessageListViewHolder holder = new MessageListViewHolder();
        holder.messageImgvw = (ImageView) itemView.findViewById(R.id.message_list_item_imgvw);
        holder.titleTxtvw = (TextView) itemView.findViewById(R.id.message_list_item_txtvw);
        holder.viewNum = uiPosition;

        Presenter.getInstance().setUserImageToContext(message.getContext().getAdmin(),mActivity,holder.messageImgvw);

        holder.titleTxtvw.setText(message.getTitle());

        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mActivity.getResources().getDimension(R.dimen.list_item_participant_height)));

        itemView.setTag(holder);

        switch (message.getActionDone()) {
            case Message.NEW_MESSAGE:
                itemView.findViewById(R.id.messageListItem_readIndicator).setActivated(true);
                break;
            case Message.CANCELLED_MESSAGE:
            case Message.DONE_MESSAGE:
                holder.titleTxtvw.setTextColor(mActivity.getResources().getColor(R.color.foregroundVeryLightColor));
            default:
                itemView.findViewById(R.id.messageListItem_readIndicator).setActivated(false);
        }

        return itemView;
    }
}
