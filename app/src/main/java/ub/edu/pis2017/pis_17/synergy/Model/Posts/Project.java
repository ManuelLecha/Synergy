package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.location.Location;

import java.util.Collection;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.HasMultipleParticipants;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by manuellechasanchez on 10/04/2018.
 */

public class Project extends Post implements HasMultipleParticipants {

    private Map<Object,User> positionUsers;
    private Collection<Object> positions;

    public Project(String id, User admin, Location location, String title, String description, Collection<Object> positions, Map<Object, User> positionUsers) {
        super(id, admin, location, title, description);
        this.positionUsers = positionUsers;
        this.positions = positions;
    }

    public Map<Object,User> getPositionUsers(){return positionUsers; }

    public void setPositionUsers(Map<Object,User> positionUsers){this.positionUsers = positionUsers;}

    public void fillVacancy(User u, Object key){
        if(positions.contains(key)){
            this.positionUsers.put(key,u);
        }
    }

    @Override
    public Collection<Object> getPositions() {
        return positions;
    }

    @Override
    public String getLabel() {
        return "Project";
    }

    @Override
    public User getParticipantFor(Object context) {
        return positionUsers.get(context);
    }

    @Override
    public boolean isParticipant(User u) {
        return positionUsers.containsValue(u);
    }

    @Override
    public int getParticipantCount() {
        return positionUsers.size();
    }


    public Object getActualParticipantCount() {
        return positionUsers.keySet().size();
    }

    public Object getMaxParticipantCount() {
        return positions.size();
    }

    @Override
    public String getId(){
        return super.getId();
    }
}
