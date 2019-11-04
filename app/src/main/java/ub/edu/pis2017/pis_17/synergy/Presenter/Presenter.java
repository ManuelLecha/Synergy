package ub.edu.pis2017.pis_17.synergy.Presenter;

import android.accounts.AuthenticatorException;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Interfaces.Available;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Presenter.Authentication.AuthInteractor;
import ub.edu.pis2017.pis_17.synergy.Presenter.Authentication.FirebaseAuthenticationInteractor;
import ub.edu.pis2017.pis_17.synergy.Presenter.RealTimeDatabase.FirebaseRealTimeDatabaseInteractor;
import ub.edu.pis2017.pis_17.synergy.Presenter.RealTimeDatabase.RealTimeDatabaseInteractor;
import ub.edu.pis2017.pis_17.synergy.Presenter.Storage.FirebaseStorageInteractor;
import ub.edu.pis2017.pis_17.synergy.Presenter.Storage.StorageInteractor;
import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Offer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Project;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.Model.FutureResult;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.R;

/**
 * Created by kanales on 24/04/2018.
 */

public class Presenter {

    // Eager instantitation: the app can't work without this!
    private static final Presenter ourInstance = new Presenter();

    private RealTimeDatabaseInteractor db;
    private StorageInteractor st;
    private AuthInteractor auth;
    private FirebaseImageLoader imgloader;

    private FutureBox<String> localUserID;
    private Drawable logoDrawable;

    static public Presenter getInstance() {
        return ourInstance;
    }

    private Presenter() {
        Log.d("DEBUG","Creating presenter");
        db = new FirebaseRealTimeDatabaseInteractor();//DummyRealTimeDatabaseInteractor(); // TODO change this in production
        st = new FirebaseStorageInteractor();
        auth = new FirebaseAuthenticationInteractor();
        imgloader = new FirebaseImageLoader();
        localUserID = FutureBox.empty();
    }

    /**
     * This method adds an image to a certain imageView
     * @param user
     * @param c
     * @param imageView
     */
    public void setUserImageToContext(User user, Activity c, ImageView imageView){
        DisplayMetrics dm = new DisplayMetrics();
        c.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float logicalDensity = dm.density;
        int dim  = (int) Math.ceil(c.getResources().getDimension(R.dimen.image_main_size) * logicalDensity);
        Glide.with(c)
                .using(imgloader)
                .load(st.getUserImageReference(user))
                .override(dim,dim)
                .centerCrop()
                .into(imageView);
    }

    //== SIGN-IN =========================================================================

    public FutureBox<String> createAccount(String name, Email mail, String password){
        FutureBox<String> futureUid = auth.createAccountWithMailAndPassword(mail, password);

        futureUid.whenFull(uid -> {
            db.initUser(uid,name,mail);
        });

        return futureUid;
    }

    //====================================================================================


    //== SIGN-OUT ========================================================================

    public void signOut() throws AuthenticatorException {
        Log.d("Login", "Logged out");
        auth.signOut();
        //this.localUserID = FutureBox.empty();
    }

    //====================================================================================

    //== RE-AUTH =========================================================================
    public FutureResult reauthenticateCurrentUser(String psw){
        return auth.reauthenticateCurrentUser(psw);
    }
    //====================================================================================

    //== LOG-IN ==========================================================================

    public FutureBox<User> logIn(Email mail, String password){
        return auth.logIn(mail,password).flatMap(this::getUser);
    }

    //====================================================================================


    //== GETTING USER INFO ===============================================================

    public FutureBox<User> getCurrentUser() {
        Optional<String> ouid = auth.getCurrentUserUid();
        if (ouid.isPresent()) {
            return getUser(ouid.get());
        } else {
            FutureBox<User> fout = FutureBox.empty();
            fout.error("Current user not found");
            return fout;
        }
    }

    public FutureBox<User> getUser(String uid) {
        FutureBox<User> usr = db.getUser(uid);
        return usr;
    }

    public FutureBox<List<Hashtag>> getUserHashtagList(User user){
        return db.readUserHashtagList(user.getUid());
    }

    public FutureBox<List<Message>> getUserInbox(User user){
        return db.readUserInbox(user.getUid());
    }

    public void unreadMessageCount(String uid){
        db.unReadMessagesCount(uid);
    }

    //====================================================================================



    //== USER INFO UPDATING===============================================================

