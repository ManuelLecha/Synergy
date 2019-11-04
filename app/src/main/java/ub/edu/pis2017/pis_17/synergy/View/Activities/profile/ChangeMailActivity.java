package ub.edu.pis2017.pis_17.synergy.View.Activities.profile;

import android.accounts.AuthenticatorException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.sign_in.LoginActivity;

public class ChangeMailActivity extends AppCompatActivity implements ProgressBarToggleable{

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mail);
        Toolbar bar = findViewById(R.id.change_mail_tlb);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_Opciones);

        progressBar = (ProgressBar) findViewById(R.id.change_mail_pgrsbar);

        Button butConfirm = findViewById(R.id.change_mail_confirm_btn);
        butConfirm.setOnClickListener(view -> {
            setToLoading();
            String newMail = ((EditText) findViewById(R.id.change_mail_new_mail_edtxt)).getText().toString();
            String newMailConfirm = ((EditText) findViewById(R.id.change_mail_confirm_new_mail_edtxt)).getText().toString();
            String actualPassword = ((EditText) findViewById(R.id.change_mail_actual_password_edtxt)).getText().toString();

            if(validateForm(newMail,newMailConfirm,actualPassword)){
                Presenter.getInstance().reauthenticateCurrentUser(actualPassword).whenDone(() -> {
                    Presenter.getInstance().getCurrentUser().whenFull(u -> {
                        Presenter.getInstance().updateCurrentUserEmail(u.getUid(),new Email(newMail)).whenDone(() -> {
                            Toast.makeText(ChangeMailActivity.this, "Mail updated to: " + newMail, Toast.LENGTH_SHORT).show();
                            setToFree();
                            finish();
                        }).ifFails(e -> {
                            Toast.makeText(ChangeMailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            setToFree();
                        });
                    });
                }).ifFails(e -> {
                    Toast.makeText(ChangeMailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    setToFree();
                });
            }

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

    private boolean validateForm(String newMail, String newMailConfirm, String actualPassword) {
        boolean valid = true;
        final String REQUIRED_MAIL = "Required mail";
        final String SAME_MAIL = "Email's must coincide";
        final String REQUIRED_PSW = "Required password";

        if (TextUtils.isEmpty(newMail)) {
            setNewMailError(REQUIRED_MAIL);
            valid = false;
        } else if(!newMail.equals(newMailConfirm)){
            setNewMailError(SAME_MAIL);
            valid = false;
        } else {
            setNewMailError(null);
        }

        if (TextUtils.isEmpty(actualPassword)) {
            setActualPasswordError(REQUIRED_PSW);
            valid = false;
        } else {
            setActualPasswordError(null);
        }

        return valid;
    }
    public void setNewMailError(String error) {
        ((EditText) findViewById(R.id.change_mail_confirm_new_mail_edtxt)).setError(error);
    }

    public void setActualPasswordError(String error) {
        ((EditText) findViewById(R.id.change_mail_actual_password_edtxt)).setError(error);
    }

    @Override
    public void setToLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setToFree() {
        progressBar.setVisibility(View.GONE);
    }
}
