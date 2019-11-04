package ub.edu.pis2017.pis_17.synergy.Model.Messages;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.Interfaces.Answerable;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 24/04/2018.
 */

public class UserMessage extends Message implements Answerable {

    protected User from;

    public UserMessage(String id, String title, String text, User to, User from, Post context, int actionDone) {
        super(id, title, text, to, context, actionDone);
        this.from = from;
    }

    public UserMessage(String title, String text, User to, User from, Post context, int actionDone) {
        super(title, text, to, context, actionDone);
        this.from = from;
    }

    @Override
    public User getAdressee() {
        return from;
    }

    @Override
    public void answer(String message) {
        //TODO database call
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }
}
