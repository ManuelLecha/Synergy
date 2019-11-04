package ub.edu.pis2017.pis_17.synergy.View.Activities.post.newPost;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewPostBuilder;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ParticipantAdapters.EditParticipantListViewAdapter;

/**
 * Created by kanales on 24/03/2018.
 */

public class NewProjectActivity extends AppCompatActivity implements ProgressBarToggleable {

    private EditParticipantListViewAdapter editParticipantListAdapter;

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private FloatingActionButton acceptFab;
    private TextInputEditText titleTxtietxt;
    private EditText descriptionEdtxt;
    private EditText hashtagsEdtxt;
    private ListView editParticipantListView;
    private Button addTeammateBtn;
    private Button deleteTeammateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        //instantiate view components
        
        progressBar = (ProgressBar) findViewById(R.id.new_project_pgrsbar);

        initToolBar();
        acceptFab = (FloatingActionButton) findViewById(R.id.new_project_fab);
        titleTxtietxt = (TextInputEditText) findViewById(R.id.new_many_participant_post_title_txtietxt);
        descriptionEdtxt = (EditText) findViewById(R.id.new_many_participant_desc_edtxt);
        hashtagsEdtxt = (EditText) findViewById(R.id.new_many_participant_hashtag_edtxt);
        editParticipantListView = (ListView) findViewById(R.id.new_many_participant_participants_lstvw);
        addTeammateBtn = (Button) findViewById(R.id.new_many_participant_add_teammate_btn);
        deleteTeammateBtn = (Button) findViewById(R.id.new_many_participant_delete_teammate_btn);

        EditText namePosition = findViewById(R.id.edit_participant_list_item_teammate_position_txtinpedttxt);
        setToLoading();

        setViewComponentData();
    }

    private void setViewComponentData() {
        //editParticipantListView content;
        editParticipantListAdapter = new EditParticipantListViewAdapter(this);

        List<String> list = new LinkedList<>();

        for(int i = 0; i < 3; i++) {
            list.add("Vacant");
        }

        editParticipantListAdapter.addAll(list);

        editParticipantListView.setAdapter(editParticipantListAdapter);

        //addTeammateBtn content;
        addTeammateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editParticipantListAdapter.add("Vacant");
                editParticipantListAdapter.notifyDataSetChanged();
            }
        });

        //deleteTeammateBtn content;
        deleteTeammateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editParticipantListAdapter.removeLast();
                editParticipantListAdapter.notifyDataSetChanged();
            }
        });

        //acceptFab content
        acceptFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToLoading();

                ViewPostBuilder projectBuilder = new ViewPostBuilder();
                projectBuilder.setDescription(descriptionEdtxt.getText().toString());
                projectBuilder.setTitle(titleTxtietxt.getText().toString());
                Collection<Object> positions = new LinkedList<>();
                editParticipantListAdapter.getList().forEach(el -> positions.add(el));
                projectBuilder.setPositions(positions);
                FutureBox<Location> locationBox = FutureBox.of(MainActivity.getLastKnownLocation());
                locationBox.whenFull(loc -> {
                    projectBuilder.setLocation(loc);
                    Log.d("NewProject", "Created location");
                });

                FutureBox<User> userBox = Presenter.getInstance().getCurrentUser();
                userBox.whenFull(u -> {
                    projectBuilder.setAdmin(u);
                    Log.d("NewProject", "Created user");
                });

                locationBox.also(userBox).resolve(() -> {
                    Log.d("NewProject", "Building project");
                    //Must be available!!!!!!!!! Be careful crerating thee BUILDEEEEEERSSSSSSSS
                    AvailableProject newProject = projectBuilder.buildAvailableProject();
                    FutureBox<String> creationResult = Presenter.getInstance().createProject(newProject);

                    creationResult.whenFull(postId -> {
                        newProject.setId(postId);
                        List<Hashtag> hlist = Hashtag.parseString(hashtagsEdtxt.getText().toString());
                        Presenter.getInstance().updatePostHashtagList(newProject, hlist);
                        Toast.makeText(NewProjectActivity.this, "Project created", Toast.LENGTH_LONG).show();
                        setToFree();
                        finish();
                    }).onCancellation(error -> {
                        Toast.makeText(NewProjectActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        setToFree();
                    });

                });

            }
        });

        setToFree();
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.new_project_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
