package ub.edu.pis2017.pis_17.synergy.Presenter.Authentication;

import android.accounts.AuthenticatorException;

import java.util.Optional;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;

public interface AuthInteractor {

    /**
     * Creates a user account on the authentification
     * @param mail
     * @param password
     * @return
     */
    public FutureBox<String> createAccountWithMailAndPassword(Email mail, String password);

    /**
     * User signing out
     * @throws AuthenticatorException
     */
    public void signOut() throws AuthenticatorException;

    /**
     * User log in
     * @param mail
     * @param password
     * @return
     */
    public FutureBox<String> logIn(Email mail, String password);

    /**
     * @return User's curretn identificator
     */
    public Optional<String> getCurrentUserUid();

    /**
     * Updates user's email
     * @param newEmail
     * @return
     */
    public FutureResult updateEmail(Email newEmail);

    /**
     * Updates user's password
     * @param password
     * @return
     */
    public FutureResult updatePassword(String password);

    /**
     * Reauthentificating a user for changes in his account
     * @return
     */
    public FutureResult reauthenticateCurrentUser(String psw);

}