    public FutureResult updateUserImage(User user, Uri image){
        final String TAG = "UpdatingImage";
        return st.addNewUserImage(user, image).flatMapR(imagePath -> db.updateUserimagePath(user.getUid(), imagePath)).setDefaultError(user.getMail().toString() + " image could not be updated");
    }

    public FutureResult updateUserHashtagList(String uid, List<Hashtag> l){
        return db.updateUserHashtagList(uid, l);
    }

    public FutureResult updateUserDescription(String uid, String description){
        return db.updateUserDescription(uid, description);
    }

    public FutureResult updateUserName(String uid, String name) {
        return db.updateUserName(uid, name);
    }

    public FutureResult updateCurrentUserEmail(String uid, Email newEmail){
        FutureResult res = FutureResult.empty();
        auth.updateEmail(newEmail).whenDone(() -> db.updateUserEmail(uid, newEmail).whenDone(() -> res.success()).ifFails( e -> res.error("Mail could not be updated")));
        return res;
    }

    public FutureResult updateCurretUserPassword(String password){
        return auth.updatePassword(password);
    }

    public FutureResult rateUser(String uid, int rate){
        return db.updateUserRating(uid, rate);
    }

    //====================================================================================



    //== CREATING POSTS ===============================================================

    public FutureBox<String> createOffer(Offer offer) {
        return db.addNewOffer(offer.getAdmin(), offer.getLocation(), offer.getTitle(), offer.getDescription());
    }

    public FutureBox<String> createRequest(Request request) {
        return db.addNewRequest(request.getAdmin(), request.getLocation(), request.getTitle(), request.getDescription());
    }

    public FutureBox<String> createProject(Project project) {
        return db.addNewProject(project.getAdmin(), project.getLocation(), project.getTitle(), project.getDescription(),project.getPositions());
    }

    //====================================================================================



    //== GETTING POST INFO ===============================================================

    public FutureBox<List<Hashtag>> getPostHashtagList(Post post){
        return db.readPostHashtagList(post.getId());
    }

    public FutureBox<List<Hashtag>> getPostHashtagList(String postId){
        return db.readPostHashtagList(postId);
    }

    //====================================================================================

    //== UPDATING ADMINISTRATING POST ====================================================

    public FutureResult deleteAdministratingOffer(AvailableOffer avOffer){
        return db.deleteAdministratingOffer(avOffer);
    }

    public FutureResult deleteAdministratingRequest(AvailableRequest avRequest){
        return db.deleteAdministratingRequest(avRequest);
    }

    public FutureResult deleteAdministratingProject(AvailableProject avProject){
        return db.deleteAdministratingProject(avProject);
    }

    public FutureResult updateAdministratingOffer(AvailableOffer avOffer){
        return db.updateAdministratingOffer(avOffer);
    }

    public FutureResult updateAdministratingRequest(AvailableRequest avRequest){
        return db.updateAdministratingRequest(avRequest);
    }

    public FutureResult updateAdministratingProject(AvailableProject avProject){
        return db.updateAdministratingProject(avProject);
    }

    public FutureResult updatePostHashtagList(Available post, List<Hashtag> hashtagList){
        return db.updatePostHashtagList(post, hashtagList);
    }

    public FutureResult fillVacancyWithUser(AvailableProject p, User participant, String vacancy){
        return db.fillVacancyWithUser(p,participant,vacancy);
    }

    //====================================================================================

    //== CHANGING POST STATE ===============================================================

    public FutureResult startOffer(AvailableOffer offer, User user) {
        return db.startOffer(offer, user);
    }

    public FutureResult startRequest(AvailableRequest request, User user) {
        return db.startRequest(request, user);
    }

    public FutureResult availableToOngoingProject(AvailableProject project) {
        return db.AvailabletoOngoingProject(project);
    }

    public FutureResult finishOngoingOffer(OngoingOffer offer, String memoir) {
        return db.OngoingToDoneOffer(offer, memoir);
    }

    public FutureResult finishOngoingRequest(OngoingRequest request, String memoir) {
        return db.OngoingToDoneRequest(request, memoir);
    }

    public FutureResult finishOngoingProject(OngoingProject project, String memoir) {
        return db.OngoingToDoneProject(project, memoir);
    }

    //====================================================================================



    //== MESSAGES ========================================================================

    public FutureResult sendPostConfirmMessage(PostConfirmMessage message){
        return db.sendPostConfirmMessage(message.getFrom().getUid(), message);
    }

