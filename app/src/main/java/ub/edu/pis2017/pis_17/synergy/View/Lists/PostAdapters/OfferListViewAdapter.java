package ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import ub.edu.pis2017.pis_17.synergy.LocationHelper;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Offer;
import ub.edu.pis2017.pis_17.synergy.R;

public class OfferListViewAdapter extends ListViewAdapter<Offer> {

    private class OfferListViewHolder extends ItemListViewHolder {
        public TextView titleTxtvw;
        public TextView locationTxtvw;
        public ImageView profileImgvw;
    }

    public OfferListViewAdapter(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected View setHolder(View itemView, Offer offer, int uiPosition) {
        String distS;
        //Inflate the itemView
        LayoutInflater inf = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inf.inflate(R.layout.list_item_one_participant_post, null);

        //Create the viewHolder and fill its attributes (ALWAYS remember to give the holder its UI position)
        OfferListViewHolder holder = new OfferListViewHolder();
        holder.titleTxtvw = (TextView) itemView.findViewById(R.id.one_participant_post_list_item_title_txtvw);
        holder.locationTxtvw = (TextView) itemView.findViewById(R.id.one_participant_post_list_item_location_txtvw);
        holder.profileImgvw = (ImageView) itemView.findViewById(R.id.one_participant_post_list_item_profile_pic_imgvw);
        holder.viewNum = uiPosition;

        holder.titleTxtvw.setText(offer.getTitle());
        holder.titleTxtvw.setTypeface(null, Typeface.BOLD);

        Location loc = MainActivity.getLastKnownLocation();

        holder.locationTxtvw.setText(LocationHelper.locationDistanceFormat(offer.getLocation(),loc));

        Presenter.getInstance().setUserImageToContext(offer.getAdmin(),mActivity,holder.profileImgvw);

        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mActivity.getResources().getDimension(R.dimen.list_item_offer_height)));

        itemView.setTag(holder);

        return itemView;
    }


}
