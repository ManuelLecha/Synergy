package ub.edu.pis2017.pis_17.synergy;

import com.google.firebase.FirebaseApp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Authentication.FirebaseAuthenticationInteractor;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;

public class PresenterTest {
    Presenter p;

    @Test
    public void authentificationTest() throws Exception {
        FutureBox<User> futureUser = p.getInstance().logIn(new Email("manu.l.sanchez@gmail.com"), "123456");
        futureUser.whenFull(u -> assertEquals(u.getUid(), "Pp8tY9pZoFVM1qiofYW3DVahW9e2"));
    }


}
