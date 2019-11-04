package ub.edu.pis2017.pis_17.synergy.View.Activities.profile;

/**
 * Created by manuellechasanchez on 24/03/2018.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UnreadMessageNumber;
import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Hashtags.HashtagShower;

import static ub.edu.pis2017.pis_17.synergy.Presenter.Presenter.getInstance;

public class UserProfileActivity extends AppCompatActivity{
    private String localUserId;
    private ImageView userProfile_img_userImage;

    private Toolbar toolbar;
    private HashtagShower hashtagShower;
    public static TextView notification;

    public static boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        notification = (TextView) findViewById(R.id.user_profile_notification_badget);
        userProfile_img_userImage = (ImageView) findViewById(R.id.show_user_profile_img_user_image);

        findViewById(R.id.user_profile_content).setVisibility(View.GONE);

        setHashtagShower();

        initToolBar();
        loadProfile(Presenter.getInstance().getCurrentUser());
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadProfile(FutureBox<User> fu) {
        fu.whenFull(user -> {
            Log.d("Profile", "User loaded");
            TextView name = findViewById(R.id.show_user_profile_txt_userName);
            name.setText(user.getName());
            TextView desc = findViewById(R.id.show_user_profile_txt_description);
            desc.setText(user.getDescription());
            TextView score = findViewById(R.id.show_user_profile_txt_ratingNumber);
            score.setText(String.format("%.1f", user.getRating()));
            Presenter.getInstance().setUserImageToContext(user,UserProfileActivity.this,userProfile_img_userImage);
            setNotificationText();
            setCardViewButtonListeners(user);
        });
        FutureBox<List<Hashtag>> list = fu.flatMap(Presenter.getInstance()::getUserHashtagList);
        list.whenFull(hl -> {
            Log.d("Profile", "Hashtags loaded");
            hashtagShower.clearHashtags();
            hashtagShower.addHashtags(hl);
        });
        fu.also(list).resolve(() -> {
            Log.d("Profile", "View loaded");
            UserProfileActivity.this.findViewById(R.id.user_profile_content).setVisibility(View.VISIBLE);
        });
    }

    public static void setNotificationText(){
        int x = UnreadMessageNumber.getInstance().getUnreadNumber();
        if (x > 0) {
            notification.setVisibility(View.VISIBLE);
            notification.setText(Integer.toString(x));
        } else {
            notification.setVisibility(View.GONE);
        }
    }
    //

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Set the hashtag shower
    public void setHashtagShower() {
        ArrayList<LinearLayout> lnlayList = new ArrayList<>();
        lnlayList.add((LinearLayout) findViewById(R.id.show_user_profile_hashtag_sub_lnlay1));
        lnlayList.add((LinearLayout) findViewById(R.id.show_user_profile_hashtag_sub_lnlay2));
        lnlayList.add((LinearLayout) findViewById(R.id.show_user_profile_hashtag_sub_lnlay3));
        hashtagShower = new HashtagShower(this, lnlayList);
    }


    private void setCardViewButtonListeners(User u) {
        ImageButton userProfile_imgbtn_editProfile = (ImageButton) findViewById(R.id.show_user_profile_imgbtn_editProfile);
        ImageButton userProfile_imgbtn_inbox = (ImageButton) findViewById(R.id.show_user_profile_imgbtn_inbox);
        ImageButton userProfile_imgbtn_history = (ImageButton) findViewById(R.id.show_user_profile_imgbtn_history);
        ImageButton userProfile_imgbtn_settings = (ImageButton) findViewById(R.id.show_user_profile_imgbtn_settings);

        userProfile_imgbtn_inbox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        userProfile_imgbtn_inbox.setBackgroundColor(Color.parseColor("#F2F5A9"));
                        return true;
                    case MotionEvent.ACTION_UP:
                        userProfile_imgbtn_inbox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        Intent Inbox = new Intent(v.getContext(), InboxActivity.class);
                        startActivity(Inbox);
                        return true;
                }
                return false;
            }
        });

        userProfile_imgbtn_history.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        userProfile_imgbtn_history.setBackgroundColor(Color.parseColor("#F2F5A9"));
                        return true;
                    case MotionEvent.ACTION_UP:
                        userProfile_imgbtn_history.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        Intent History = new Intent(v.getContext(), HistoryActivity.class);
                        startActivity(History);
                        return true;
                }
                return false;
            }
        });

        userProfile_imgbtn_editProfile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        userProfile_imgbtn_editProfile.setBackgroundColor(Color.parseColor("#F2F5A9"));
                        return true;
                    case MotionEvent.ACTION_UP:
                        userProfile_imgbtn_editProfile.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        Intent UserEdit = new Intent (v.getContext(), UserEditProfileActivity.class);
                        startActivityForResult(UserEdit, 0);
                        return true;
                }
                return false;
            }
        });


        userProfile_imgbtn_settings.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        userProfile_imgbtn_settings.setBackgroundColor(Color.parseColor("#F2F5A9"));
                        return true;
                    case MotionEvent.ACTION_UP:
                        userProfile_imgbtn_settings.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        Intent Options = new Intent(v.getContext(), OptionsActivity.class);
                        startActivity(Options);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loadProfile(Presenter.getInstance().getCurrentUser());
    }

    @Override
    public void onResume(){
        super.onResume();
        loadProfile(Presenter.getInstance().getCurrentUser());
    }

    public void setUserImage(int newIMg){
        this.userProfile_img_userImage.setImageResource(newIMg);
    }
}
