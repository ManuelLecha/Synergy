package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.MessagesDatabase.MessageModel;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.UserMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 08/05/2018.
 */

public class MessageBuilder{
    protected MessageModel messageModel;
    protected User recipient,sender;
    private Runnable action;
    protected Post context;

    public MessageBuilder setModel(MessageModel m) {
        this.messageModel = m;
        return this;
    }

    public MessageBuilder setRecipient(User recipient) {
        this.recipient = recipient;
        return this;
    }

    public MessageBuilder setSender(User sender){
        this.sender = sender;
        return this;
    }

    public MessageBuilder setAction(Runnable r) {
        this.action = r;
        return this;
    }

    public MessageBuilder setContext(Post p) {
        this.context = p;
        return this;
    }

    public Message build(){
        switch(messageModel.getMessageCategory()){
            case POST_CONFIRM_MESSAGE:
                return new PostConfirmMessage(messageModel.getId(),messageModel.getTitle(), messageModel.getMessage(), recipient, sender,context, messageModel.getActionDone());
            case RATE_MESSAGE:
                return new RateMessage(messageModel.getId(), messageModel.getTitle(),messageModel.getMessage(),recipient, sender, context,messageModel.getActionDone());
            case SYSTEM_MESSAGE:
                return new SystemMessage(messageModel.getId(),messageModel.getTitle(), messageModel.getMessage(), recipient, context, messageModel.getActionDone());
            case USER_MESSAGE:
                return new UserMessage(messageModel.getId(), messageModel.getTitle(), messageModel.getMessage(), recipient,  sender,context, messageModel.getActionDone());
            case VACANT_CONFIRM_MESSAGE:
                return new VacantConfirmMessage(messageModel.getId(),messageModel.getTitle(),messageModel.getMessage(),recipient,sender,context,messageModel.getActionDone(),messageModel.getVacant());
            default:
                throw new UnknownError();
        }
    }



}
