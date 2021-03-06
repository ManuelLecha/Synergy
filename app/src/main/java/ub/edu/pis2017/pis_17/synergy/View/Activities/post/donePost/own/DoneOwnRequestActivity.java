package ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.own;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.LocationHelper;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien.AvailableAlienRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.AlienProfileActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.UserProfileActivity;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.SeeMemoirDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Hashtags.HashtagShower;

/**
 * Created by ruben on 26/03/2018.
 */

public class DoneOwnRequestActivity extends AppCompatActivity implements ProgressBarToggleable {

    private DoneRequest request;
    private List<Hashtag> hashtagList;

    private ProgressBar progressBar;
    private HashtagShower hashtagShower;
    private Toolbar toolbar;
    private FloatingActionButton memoirFab;
    private TextView titleTxtvw;
    private TextView descriptionTxtvw;
    private ImageView adminImgvw;
    private TextView locationTxtvw;
    private TextView adminNameTxtvw;
    private ImageView participantImgvw;
    private TextView participantNameTxtvw;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_one_participant_post);

        findViewById(R.id.content_show_one_participant_with_post).setVisibility(View.INVISIBLE);

        //instantiate view components
        progressBar = (ProgressBar) findViewById(R.id.done_one_participant_pgrsbar);
        initHashtagShower();
        initToolBar();
        memoirFab = (FloatingActionButton) findViewById(R.id.done_one_participant_memoir_fabitem);
        titleTxtvw = (TextView) findViewById(R.id.show_one_participant_with_title_txtvw);
        descriptionTxtvw = (TextView) findViewById(R.id.show_one_participant_with_desc_txtvw);
        adminImgvw = (ImageView) findViewById(R.id.show_one_participant_with_admin_imgvw);
        locationTxtvw = (TextView) findViewById(R.id.show_one_participant_with_location_txtvw);
        adminNameTxtvw = (TextView) findViewById(R.id.show_one_participant_with_admin_name_txtvw);
        participantImgvw = (ImageView) findViewById(R.id.show_one_participant_with_participant_imgvw);
        participantNameTxtvw = (TextView) findViewById(R.id.show_one_participant_with_participant_name_txtvw);

        //show progress bar and negate user interaction
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //presenter calls
        String postId = getIntent().getExtras().getString("PostID");

        FutureBox<Post> postBox = Presenter.getInstance().getPost(postId);
        postBox.whenFull(databaseOffer -> {
            request = (DoneRequest) databaseOffer;
        });

        FutureBox<List<Hashtag>> hashtagListBox = Presenter.getInstance().getPostHashtagList(postId);
        hashtagListBox.whenFull(databaseHashtagList -> {
            hashtagList = databaseHashtagList;
        });

        postBox.also(hashtagListBox).resolve(() -> {
            setViewComponentData();
        });
    }

    private void setViewComponentData() {

        //hashtagShower content
        hashtagShower.clearHashtags();
        hashtagShower.addHashtags(hashtagList);

        //cancelFab content
        memoirFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                SeeMemoirDialogFragment dialog = new SeeMemoirDialogFragment();
                dialog.setMemoir(request.getMemoir());
                dialog.setTitle(request.getTitle());
                dialog.show(fm, "Memoir dialog");
            }
        });

        //titleTxtvw content
        titleTxtvw.setText(request.getTitle());

        //descriptionTxtvw content
        descriptionTxtvw.setText(request.getDescription());

        //adminImgvw content
        Presenter.getInstance().setUserImageToContext(request.getAdmin(), DoneOwnRequestActivity.this, adminImgvw);
        adminImgvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoneOwnRequestActivity.this, UserProfileActivity.class);
                i.putExtra("uid", request.getAdmin().getUid());
                startActivity(i);
            }
        });

        //locationTxtvw content
        locationTxtvw.setText(LocationHelper.locationDistanceFormat(request.getLocation(),MainActivity.getLastKnownLocation()));

        //adminNameTxtvw content
        adminNameTxtvw.setText(request.getAdmin().getName());

        //participantImgvw content
        Presenter.getInstance().setUserImageToContext(request.getParticipant(), DoneOwnRequestActivity.this, participantImgvw);

        //participantNameTxtvw content
        participantNameTxtvw.setText(request.getParticipant().getName());

        //hide progress bar
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        findViewById(R.id.content_show_one_participant_with_post).setVisibility(View.VISIBLE);
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.done_one_participant_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initHashtagShower() {
        ArrayList<LinearLayout> lnlayList = new ArrayList<>();
        lnlayList.add((LinearLayout) findViewById(R.id.show_one_participant_with_hashtag_sub_lnlay1));
        lnlayList.add((LinearLayout) findViewById(R.id.show_one_participant_with_hashtag_sub_lnlay2));
        lnlayList.add((LinearLayout) findViewById(R.id.show_one_participant_with_hashtag_sub_lnlay3));
        hashtagShower = new HashtagShower(this, lnlayList);
        hashtagShower.setCurrentPostType(MainActivity.REQUESTS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setToLoading() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void setToFree() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
