package ub.edu.pis2017.pis_17.synergy.View.Activities.sign_in;

import android.accounts.AuthenticatorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;

/**
 * Created by gerar on 26/03/2018.
 */

public class LoginActivity extends Activity implements ProgressBarToggleable {


    private TextView signInTxtvw;
    private Button logInBtn;
    private TextView mailTxt;
    private TextView passwordTxt;
    private TextView loginProblemTxt;
    private ProgressBar progressBar;


    @Override
    protected void onStart() {
        super.onStart();
        try {
            Presenter.getInstance().getCurrentUser().whenFull(user -> {
                Presenter.getInstance().unreadMessageCount(user.getUid());
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.login_pgrsbar);

        //TEXTVIEWS
        mailTxt = (TextView) findViewById(R.id.login_layout_username_txtietxt);
        passwordTxt = (TextView) findViewById(R.id.login_layout_password_txtietxt);
        signInTxtvw = (TextView) findViewById(R.id.login_layout_signin_txtvw);
        loginProblemTxt = (TextView) findViewById(R.id.login_layout_problem_txtvw);
        /////

        ///BUTTONS
        logInBtn = (Button) findViewById(R.id.login_layout_login_btn);
        //

        //TEXTVIEW LISTENERS
        signInTxtvw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignInActivity1.class));
            }
        });
        ////

        ////BUTTON LISTENERS

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    setToLoading();
                    Email email = new Email(mailTxt.getText().toString());
                    Presenter.getInstance().logIn(email, passwordTxt.getText().toString()).whenFull((user) -> {
                        Presenter.getInstance().unreadMessageCount(user.getUid());
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("localUser",user.getUid());
                        startActivity(i);
                        setToFree();
                    }).onCancellation(e -> {
                        loginProblemTxt.setText(e.toString());
                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        setToFree();
                    });
                }
            }
        });

        ////
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mailTxt.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mailTxt.setError("Required e-mail");
            valid = false;
        } else {
            mailTxt.setError(null);
        }

        String password = passwordTxt.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTxt.setError("Required password");
            valid = false;
        } else if(password.length() < 6){
            Toast.makeText(LoginActivity.this, "Authentication failed",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        } else{
            passwordTxt.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {/*Do nothing*/}

    @Override
    public void setToLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setToFree() {
        progressBar.setVisibility(View.GONE);
    }

}
