package ub.edu.pis2017.pis_17.synergy.Model.Messages;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.Interfaces.Confirmable;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 24/04/2018.
 */

public class PostConfirmMessage extends UserMessage implements Confirmable {

    public PostConfirmMessage(String id, String title, String text, User to, User from, Post context, int actionDone) {
        super(id, title, text, to, from, context, actionDone);
    }

    public PostConfirmMessage(String title, String text, User to, User from, Post context, int actionDone) {
        super(title, text, to, from, context, actionDone);
    }

    @Override
    public void confirm(boolean confirm) {
        // TODO: 12/05/2018 make this in the view not the model
    }
}
