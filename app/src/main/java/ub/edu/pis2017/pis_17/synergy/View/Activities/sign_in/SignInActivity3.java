package ub.edu.pis2017.pis_17.synergy.View.Activities.sign_in;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;

/**
 * Created by gerar on 26/03/2018.
 */

public class SignInActivity3 extends Activity {

    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    //Buttons
    private FloatingActionButton nextFab;
    private Button galleryBtn;
    //private Button cameraBtn;
    //

    private ProgressBar progressBar;
    private ImageView profilePhotoImg;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout_3);

        progressBar = (ProgressBar) findViewById(R.id.sign_in_3_pgrsbar);
        galleryBtn = (Button) findViewById(R.id.signin_layout_3_gallery_btn);
        nextFab = (FloatingActionButton) findViewById(R.id.signin_layout_3_next_fab);

        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        profilePhotoImg = (ImageView) findViewById(R.id.signin_layout_3_profile_photo_imgvw);

        String uid = getIntent().getStringExtra("UID");

        galleryBtn.setOnClickListener(view -> openGallery());

        //cameraBtn.setOnClickListener(view -> dispatchTakePictureIntent());

        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Intent i = new Intent(SignInActivity3.this, MainActivity.class);
                FutureBox<User> fu = Presenter.getInstance().getCurrentUser();
                fu.whenFull(u -> {
                    progressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    if(imageUri != null) {
                        Presenter.getInstance().updateUserImage(u,imageUri);
                    }
                    i.putExtra("UID", uid);
                    startActivity(i);
                });
            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            profilePhotoImg.setImageURI(imageUri);
        }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            profilePhotoImg.setImageURI(imageUri);
        }
    }



}
