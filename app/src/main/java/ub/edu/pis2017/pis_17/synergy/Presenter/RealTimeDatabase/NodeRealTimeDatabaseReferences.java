package ub.edu.pis2017.pis_17.synergy.Presenter.RealTimeDatabase;

/**
 * Created by manuellechasanchez on 14/04/2018.
 */

class NodeRealTimeDatabaseReferences {



    //USER-INFO
    //=========

    //NODE:Users
    final public static String USER_REFERENCE = "Users";
    final public static String USER_MAIL_REFERENCE = "mail";
    final public static String USER_UID_REFERENCE = "uid";
    final public static String USER_IMAGE_PATH_REFERENCE = "imagePath";
    final public static String USER_NAME_REFERENCE = "name";
    final public static String USER_RATING_REFERENCE = "rating";
    final public static String USER_DESCRIPTION_REFERENCE = "description";
    final public static String USER_TIMES_RATED = "timesRated";
    //NODE: User_Hashtags
    final public static String USER_HASHTAGS_REFERENCE = "UserHashtags";
    //NODE:User_Inbox
    final public static String USER_INBOX_REFERENCE = "UserInbox";
    //NODE:User_Offers_Administrating
    final public static String USER_OFFERS_ADMINISTRATING_REFERENCE = "UserOffersAdministrating";
    //NODE:User_Requests_Administrating
    final public static String USER_REQUESTS_ADMINISTRATING_REFERENCE = "UserRequestsAdministrating";
    //NODE:User_Projects_Administrating
    final public static String USER_PROJECTS_ADMINISTRATING_REFERENCE = "UserProjectsAdministrating";
    //NODE:User_Ongoing_Offers
    final public static String USER_ONGOING_OFFERS_REFERENCE = "UserOngoingOffers";
    //NODE:User_Ongoing_Requests
    final public static String USER_ONGOING_REQUESTS_REFERENCE = "UserOngoingRequests";
    //NODE:User_Ongoing_Projects
    final public static String USER_ONGOING_PROJECTS_REFERENCE = "UserOngoingProjects";
    //NODE:User_Done_Offers
    final public static String USER_DONE_OFFERS_REFERENCE = "UserDoneOffers";
    //NODE:User_Done_Requests
    final public static String USER_DONE_REQUESTS_REFERENCE = "UserDoneRequests";
    //NODE:User_Done_Projects
    final public static String USER_DONE_PROJECTS_REFERENCE = "UserDoneProjects";

    //========


    //POSTS-INFO
    //=========

    //NODE:Posts
    final public static String POST_REFERENCE = "Posts";
    //Node:Post_Hashtags
    final public static String POST_HASHTAGS_REFERENCE = "PostHashtags";

    //Post common atributes
    final public static String POST_ID_REFERENCE = "id";
    final public static String POST_TITLE_REFERENCE = "title";
    final public static String POST_LOCATION_REFERENCE = "location";
    final public static String POST_ADMIN_UID_REFERENCE = "adminUid";
    final public static String POST_DESCRIPTION_REFERENCE = "description";
    final public static String POST_CATEGORY = "postType";
    final public static String DONE_POST_MEMOIR_REFERENCE = "memoir";


    //NODE: Available_Offers
    final public static  String AVAILABLE_OFFERS = "AvailableOffers";
    //NODE:Available_Requests
    final public static String AVAILABLE_REQUESTS = "AvailableRequests";

    //Offer and request common attributes
    final public static String PARTICIPANT_UID_REFERENCE = "participantUid";

    //NODE:Available_Projects
    final public static String AVAILABLE_PROJECTS = "AvailableProjects";
    final public static String VACANTS_REFERENCE = "vacants";

    //HASHTAG-POST
    //============
    //NODE: Hashtag_Post
    final public static String HASHTAGS_POST_REFERENCE = "HashtagPost";
    //============

    //====================

    //HASHTAG
    //=======
    //NODE: Hashtags
    final public static String HASHTAGS_REFERENCE = "Hashtags";
    //=======



    //MESSAGES
    //========
    final public static String MESSAGE_RECIEVER_REFERENCE = "recieverId";
    final public static String MESSAGE_ACTION_DONE_REFERENCE = "actionDone";
    final public static String MESSAGE_DATE_REFERENCE = "date";


    //FOR LOGIC CONTROL
    final public static String USER_CONFIRMATION_ASKING_REFERENCE = "userConfirmationAsking";

    final public static String MESSAGE_PENDING_FOR_CONFIRMATION = "messagePendingForConfirmation";

    final public static String POST_PARTICIPATING_MESSAGE = "postParticipatingMessage";




    
}
