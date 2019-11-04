package ub.edu.pis2017.pis_17.synergy.Presenter.Authentication;

import android.accounts.AuthenticatorException;
import android.util.Log;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Optional;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;

public class FirebaseAuthenticationInteractor implements AuthInteractor{

    private FirebaseAuth firebaseAuth;

    public FirebaseAuthenticationInteractor(){
        Log.d("Firebase", "Asking for firebase auth");
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("Firebase", "Firebase auth ok");
    }

    @Override
    public FutureBox<String> createAccountWithMailAndPassword(Email mail, String password) {
        final String TAG = "SignIn";
        FutureBox<String> fout = FutureBox.empty();

        firebaseAuth.createUserWithEmailAndPassword(mail.toString(),password).addOnCompleteListener(res -> {
            if(res.isSuccessful()){
                String uid = res.getResult().getUser().getUid().toString();
                Log.i(TAG, "Account with mail " + uid + " created");
                fout.give(uid);
            }else{
                fout.error("Error");
            }
        });

        return fout;
    }

    @Override
    public FutureBox<String> logIn(Email mail, String password) {
        final FutureBox<String> fout = FutureBox.empty();
        firebaseAuth.signInWithEmailAndPassword(mail.toString(),password).addOnCompleteListener(task -> {
            if(task.isSuccessful() && getCurrentUserUid().isPresent()) {
                String uid = getCurrentUserUid().get();
                fout.give(uid);
            } else{
                fout.error("LogIn error");
            }
        });
        return fout;
    }

    @Override
    public Optional<String> getCurrentUserUid() {
        FirebaseUser u = firebaseAuth.getCurrentUser();
        if (u == null) {
            return Optional.empty();
        } else {
            return Optional.of(u.getUid());
        }
    }

    @Override
    public void signOut() throws AuthenticatorException {
        if(firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        } else {
            throw new AuthenticatorException("Current user is null.");
        }
    }

    public FutureResult updateEmail(Email newMail){
        FutureResult res = FutureResult.empty();
        firebaseAuth.getCurrentUser().updateEmail(newMail.toString()).addOnCompleteListener(t -> {
            if(t.isSuccessful()){
                res.success();
            }else{
                res.error();
            }
        });
        return res;
    }

    public FutureResult updatePassword(String newPassword){
        FutureResult res = FutureResult.empty();
        firebaseAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(t -> {
            if(t.isSuccessful()){
                res.success();
            }else{
                res.error();
            }
        });
        return res;
    }

    @Override
    public FutureResult reauthenticateCurrentUser(String psw) {
        FutureResult res = FutureResult.empty();
        FirebaseUser u = firebaseAuth.getCurrentUser();
        AuthCredential cr = EmailAuthProvider.getCredential(u.getEmail(), psw);
        u.reauthenticate(cr).addOnCompleteListener(t ->{
            if(t.isSuccessful()){
                res.success();
            }else{
                res.error("Wrong password");
            }
        });

        return res;
    }
}
