package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.HashtagDatabase;

import java.util.function.Consumer;

import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;

public class HashtagModel {

    private String hashtagName;

    public HashtagModel(String hashtagName){
        this.hashtagName = hashtagName;
    }
    public String getHashtagName(){
        return this.hashtagName;
    }
    public HashtagModel() {
    }

}
