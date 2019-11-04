package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UserDatabase;


public class UserModel {

    private String uid;
    private String name;
    private String mail;
    private String imagePath;
    private String description;
    private float rating;
    private int timesRated;


    public UserModel(String uid, String name, String mail, String imagePath, String description, float rating, int timesRated) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.imagePath = imagePath;
        this.description = description;
        this.rating = rating;
        this.timesRated = timesRated;
    }

    public UserModel(){

    }

    public String getUid() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }

    public String getMail() {
        return this.mail;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public String getDescription() {
        return description;
    }

    public float getRating() {
        return rating;
    }

    public int getTimesRated() { return timesRated; }

}


