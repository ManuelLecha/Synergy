package ub.edu.pis2017.pis_17.synergy.View.Activities.post.modifyPost;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ParticipantAdapters.EditParticipantListViewAdapter;

public class ModifyProjectActivity extends AppCompatActivity implements ProgressBarToggleable {

    private AvailableProject project;
    private List<Hashtag> hashtagList;
    private EditParticipantListViewAdapter editParticipantListAdapter;

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private FloatingActionButton acceptFab;
    private TextInputEditText titleTxtietxt;
    private EditText descriptionEdtxt;
    private EditText hashtagsEdtxt;
    //private ListView editParticipantListView;
    //private Button addTeammateBtn;
    //private Button deleteTeammateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_project);

        //instantiate view components
        progressBar = (ProgressBar) findViewById(R.id.modify_project_pgrsbar);
        initToolBar();
        acceptFab = (FloatingActionButton) findViewById(R.id.modify_project_fab);
        titleTxtietxt = (TextInputEditText) findViewById(R.id.edit_many_participant_post_title_txtietxt);
        descriptionEdtxt = (EditText) findViewById(R.id.edit_many_participant_desc_edtxt);
        hashtagsEdtxt = (EditText) findViewById(R.id.edit_many_participant_hashtag_edtxt);
        //editParticipantListView = (ListView) findViewById(R.id.edit_many_participant_participants_lstvw);
        //addTeammateBtn = (Button) findViewById(R.id.edit_many_participant_add_teammate_btn);
        //deleteTeammateBtn = (Button) findViewById(R.id.edit_many_participant_delete_teammate_btn);

        //show progress bar and negate user interaction
        setToLoading();

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

        postBox.also(hashtagListBox).resolve(() -> {
            setViewComponentData();
        });
    }

    private void setViewComponentData() {

        //acceptFab content
        acceptFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToLoading();

                project.setTitle(titleTxtietxt.getText().toString());
                project.setDescription(descriptionEdtxt.getText().toString());

                List<Hashtag> newHashtagsList = Hashtag.parseString(hashtagsEdtxt.getText().toString());

                FutureResult projectResult = Presenter.getInstance().updateAdministratingProject(project);
                FutureResult hashtagsResult = Presenter.getInstance().updatePostHashtagList(project, newHashtagsList);

                projectResult.then(() -> hashtagsResult.whenDone(() -> {
                    Toast.makeText(ModifyProjectActivity.this, "Project updated", Toast.LENGTH_SHORT).show();
                    setToFree();
                    finish();
                })).ifFails(e -> {
                    Toast.makeText(ModifyProjectActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    setToFree();
                });
            }
        });

        //titleTxtietxt content
        titleTxtietxt.setText(project.getTitle());

        //descriptionEdtxt content
        descriptionEdtxt.setText(project.getDescription());

        //hashtagsEdtxt content
        String hashtagText = "";
        for(Hashtag hashtag : hashtagList) {
            hashtagText = hashtagText + hashtag.toString() + " ";
        }
        hashtagsEdtxt.setText(hashtagText);

        /*
        //editParticipantListView content;
        editParticipantListAdapter = new EditParticipantListViewAdapter(ModifyProjectActivity.this);
        ArrayList<Object> positions = (ArrayList<Object>) project.getPositionUsers();
        ArrayList<String> strPositions = new ArrayList<>();
        for(int i = 0; i < positions.size(); i++) {
            strPositions.add(positions.get(i).toString());
        }
        editParticipantListAdapter.addAll(strPositions);
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
        */

        //hide progress bar
        setToFree();
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.modify_project_toolbar);
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
