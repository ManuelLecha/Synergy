package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.MessagesDatabase;

import java.util.Date;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.MessageCategory;

public class MessageModel {

    private String id;
    private MessageCategory messageCategory;
    private String title;
    private String message;
    private String recieverId;
    private String postId;
    private String senderId;
    private int actionDone;
    private String vacant;
    private long date;


    public MessageModel(String id, MessageCategory messageCategory, String title, String message, String recieverId, String postId, String senderId, int actionDone, String vacant) {
        this.id = id;
        this.messageCategory = messageCategory;
        this.title = title;
        this.message = message;
        this.recieverId = recieverId;
        this.postId = postId;
        this.senderId = senderId;
        this.actionDone = actionDone;
        this.vacant = vacant;
        this.date = getCurrentTimeStamp();
    }

    public MessageModel(String id, MessageCategory messageCategory, String title, String message, String recieverId, String postId, String senderId, int actionDone) {
        this.id = id;
        this.messageCategory = messageCategory;
        this.title = title;
        this.message = message;
        this.recieverId = recieverId;
        this.postId = postId;
        this.senderId = senderId;
        this.actionDone = actionDone;
        this.vacant = null;
        this.date = getCurrentTimeStamp();
    }

    public MessageModel(String id, MessageCategory messageCategory, String title, String message, String recieverId, String postId, int actionDone, String vacant) {
        this.id = id;
        this.messageCategory = messageCategory;
        this.title = title;
        this.message = message;
        this.recieverId = recieverId;
        this.postId = postId;
        this.actionDone = actionDone;
        this.vacant = vacant;
        this.senderId = null;
        this.date = getCurrentTimeStamp();
    }

    public MessageModel(String id, MessageCategory messageCategory, String title, String message, String recieverId, String postId, int actionDone) {
        this.id = id;
        this.messageCategory = messageCategory;
        this.title = title;
        this.message = message;
        this.recieverId = recieverId;
        this.postId = postId;
        this.actionDone = actionDone;
        this.vacant = null;
        this.senderId = null;
        this.date = getCurrentTimeStamp();
    }

    public MessageModel() {
    }

    public MessageCategory getMessageCategory() {
        return messageCategory;
    }

    public String getMessage() {
        return message;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public String getPostId() {
        return postId;
    }

    public String getSenderId(){return this.senderId;}

    public int getActionDone(){return this.actionDone;}

    public String getId(){return this.id;}

    public String getTitle(){ return this.title;}

    public String getVacant(){return this.vacant;}

    public long getDate(){return this.date;}

    private long getCurrentTimeStamp(){
        Date d = new Date();
        return - d.getTime();
    }


}

