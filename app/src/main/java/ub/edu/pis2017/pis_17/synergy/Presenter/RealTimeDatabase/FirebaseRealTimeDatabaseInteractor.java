package ub.edu.pis2017.pis_17.synergy.Presenter.RealTimeDatabase;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.HashtagBuilder;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.HashtagDatabase.HashtagModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.MessageBuilder;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.MessageCategory;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.MessagesDatabase.MessageModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.OfferBuilder;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available.AvailableOfferModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available.AvailableProjectModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Available.AvailableRequestModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done.DoneOfferModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done.DoneProjectModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Done.DoneRequestModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing.OngoingOfferModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing.OngoingProjectModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase.Ongoing.OngoingRequestModel;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.ProjectBuilder;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.RequestBuilder;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UnreadMessageNumber;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UserBuilder;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UserDatabase.UserModel;
import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
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


public class FirebaseRealTimeDatabaseInteractor implements RealTimeDatabaseInteractor {


    private DatabaseReference database;
    private NodeRealTimeDatabaseReferences ref;


    private final String S = "SUCCESS";
    private final String E = "ERROR";
    private final String DefaultUserimagePath = "logo.png";
    private final String DefaultUserDescription = "";
    private final String EMPTY_VACANT = "Empty";


    public FirebaseRealTimeDatabaseInteractor(){
        database = FirebaseDatabase.getInstance().getReference();
        ref = new NodeRealTimeDatabaseReferences();
    }

    //USER INFO UPDATING ====================================================================//
    //======================================================================================//

    @Override
    public FutureResult initUser(String uid, String name, Email mail) {
        UserModel user = new UserModel(uid, name,mail.toString(),DefaultUserimagePath,DefaultUserDescription,(float) 0.0, 0);
        return setValueOnDatabaseReference(getUserReference(uid), user);
    }


    @Override
    public FutureResult updateUserName(String uid, String name) {
        return setValueOnDatabaseReference(getUserNameReference(uid),name);
    }

    @Override
    public FutureResult updateUserimagePath(String uid, String imagePath) {
        return setValueOnDatabaseReference(getUserImagePathReference(uid), imagePath);

    }

    @Override
    public FutureResult updateUserDescription(String uid, String description) {
        return setValueOnDatabaseReference(getUserDescriptionReference(uid),description);

    }

    @Override
    public FutureResult updateUserRating(String uid, int r) {

        FutureResult res = FutureResult.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(getUserReference(uid)).whenFull(d -> {
            float rate = d.child(ref.USER_RATING_REFERENCE).getValue(Float.class);
            int timesRated = d.child(ref.USER_TIMES_RATED).getValue(Integer.class);
            float rating = ((rate*timesRated) + r)/(timesRated + 1);
            setValueOnDatabaseReference(getUserRatingReference(uid),rating).
                    whenDone(() ->
                            setValueOnDatabaseReference(getUserTimesRatedReference(uid),timesRated + 1).
                                    whenDone(() -> res.success()).ifFails(e -> res.error())).ifFails(e -> res.error(e));
        }).onCancellation(e -> res.error());

        return res;
    }



    @Override
    public FutureResult updateUserHashtagList(String uid, List<Hashtag> hashtagList) {

        FutureResult res = FutureResult.empty();
        List<String> hashtagNameList = new LinkedList<>();

        for(Hashtag h: hashtagList){
            addHashtag(h);
            hashtagNameList.add(h.getHashtagName());
        }

        return setValueOnDatabaseReference(getUserHashtagsReference(uid),hashtagNameList);

    }

    @Override
    public FutureResult updateUserEmail(String uid, Email newEmail) {
        return setValueOnDatabaseReference(getUserMailReference(uid),newEmail.toString());

    }

    //======================================================================================//
    //======================================================================================//


    //== GETTING USER INFO =================================================================//
    //======================================================================================//


    @Override
    public FutureBox<User> getUser(String uid) {

        FutureBox<User> futureUser = FutureBox.empty();

        readValueOfDatabaseReferenceWithSingleValueEvent(getUserReference(uid)).whenFull(d -> {
            UserBuilder ub = new UserBuilder();
            UserModel x = d.getValue(UserModel.class);
            if (x == null) {
                futureUser.error("User not found!");
                return;
            }
            User u = ub.setModel(d.getValue(UserModel.class)).build();
            if (u != null) {
                futureUser.give(u);
            } else {
                futureUser.error("User not found!");
            }
        }).onCancellation(e -> futureUser.error(e));

        return futureUser;

    }


