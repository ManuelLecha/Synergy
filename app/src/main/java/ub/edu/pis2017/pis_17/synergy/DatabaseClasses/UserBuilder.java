package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UserDatabase.UserModel;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 07/05/2018.
 */

public class UserBuilder implements Builder<UserModel,User> {
    private UserModel u;

    /*
    public void getUserModel(UserDatabase userDatabase, Consumer<User> userConsumer){
        userConsumer.accept(new User(userDatabase.getUid(), userDatabase.getName(), new Email(userDatabase.getMail()),userDatabase.getimagePath()));
    }
*/

    public UserBuilder setModel(UserModel u) {
        this.u = u;
        return this;
    }


    public User build() {
        String uid = u.getUid();
        String name = u.getName();
        Email email = new Email(u.getMail());
        String imgurl = u.getImagePath();
        float rating = u.getRating();
        String description = u.getDescription();
        return new User(uid, name, email, imgurl,rating,description);
    }
}
