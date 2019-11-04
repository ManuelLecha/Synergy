package ub.edu.pis2017.pis_17.synergy.Presenter.RealTimeDatabase;

import android.location.Location;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.UserMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by kanales on 17/04/2018.
 */

public interface RealTimeDatabaseInteractor {




    //USER INFO UPDATING ====================================================================//
    //======================================================================================//

    /**
     * Initial values on database for a new user
     * @param uid
     * @param name
     * @param mail
     */
    public FutureResult initUser(String uid, String name, Email mail);

    /**
     * Updates the name of a certain user
     * @param uid
     * @param name
     */
    public FutureResult updateUserName(String uid, String name);

    /**
     * Updates the imagePath from the user
     * @param uid
     * @param newImageURI
     */
    public FutureResult updateUserimagePath(String uid, String newImageURI);

    /**
     * Changes de description of a user
     * @param description
     */
    public FutureResult updateUserDescription(String uid, String description);

    /**
     * Rates a user
     * @param uid
     * @param rate
     */
    public FutureResult updateUserRating(String uid, int rate);

    /**
     * Adds a list of hashtags to a certain user
     * @param uid
     * @param hashtagList
     */
    public FutureResult updateUserHashtagList(String uid, List<Hashtag> hashtagList);

    /**
     *
     * @return
     */
    public FutureResult updateUserEmail(String uid, Email newEmail);


    //======================================================================================//
    //======================================================================================//



    //== GETTING USER INFO =================================================================//
    //======================================================================================//

    /**
     * Gets a user
     * @param uid
     * @return
     */
    public FutureBox<User> getUser(String uid);


    /**
     * This method returns the messages from the user inbox
     * @param uid
     */
    public FutureBox<List<Message>> readUserInbox(String uid);

    /**
     * @param uid
     * @return
     */
    public void unReadMessagesCount(String uid);


    /**
     * Reads the hashtag list of a certain user
     * @param uid
     */
    public FutureBox<List<Hashtag>> readUserHashtagList(String uid);



    //======================================================================================//
    //======================================================================================//



    //== NEW POSTS  //======================================================================//
    //======================================================================================//

    /**
     * Adds a new offer to the database on the offers node, and adds a reference of its id on administrating offer node of the user, and available offers
     * Sets postCategory on teh database to availableOffer
     * @param admin
     * @param location
     * @param title
     * @param description
     * @return postID
     */
    public FutureBox<String> addNewOffer(User admin, Location location, String title, String description);


    /**
     * Adds a new request to the database on the offers node, and adds a reference of its id on administrating request node of the user, and available requests
     * Sets postCategory on teh database to availableRequest
     * @param admin
     * @param location
     * @param title
     * @param description
     * @return postID
     */
    public FutureBox<String> addNewRequest(User admin, Location location ,String title, String description);

    /**
     * Adds a new project to the database on the projects node, and adds a reference of its id on administrating projects node of the user, and available projects
     * Sets postCategory on teh database to availableProject
     * @param admin
     * @param location
     * @param title
     * @param description
     * @param vacants
     * @return postId
     */
    public FutureBox<String> addNewProject(User admin, Location location, String title, String description, Collection<Object> vacants);

    //=======================================================================================//
    //======================================================================================//


    //== UPDATE ADMINISTRATING POSTS  ======================================================//
    //======================================================================================//

    /**
     * Updates the list of hashtags of a post
     * @param post
     * @param hashtagList
     * @return
     */
    public FutureResult updatePostHashtagList(Available post, List<Hashtag> hashtagList);

    /**
     * Updates the information of an administrating offer
     * @param availableOffer
     */
    public FutureResult updateAdministratingOffer(AvailableOffer availableOffer);


    /**
     * Updates the information of an administrating request
     * @param availableRequest
     */
    public FutureResult updateAdministratingRequest(AvailableRequest availableRequest);


    /**
     * Updates the information of an administrating project, and if vacants are filled, errases from administrating offers user node and available project node, and adds it to the ongoing
     * Changes postCategory on the database to ongoingProject
     * @param availableProject
     */
    public FutureResult updateAdministratingProject(AvailableProject availableProject);

