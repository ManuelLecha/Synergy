package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available.AvailableRequestModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done.DoneRequestModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing.OngoingRequestModel;
import ub.edu.pis2017.pis_17.synergy.LocationHelper;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 07/05/2018.
 */

public class RequestBuilder extends PostBuilder {
    User admin, participant;

    public RequestBuilder setAdmin(User a) {
        this.admin = a;
        return this;
    }

    public RequestBuilder setParticipant(User a) {
        this.participant = a;
        return this;
    }

    @Override
    public Post build() {
        Location loc = LocationHelper.read(postModel.getLocation());

        if (this.postModel instanceof AvailableRequestModel) {
            return new AvailableRequest(postModel.getId(), admin, loc, postModel.getTitle(), postModel.getDescription());
        } else if (this.postModel instanceof OngoingRequestModel) {
            return new OngoingRequest(postModel.getId(), admin, loc, postModel.getTitle(), postModel.getDescription(), participant);
        } else if (this.postModel instanceof DoneRequestModel) {
            String memoir = ((DoneRequestModel) postModel).getMemoir();
            return new DoneRequest(postModel.getId(), admin, loc, postModel.getTitle(), postModel.getDescription(), memoir, participant);
        } else {
            // wat
            throw new UnknownError();
        }
    }
}
