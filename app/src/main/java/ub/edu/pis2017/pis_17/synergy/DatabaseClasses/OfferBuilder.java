package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available.AvailableOfferModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done.DoneOfferModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing.OngoingOfferModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UserDatabase.UserModel;
import ub.edu.pis2017.pis_17.synergy.LocationHelper;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 07/05/2018.
 */

public class OfferBuilder extends PostBuilder {
    User admin, participant;

    public OfferBuilder setAdmin(User a) {
        this.admin = a;
        return this;
    }

    public OfferBuilder setParticipant(User a) {
        this.participant = a;
        return this;
    }

    @Override
    public Post build() {
        Location loc = LocationHelper.read(postModel.getLocation());

        if (this.postModel instanceof AvailableOfferModel) {
            return new AvailableOffer(postModel.getId(),admin,loc,postModel.getTitle(),postModel.getDescription());
        } else if (this.postModel instanceof OngoingOfferModel) {
            return new OngoingOffer(postModel.getId(),admin,loc,postModel.getTitle(),postModel.getDescription(),participant);
        } else if (this.postModel instanceof DoneOfferModel) {
            String memoir = ((DoneOfferModel)postModel).getMemoir();
            return new DoneOffer(postModel.getId(),admin,loc,postModel.getTitle(),postModel.getDescription(),memoir,participant);
        } else {
            // wat
            throw new UnknownError();
        }
    }
}
