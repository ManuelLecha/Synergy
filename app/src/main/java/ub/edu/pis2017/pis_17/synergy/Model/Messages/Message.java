package ub.edu.pis2017.pis_17.synergy.Model.Messages;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 24/04/2018.
 */

public abstract class Message  {
    //MESSAGE states/////////////////
    public static final int NEW_MESSAGE = 0
                          , READ_MESSAGE = 1
                          , DONE_MESSAGE = 2
                          , CANCELLED_MESSAGE = 3;

    protected String id;
    protected String title;
    protected String text;
    protected User to;
    protected Post context;
    protected int actionDone;

    public Message(String id, String title, String text, User to, Post context, int actionDone) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.to = to;
        this.context = context;
        this.actionDone = actionDone;
    }

    public Message(String title, String text, User to, Post context, int actionDone) {
        this.title = title;
        this.text = text;
        this.to = to;
        this.context = context;
        this.actionDone = actionDone;
    }

    public String getId(){return this.id;}

    public String getText() {
        return text;
    }

    public User getTo() {
        return to;
    }

    public Post getContext() {
        return context;
    }

    public int getActionDone(){return actionDone;}

    public String getTitle(){return this.title;}
}
