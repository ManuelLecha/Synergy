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

import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.sign_in.LoginActivity;

public class ChangePasswordActivity extends AppCompatActivity implements ProgressBarToggleable {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar bar = findViewById(R.id.change_password_tlb);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_Opciones);

        progressBar = (ProgressBar) findViewById(R.id.change_password_pgrsbar);

        Button butConfirm = findViewById(R.id.change_password_confirm_btn);
        butConfirm.setOnClickListener(view -> {
            setToLoading();
            String newPassword = ((EditText) findViewById(R.id.change_password_new_password_edtxt)).getText().toString();
            String newPasswordConfirm = ((EditText) findViewById(R.id.change_password_confirm_new_password_edtxt)).getText().toString();
            String actualPassword = ((EditText) findViewById(R.id.change_password_actual_password_edtxt)).getText().toString();

            if (validateForm(newPassword, newPasswordConfirm, actualPassword)) {
                Presenter.getInstance().reauthenticateCurrentUser(actualPassword).then(() ->
                        Presenter.getInstance().updateCurretUserPassword(newPassword))
                        .whenDone(() -> {
                            Toast.makeText(ChangePasswordActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                            setToFree();
                            finish();
                        }).ifFails(e -> {
                    Toast.makeText(ChangePasswordActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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

    private boolean validateForm(String newPassword, String newPasswordConfirm, String actualPassword) {
        boolean valid = true;
        final String REQUIRED_NEW_PSW = "Required new password";
        final String SAME_PSW = "Passwords must coincide";
        final String REQUIRED_PSW = "Required password";
        final String PASSWORD_LENGTH = "6 characters password minimum";
        final int minPasswordLenght = 6;

        if (TextUtils.isEmpty(newPassword)) {
            setNewPasswordError(REQUIRED_NEW_PSW);
            valid = false;
        } else if(newPassword.length() < minPasswordLenght){
            setNewPasswordError(PASSWORD_LENGTH);
            valid = false;
        } else if(!newPassword.equals(newPasswordConfirm)){
            setNewPasswordError(SAME_PSW);
            valid = false;
        } else {
            setNewPasswordError(null);
        }

        if (TextUtils.isEmpty(actualPassword)) {
            setActualPasswordError(REQUIRED_PSW);
            valid = false;
        } else {
            setActualPasswordError(null);
        }
        return valid;
    }

    public void setNewPasswordError(String error) {
        ((EditText) findViewById(R.id.change_password_new_password_edtxt)).setError(error);
    }

    public void setActualPasswordError(String error) {
        ((EditText) findViewById(R.id.change_password_actual_password_edtxt)).setError(error);
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