    /**
     * Delete availableOffer
     * @param availableOffer
     * @return
     */
    public FutureResult deleteAdministratingOffer(AvailableOffer availableOffer);

    /**
     * Delete availableRequest
     * @param availableRequest
     * @return
     */
    public FutureResult deleteAdministratingRequest(AvailableRequest availableRequest);

    /**
     * Delete availableProject
     * @param availableProject
     * @return
     */
    public FutureResult deleteAdministratingProject(AvailableProject availableProject);

    //======================================================================================//
    //======================================================================================//




    //== AVAILABLE TO ONGOING  //============================================================//
    //======================================================================================//

    /**
     * participant is added, errases from administrating offers user node and available offers node, and adds it to the ongoing
     * Changes postCategory on the database to ongoingOffer
     * @param availableOffer
     * @param participant
     */
    public FutureResult startOffer(AvailableOffer availableOffer, User participant);

    /**
     * participant is added, errases from administrating request user node and available requests node, and adds it to the ongoing
     * Changes postCategoty on the database to ongoingRequest
     * @param availableRequest
     * @param participant
     */
    public FutureResult startRequest(AvailableRequest availableRequest, User participant);

    /**
     * Deletes the project from database Available node and User administrating projects node and adds it to the ongoing node
     * Changes post type to ongoingProject
     * @param availableProject
     */
    public FutureResult AvailabletoOngoingProject(AvailableProject availableProject);


    //======================================================================================//
    //======================================================================================//




    //ONGOING TO DONE ======================================================================//
    //======================================================================================//

    /**
     * Deletes an offer from the ongoing offers node and adds it to the done offers node
     * Sets the postCategory to doneOffer
     * @param ongoingOffer
     * @param memoir
     */
    public FutureResult OngoingToDoneOffer(OngoingOffer ongoingOffer, String memoir);

    /**
     * Deletes an offer from the ongoing request node and adds it to the done requests node
     * Sets the postCategory to doneRequest
     * @param ongoingRequest
     * @param memoir
     */
    public FutureResult OngoingToDoneRequest(OngoingRequest ongoingRequest, String memoir);

    /**
     * Deletes an offer from the ongoing projects node and adds it to the done projects node
     * Sets the postCategory to doneProject
     * @param ongoingProject
     * @param memoir
     */
    public FutureResult OngoingToDoneProject(OngoingProject ongoingProject, String memoir);


    //======================================================================================//
    //======================================================================================//



    //== LOOK FOR AVAILABLE POSTS  =========================================================//
    //======================================================================================//



    /**
     * @return Available Offers on the database
     */
    public FutureBox<List<Post>> readAvailableOffers();

    /**
     * @return Available request on teh database
     */
    public FutureBox<List<Post>> readAvailableRequests();

    /**
     * @return Available projects on the database
     */
    public FutureBox<List<Post>> readAvailableProjects();

    /**
     * @param h
     * @return
     */
    public FutureBox<List<Post>> readAvailableProjectsFilteredByHashtag(Hashtag h);

    /**
     * @param h
     * @return
     */
    public FutureBox<List<Post>> readAvailableRequestsFilteredByHashtag(Hashtag h);

    /**
     * @param h
     * @return
     */
    public FutureBox<List<Post>> readAvailableOffersFilteredByHashtag(Hashtag h);

    //======================================================================================//
    //======================================================================================//


    //== GETTING USER-POSTS  //=============================================================//
    //======================================================================================//

    public FutureBox<Collection<Post>> readPostsAdministrating(String uid);

    /**
     * Reads the list of ongoing offers of a user
     * @param user
     */
    public FutureBox<List<Post>> readOngoingOffers(User user);

    /**
     * Reads the list of offers that a user is administrating
     * @param user
     */
    public FutureBox<List<Post>> readAdministratingOffers(User user);

    /**
     * Reads the list of requests that a user is administrating
     * @param user
     */
    public FutureBox<List<Post>> readAdministratingRequests(User user);

    /**
     * Reads the list of projects that a user is administrating
     * @param user
     */
    public FutureBox<List<Post>> readAdministratingProjects(User user);

