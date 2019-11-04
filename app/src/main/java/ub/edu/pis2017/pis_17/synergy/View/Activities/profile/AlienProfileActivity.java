package ub.edu.pis2017.pis_17.synergy.View.Activities.profile;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.MessageReplyDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Hashtags.HashtagShower;

/**
 * Created by manuellechasanchez on 24/03/2018.
 */


public class AlienProfileActivity extends AppCompatActivity{

    private User user;

    private Toolbar toolbar;
    private FloatingActionButton SendMessageButton;
    private HashtagShower hashtagShower;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alien_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setHashtagShower();

        String uid = getIntent().getExtras().getString("uid");
        loadProfile(Presenter.getInstance().getUser(uid));

        /*SendMessageButton = (FloatingActionButton) findViewById(R.id.alien_profile_send_message_fab);
        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                MessageReplyDialogFragment dialogFragment = new MessageReplyDialogFragment();
                dialogFragment.setToUser(user);
                dialogFragment.show(fm, "Sample Fragment");
            }
        });*/
        // esconder hasta que se cargue
        findViewById(R.id.content_show_alien_profile).setVisibility(View.GONE);
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.alien_profile_tlb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadProfile(FutureBox<User> fu) {
        fu.whenFull(user -> {
            ImageView userImage = findViewById(R.id.show_alien_profile_img_userImage);
            Log.d("Profile", "User loaded");
            TextView name = findViewById(R.id.show_alien_profile_txt_userName);
            name.setText(user.getName());
            TextView desc = findViewById(R.id.show_alien_profile_txt_description);
            desc.setText(user.getDescription());
            TextView score = findViewById(R.id.show_alien_profile_txt_ratingNumber);
            score.setText(String.format("%.1f", user.getRating()));
            Presenter.getInstance().setUserImageToContext(user,AlienProfileActivity.this,userImage);
        });

        FutureBox<List<Hashtag>> list = fu.flatMap(Presenter.getInstance()::getUserHashtagList);
        list.whenFull(hl -> {
            Log.d("Profile", "Hashtags loaded");
            hashtagShower.clearHashtags();
            hashtagShower.addHashtags(hl);
        });

        fu.also(list).resolve(() -> {
            Log.d("Profile", "View loaded");
            AlienProfileActivity.this.findViewById(R.id.content_show_alien_profile).setVisibility(View.VISIBLE);
        });
    }

    private void setHashtagShower(){
        ArrayList<LinearLayout> lnlayList = new ArrayList<>();
        lnlayList.add((LinearLayout) findViewById(R.id.show_alien_profile_hashtag_sub_lnlay1));
        lnlayList.add((LinearLayout) findViewById(R.id.show_alien_profile_hashtag_sub_lnlay2));
        lnlayList.add((LinearLayout) findViewById(R.id.show_alien_profile_hashtag_sub_lnlay3));
        hashtagShower = new HashtagShower(this, lnlayList);
    }


}
