package ub.edu.pis2017.pis_17.synergy.View.Activities.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;

/**
 * Created by manuellechasanchez on 24/03/2018.
 */

public class UserEditProfileActivity extends AppCompatActivity implements ProgressBarToggleable{
    private static final int PICK_IMAGE = 1;
    private User currentUser;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_edit_user_profile);
        findViewById(R.id.edit_user_profile_content).setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.edit_user_profile_pgrsbar);
        setToLoading();
        loadData(Presenter.getInstance().getCurrentUser());
        initToolBar();
    }

    public void loadData(FutureBox<User> fu) {
        fu.whenFull(user -> {
            currentUser = user;
            ImageView imgv = findViewById(R.id.edit_user_profile_photo_imgvw);
            Presenter.getInstance().setUserImageToContext(user,this,imgv);
            TextView username = findViewById(R.id.edit_user_profile_username_txtietxt);
            username.setText(user.getName());
            TextView description = findViewById(R.id.edit_user_profile_desc_edtxt);
            description.setText(user.getDescription());
        });
        FutureBox<List<Hashtag>> list = fu.flatMap(Presenter.getInstance()::getUserHashtagList);
        list.whenFull(hl -> {
            TextView hashtags = findViewById(R.id.edit_user_profile_hashtag_edtxt);
            CharSequence txt = hl.stream()
                                 .map(Hashtag::toString)
                                 .collect(Collectors.joining(" "));
            hashtags.setText(txt);
        });
        fu.whenFull(this::setFabListener);
        fu.whenFull(x -> this.setEditImageButton());
        fu.also(list).resolve(() -> {
            Log.d("Profile","Making edit profile visible");

            findViewById(R.id.edit_user_profile_content).setVisibility(View.VISIBLE);
            setToFree();
        });
    }

    private void setEditImageButton() {
        FloatingActionButton fab = findViewById(R.id.editImageButton);
        fab.setOnClickListener(view -> openGallery());
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }


    public void setFabListener(User u) {
        final Presenter p = Presenter.getInstance();
        final EditText descEtxt = findViewById(R.id.edit_user_profile_desc_edtxt);
        final EditText nameEtxt = findViewById(R.id.edit_user_profile_username_txtietxt);
        final EditText hashtagsEtxt = findViewById(R.id.edit_user_profile_hashtag_edtxt);
        final FloatingActionButton fab = findViewById(R.id.editUserProfile_fab);

        String ogDesc = descEtxt.getText().toString(),
                ogName = nameEtxt.getText().toString(),
                ogHashtags = hashtagsEtxt.getText().toString();


        fab.setOnClickListener(v -> {
            setToLoading();
            FutureResult res = FutureResult.ofSuccess();
            final String newDesc = descEtxt.getText().toString(),
                         newName = nameEtxt.getText().toString(),
                         newHashtags = hashtagsEtxt.getText().toString();
            if (!ogDesc.equals(newDesc)) res = res.then(() -> p.updateUserDescription(u.getUid(),newDesc));
            if (!ogName.equals(newName)) res = res.then(() -> p.updateUserName(u.getUid(),newName));
            if (!ogHashtags.equals(newHashtags)) res = res.then(() -> p.updateUserHashtagList(u.getUid(),Hashtag.parseString(newHashtags)));
            res.whenDone(() -> {
                setToFree();
                Toast.makeText(this,"User saved",Toast.LENGTH_LONG).show();
                finish();
            });
            res.ifFails((err) -> {
                setToFree();
                Toast.makeText(this,"User not saved. Error: " + err,Toast.LENGTH_LONG).show();
            });
        });
    }

    public void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            final Uri imageUri = data.getData();
            ImageView imvw = findViewById(R.id.edit_user_profile_photo_imgvw);
            imvw.setImageURI(imageUri);
            Presenter.getInstance().updateUserImage(currentUser, imageUri);
        }
    }

}
