package ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.R;

public class RequestListViewAdapter extends ListViewAdapter<Request> {

    private class RequestListViewHolder extends ItemListViewHolder {
        public TextView titleTxtvw;
        public TextView locationTxtvw;
        public ImageView profileImgvw;
    }

    public RequestListViewAdapter(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected View setHolder(View itemView, Request request, int uiPosition) {

        //Inflate the itemView
        LayoutInflater inf = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inf.inflate(R.layout.list_item_one_participant_post, null);

        //Create the viewHolder and fill its attributes (ALWAYS remember to give the holder its UI position)
        RequestListViewHolder holder = new RequestListViewHolder();
        holder.titleTxtvw = (TextView) itemView.findViewById(R.id.one_participant_post_list_item_title_txtvw);
        holder.locationTxtvw = (TextView) itemView.findViewById(R.id.one_participant_post_list_item_location_txtvw);
        holder.profileImgvw = (ImageView) itemView.findViewById(R.id.one_participant_post_list_item_profile_pic_imgvw);
        holder.viewNum = uiPosition;

        holder.titleTxtvw.setText(request.getTitle());
        holder.titleTxtvw.setTypeface(null, Typeface.BOLD);

        Location loc = MainActivity.getLastKnownLocation();

        Float dist = request.getLocation().distanceTo(loc) / 1000; // distance in metres
        String distS;
        if (dist < 1000) {
            distS = String.format("%.0f m",request.getLocation().distanceTo(loc));
        } else  {
            distS = String.format("%.2f km",dist/1000);
        }
        holder.locationTxtvw.setText(distS);

        //TODO (hay que implementar llamada a la base de datos, pero de momento una imagen hardcodeada)
        //holder.profileImgvw.setImageDrawable(project.getProfilePic());
        Presenter.getInstance().setUserImageToContext(request.getAdmin(),mActivity,holder.profileImgvw);

        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mActivity.getResources().getDimension(R.dimen.list_item_offer_height)));

        itemView.setTag(holder);

        return itemView;
    }


}
