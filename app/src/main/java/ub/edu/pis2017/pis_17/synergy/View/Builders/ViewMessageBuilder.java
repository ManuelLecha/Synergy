package ub.edu.pis2017.pis_17.synergy.View.Builders;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.UserMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

public class ViewMessageBuilder {

    private String id;
    private String title;
    private String content;
    private User fromUser;
    private User toUser;
    private Post postContext;
    private int actionDone;

    public ViewMessageBuilder() {}

    public void setId(String id) {this.id = id;}
    public void setTitle(String title) {this.title = title;}
    public void setContent(String content) {this.content = content;}
    public void setFromUser(User fromUser) {this.fromUser = fromUser;}
    public void setToUser(User toUser) {this.toUser = toUser;}
    public void setPostContext(Post postContext) {this.postContext = postContext;}
    public void setActionDone(int actionDone) {this.actionDone = actionDone;}

    public UserMessage buildUserMessage() {
        return new UserMessage(id, title, content, toUser, fromUser, postContext, actionDone);
    }

    public SystemMessage buildSystemMessage() {
        return new SystemMessage(id, title, content, toUser, postContext, actionDone);
    }

    public RateMessage buildRateMessage() {
        return new RateMessage(id, title, content, toUser, fromUser, postContext, actionDone);
    }

}
