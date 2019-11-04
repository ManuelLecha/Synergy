package ub.edu.pis2017.pis_17.synergy.Model.Messages;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 24/04/2018.
 */

public class SystemMessage extends Message {

    public SystemMessage(String id, String title, String text, User to, Post context, int actionDone) {
        super(id, title, text, to, context, actionDone);
    }

    public SystemMessage(String title, String text, User to, Post context, int actionDone) {
        super(title, text, to, context, actionDone);
    }
}
