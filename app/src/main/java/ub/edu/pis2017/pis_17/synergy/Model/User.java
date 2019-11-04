package ub.edu.pis2017.pis_17.synergy.Model;

import java.util.Objects;

/**
 * Created by manuellechasanchez on 10/04/2018.
 */

public class User {

    private String uid;
    private String name;
    private Email mail;
    private String imagePath;
    private Float rating;
    private String description;

    public User(Email mail, String uid) {
        this.mail = mail;
        this.uid = uid;
        this.name = "";
        this.imagePath = "";
    }

    public User(String uid, String name, Email mail, String imagePath, Float rating, String description){
        this.mail = mail;
        this.uid = uid;
        this.name = name;
        this.imagePath = imagePath;
        this.rating = rating;
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Email getMail() {
        return mail;
    }

    public void setMail(Email mail) {
        this.mail = mail;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(uid, user.uid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(uid);
    }
}
