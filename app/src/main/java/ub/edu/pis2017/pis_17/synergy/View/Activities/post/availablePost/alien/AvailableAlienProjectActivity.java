package ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.LocationHelper;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Participant;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.AlienProfileActivity;
import ub.edu.pis2017.pis_17.synergy.View.Hashtags.HashtagShower;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ParticipantAdapters.ParticipantListViewAdapter;

/**
 * Created by gerar on 27/03/2018.
 */

public class AvailableAlienProjectActivity extends AppCompatActivity implements ProgressBarToggleable {

    private AvailableProject project;
    private List<Hashtag> hashtagList;
    private ParticipantListViewAdapter participantsListAdapter;

    private ProgressBar progressBar;
    private HashtagShower hashtagShower;
    private ListView participantsListView;
    private Toolbar toolbar;
    private ImageView profileImgvw;
    private TextView titleTxtvw;
    private TextView descriptionTxtvw;
    private TextView locationTxtvw;
    private TextView adminNameTxtvw;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alien_administrating_many_participant_post);

        findViewById(R.id.content_show_many_participant_post).setVisibility(View.INVISIBLE);

        //instantiate view components
        progressBar = (ProgressBar) findViewById(R.id.alien_administrating_many_participant_pgrsbar);
        initHashtagShower();
        participantsListView = (ListView) findViewById(R.id.show_many_participant_participants_lstvw);
        initToolBar();
        profileImgvw = (ImageView) findViewById(R.id.show_many_participant_creator_imgvw);
        titleTxtvw = (TextView) findViewById(R.id.show_many_participant_post_title_txtvw);
        descriptionTxtvw = (TextView) findViewById(R.id.show_many_participant_desc_txtvw);
        locationTxtvw = (TextView) findViewById(R.id.show_many_participant_layout_location_txtvw);
        adminNameTxtvw = (TextView) findViewById(R.id.show_many_participant_admin_name_txtvw);

        //show progress bar and negate user interaction
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        //presenter calls
        String postId = getIntent().getExtras().getString("PostID");

        FutureBox<Post> postBox = Presenter.getInstance().getPost(postId);
        postBox.whenFull(databaseProject -> {
            project = (AvailableProject) databaseProject;
        });

        FutureBox<List<Hashtag>> hashtagListBox = Presenter.getInstance().getPostHashtagList(postId);
        hashtagListBox.whenFull(databaseHashtagList -> {
            hashtagList = databaseHashtagList;
        });

        FutureBox<User> futureUser = Presenter.getInstance().getCurrentUser();
        futureUser.whenFull(user-> currentUser = user);

        futureUser.also(postBox).also(hashtagListBox).resolve(() -> {
            setViewComponentData();
        });
    }

    private void setViewComponentData() {

        //hashtagShower content
        hashtagShower.clearHashtags();
        hashtagShower.addHashtags(hashtagList);

        //participantsListView content
        participantsListAdapter = new ParticipantListViewAdapter(AvailableAlienProjectActivity.this);
        List<Participant> participants = new LinkedList();

        Collection<Object> positions = project.getPositions();
        Map<Object,User> positionUser = project.getPositionUsers();

        positions.forEach(p -> participants.add(new Participant(positionUser.getOrDefault(p,null),p.toString())));

        participantsListAdapter.addAll(participants);
        participantsListView.setAdapter(participantsListAdapter);
        Log.d("NUM PARTICIPANTS", String.valueOf(participants.size()));

        participantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = participantsListAdapter.getItem(position).getUser();
                if(user == null) {
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    VacantConfirmMessage m = new VacantConfirmMessage(project.getTitle(), project.getTitle(), project.getAdmin(),currentUser, project,0, participantsListAdapter.getItem(position).getPosition());
                    FutureResult result = Presenter.getInstance().sendVacantConfirmMessage(m);
                    result.whenDone(() -> {
                        Toast.makeText(AvailableAlienProjectActivity.this, "You have applied for vacancy: " + participantsListAdapter.getItem(position).getPosition(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }).ifFails(error -> {
                        Toast.makeText(AvailableAlienProjectActivity.this,  error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    });
                }
                else {
                    Intent i = new Intent(new Intent(AvailableAlienProjectActivity.this, AlienProfileActivity.class));
                    i.putExtra("uid", user.getUid());
                    startActivity(i);
                }
            }
        });

        //profileImgvw content
        Presenter.getInstance().setUserImageToContext(project.getAdmin(), AvailableAlienProjectActivity.this, profileImgvw);
        profileImgvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AvailableAlienProjectActivity.this, AlienProfileActivity.class);
                i.putExtra("uid", project.getAdmin().getUid());
                startActivity(i);
            }
        });

        //titleTxtvw content
        titleTxtvw.setText(project.getTitle());

        //descriptionTxtvw content
        descriptionTxtvw.setText(project.getDescription());

        //locationTxtvw content
        locationTxtvw.setText(LocationHelper.locationDistanceFormat(project.getLocation(), MainActivity.getLastKnownLocation()));

        //adminNameTxtvw content
        adminNameTxtvw.setText(project.getAdmin().getName());

        //Set user image in imgView
        Presenter.getInstance().setUserImageToContext(project.getAdmin(), AvailableAlienProjectActivity.this, profileImgvw);

        //hide progress bar
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        findViewById(R.id.content_show_many_participant_post).setVisibility(View.VISIBLE);
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.alien_administrating_many_participant_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initHashtagShower() {
        ArrayList<LinearLayout> lnlayList = new ArrayList<>();
        lnlayList.add((LinearLayout) findViewById(R.id.show_many_participant_hashtag_sub_lnlay1));
        lnlayList.add((LinearLayout) findViewById(R.id.show_many_participant_hashtag_sub_lnlay2));
        lnlayList.add((LinearLayout) findViewById(R.id.show_many_participant_hashtag_sub_lnlay3));
        hashtagShower = new HashtagShower(this, lnlayList);
        hashtagShower.setCurrentPostType(MainActivity.PROJECTS);
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