    public FutureResult sendVacantConfirmMessage(VacantConfirmMessage message) {
        return db.sendVacantConfirmMessage(message.getFrom().getUid(), message);
    }

    public FutureResult sendUserMessage(UserMessage message) {
        return db.sendUserMessage(message);
    }

    public FutureResult sendSystemMessage(SystemMessage message) {
        return db.sendSystemMessage(message);
    }

    public FutureResult sendRateMessage(RateMessage message) {
        return db.sendRateMessage(message);
    }

    public FutureResult markMessageAsDone(Message m){return db.markMessageActionDone(m.getTo().getUid(), m.getId());}

    public FutureResult markMessageAsRead(Message m){return db.markMessageActionRead(m.getTo().getUid(),m.getId());}




    //====================================================================================


    // GETTING AVAILABLE POSTS ===========================================================

    private List<Post> filterRadius(List<Post> p, Float radius, Location loc, User u) {
        return p.stream()
                .filter(o -> o.getLocation().distanceTo(loc) < radius)
                .filter(o -> !o.getAdmin().equals(u))
                .collect(Collectors.toList());
    }

    public FutureBox<List<Post>> readAvailableOffersInARadius(Float radius, Location loc, User u){
        return db.readAvailableOffers().map(ol -> filterRadius(ol,radius,loc,u));
    }

    public FutureBox<List<Post>> readAvailableRequestsInARadius(Float radius, Location loc, User u){
        return db.readAvailableRequests().map(rl -> filterRadius(rl,radius,loc,u));
    }

    public FutureBox<List<Post>> readAvailableProjectsInARadius(Float radius, Location loc, User u){
        return db.readAvailableProjects().map(rl -> filterRadius(rl,radius,loc,u));
    }

    public FutureBox<List<Post>> readAvailableProjectsFilteredByHashtag(Hashtag h, Float radius, Location loc, User u){
        return db.readAvailableProjectsFilteredByHashtag(h).map(l -> filterRadius(l,radius,loc,u));
    }


    public FutureBox<List<Post>> readAvailableRequestsFilteredByHashtag(Hashtag h, Float radius, Location loc, User u){
        return db.readAvailableRequestsFilteredByHashtag(h).map(l -> filterRadius(l,radius,loc,u));
    }


    public FutureBox<List<Post>> readAvailableOffersFilteredByHashtag(Hashtag h, Float radius, Location loc, User u){
        return db.readAvailableOffersFilteredByHashtag(h).map(l -> {
            Log.d("Search", "Filtering by distance " + l);
            return filterRadius(l,radius,loc,u);
        });
    }

    //====================================================================================

    public FutureBox<Post> getPost(String postId) {
        return db.getPost(postId);
    }


    // ====================
    /**
     * Reads the list of ongoing offers of a user
     * @param user
     */
    public FutureBox<List<Post>> readOngoingOffers(User user) {
        return db.readOngoingOffers(user);
    }

    /**
     * Reads the list of offers that a user is administrating
     * @param user
     */
    public FutureBox<List<Post>> readAdministratingOffers(User user) {
        return db.readAdministratingOffers(user);
    }

    /**
     * Reads the list of requests that a user is administrating
     * @param user
     */
    public FutureBox<List<Post>> readAdministratingRequests(User user) {
        return db.readAdministratingRequests(user);
    }

    /**
     * Reads the list of projects that a user is administrating
     * @param user
     */
    public FutureBox<List<Post>> readAdministratingProjects(User user) {
        return db.readAdministratingProjects(user);
    }

    /**
     * Reads the list of ongoing requests of a user
     * @param user
     */
    public FutureBox<List<Post>> readOngoingRequests(User user) {
        return db.readOngoingRequests(user);
    }

    /**
     * Reads the list of ongoing projects of a user
     * @param user
     */
    public FutureBox<List<Post>> readOngoingProjects(User user) {
        return db.readOngoingProjects(user);
    }

    /**
     * Reads the list od done offers of a user
     * @param user
     */
    public FutureBox<List<Post>> readDoneOffers(User user) {
        return db.readDoneOffers(user);
    }

    /**
     * Reads the list of done requests of a user
     * @param user
     */
    public FutureBox<List<Post>> readDoneRequests(User user) {
        return db.readDoneRequests(user);
    }

    /**
     * Reads the list of done projects of a user
     * @param user
     */
    public FutureBox<List<Post>> readDoneProjects(User user) {
        return db.readDoneProjects(user);
    }
}
