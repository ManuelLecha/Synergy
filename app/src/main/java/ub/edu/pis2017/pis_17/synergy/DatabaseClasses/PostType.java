package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

/**
 * Created by kanales on 21/04/2018.
 */

public enum PostType {

    AVAILABLE_OFFER,
    AVAILABLE_PROJECT,
    AVAILABLE_REQUEST,
    ONGOING_OFFER,
    ONGOING_PROJECT,
    ONGOING_REQUEST,
    DONE_OFFER,
    DONE_PROJECT,
    DONE_REQUEST;

    private String label;

    static {
        AVAILABLE_OFFER.label = "availableOffer";
        AVAILABLE_PROJECT.label = "availableProject";
        AVAILABLE_REQUEST.label = "availableRequest";
        ONGOING_OFFER.label = "ongoingOffer";
        ONGOING_REQUEST.label = "ongoingRequest";
        ONGOING_PROJECT.label = "ongoingProject";
        DONE_OFFER.label = "doneOffer";
        DONE_REQUEST.label = "doneRequest";
        DONE_PROJECT.label = "doneProject";
    }

    public String getLabel() {
        return label;
    }
}
