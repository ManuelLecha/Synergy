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
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Project;
import ub.edu.pis2017.pis_17.synergy.R;

public class ProjectListViewAdapter extends ListViewAdapter<Project> {

    private class ProjectListViewHolder extends ItemListViewHolder {
        public TextView titleTxtvw;
        public TextView locationTxtvw;
        public TextView vacantsTxtvw;
        public ImageView profileImgvw;
    }

    public ProjectListViewAdapter(Activity mActivity) {
        super(mActivity);
    }

    @Override
    protected View setHolder(View itemView, Project project, int uiPosition) {

        //Inflate the itemView
        LayoutInflater inf = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemView = inf.inflate(R.layout.list_item_many_participant_post, null);

        //Create the viewHolder and fill its attributes (ALWAYS remember to give the holder its UI position)
        ProjectListViewHolder holder = new ProjectListViewHolder();
        holder.titleTxtvw = (TextView) itemView.findViewById(R.id.many_participant_post_list_item_title_txtvw);
        holder.locationTxtvw = (TextView) itemView.findViewById(R.id.many_participant_post_list_item_location_txtvw);
        holder.vacantsTxtvw = (TextView) itemView.findViewById(R.id.many_participant_post_list_item_vacants_txtvw);
        holder.profileImgvw = (ImageView) itemView.findViewById(R.id.many_participant_post_list_item_profile_pic_imgvw);
        holder.viewNum = uiPosition;

        holder.titleTxtvw.setText(project.getTitle());
        holder.titleTxtvw.setTypeface(null, Typeface.BOLD);

        Location loc = MainActivity.getLastKnownLocation();

        Float dist = project.getLocation().distanceTo(loc) / 1000; // distance in metres
        String distS;
        if (dist < 1000) {
            distS = String.format("%.0f m",project.getLocation().distanceTo(loc));
        } else  {
            distS = String.format("%.2f km",dist/1000);
        }

        holder.locationTxtvw.setText(distS);

        ;
        String vacancies = String.format( "%s : %d/%d"
                                        , mActivity.getResources().getString(R.string.participants)
                                        , project.getActualParticipantCount()
                                        , project.getMaxParticipantCount()
                                        );
        holder.vacantsTxtvw.setText(vacancies);

        //TODO (hay que implementar llamada a la base de datos, pero de momento una imagen hardcodeada)
        //holder.profileImgvw.setImageDrawable(project.getProfilePic());
        Presenter.getInstance().setUserImageToContext(project.getAdmin(),mActivity,holder.profileImgvw);

        itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) mActivity.getResources().getDimension(R.dimen.list_item_project_height)));

        itemView.setTag(holder);

        return itemView;
    }


}
