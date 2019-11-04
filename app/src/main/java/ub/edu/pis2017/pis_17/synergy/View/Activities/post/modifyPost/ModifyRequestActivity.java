package ub.edu.pis2017.pis_17.synergy.View.Activities.post.modifyPost;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Offer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;

/**
 * Created by ruben on 27/03/2018.
 */

public class ModifyRequestActivity extends AppCompatActivity implements ProgressBarToggleable {

    private AvailableRequest request;
    private List<Hashtag> hashtagList;

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private FloatingActionButton acceptFab;
    private TextInputEditText titleTxtietxt;
    private EditText descriptionEdtxt;
    private EditText hashtagsEdtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_request);

        //instantiate view components
        progressBar = (ProgressBar) findViewById(R.id.modify_request_pgrsbar);
        initToolBar();
        acceptFab = (FloatingActionButton) findViewById(R.id.modify_request_fab);
        titleTxtietxt = (TextInputEditText) findViewById(R.id.edit_one_participant_post_title_txtietxt);
        descriptionEdtxt = (EditText) findViewById(R.id.edit_one_participant_desc_edtxt);
        hashtagsEdtxt = (EditText) findViewById(R.id.edit_one_participant_hashtag_edtxt);

        //show progress bar and negate user interaction
        setToLoading();

        //presenter calls

        String postId = getIntent().getExtras().getString("PostID");

        FutureBox<Post> postBox = Presenter.getInstance().getPost(postId);
        postBox.whenFull(databaseRequest -> {
            request = (AvailableRequest) databaseRequest;
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

                request.setTitle(titleTxtietxt.getText().toString());
                request.setDescription(descriptionEdtxt.getText().toString());

                List<Hashtag> newHashtagsList = Hashtag.parseString(hashtagsEdtxt.getText().toString());

                FutureResult requestResult = Presenter.getInstance().updateAdministratingRequest(request);
                FutureResult hashtagsResult = Presenter.getInstance().updatePostHashtagList(request, newHashtagsList);

                requestResult.then(() -> hashtagsResult.whenDone(() -> {
                    Toast.makeText(ModifyRequestActivity.this, "Request updated", Toast.LENGTH_SHORT).show();
                    setToFree();
                    finish();
                })).ifFails(e -> {
                    Toast.makeText(ModifyRequestActivity.this, "Error : " + e, Toast.LENGTH_SHORT).show();
                    setToFree();
                });

            }
        });

        //titleTxtietxt content
        titleTxtietxt.setText(request.getTitle());

        //descriptionEdtxt content
        descriptionEdtxt.setText(request.getDescription());

        //hashtagsEdtxt content
        String hashtagText = "";
        for(Hashtag hashtag : hashtagList) {
            hashtagText = hashtagText + hashtag.toString() + " ";
        }
        hashtagsEdtxt.setText(hashtagText);

        //hide progress bar
        setToFree();
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.modify_request_toolbar);
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