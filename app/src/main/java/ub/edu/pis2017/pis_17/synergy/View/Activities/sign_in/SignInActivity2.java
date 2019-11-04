package ub.edu.pis2017.pis_17.synergy.View.Activities.sign_in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gerar on 26/03/2018.
 */

public class SignInActivity2 extends Activity {

    private FloatingActionButton nextFab;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout_2);

        String uid = getIntent().getStringExtra("UID");

        nextFab = (FloatingActionButton) findViewById(R.id.signin_layout_2_next_fab);

        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText descriptionTxt = (EditText) findViewById(R.id.sign_in_2_desc_edtxt);
                final EditText hashtagTxt = (EditText) findViewById(R.id.sign_in_2_hashtag_edtxt);
                String description = descriptionTxt.getText().toString();
                String hashtags = hashtagTxt.getText().toString();
                List<Hashtag> hashtagList = Hashtag.parseString(hashtags);
                Presenter.getInstance().updateUserDescription(uid,description);
                Presenter.getInstance().updateUserHashtagList(uid,hashtagList);
                Intent i = new Intent(SignInActivity2.this, SignInActivity3.class);
                i.putExtra("UID", uid);
                startActivity(i);
            }
        });
    }

}