    @Override
    public FutureBox<List<Message>> readUserInbox(String uid) {
        FutureBox<List<Message>> futureInbox = FutureBox.empty();
        database.child(ref.USER_INBOX_REFERENCE).child(uid).orderByChild(ref.MESSAGE_DATE_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<FutureBox> l = new LinkedList<>();
                List<Message> ml = new LinkedList<>();

                dataSnapshot.getChildren().forEach(c -> {
                     FutureBox<Message> m = getMessage(c.getValue(MessageModel.class));
                    m.whenFull(r -> ml.add(r));
                    l.add(m);
                });

                FutureBox.chain(l).resolve(() ->{
                    futureInbox.give(ml);
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { futureInbox.error("Database error: Could not read user's inbox");}

        });

        return futureInbox;
    }

    private class Counter {
        public int x;
        public Counter() {x=0;}
        public void plusOne(){this.x++;}
        public Integer getCounter(){return this.x;}
    }

    @Override
    public void unReadMessagesCount(String uid) {

        getUserInboxReference(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot d) {
                final Counter counter = new Counter();
                //final Counter c2 = new Counter();

                d.getChildren().forEach(c -> {
                    if (c.child(ref.MESSAGE_ACTION_DONE_REFERENCE).getValue(Integer.class) == 0) {
                        counter.plusOne();
                    }
                    //c2.plusOne();
                });

                UnreadMessageNumber.getInstance().setUnreadNumber(counter.getCounter());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }

    @Override
    public FutureBox<List<Hashtag>> readUserHashtagList(String uid) {
        FutureBox<List<Hashtag>> futureHashtagList = FutureBox.empty();
        List<Hashtag> l = new LinkedList<>();
        HashtagBuilder hashtagBuilder = new HashtagBuilder();

        readValueOfDatabaseReferenceWithSingleValueEvent(getUserHashtagsReference(uid)).whenFull(d -> {
            d.getChildren().forEach(c -> {
                hashtagBuilder.setModel(new HashtagModel((String) c.getValue()));
                l.add(hashtagBuilder.build());
            });
            futureHashtagList.give(l);
        }).onCancellation(e -> futureHashtagList.error(e));



       return futureHashtagList;
    }


    //======================================================================================//
    //======================================================================================//


    //== NEW POSTS  //======================================================================//
    //======================================================================================//

    private DatabaseReference getPostReference(String postId){
        return getPostReference().child(postId);
    }

    private DatabaseReference getPostReference(){
        return database.child(ref.POST_REFERENCE);
    }

    private DatabaseReference getUserOffersAdministratingReference(String admin_uid){
        return database.child(ref.USER_OFFERS_ADMINISTRATING_REFERENCE).child(admin_uid);
    }

    private DatabaseReference getUserOfferAdministratingReference(String admin_uid, String offer_id){
        return getUserOffersAdministratingReference(admin_uid).child(offer_id);
    }

    private DatabaseReference getAvailableOfferReference(String offer_id){
        return database.child(ref.AVAILABLE_OFFERS).child(offer_id);
    }

    private DatabaseReference getUserRequestsAdministratingReference(String admin_uid){
        return database.child(ref.USER_REQUESTS_ADMINISTRATING_REFERENCE).child(admin_uid);
    }

    private DatabaseReference getUserRequestAdministratingReference(String admin_uid, String request_id){
        return getUserRequestsAdministratingReference(admin_uid).child(request_id);
    }

    private DatabaseReference getAvailableRequestReference(String request_id){
        return database.child(ref.AVAILABLE_REQUESTS).child(request_id);
    }


    private DatabaseReference getUserProjectsAdministratingReference(String admin_uid){
        return database.child(ref.USER_PROJECTS_ADMINISTRATING_REFERENCE).child(admin_uid);
    }

    private DatabaseReference getUserProjectAdministratingReference(String admin_uid, String project_id){
        return getUserProjectsAdministratingReference(admin_uid).child(project_id);
    }

    private DatabaseReference getAvailableProjectsReference(String project_id){
        return database.child(ref.AVAILABLE_PROJECTS).child(project_id);
    }



    @Override
    public FutureBox<String> addNewOffer(User admin, Location location, String title, String description) {

        FutureBox<String> postId = FutureBox.empty();
        String id = genKey(getPostReference());
        String adminUid = admin.getUid();
        AvailableOfferModel offer = new AvailableOfferModel(id, adminUid, location, title, description, PostType.AVAILABLE_OFFER);
        setValueOnDatabaseReference(getPostReference(id),offer).whenDone(() -> {
            setValueOnDatabaseReference(getUserOfferAdministratingReference(adminUid,id),true);
            setValueOnDatabaseReference(getAvailableOfferReference(id),true);
            postId.give(id);
        }).ifFails(e -> postId.error(e));


        return postId;
    }


    @Override
    public FutureBox<String> addNewRequest(User admin, Location location, String title, String description) {

        FutureBox<String> postId = FutureBox.empty();
        String id = genKey(getPostReference());
        AvailableRequestModel request = new AvailableRequestModel(id, admin.getUid(), location, title, description, PostType.AVAILABLE_REQUEST);
        setValueOnDatabaseReference(getPostReference(id),request).whenDone(() -> {
            setValueOnDatabaseReference(getUserRequestAdministratingReference(admin.getUid(), id), true);
            setValueOnDatabaseReference(getAvailableRequestReference(id),true);
            postId.give(id);
        }).ifFails(e -> postId.error(e));

        return postId;
    }


    @Override
    public FutureBox<String> addNewProject(User admin, Location location, String title, String description, Collection<Object> positions) {

        FutureBox<String> postId = FutureBox.empty();
        String id = genKey(getPostReference());
        String admin_uid = admin.getUid();
        Map<String,String> vacantUid = new HashMap<>();
        positions.forEach(k -> vacantUid.put(k.toString(), EMPTY_VACANT));

        AvailableProjectModel project = new AvailableProjectModel(id,admin_uid,location,title,description, PostType.AVAILABLE_PROJECT,vacantUid);
        setValueOnDatabaseReference(getPostReference(id),project).whenDone(() -> {
            setValueOnDatabaseReference(getUserProjectAdministratingReference(admin_uid,id),true);
            setValueOnDatabaseReference(getAvailableProjectsReference(id),true);
            postId.give(id);
        }).ifFails(e -> postId.error(e));

        return postId;
    }

    //======================================================================================//
    //======================================================================================//

    //==GETTING POSTS======================================================================//
    //=====================================================================================//

    private DatabaseReference getPostCategoryReference(String postId){
        return getPostReference(postId).child(ref.POST_CATEGORY);
    }

    @Override
    public FutureBox<Post> getPost(String postID) {
        FutureBox<Post> futurePost = FutureBox.empty();

        readValueOfDatabaseReferenceWithSingleValueEvent(getPostCategoryReference(postID)).whenFull(d ->{
            PostType category = d.getValue(PostType.class);
            switch (category){
                case AVAILABLE_OFFER:
                    FutureBox<AvailableOffer>  foffer = getAvailableOffer(postID);
                    foffer.whenFull(o -> futurePost.give(o));
                    break;
                case AVAILABLE_PROJECT:
                    FutureBox<AvailableProject> fproject = getAvailableProject(postID);
                    fproject.whenFull(p -> futurePost.give(p));
                    break;
                case AVAILABLE_REQUEST:
                    FutureBox<AvailableRequest> frequest = getAvailableRequest(postID);
                    frequest.whenFull(r -> {
                        futurePost.give(r);
                    });
                    break;
                case ONGOING_OFFER:
                    FutureBox<OngoingOffer>  fonoffer = getOngoingOffer(postID);
                    fonoffer.whenFull(o -> futurePost.give(o));
                    break;
                case ONGOING_PROJECT:
                    FutureBox<OngoingProject> fonproject = getOngoingProject(postID);
                    fonproject.whenFull(p -> futurePost.give(p));
                    break;
                case ONGOING_REQUEST:
                    FutureBox<OngoingRequest> fonRequest = getOngoingRequest(postID);
                    fonRequest.whenFull(r -> futurePost.give(r));
                    break;
                case DONE_OFFER:
                    FutureBox<DoneOffer> fdnOffer = getDoneOffer(postID);
                    fdnOffer.whenFull(o -> futurePost.give(o));
                    break;
                case DONE_PROJECT:
                    FutureBox<DoneProject> fdnProject = getDoneProject(postID);
                    fdnProject.whenFull(p -> futurePost.give(p));
                    break;
                case DONE_REQUEST:
                    FutureBox<DoneRequest> fdnRequest = getDoneRequest(postID);
                    fdnRequest.whenFull(r -> futurePost.give(r));
                    break;

                default:
                    throw new UnknownError();

            }
        }).onCancellation(e -> futurePost.error(e));

        return futurePost;
    }


    public FutureBox<AvailableOffer> getAvailableOffer(String offer_id){

        FutureBox<AvailableOffer> futureOffer = FutureBox.empty();
        final OfferBuilder offerBuilder = new OfferBuilder();
        readValueOfDatabaseReferenceWithSingleValueEvent(getPostReference(offer_id)).whenFull(d ->{
            AvailableOfferModel om = d.getValue(AvailableOfferModel.class);
            offerBuilder.setModel(om);
            FutureBox<User> futureAdmin = getUser(om.getAdminUid());
            futureAdmin.whenFull(user -> {
                offerBuilder.setAdmin(user);
                futureOffer.give((AvailableOffer) offerBuilder.build());
            });
        }).onCancellation(e -> futureOffer.error(e));

        return futureOffer;

    }

    public FutureBox<OngoingOffer> getOngoingOffer(String offer_id){
        FutureBox<OngoingOffer> futureOffer = FutureBox.empty();
        final OfferBuilder offerBuilder = new OfferBuilder();

        readValueOfDatabaseReferenceWithSingleValueEvent(getPostReference(offer_id)).whenFull(d -> {
            OngoingOfferModel om = d.getValue(OngoingOfferModel.class);
            offerBuilder.setModel(om);

            FutureBox<User> futureAdmin = getUser(om.getAdminUid());
            FutureBox<User> futureParticipant = getUser(om.getParticipantUid());

            futureAdmin.whenFull(u-> offerBuilder.setAdmin(u));
            futureParticipant.whenFull(u -> offerBuilder.setParticipant(u));

            futureAdmin.also(futureParticipant).resolve(() -> {
                futureOffer.give((OngoingOffer) offerBuilder.build());
            });
        }).onCancellation(e -> futureOffer.error(e));

        return futureOffer;

    }

    public FutureBox<DoneOffer> getDoneOffer(String offer_id){

        FutureBox<DoneOffer> futureOffer = FutureBox.empty();
        final OfferBuilder offerBuilder = new OfferBuilder();

        database.child(ref.POST_REFERENCE).child(offer_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DoneOfferModel om = dataSnapshot.getValue(DoneOfferModel.class);
                offerBuilder.setModel(om);

                FutureBox<User> futureAdmin = getUser(om.getAdminUid());
                FutureBox<User> futureParticipant = getUser(om.getParticipantUid());

                futureAdmin.whenFull(u-> offerBuilder.setAdmin(u));
                futureParticipant.whenFull(u -> offerBuilder.setParticipant(u));

                futureAdmin.also(futureParticipant).resolve(() -> {
                    futureOffer.give((DoneOffer) offerBuilder.build());
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { futureOffer.error("Error");}
        });
        return futureOffer;
    }

    public FutureBox<AvailableRequest> getAvailableRequest(String request_id){
        FutureBox<AvailableRequest> futureRequest = FutureBox.empty();
        final RequestBuilder requestBuilder = new RequestBuilder();

        database.child(ref.POST_REFERENCE).child(request_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AvailableRequestModel rm = dataSnapshot.getValue(AvailableRequestModel.class);
                requestBuilder.setModel(rm);
                FutureBox<User> futureAdmin = getUser(rm.getAdminUid());
                futureAdmin.whenFull(user -> {
                    requestBuilder.setAdmin(user);
                    AvailableRequest ar = (AvailableRequest) requestBuilder.build();
                    Log.i("Request", ar.toString());
                    futureRequest.give(ar);
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { futureRequest.error("Error");}
        });
        return futureRequest;

    }

    public FutureBox<OngoingRequest> getOngoingRequest(String request_id){

        FutureBox<OngoingRequest> futureRequest = FutureBox.empty();
        final RequestBuilder requestBuilder = new RequestBuilder();

        database.child(ref.POST_REFERENCE).child(request_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                OngoingRequestModel rm = dataSnapshot.getValue(OngoingRequestModel.class);
                requestBuilder.setModel(rm);

                FutureBox<User> futureAdmin = getUser(rm.getAdminUid());
                FutureBox<User> futureParticipant = getUser(rm.getParticipantUid());

                futureAdmin.whenFull(u-> requestBuilder.setAdmin(u));
                futureParticipant.whenFull(u -> requestBuilder.setParticipant(u));

                futureAdmin.also(futureParticipant).resolve(() -> {
                    futureRequest.give((OngoingRequest) requestBuilder.build());
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { futureRequest.error("Error");}
        });
        return futureRequest;

    }

    public FutureBox<DoneRequest> getDoneRequest(String request_id){

        FutureBox<DoneRequest> futureRequest = FutureBox.empty();
        final RequestBuilder requestBuilder = new RequestBuilder();

        database.child(ref.POST_REFERENCE).child(request_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DoneRequestModel rm = dataSnapshot.getValue(DoneRequestModel.class);
                requestBuilder.setModel(rm);

                FutureBox<User> futureAdmin = getUser(rm.getAdminUid());
                FutureBox<User> futureParticipant = getUser(rm.getParticipantUid());

                futureAdmin.whenFull(u-> requestBuilder.setAdmin(u));
                futureParticipant.whenFull(u -> requestBuilder.setParticipant(u));

                futureAdmin.also(futureParticipant).resolve(() -> {
                    futureRequest.give((DoneRequest) requestBuilder.build());
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { futureRequest.error("Error");}
        });

        return futureRequest;

    }

    private FutureBox<Map<Object,User>> getMapPositionUser(Map<String,String> vacants){
        List<FutureBox> fl = new LinkedList<>();

        final FutureBox<Map<Object,User>> fout = FutureBox.empty();
        final Map<Object,User> temp = new HashMap<>();

        vacants.forEach((k,v) -> {
            if (v.equals(EMPTY_VACANT)) return;
            FutureBox<User>  fu = getUser(v);
            fu.whenFull(u -> temp.put(k,u));
            fl.add(fu);

        });

        FutureBox.chain(fl).resolve(() -> {
            fout.give(temp);
        });

        return fout;
    }

    public FutureBox<AvailableProject> getAvailableProject(String project_id){

        FutureBox<AvailableProject> futureProject = FutureBox.empty();
        final ProjectBuilder projectBuilder = new ProjectBuilder();

        database.child(ref.POST_REFERENCE).child(project_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AvailableProjectModel pm = dataSnapshot.getValue(AvailableProjectModel.class);
                projectBuilder.setModel(pm);

                FutureBox<User> futureAdmin = getUser(pm.getAdminUid());
                futureAdmin.whenFull(admin-> projectBuilder.setAdmin(admin));

                Map<String,String> vacants = pm.getVacants();
                FutureBox<Map<Object,User>> futureVacants = getMapPositionUser(vacants);
                futureVacants.whenFull(m-> {
                    projectBuilder.setParticipants(m);

                });

                Collection<Object> positions = new LinkedList();
                vacants.forEach((k,v) -> positions.add(k));
                projectBuilder.setPositions(positions);

                futureAdmin.also(futureVacants).resolve(()->futureProject.give((AvailableProject) projectBuilder.build()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { futureProject.error("Error");}
        });

        return futureProject;

    }

    public FutureBox<OngoingProject> getOngoingProject(String project_id) {
        FutureBox<OngoingProject> futureProject = FutureBox.empty();
        final ProjectBuilder projectBuilder = new ProjectBuilder();

        database.child(ref.POST_REFERENCE).child(project_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                OngoingProjectModel pm = dataSnapshot.getValue(OngoingProjectModel.class);

                projectBuilder.setModel(pm);

                FutureBox<User> futureAdmin = getUser(pm.getAdminUid());
                futureAdmin.whenFull(admin-> projectBuilder.setAdmin(admin));

                Map<String,String> vacants = pm.getVacants();
                FutureBox<Map<Object,User>> futureVacants = getMapPositionUser(vacants);
                futureVacants.whenFull(m-> {
                    projectBuilder.setParticipants(m);
                });

                Collection<Object> positions = new LinkedList();
                vacants.forEach((k,v) -> positions.add(k));
                projectBuilder.setPositions(positions);

                futureAdmin.also(futureVacants).resolve(()->futureProject.give((OngoingProject) projectBuilder.build()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { futureProject.error("Error");}
        });

        return futureProject;
    }

    public FutureBox<DoneProject> getDoneProject(String project_id) {

        FutureBox<DoneProject> futureProject = FutureBox.empty();
        final ProjectBuilder projectBuilder = new ProjectBuilder();

        database.child(ref.POST_REFERENCE).child(project_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DoneProjectModel pm = dataSnapshot.getValue(DoneProjectModel.class);

                projectBuilder.setModel(pm);

                FutureBox<User> futureAdmin = getUser(pm.getAdminUid());
                futureAdmin.whenFull(admin-> projectBuilder.setAdmin(admin));

                Map<String,String> vacants = pm.getVacants();
                FutureBox<Map<Object,User>> futureVacants = getMapPositionUser(vacants);
                futureVacants.whenFull(m-> {
                    projectBuilder.setParticipants(m);

                });

                Collection<Object> positions = new LinkedList();
                vacants.forEach((k,v) -> positions.add(k));
                projectBuilder.setPositions(positions);

                futureAdmin.also(futureVacants).resolve(()->futureProject.give((DoneProject) projectBuilder.build()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) { futureProject.error("Error");}
        });

        return futureProject;

    }
    //=====================================================================================//
    //=====================================================================================//

    //== UPDATE ADMINISTRATING POSTS  ======================================================//
    //======================================================================================//

    private DatabaseReference getPostHashtagListReference(String post_id){
        return database.child(ref.POST_HASHTAGS_REFERENCE).child(post_id);
    }


    @Override
    public FutureResult updatePostHashtagList(Available post, List<Hashtag> hashtagList) {

        String post_id = post.getId();
        List<String> hashtagNameList = new LinkedList<>();

        for(Hashtag h : hashtagList){
            addHashtag(h);
            hashtagNameList.add(h.getHashtagName());
        }

        return setValueOnDatabaseReference(getPostHashtagListReference(post_id),hashtagNameList);

    }


    @Override
    public FutureResult updateAdministratingOffer(AvailableOffer availableOffer) {

        AvailableOfferModel offer = new AvailableOfferModel(availableOffer.getId(),availableOffer.getAdmin().getUid(),availableOffer.getLocation(),availableOffer.getTitle(),availableOffer.getDescription(), PostType.AVAILABLE_OFFER);
        return setValueOnDatabaseReference(getPostReference(availableOffer.getId()),offer);

    }

    @Override
    public FutureResult updateAdministratingRequest(AvailableRequest availableRequest) {

        AvailableRequestModel request = new AvailableRequestModel(availableRequest.getId(),availableRequest.getAdmin().getUid(),availableRequest.getLocation(),availableRequest.getTitle(),availableRequest.getDescription(), PostType.AVAILABLE_REQUEST);
        return setValueOnDatabaseReference(getPostReference(availableRequest.getId()),request);

    }

    @Override
    public FutureResult updateAdministratingProject(AvailableProject availableProject) {
        FutureResult res = FutureResult.empty();

        String id = availableProject.getId();
        String admin_uid = availableProject.getAdmin().getUid();
        Location location = availableProject.getLocation();
        String title = availableProject.getTitle();
        String description = availableProject.getDescription();

        Map<Object,User> vacants = availableProject.getPositionUsers();
        Map<String,String> vacantUid = new HashMap<>();

        vacants.forEach((k,v)-> {
            if(v != null){
                vacantUid.put(k.toString(), v.getUid());
            }else{
                vacantUid.put(k.toString(), EMPTY_VACANT);
            }
        });

        AvailableProjectModel project = new AvailableProjectModel(id,admin_uid,location,title,description, PostType.AVAILABLE_PROJECT,vacantUid);

        database.child(ref.POST_REFERENCE).child(id).setValue(project).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                res.success();
            }else{
                res.error("Could no be uploaded");
            }
        });

        return res;
    }

    @Override
    public FutureResult deleteAdministratingOffer(AvailableOffer availableOffer) {

        FutureResult res = FutureResult.empty();
        String avOfferId = availableOffer.getId();
        String uid = availableOffer.getAdmin().getUid();

//        return removeValueFromReference(database.child(ref.AVAILABLE_OFFERS).child(avOfferId)).
//                then((Supplier<FutureResult>) removeValueFromReference( database.child(ref.USER_OFFERS_ADMINISTRATING_REFERENCE).child(uid).child(avOfferId))).
//                        then((Supplier<FutureResult>) removeValueFromReference(database.child(ref.POST_REFERENCE).child(avOfferId)));


        database.child(ref.AVAILABLE_OFFERS).child(avOfferId).removeValue().addOnCompleteListener(task -> {
        if(task.isSuccessful()){
            database.child(ref.USER_OFFERS_ADMINISTRATING_REFERENCE).child(uid).child(avOfferId).removeValue().addOnCompleteListener(t2 ->{
                if(t2.isSuccessful()){
                    database.child(ref.POST_REFERENCE).child(avOfferId).removeValue().addOnCompleteListener(t3 -> {
                        if(t3.isSuccessful()){
                            res.success();
                        }else{
                            res.error();
                        }
                    });
                }else{
                    res.error();
                }
            });
        }else{
            res.error();
        } });

        return res;
    }

    @Override
    public FutureResult deleteAdministratingRequest(AvailableRequest availableRequest) {

        FutureResult res = FutureResult.empty();
        String avRequestId = availableRequest.getId();
        String uid = availableRequest.getAdmin().getUid();

        database.child(ref.AVAILABLE_REQUESTS).child(avRequestId).removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                database.child(ref.USER_REQUESTS_ADMINISTRATING_REFERENCE).child(uid).child(avRequestId).removeValue().addOnCompleteListener(t2 ->{
                    if(t2.isSuccessful()){
                        database.child(ref.POST_REFERENCE).child(avRequestId).removeValue().addOnCompleteListener(t3 -> {
                            if(t3.isSuccessful()){
                                res.success();
                            }else{
                                res.error();
                            }
                        });
                    }else{
                        res.error();
                    }
                });
            }else{
                res.error();
            } });

        return res;
    }

    @Override
    public FutureResult deleteAdministratingProject(AvailableProject availableProject) {

        FutureResult res = FutureResult.empty();
        String avProjectId = availableProject.getId();
        String uid = availableProject.getAdmin().getUid();

        database.child(ref.AVAILABLE_PROJECTS).child(avProjectId).removeValue().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                database.child(ref.USER_PROJECTS_ADMINISTRATING_REFERENCE).child(uid).child(avProjectId).removeValue().addOnCompleteListener(t2 ->{
                    if(t2.isSuccessful()){
                        database.child(ref.POST_REFERENCE).child(avProjectId).removeValue().addOnCompleteListener(t3 -> {
                            if(t3.isSuccessful()){
                                res.success();
                            }else{
                                res.error();
                            }
                        });
                    }else{
                        res.error();
                    }
                });
            }else{
                res.error();
            } });

