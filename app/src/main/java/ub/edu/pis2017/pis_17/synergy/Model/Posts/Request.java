package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.content.Context;
import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by manuellechasanchez on 10/04/2018.
 */

public class Request extends Post {

    public Request(String id, User admin, Location location, String title, String description) {
        super(id, admin, location, title, description);
    }

    @Override
    public String getLabel() {
        return "Request";
    }
}
