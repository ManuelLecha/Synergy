package ub.edu.pis2017.pis_17.synergy.Model;

import ub.edu.pis2017.pis_17.synergy.Model.User;

public class Participant {

    private User user;
    private String position;

    public Participant(User user, String position) {
        this.user = user;
        this.position = position;
    }

    public User getUser() {return user;}
    public String getPosition() {return position;}

}
