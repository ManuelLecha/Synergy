package ub.edu.pis2017.pis_17.synergy.Model.Messages;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.Interfaces.Confirmable;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

public class VacantConfirmMessage extends UserMessage implements Confirmable{

    private Object vacant;

    public VacantConfirmMessage(String id, String title, String text, User to, User from, Post context, int actionDone, Object vacant) {
        super(id, title, text, to, from, context, actionDone);
        this.vacant = vacant;
    }

    public VacantConfirmMessage(String title, String text, User to, User from, Post context, int actionDone, Object vacant) {
        super(title, text, to, from, context, actionDone);
        this.vacant = vacant;
    }


    public Object getVacant(){
        return this.vacant;
    }
    @Override
    public void confirm(boolean confirm) {
        //??
    }
}
