package ub.edu.pis2017.pis_17.synergy.View.Showers;

import android.content.Context;
import android.content.Intent;

import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.AlienProfileActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.UserProfileActivity;

public class UserShower implements Shower<User> {

    private Context context;

    public UserShower(Context context) {
        this.context = context;
    }

    @Override
    public void show(User user) {
        Intent intent;
        if(user == null/*current user*/) {
            intent = new Intent(context, UserProfileActivity.class);
            context.startActivity(intent);
        }
        else {
            intent = new Intent(context, AlienProfileActivity.class);
            intent.putExtra("UserID", user.getUid());
            context.startActivity(intent);
        }
    }

}
