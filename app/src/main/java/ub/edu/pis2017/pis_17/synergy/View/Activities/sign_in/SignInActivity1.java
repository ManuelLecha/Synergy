package ub.edu.pis2017.pis_17.synergy.View.Activities.sign_in;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;

/**
 * Created by gerar on 26/03/2018.
 */

public class SignInActivity1 extends Activity {

    private Button confirmBtn;
    private TextView firstNameTxt;
    private TextView lastNameTxt;
    private TextView mailTxt;
    private TextView passwordTxt;
    private TextView repeatMailTxt;
    private TextView repeatPasswordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.i("SignIn", "Creating SignIn activity 1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout_1);

        //BUTTON
        confirmBtn = (Button) findViewById(R.id.signin_layout_1_confirm_btn);
        ////

        //TEXTVIEWS
        firstNameTxt = (TextView) findViewById(R.id.signin_layout1_firstname_txtietxt);
        lastNameTxt = (TextView) findViewById(R.id.signin_layout1_lastname_txtietxt);
        mailTxt = (TextView) findViewById(R.id.signin_layout1_email_txtietxt);
        repeatMailTxt = (TextView) findViewById(R.id.signin_layout1_repeat_email_txtietxt);
        passwordTxt = (TextView) findViewById(R.id.signin_layout1_password_txtietxt);
        repeatPasswordTxt = (TextView) findViewById(R.id.signin_layout1_repeat_password_txtietxt);
        ////


        //BUTTON LISTENERS
        confirmBtn.setOnClickListener(view -> {
            if(validateForm()){
                String name = firstNameTxt.getText().toString() + " " + lastNameTxt.getText().toString();

                Email e = new Email(mailTxt.getText().toString());
                String pw = passwordTxt.getText().toString();
                FutureBox<String> futureUidCreatingAccount = Presenter.getInstance().createAccount(name,e,pw);
                futureUidCreatingAccount.whenFull((uid) -> {
                    String user_id = uid;
                    Intent i = new Intent(SignInActivity1.this, SignInActivity2.class);
                    i.putExtra("UID", user_id);
                    startActivity(i);
                }).onCancellation(c -> Toast.makeText(SignInActivity1.this, "Mail already matched with an account", Toast.LENGTH_SHORT).show());
            }
        });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mailTxt.getText().toString();
        String repeatEmail = repeatMailTxt.getText().toString();

        String password = passwordTxt.getText().toString();
        String repeatPassword = repeatPasswordTxt.getText().toString();


        if (TextUtils.isEmpty(email)) {
            mailTxt.setError("Required e-mail");
            valid = false;
        } else if(!isValidEmail(email)){
            mailTxt.setError("Not valid e-mail");
            valid = false;
        }else if(!email.equals(repeatEmail)){
            repeatMailTxt.setError("E-mail must be equal");
            valid = false;
        } else{
            mailTxt.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordTxt.setError("Required password.");
            valid = false;
        } else if(!password.equals(repeatPassword)){
            repeatPasswordTxt.setError("Password must be equal.");
            valid = false;
        } else if (password.length() < 6){
            valid = false;
            passwordTxt.setError("Password must have at least 6 characters.");
        } else {
            passwordTxt.setError(null);
        }

        return valid;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
