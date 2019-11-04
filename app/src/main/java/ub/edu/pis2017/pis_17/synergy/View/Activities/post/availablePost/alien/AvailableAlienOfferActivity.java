package ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.LocationHelper;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Hashtags.HashtagShower;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.AlienProfileActivity;

/**
 * Created by ruben on 25/03/2018.
 */

public class AvailableAlienOfferActivity extends AppCompatActivity implements ProgressBarToggleable {

    private AvailableOffer offer;
    private List<Hashtag> hashtagList;

    private ProgressBar progressBar;
    private HashtagShower hashtagShower;
    private Toolbar toolbar;
    private FloatingActionButton acceptFab;
    private ImageView profileImgvw;
    private TextView titleTxtvw;
    private TextView descriptionTxtvw;
    private TextView locationTxtvw;
    private TextView adminNameTxtvw;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("Test message", "Retrieving bundle");
        Log.d("Test message", "Bundle null? " + String.valueOf(savedInstanceState == null));
        setContentView(R.layout.activity_alien_administrating_one_participant_post);

        findViewById(R.id.content_show_one_participant_without_post).setVisibility(View.INVISIBLE);

        //instantiate view components
        progressBar = findViewById(R.id.alien_administrating_one_participant_pgrsbar);
        initHashtagShower();
        initToolBar();
        acceptFab = findViewById(R.id.alien_administrating_one_participant_accept_fabitem);
        profileImgvw = findViewById(R.id.show_one_participant_without_creator_imgvw);
        titleTxtvw = findViewById(R.id.show_one_participant_without_title_txtvw);
        descriptionTxtvw = findViewById(R.id.show_one_participant_without_desc_txtvw);
        locationTxtvw = findViewById(R.id.show_one_participant_without_location_txtvw);
        adminNameTxtvw = findViewById(R.id.show_one_participant_without_admin_name_txtvw);

        //show progress bar and negate user interaction
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //presenter calls
        String postId = getIntent().getExtras().getString("PostID");
        Log.d("POSTID", postId);

        FutureBox<Post> postBox = Presenter.getInstance().getPost(postId);
        postBox.whenFull(databaseOffer -> {
            offer = (AvailableOffer) databaseOffer;
        });

        FutureBox<User> futureUser = Presenter.getInstance().getCurrentUser();

        futureUser.whenFull(u -> {
            currentUser = u;
        });


        FutureBox<List<Hashtag>> hashtagListBox = Presenter.getInstance().getPostHashtagList(postId);
        hashtagListBox.whenFull(databaseHashtagList -> {
            hashtagList = databaseHashtagList;
        });

        futureUser.also(postBox).also(hashtagListBox).resolve(() -> {
            setViewComponentData();
        });

    }

    private void setViewComponentData() {

        //hashtagShower content
        hashtagShower.clearHashtags();
        hashtagShower.addHashtags(hashtagList);

        //acceptFab content
        acceptFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                PostConfirmMessage m = new PostConfirmMessage(offer.getTitle(), offer.getTitle() , offer.getAdmin(), currentUser, offer, 0);
                FutureResult result = Presenter.getInstance().sendPostConfirmMessage(m);
                result.whenDone(() -> {
                    Toast.makeText(AvailableAlienOfferActivity.this, "Accept message has been sent", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }).ifFails(error -> {
                    Toast.makeText(AvailableAlienOfferActivity.this,  error.toString(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                });
            }
        });

        //profileImgvw content
        Presenter.getInstance().setUserImageToContext(offer.getAdmin(), AvailableAlienOfferActivity.this, profileImgvw);
        profileImgvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AvailableAlienOfferActivity.this, AlienProfileActivity.class);
                i.putExtra("uid", offer.getAdmin().getUid());
                startActivity(i);
            }
        });

        //titleTxtvw content
        titleTxtvw.setText(offer.getTitle());

        //descriptionTxtvw content
        descriptionTxtvw.setText(offer.getDescription());

        //locationTxtvw content
        locationTxtvw.setText(LocationHelper.locationDistanceFormat(offer.getLocation(), MainActivity.getLastKnownLocation()));
        //adminNameTxtvw content
        adminNameTxtvw.setText(offer.getAdmin().getName());

        //Set user image in imgView
        Presenter.getInstance().setUserImageToContext(offer.getAdmin(), AvailableAlienOfferActivity.this, profileImgvw);

        //hide progress bar and activate user interaction
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        findViewById(R.id.content_show_one_participant_without_post).setVisibility(View.VISIBLE);
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.alien_administrating_one_participant_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initHashtagShower() {
        ArrayList<LinearLayout> lnlayList = new ArrayList<>();
        lnlayList.add((LinearLayout) findViewById(R.id.show_one_participant_without_hashtag_sub_lnlay1));
        lnlayList.add((LinearLayout) findViewById(R.id.show_one_participant_without_hashtag_sub_lnlay2));
        lnlayList.add((LinearLayout) findViewById(R.id.show_one_participant_without_hashtag_sub_lnlay3));
        hashtagShower = new HashtagShower(this, lnlayList);
        hashtagShower.setCurrentPostType(MainActivity.OFFERS);
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