    /**
     * Reads the list of ongoing requests of a user
     * @param user
     */
    public FutureBox<List<Post>> readOngoingRequests(User user);

    /**
     * Reads the list of ongoing projects of a user
     * @param user
     */
    public FutureBox<List<Post>> readOngoingProjects(User user);

    /**
     * Reads the list od done offers of a user
     * @param user
     */
    public FutureBox<List<Post>> readDoneOffers(User user);

    /**
   * Reads the list of done requests of a user
     * @param user
     */
    public FutureBox<List<Post>> readDoneRequests(User user);

    /**
     * Reads the list of done projects of a user
     * @param user
     */
    public FutureBox<List<Post>> readDoneProjects(User user);

    //======================================================================================//
    //======================================================================================//

    //==HASHTAG- POST =======================================================================//
    //======================================================================================//
    /**
     * Reads the hashtag list of a certain post
     */
    public FutureBox<List<Hashtag>> readPostHashtagList(String postId);
    //======================================================================================//
    //======================================================================================//



    //DATABASE-HASHTAG =====================================================================//
    //======================================================================================//

    /**
     * Adds hashtag to the database
     * @param tag
     * @return true if it was added, false otherwise
     */
    public FutureResult addHashtag(Hashtag tag);

    /**
     * Returns all the hashtags that begin with a string s
     * @param s
     * @return list of Hashtags
     */
    public FutureBox<List<Hashtag>> hashtagsBeginWith(String s);

    public FutureBox<Hashtag> getHashtag(String hashtagName);

    //======================================================================================//
    //======================================================================================//


    public FutureBox<Post> getPost(String postID);


    /**
     * Sending a UserMessage
     * @param message
     * @return
     */
    public FutureResult sendUserMessage(UserMessage message);

    /**
     * Sending a system message
     * @param message
     * @return
     */
    public FutureResult sendSystemMessage(SystemMessage message);

    /**
     * Sending a rate message
     * @param message
     * @return
     */
    public FutureResult sendRateMessage(RateMessage message);

    /**
     * Sending a confirm message of a post
     * @param message
     * @return
     */
    public FutureResult sendPostConfirmMessage(String uid, PostConfirmMessage message);

    /**
     * Sending a cofirm message for a vacant
     * @param message
     * @return
     */
    public FutureResult sendVacantConfirmMessage(String uid, VacantConfirmMessage message);


    /**
     * @param uid
     * @param messageId
     * @return
     */
    public FutureResult markMessageActionRead(String uid, String messageId);

    /**
     * @param uid
     * @param messageId
     * @return
     */
    public FutureResult markMessageAsCancelled(String uid, String messageId);


    /**
     * marks message action as done
     * @param uid
     * @param messageId
     * @return
     */
    public FutureResult markMessageActionDone(String uid, String messageId);


    /**
     * @param offer_id
     * @return Available offer
     */
    public FutureBox<AvailableOffer> getAvailableOffer(String offer_id);

    /**
     * @param offer_id
     * @return
     */
    public FutureBox<OngoingOffer> getOngoingOffer(String offer_id);

    /**
     * @param offer_id
     * @return
     */
    public FutureBox<DoneOffer> getDoneOffer(String offer_id);

    /**
     * @param request_id
     * @return
     */
    public FutureBox<AvailableRequest> getAvailableRequest (String request_id);

    /**
     * @param request_id
     * @return
     */
    public FutureBox<OngoingRequest> getOngoingRequest(String request_id);

    /**
     * @param request_id
     * @return
     */
    public FutureBox<DoneRequest> getDoneRequest(String request_id);

    /**
     * @param project_id
     * @return
     */
    public FutureBox<AvailableProject> getAvailableProject(String project_id);

    /**
     * @param project_id
     * @return
     */
    public FutureBox<OngoingProject> getOngoingProject(String project_id);

    /**
     * @param project_id
     * @return
     */
    public FutureBox<DoneProject> getDoneProject(String project_id);

    /**
     * @param availableProject
     * @param participant
     * @param vacancy
     * @return
     */
    public FutureResult fillVacancyWithUser(AvailableProject availableProject, User participant, String vacancy);

}
