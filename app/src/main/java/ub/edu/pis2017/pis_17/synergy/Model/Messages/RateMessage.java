package ub.edu.pis2017.pis_17.synergy.Model.Messages;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.Interfaces.Rateable;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 24/04/2018.
 */

public class RateMessage extends UserMessage implements Rateable {

    public RateMessage(String id, String title, String text, User to, User from, Post context, int actionDone) {
        super(id, title, text, to, from, context, actionDone);
    }

    public RateMessage(String title, String text, User to, User from, Post context, int actionDone) {
        super(title, text, to, from, context, actionDone);
    }

    @Override
    public void rate(float score) {
        //TODO database call
    }
}
