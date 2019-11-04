package ub.edu.pis2017.pis_17.synergy.View.Activities.profile;

import android.accounts.AuthenticatorException;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.sign_in.LoginActivity;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar bar = findViewById(R.id.options_tlb);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_Opciones);

        Button butLogout = findViewById(R.id.options_logout_btn);
        butLogout.setOnClickListener(view -> {
            try {
                Presenter.getInstance().signOut();
                Intent i = new Intent(OptionsActivity.this, LoginActivity.class);
                startActivity(i);
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            }
        });

        Button butChangeMail = findViewById(R.id.options_change_mail_btn);
        butChangeMail.setOnClickListener(view -> {
            Intent i = new Intent(OptionsActivity.this, ChangeMailActivity.class);
            startActivity(i);
        });

        Button butChangePassword = findViewById(R.id.options_change_password_btn);
        butChangePassword.setOnClickListener(view -> {
            Intent i = new Intent(OptionsActivity.this, ChangePasswordActivity.class);
            startActivity(i);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
