package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import android.location.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available.AvailableProjectModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available.AvailableRequestModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done.DoneProjectModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done.DoneRequestModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing.OngoingProjectModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing.OngoingRequestModel;
import ub.edu.pis2017.pis_17.synergy.LocationHelper;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Ongoing;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Project;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 07/05/2018.
 */

public class ProjectBuilder extends PostBuilder {
    User admin;
    Map<Object,User> participants;
    Collection<Object> positions;

    public ProjectBuilder() {
        this.participants = new HashMap<>();
    }

    public ProjectBuilder setAdmin(User a) {
        this.admin = a;
        return this;
    }

    public ProjectBuilder setParticipants(Map<Object, User> participants) {
        this.participants = participants;
        return this;
    }

    public ProjectBuilder setPositions(Collection<Object> positions){
        this.positions = positions;
        return this;
    }


    @Override
    public Post build() {
        Location loc = LocationHelper.read(postModel.getLocation());

        if (this.postModel instanceof AvailableProjectModel) {
            return new AvailableProject(postModel.getId(),admin,loc,postModel.getTitle(),postModel.getDescription(),positions,participants);
        } else if (this.postModel instanceof OngoingProjectModel) {
            return new OngoingProject(postModel.getId(),admin,loc,postModel.getTitle(),postModel.getDescription(),positions,participants);
        } else if (this.postModel instanceof DoneProjectModel) {
            String memoir = ((DoneProjectModel)postModel).getMemoir();
            return new DoneProject(postModel.getId(),admin,loc,postModel.getTitle(),postModel.getDescription(),positions,participants,memoir);
        }else {
            throw new UnknownError();
        }
    }
}