        return res;
    }


    //======================================================================================//
    //======================================================================================//


    //== AVAILABLE TO ONGOING  //============================================================//
    //======================================================================================//




    @Override
    public FutureResult startOffer(AvailableOffer availableOffer, User participant) {

        String admin_uid = availableOffer.getAdmin().getUid();
        assert participant != null;
        Log.d("Inbox", "participant is null?" + (participant==null));
        String participant_uid = participant.getUid();
        String offer_id = availableOffer.getId();

        OngoingOfferModel offer = new OngoingOfferModel(offer_id,admin_uid,availableOffer.getLocation(),availableOffer.getTitle(),availableOffer.getDescription(), PostType.ONGOING_OFFER,participant_uid);

        //Removing the obsolete references to the available offer
        database.child(ref.USER_OFFERS_ADMINISTRATING_REFERENCE).child(admin_uid).child(offer_id).removeValue();
        database.child(ref.AVAILABLE_OFFERS).child(offer_id).removeValue();

        //New references to the onoging offer
        database.child(ref.POST_REFERENCE).child(offer_id).setValue(offer);
        database.child(ref.USER_ONGOING_OFFERS_REFERENCE).child(admin_uid).child(offer_id).setValue(true);
        database.child(ref.USER_ONGOING_OFFERS_REFERENCE).child(participant_uid).child(offer_id).setValue(true);

        sendAcceptanceMessageonOneParticipantPost(availableOffer,participant);


        return messageLogicFilledPost(availableOffer.getAdmin().getUid(), availableOffer.getId());
    }

    private void sendAcceptanceMessageonOneParticipantPost(Available av ,User participant){
        String m_id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(participant.getUid()));
        setValueOnDatabaseReference(database.child(ref.POST_PARTICIPATING_MESSAGE).child(av.getId()).child(participant.getUid()), m_id);
        sendSystemMessageWithId(m_id,new SystemMessage("Accepted","You are currently participating on " + av.getTitle(), participant, (Post) av, Message.NEW_MESSAGE));
    }

    private void sendAcceptanceMessageOnMultipleParticipantPost(AvailableProject p, User participant, String vacancy){
        String m_id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(participant.getUid()));
        setValueOnDatabaseReference(database.child(ref.POST_PARTICIPATING_MESSAGE).child(p.getId()).child(vacancy).child(participant.getUid()), m_id);
        sendSystemMessageWithId(m_id,new SystemMessage("Accepted","You are currently participating on " + p.getTitle() + "as " + vacancy, participant, p, Message.NEW_MESSAGE));
    }


    @Override
    public FutureResult startRequest(AvailableRequest availableRequest, User participant) {

        String admin_uid = availableRequest.getAdmin().getUid();
        String participant_uid = participant.getUid();
        String request_id = availableRequest.getId();

        OngoingRequestModel request = new OngoingRequestModel(request_id,admin_uid,availableRequest.getLocation(),availableRequest.getTitle(),availableRequest.getDescription(), PostType.ONGOING_REQUEST,participant_uid);

        //Remove the obsolete references to the available request
        database.child(ref.USER_REQUESTS_ADMINISTRATING_REFERENCE).child(admin_uid).child(request_id).removeValue();
        database.child(ref.AVAILABLE_REQUESTS).child(request_id).removeValue();

        //New references to the ongoing request
        database.child(ref.POST_REFERENCE).child(request_id).setValue(request);
        database.child(ref.USER_ONGOING_REQUESTS_REFERENCE).child(admin_uid).child(request_id).setValue(true);
        database.child(ref.USER_ONGOING_REQUESTS_REFERENCE).child(participant_uid).child(request_id).setValue(true);

        sendAcceptanceMessageonOneParticipantPost(availableRequest, participant);

        return messageLogicFilledPost(availableRequest.getAdmin().getUid(), availableRequest.getId());
    }

    @Override
    public FutureResult fillVacancyWithUser(AvailableProject availableProject, User participant, String vacancy){
        FutureResult res = FutureResult.empty();

        sendAcceptanceMessageOnMultipleParticipantPost(availableProject, participant, vacancy.toString());

        setValueOnDatabaseReference(database.child(ref.POST_REFERENCE).child(availableProject.getId()).child(ref.VACANTS_REFERENCE).child(vacancy.toString()),participant.getUid()).
                whenDone(() ->{
                    messageLogicFilledVacancy(availableProject.getAdmin().getUid(), availableProject.getId(), vacancy).whenDone(() ->{
                        /*
                        if(availableProject.getPositionUsers().size() == availableProject.getPositions().size() - 1){
                            availableProject.addParticipant(participant,vacancy);
                            this.AvailabletoOngoingProject(availableProject);
                        }jh
                        */
                        res.success();
                    }).ifFails(n -> res.error(n));
                }).ifFails(e -> res.error());

        return res;
    }


    @Override
    public FutureResult AvailabletoOngoingProject(AvailableProject availableProject) {

        String project_id = availableProject.getId();
        String admin_uid = availableProject.getAdmin().getUid();

        Map<Object,User> vacants = availableProject.getPositionUsers();
        Map<String,String> vacantsUid = new HashMap<>();

        vacants.forEach((k,v)->vacantsUid.put(k.toString(),v.getUid()));

        //Removing old references
        setValueOnDatabaseReference(getPostReference(project_id).child(ref.POST_CATEGORY), PostType.ONGOING_PROJECT);
        database.child(ref.USER_PROJECTS_ADMINISTRATING_REFERENCE).child(admin_uid).child(project_id).removeValue();
        database.child(ref.AVAILABLE_PROJECTS).child(project_id).removeValue();

        //New ongoing references
        database.child(ref.USER_ONGOING_PROJECTS_REFERENCE).child(admin_uid).child(project_id).setValue(true);
        vacantsUid.forEach((k,v) -> database.child(ref.USER_ONGOING_PROJECTS_REFERENCE).child(v).child(project_id).setValue(true));

        return FutureResult.ofSuccess();
    }

    //======================================================================================//
    //======================================================================================//


    //ONGOING TO DONE ======================================================================//
    //======================================================================================//


    @Override
    public FutureResult OngoingToDoneOffer(OngoingOffer ongoingOffer, String memoir) {

        String offer_id = ongoingOffer.getId();
        String admin_uid = ongoingOffer.getAdmin().getUid();
        String participant_uid = ongoingOffer.getParticipant().getUid();

        DoneOfferModel offer = new DoneOfferModel(offer_id,admin_uid,ongoingOffer.getLocation(),ongoingOffer.getTitle(),ongoingOffer.getDescription(), PostType.DONE_OFFER,participant_uid,memoir);

        //Remove references to ongoing offer
        database.child(ref.USER_ONGOING_OFFERS_REFERENCE).child(admin_uid).child(offer_id).removeValue();
        database.child(ref.USER_ONGOING_OFFERS_REFERENCE).child(participant_uid).child(offer_id).removeValue();

        //New references to done offer
        database.child(ref.POST_REFERENCE).child(offer_id).setValue(offer);
        database.child(ref.USER_DONE_OFFERS_REFERENCE).child(admin_uid).child(offer_id).setValue(true);
        database.child(ref.USER_DONE_OFFERS_REFERENCE).child(participant_uid).child(offer_id).setValue(true);

        return FutureResult.ofSuccess();
    }


    @Override
    public FutureResult OngoingToDoneRequest(OngoingRequest ongoingRequest, String memoir) {

        String request_id = ongoingRequest.getId();
        String admin_uid = ongoingRequest.getAdmin().getUid();
        String participant_uid = ongoingRequest.getParticipant().getUid();

        DoneRequestModel request = new DoneRequestModel(request_id,admin_uid,ongoingRequest.getLocation(),ongoingRequest.getTitle(),ongoingRequest.getDescription(), PostType.DONE_REQUEST,participant_uid,memoir);

        //Remove references to ongoing request
        database.child(ref.USER_ONGOING_REQUESTS_REFERENCE).child(admin_uid).child(request_id).removeValue();
        database.child(ref.USER_ONGOING_REQUESTS_REFERENCE).child(participant_uid).child(request_id).removeValue();

        //New references to done request
        database.child(ref.POST_REFERENCE).child(request_id).setValue(request);
        database.child(ref.USER_DONE_REQUESTS_REFERENCE).child(admin_uid).child(request_id).setValue(true);
        database.child(ref.USER_DONE_REQUESTS_REFERENCE).child(participant_uid).child(request_id).setValue(true);

        return FutureResult.ofSuccess();
    }


    @Override
    public FutureResult OngoingToDoneProject(OngoingProject ongoingProject, String memoir) {

        String project_id = ongoingProject.getId();
        String admin_uid = ongoingProject.getAdmin().getUid();
        Location location = ongoingProject.getLocation();
        String title = ongoingProject.getTitle();
        String description = ongoingProject.getDescription();

        Map<Object,User> vacants = ongoingProject.getPositionUsers();
        Map<String,String> vacantsUid = new HashMap<>();

        vacants.forEach((k,v)->vacantsUid.put(k.toString(),v.getUid()));

        DoneProjectModel project = new DoneProjectModel(project_id,admin_uid,location,title,description, PostType.DONE_PROJECT, vacantsUid, memoir);

        //Removing old references
        database.child(ref.USER_ONGOING_PROJECTS_REFERENCE).child(admin_uid).child(project_id).removeValue();
        vacantsUid.forEach((k,v) -> database.child(ref.USER_ONGOING_PROJECTS_REFERENCE).child(v).child(project_id).removeValue());

        //New ongoing references
        database.child(ref.POST_REFERENCE).child(project_id).setValue(project);
        database.child(ref.USER_DONE_PROJECTS_REFERENCE).child(admin_uid).child(project_id).setValue(true);
        vacantsUid.forEach((k,v) -> database.child(ref.USER_DONE_PROJECTS_REFERENCE).child(v).child(project_id).setValue(true));

        return FutureResult.ofSuccess();
    }


    //======================================================================================//
    //======================================================================================//


    //== LOOK FOR AVAILABLE POSTS  =========================================================//
    //======================================================================================//

    private boolean containsValue(String val, DataSnapshot d) {
        boolean b = false;
        for (DataSnapshot c: d.getChildren()) {
            if (c.getValue().toString().equals(val)) {
                b = true;
                break;
            }
        }

        return b;
    }

    @Override
    public FutureBox<List<Post>> readAvailableOffersFilteredByHashtag(Hashtag h){

        final FutureBox<List<Post>> fout = FutureBox.empty();

        database.child(ref.AVAILABLE_OFFERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FutureBox> l = new LinkedList<>();
                List<Post> listvOffers = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    fout.give(listvOffers);
                    return;
                }


                dataSnapshot.getChildren().forEach(c -> {
                    String oid = c.getKey();
                    database.child(ref.POST_HASHTAGS_REFERENCE)
                            .child(oid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (containsValue(h.getHashtagName(),dataSnapshot)) {
                                        FutureBox<AvailableOffer> foffer = getAvailableOffer(oid);
                                        l.add(foffer);
                                        foffer.whenFull(o -> {
                                            listvOffers.add(o);
                                            // cuando haya tantas ofertas cargadas como ofertas hay que cargar
                                            if (l.size() == listvOffers.size()) {
                                                FutureBox.chain(l).resolve(() -> fout.give(listvOffers));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    fout.error("Database error: " + databaseError);
                                }
                            });
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { fout.error("Database error");}
        });

        return fout;
    }

    @Override
    public FutureBox<List<Post>> readAvailableRequestsFilteredByHashtag(Hashtag h){

        final FutureBox<List<Post>> fout = FutureBox.empty();

        database.child(ref.AVAILABLE_REQUESTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FutureBox> l = new LinkedList<>();
                List<Post> listvOffers = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    fout.give(listvOffers);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    String oid = c.getKey();
                    database.child(ref.POST_HASHTAGS_REFERENCE)
                            .child(oid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (containsValue(h.getHashtagName(),dataSnapshot)) {
                                        FutureBox<AvailableRequest> foffer = getAvailableRequest(oid);
                                        l.add(foffer);
                                        foffer.whenFull(o -> {
                                            listvOffers.add(o);
                                            // cuando haya tantas ofertas cargadas como ofertas hay que cargar
                                            if (l.size() == listvOffers.size()) {
                                                FutureBox.chain(l).resolve(() -> fout.give(listvOffers));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    fout.error("Database error: " + databaseError);
                                }
                            });
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { fout.error("Database error");}
        });

        return fout;
    }

    @Override
    public FutureBox<List<Post>> readAvailableProjectsFilteredByHashtag(Hashtag h){

        final FutureBox<List<Post>> fout = FutureBox.empty();

        database.child(ref.AVAILABLE_PROJECTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FutureBox> l = new LinkedList<>();
                List<Post> listvOffers = new LinkedList<>();
                if (!dataSnapshot.hasChildren()) {
                    fout.give(listvOffers);
                    return;
                }


                dataSnapshot.getChildren().forEach(c -> {
                    String oid = c.getKey();
                    database.child(ref.POST_HASHTAGS_REFERENCE)
                            .child(oid)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (containsValue(h.getHashtagName(),dataSnapshot)) {
                                        FutureBox<AvailableProject> foffer = getAvailableProject(oid);
                                        l.add(foffer);
                                        foffer.whenFull(o -> {
                                            listvOffers.add(o);
                                            // cuando haya tantas ofertas cargadas como ofertas hay que cargar
                                            if (l.size() == listvOffers.size()) {
                                                FutureBox.chain(l).resolve(() -> fout.give(listvOffers));
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    fout.error("Database error: " + databaseError);
                                }
                            });
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { fout.error("Database error");}
        });

        return fout;
    }

    @Override
    public FutureBox<List<Post>> readAvailableOffers() {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.AVAILABLE_OFFERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();
                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    String offer_id = c.getKey();
                    FutureBox<AvailableOffer> futureavOffer = getAvailableOffer(offer_id);
                    l.add(futureavOffer);

                    futureavOffer.whenFull(avOffer -> {
                        list.add(avOffer);
                        if(l.size() == list.size()){FutureBox.chain(l).resolve(() -> futureList.give(list));}
                    });

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available offers");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readAvailableRequests() {

        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.AVAILABLE_REQUESTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    Log.d("Empty", "No children");
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<AvailableRequest> futureavRequest = getAvailableRequest(c.getKey());
                    l.add(futureavRequest);

                    futureavRequest.whenFull(avRequest -> {
                        list.add(avRequest);
                        if(l.size() == list.size())FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });

        return futureList;

    }

    @Override
    public FutureBox<List<Post>> readAvailableProjects() {

        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.AVAILABLE_PROJECTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<AvailableProject> futureavProject = getAvailableProject(c.getKey());
                    l.add(futureavProject);

                    futureavProject.whenFull(avProject -> {
                        list.add(avProject);
                        if(l.size() == list.size())FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });

        return futureList;
    }

    //======================================================================================//
    //======================================================================================//


    //== GETTING USER-POSTS  //=============================================================//
    //======================================================================================//


    @Override
    public FutureBox<Collection<Post>> readPostsAdministrating(String uid) {
        return null;
    }


    @Override
    public FutureBox<List<Post>> readAdministratingOffers(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_OFFERS_ADMINISTRATING_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                dataSnapshot.getChildren().forEach(c->{
                    FutureBox<AvailableOffer> futureavOffer = getAvailableOffer(c.getKey());
                    l.add(futureavOffer);
                    futureavOffer.whenFull(avOffer -> {
                        list.add(avOffer);
                        if(l.size() == list.size()) FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {futureList.error("Database error reading administrating offers from a user");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readAdministratingRequests(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_REQUESTS_ADMINISTRATING_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }


                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<AvailableRequest> futureavRequest = getAvailableRequest(c.getKey());
                    l.add(futureavRequest);
                    futureavRequest.whenFull(avRequest -> {
                        list.add(avRequest);
                        if(l.size() == list.size()) FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readAdministratingProjects(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_PROJECTS_ADMINISTRATING_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }


                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<AvailableProject> futureavProject = getAvailableProject(c.getKey());
                    l.add(futureavProject);
                    futureavProject.whenFull(avProject -> {
                        list.add(avProject);
                        if(l.size() == list.size()) FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });
        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readOngoingOffers(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_ONGOING_OFFERS_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c->{
                    FutureBox<OngoingOffer> futureonOffer = getOngoingOffer(c.getKey());
                    l.add(futureonOffer);
                    futureonOffer.whenFull(onOffer -> {
                        list.add(onOffer);
                        if(l.size() == list.size()){FutureBox.chain(l).resolve(() -> futureList.give(list));}
                    });

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {futureList.error("Database error reading administrating offers from a user");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readOngoingRequests(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_ONGOING_REQUESTS_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<OngoingRequest> futureonRequest = getOngoingRequest(c.getKey());
                    l.add(futureonRequest);
                    futureonRequest.whenFull(onRequest -> {
                        list.add(onRequest);
                        if(l.size() == list.size()) FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readOngoingProjects(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_ONGOING_PROJECTS_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<OngoingProject> futureonProject = getOngoingProject(c.getKey());
                    l.add(futureonProject);
                    futureonProject.whenFull(onProject -> {
                        list.add(onProject);
                        if(l.size() == list.size())FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readDoneOffers(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_DONE_OFFERS_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c->{
                    FutureBox<DoneOffer> futurednOffer = getDoneOffer(c.getKey());
                    l.add(futurednOffer);
                    futurednOffer.whenFull(dnOffer -> {
                        list.add(dnOffer);
                        if(l.size() == list.size()) FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {futureList.error("Database error reading administrating offers from a user");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readDoneRequests(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_DONE_REQUESTS_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<DoneRequest> futurednRequest = getDoneRequest(c.getKey());
                    l.add(futurednRequest);
                    futurednRequest.whenFull(dnRequest ->{
                        list.add(dnRequest);
                        if(l.size() == list.size()) FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });

        return futureList;
    }

    @Override
    public FutureBox<List<Post>> readDoneProjects(User user) {
        FutureBox<List<Post>> futureList = FutureBox.empty();

        database.child(ref.USER_DONE_PROJECTS_REFERENCE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Post> list = new LinkedList<>();
                List<FutureBox> l = new LinkedList<>();

                if (!dataSnapshot.hasChildren()) {
                    futureList.give(list);
                    return;
                }

                dataSnapshot.getChildren().forEach(c -> {
                    FutureBox<DoneProject> futurednProject = getDoneProject(c.getKey());
                    l.add(futurednProject);
                    futurednProject.whenFull(dnProject -> {
                        list.add(dnProject);
                        if(l.size() == list.size())FutureBox.chain(l).resolve(() -> futureList.give(list));
                    });

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError){futureList.error("Database error reading available requests");}
        });

        return futureList;
    }


    //======================================================================================//
    //======================================================================================//

    @Override
    public FutureBox<List<Hashtag>> readPostHashtagList(String postId) {
        FutureBox<List<Hashtag>> futureHashtagList = FutureBox.empty();
        List<Hashtag> l = new LinkedList<>();
        HashtagBuilder hashtagBuilder = new HashtagBuilder();

        readValueOfDatabaseReferenceWithSingleValueEvent(getPostHashtagListReference(postId)).whenFull(d -> {
                d.getChildren().forEach(c -> {
                hashtagBuilder.setModel(new HashtagModel((String) c.getValue()));
                l.add(hashtagBuilder.build());
            });
            futureHashtagList.give(l);
        }).onCancellation(e -> futureHashtagList.error(e));

        return futureHashtagList;
//
//        database.child(ref.POST_HASHTAGS_REFERENCE).child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                dataSnapshot.getChildren().forEach(c -> {
//                    hashtagBuilder.setModel(new HashtagModel((String) c.getValue()));
//                    l.add(hashtagBuilder.build());
//                });
//                futureHashtagList.give(l);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {futureHashtagList.error("Database error");}
//        });
//
//        return futureHashtagList;
    }


    //DATABASE-HASHTAG =====================================================================//
    //======================================================================================//

    private DatabaseReference getHashtagReference(){
        return database.child(ref.HASHTAGS_REFERENCE);
    }
    @Override
    public FutureResult addHashtag(Hashtag tag) {
        return setValueOnDatabaseReference(getHashtagReference().child(tag.getHashtagName()),true);
        //database.child(ref.HASHTAGS_REFERENCE).child(tag.getHashtagName()).setValue(true);
        //return FutureResult.ofSuccess();
    }


    @Override
    public FutureBox<Hashtag> getHashtag(String hashtagName) {

        FutureBox<Hashtag> futureHashtag = FutureBox.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(getHashtagReference()).whenFull(d -> {
            if(d.hasChild(hashtagName)){
                HashtagBuilder hBuilder = new HashtagBuilder();
                hBuilder.setModel(new HashtagModel(hashtagName));
                futureHashtag.give(hBuilder.build());
            }
        }).onCancellation(e -> futureHashtag.error(e));

        return futureHashtag;

//        database.child(ref.HASHTAGS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild(hashtagName)){
//                    HashtagBuilder hBuilder = new HashtagBuilder();
//                    hBuilder.setModel(new HashtagModel(hashtagName));
//                    futureHashtag.give(hBuilder.build());
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) { futureHashtag.error("Error"); }
//        });

 //       return futureHashtag;
    }

    @Override
    public FutureBox<List<Hashtag>> hashtagsBeginWith(String s) {

        FutureBox<List<Hashtag>> futureList = FutureBox.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.HASHTAGS_REFERENCE)).whenFull(d -> {
            List<Hashtag> hList = new LinkedList<>();
            HashtagBuilder hBuilder = new HashtagBuilder();

            d.getChildren().forEach(c -> {
                String hName = c.getKey();
                if(hName.startsWith(s)){
                    hBuilder.setModel(new HashtagModel(hName));
                    hList.add(hBuilder.build());
                }
            });

            if(!hList.isEmpty()) futureList.give(hList);
        }).onCancellation(e -> futureList.error(e));

        return futureList;
//        database.child(ref.HASHTAGS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<Hashtag> hList = new LinkedList<>();
//                HashtagBuilder hBuilder = new HashtagBuilder();
//
//                dataSnapshot.getChildren().forEach(c -> {
//                    String hName = c.getKey();
//                    if(hName.startsWith(s)){
//                        hBuilder.setModel(new HashtagModel(hName));
//                        hList.add(hBuilder.build());
//                    }
//                });
//
//                if(!hList.isEmpty()) futureList.give(hList);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {futureList.error("Database error finding hashtags staritng with a prefix");}
//        });
//
//        return futureList;
    }

    //======================================================================================//
    //======================================================================================//


    //== MESSAGES DATABASE =============================================================================================================//
    //==================================================================================================================================//


    /////////////////////////////////

    //// Aux functions

    private FutureResult addMessageToRecieverInbox(MessageModel messageModel){
        String reciever_id = messageModel.getRecieverId();
        String messageId = messageModel.getId();
        return setValueOnDatabaseReference(getMessageReference(reciever_id,messageId),messageModel);
    }


    private FutureBox<Boolean> isUserAskingForVacant(String uid, String postId, String vacancy){
        FutureBox<Boolean> b = FutureBox.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.USER_CONFIRMATION_ASKING_REFERENCE).child(uid).child(postId)).whenFull(d ->{
            if(d.hasChild(vacancy)) {b.give(true);}else{b.give(false);}
        }).onCancellation(r -> b.error(r.toString()));
        return b;
    }

    private FutureBox<Boolean> isUserAskingForPost(String uid, String postId){
        FutureBox<Boolean> b = FutureBox.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.USER_CONFIRMATION_ASKING_REFERENCE).child(uid)).whenFull(d -> {
            if(d.hasChild(postId)) {b.give(true);}else{b.give(false);}
        }).onCancellation(r -> b.error(r));
        return b;
    }


    private FutureResult addNoSenderNoVacantMessageToDatabase(Message message, MessageCategory messageCategory){
        FutureResult res = FutureResult.empty();
        String reciever_id = message.getTo().getUid();
        String id = genKey(getUserInboxReference(reciever_id));//database.child(ref.USER_INBOX_REFERENCE).child(reciever_id).push().getKey();
        MessageModel messageModel = new MessageModel(id, messageCategory,message.getTitle(), message.getText(), reciever_id, message.getContext().getId(), Message.NEW_MESSAGE);
        return addMessageToRecieverInbox(messageModel);

    }

    private FutureResult addNoSenderNoVacantMessageToDatabaseWithId(String id, Message message, MessageCategory messageCategory){
        FutureResult res = FutureResult.empty();
        String reciever_id = message.getTo().getUid();
        MessageModel messageModel = new MessageModel(id, messageCategory,message.getTitle(), message.getText(), reciever_id, message.getContext().getId(), Message.NEW_MESSAGE);
        return addMessageToRecieverInbox(messageModel);

    }

    private  FutureResult sendSystemMessageWithId(String id, Message message){
        return addNoSenderNoVacantMessageToDatabaseWithId(id, message, MessageCategory.SYSTEM_MESSAGE);
    }

    private DatabaseReference getMessageReference(String uid, String messageId){
        return getUserInboxReference(uid).child(messageId);
    }
    private DatabaseReference getMessageActionDoneReference(String uid, String messageId){
        return getMessageReference(uid,messageId).child(ref.MESSAGE_ACTION_DONE_REFERENCE);
    }

    private FutureResult setMessageState(String uid, String messageId, int state){
        return setValueOnDatabaseReference(getMessageActionDoneReference(uid,messageId),state);
    }


    /////

    //// Messages State

    @Override
    public FutureResult markMessageActionRead(String uid, String messageId) {
        FutureResult res = FutureResult.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.USER_INBOX_REFERENCE).child(uid).child(messageId).child(ref.MESSAGE_ACTION_DONE_REFERENCE)).whenFull(a ->{
            if(a.getValue(Integer.class) == Message.NEW_MESSAGE){
                setMessageState(uid,messageId, Message.READ_MESSAGE).whenDone(() -> res.success()).ifFails(er -> res.error(er));
            }
        }).onCancellation(e -> res.error());
        return res;
    }

    @Override
    public FutureResult markMessageActionDone(String uid, String messageId){
        return setMessageState(uid, messageId, Message.DONE_MESSAGE);
    }

    @Override
    public FutureResult markMessageAsCancelled(String uid, String messageId) {
        return setMessageState(uid, messageId, Message.CANCELLED_MESSAGE);
    }


    private FutureResult messageLogicFilledVacancy(String admin_uid, String postId, String vacancy) {
        FutureResult res = FutureResult.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.MESSAGE_PENDING_FOR_CONFIRMATION).child(admin_uid).child(postId).child(vacancy)).whenFull(d -> {
            d.getChildren().forEach(c -> {
                String reciever_message_id = c.getKey();
                markMessageActionDone(admin_uid, reciever_message_id);
                String sender_id = c.getValue(String.class);
                readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.USER_CONFIRMATION_ASKING_REFERENCE).child(sender_id).child(postId).child(vacancy)).whenFull(d2 ->{
                    markMessageAsCancelled(sender_id, d2.getValue(String.class));
                }).onCancellation(b -> res.error());
                res.success();
            });
        }).onCancellation(e -> res.error(e.toString()));
        return res;
    }


    private FutureResult messageLogicFilledPost(String admin_uid, String postId) {
        FutureResult res = FutureResult.empty();
        readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.MESSAGE_PENDING_FOR_CONFIRMATION).child(admin_uid).child(postId)).whenFull(d -> {
            d.getChildren().forEach(c -> {
                String reciever_message_id = c.getKey();
                String sender_id = c.getValue(String.class);
                markMessageActionDone(admin_uid, reciever_message_id);
                readValueOfDatabaseReferenceWithSingleValueEvent(database.child(ref.USER_CONFIRMATION_ASKING_REFERENCE).child(sender_id).child(postId)).whenFull(d2 ->{
                    markMessageAsCancelled(sender_id, d2.getValue(String.class));
                }).onCancellation(b -> res.error());
                res.success();
            });
        }).onCancellation(b2 -> res.error());

        return res;
    }

    /////////



    ////Send messages

    @Override
    public FutureResult sendUserMessage(UserMessage message) {

        FutureResult res = FutureResult.empty();

        String post_id = message.getContext().getId();
        String reciever_id = message.getTo().getUid();
        String sender_id = message.getFrom().getUid();
        String text = message.getText();
        String id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(reciever_id));

        MessageModel m = new MessageModel(id, MessageCategory.USER_MESSAGE, message.getTitle(), text, reciever_id, post_id, sender_id, Message.NEW_MESSAGE);

        return setValueOnDatabaseReference(database.child(ref.USER_INBOX_REFERENCE).child(reciever_id).child(id), m);
    }


    @Override
    public FutureResult sendPostConfirmMessage(String uid, PostConfirmMessage message) {
        final String SMT = "Application submitted";
        final String SMt = "Waiting for %s's response on %s";

        FutureResult res = FutureResult.empty();
        String postID = message.getContext().getId();

        isUserAskingForPost(uid,postID).whenFull(b -> {
            String text;
            if(b){
                res.error("You have already applied for this vacancy");
            }else{
                String reciever_id = message.getTo().getUid();
                String id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(reciever_id));
                String sm_id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(uid));
                text = String.format(SMt,message.getTo().getName(), message.getContext().getTitle());
                sendSystemMessageWithId(sm_id, new SystemMessage(SMT, text, message.getFrom(), message.getContext(), Message.NEW_MESSAGE));
                setValueOnDatabaseReference( database.child(ref.USER_CONFIRMATION_ASKING_REFERENCE).child(uid).child(postID), sm_id).whenDone(() -> {
                    MessageModel messageModel = new MessageModel(id, MessageCategory.POST_CONFIRM_MESSAGE,message.getTitle(), message.getText(), reciever_id, message.getContext().getId(), uid, Message.NEW_MESSAGE);
                    addMessageToRecieverInbox(messageModel).whenDone(()-> {
                        setValueOnDatabaseReference(database.child(ref.MESSAGE_PENDING_FOR_CONFIRMATION).child(reciever_id).child(postID).child(id), uid).whenDone(() -> res.success()).ifFails(er -> res.error());
                    }).ifFails(bool -> res.error(bool));

                });
            }
        }).onCancellation(e -> res.error(e.toString()));

        return res;
    }


    @Override
    public FutureResult sendVacantConfirmMessage(String uid, VacantConfirmMessage message){

        final String SMT = "Application submitted";
        final String SMt = "Waiting for %s's response on %s, to fill %s";

        FutureResult res = FutureResult.empty();

        String postID = message.getContext().getId();
        String vacancy = message.getVacant().toString();

        FutureBox<Boolean> isAskingForVacant = isUserAskingForVacant(uid,postID,vacancy);

        isAskingForVacant.whenFull(b ->{
            String text;
            if(b){
                res.error("You have already applied for this vacancy");
            }else{
                String reciever_id = message.getTo().getUid();
                String id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(reciever_id));

                String sm_id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(uid));

                text = String.format(SMt,message.getTo().getName(), message.getContext().getTitle(), message.getVacant().toString());
                sendSystemMessageWithId(sm_id, new SystemMessage(SMT, text, message.getFrom(), message.getContext(), Message.NEW_MESSAGE));

                setValueOnDatabaseReference(database.child(ref.USER_CONFIRMATION_ASKING_REFERENCE).child(uid).child(postID).child(vacancy), sm_id).whenDone(() -> {
                    MessageModel messageModel = new MessageModel(id, MessageCategory.VACANT_CONFIRM_MESSAGE,message.getTitle(), message.getText(), reciever_id, message.getContext().getId(), uid, Message.NEW_MESSAGE, ((VacantConfirmMessage) message).getVacant().toString());
                    addMessageToRecieverInbox(messageModel).whenDone(()-> {
                        setValueOnDatabaseReference(database.child(ref.MESSAGE_PENDING_FOR_CONFIRMATION).child(reciever_id).child(postID).child(vacancy).child(id), uid).whenDone(() -> res.success()).ifFails(er -> res.error());
                    }).ifFails(bool -> res.error(bool));


                }).ifFails(e -> res.error());
            }
        });

        return res;
    }

    @Override
    public FutureResult sendSystemMessage(SystemMessage message) {
        return addNoSenderNoVacantMessageToDatabase(message, MessageCategory.SYSTEM_MESSAGE);
    }


    @Override
    public FutureResult sendRateMessage(RateMessage message) {
        FutureResult res = FutureResult.empty();

        String post_id = message.getContext().getId();
        String reciever_id = message.getTo().getUid();
        String sender_id = message.getFrom().getUid();
        String text = message.getText();
        String id = genKey(database.child(ref.USER_INBOX_REFERENCE).child(reciever_id));

        MessageModel m = new MessageModel(id, MessageCategory.RATE_MESSAGE, message.getTitle(), text, reciever_id, post_id, sender_id, Message.NEW_MESSAGE);

        return setValueOnDatabaseReference(database.child(ref.USER_INBOX_REFERENCE).child(reciever_id).child(id), m);
    }

    ////////////////////////

    public FutureBox<Message> getMessage(MessageModel message){

        FutureBox<Message> futureMessage = FutureBox.empty();

        MessageBuilder b = new MessageBuilder();
        b.setModel(message);

        FutureBox<Post> futureContext = getPost(message.getPostId());
        futureContext.whenFull(c -> b.setContext(c));

        FutureBox<User> futureReciever = getUser(message.getRecieverId());
        futureReciever.whenFull(r-> b.setRecipient(r));

        String sender_id = message.getSenderId();

        if(sender_id == null){
            futureContext.also(futureReciever).resolve(() -> futureMessage.give(b.build()));
        }else{
            FutureBox<User> futureSender = getUser(sender_id);
            futureSender.whenFull(s -> b.setSender(s));
            futureContext.also(futureReciever).also(futureSender).resolve(()-> futureMessage.give(b.build()));
        }

       return futureMessage;
    }

    //======================================================================================//
    //======================================================================================//

    /// AUX Database functions====

    private String genKey(DatabaseReference ref){
        return ref.push().getKey();
    }

    private FutureResult removeValueFromReference(DatabaseReference reference){
        FutureResult res = FutureResult.empty();
        reference.removeValue().addOnCompleteListener(t -> {
            if(t.isSuccessful()){
                res.success();
            }else{
                res.error();
            }
        });
        return res;
    }

    private FutureResult setValueOnDatabaseReference(DatabaseReference reference, Object value){
        FutureResult res = FutureResult.empty();
        reference.setValue(value).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                res.success();
            }else{
                res.error();
            }
        });
        return res;
    }

    private FutureResult setValueOnDatabaseReference(String childPath, Object value){
        FutureResult res = FutureResult.empty();
        database.child(childPath).setValue(value).addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                res.success();
            }else{
                res.error();
            }
        });
        return res;
    }

    private FutureBox<DataSnapshot> readValueOfDatabaseReferenceWithSingleValueEvent(DatabaseReference reference){
        FutureBox<DataSnapshot> d = FutureBox.empty();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {d.give(dataSnapshot);}
            @Override
            public void onCancelled(DatabaseError databaseError) {d.error(databaseError);}
        });
        return d;
    }


    private FutureBox<DataSnapshot> readValueOfDatabaseReferenceWithValueEvent(DatabaseReference reference){
        FutureBox<DataSnapshot> d = FutureBox.empty();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {d.give(dataSnapshot);}
            @Override
            public void onCancelled(DatabaseError databaseError) {d.error(databaseError);}
        });
        return d;
    }


    private FutureBox<DataSnapshot> readValueOfDatabaseReference(String path){
        FutureBox<DataSnapshot> d = FutureBox.empty();
        database.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {d.give(dataSnapshot);}
            @Override
            public void onCancelled(DatabaseError databaseError) {d.error(databaseError);}
        });
        return d;
    }

    private DatabaseReference getUserReference(String uid){
        return database.child(ref.USER_REFERENCE).child(uid);
    }
    private DatabaseReference getUserNameReference(String uid){
        return getUserReference(uid).child(ref.USER_NAME_REFERENCE);
    }
    private DatabaseReference getUserImagePathReference(String uid){
        return getUserReference(uid).child(ref.USER_IMAGE_PATH_REFERENCE);
    }
    private DatabaseReference getUserDescriptionReference(String uid){
        return getUserReference(uid).child(ref.USER_DESCRIPTION_REFERENCE);
    }
    private DatabaseReference getUserRatingReference(String uid){
        return getUserReference(uid).child(ref.USER_RATING_REFERENCE);
    }
    private DatabaseReference getUserTimesRatedReference(String uid){
        return getUserReference(uid).child(ref.USER_TIMES_RATED);
    }
    private DatabaseReference getUserHashtagsReference(String uid){
        return database.child(ref.USER_HASHTAGS_REFERENCE).child(uid);
    }
    private DatabaseReference getUserMailReference(String uid){
        return getUserReference(uid).child(ref.USER_MAIL_REFERENCE);
    }
    private DatabaseReference getUserInboxReference(String uid){
        return database.child(ref.USER_INBOX_REFERENCE).child(uid);
    }
    ///===========================


}
