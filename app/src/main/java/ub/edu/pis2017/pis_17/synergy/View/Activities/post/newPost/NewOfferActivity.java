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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Builders.ViewPostBuilder;

/**
 * Created by kanales on 24/03/2018.
 */

public class NewOfferActivity extends AppCompatActivity implements ProgressBarToggleable {

    private ProgressBar progressBar;
    private Toolbar toolbar;
    private FloatingActionButton acceptFab;
    private TextInputEditText titleTxtietxt;
    private EditText descriptionEdtxt;
    private EditText hashtagsEdtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);

        //instantiate view components
        progressBar = (ProgressBar) findViewById(R.id.new_offer_pgrsbar);
        initToolBar();
        acceptFab = (FloatingActionButton) findViewById(R.id.new_offer_fab);
        titleTxtietxt = (TextInputEditText) findViewById(R.id.edit_one_participant_post_title_txtietxt);
        descriptionEdtxt = (EditText) findViewById(R.id.edit_one_participant_desc_edtxt);
        hashtagsEdtxt = (EditText) findViewById(R.id.edit_one_participant_hashtag_edtxt);

        //hide progress bar and negate user interaction
        setToLoading();

        setViewComponentData();
    }

    private void setViewComponentData() {

        //acceptFab content
        acceptFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToLoading();

                ViewPostBuilder offerBuilder = new ViewPostBuilder();
                offerBuilder.setDescription(descriptionEdtxt.getText().toString());
                offerBuilder.setTitle(titleTxtietxt.getText().toString());


                FutureBox<Location> locationBox = FutureBox.of(MainActivity.getLastKnownLocation());
                locationBox.whenFull(loc -> {
                    offerBuilder.setLocation(loc);
                    Log.d("NewOffer", "Location creada");
                });

                FutureBox<User> userBox = Presenter.getInstance().getCurrentUser();
                userBox.whenFull(u -> {
                    offerBuilder.setAdmin(u);
                    Log.d("NewOffer", "User Creado");
                });

                locationBox.also(userBox).resolve(() -> {
                    Log.d("NewOffer", "Building offer");
                    //Must be available!!!!!!!!! Be careful crerating thee BUILDEEEEEERSSSSSSSS
                    AvailableOffer newOffer = offerBuilder.buildAvailableOffer();
                    FutureBox<String> creationResult = Presenter.getInstance().createOffer(newOffer);

                    creationResult.whenFull(postId -> {
                        newOffer.setId(postId);
                        List<Hashtag> hlist = Hashtag.parseString(hashtagsEdtxt.getText().toString());
                        Presenter.getInstance().updatePostHashtagList(newOffer, hlist);
                        Toast.makeText(NewOfferActivity.this, "Offer created", Toast.LENGTH_LONG).show();
                        setToFree();
                        finish();
                    }).onCancellation(error -> {
                        Toast.makeText(NewOfferActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        setToFree();
                    });

                });

            }
        });

        setToFree();
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.new_offer_toolbar);
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
