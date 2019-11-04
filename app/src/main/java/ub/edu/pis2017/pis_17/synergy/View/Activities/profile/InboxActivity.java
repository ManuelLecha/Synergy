package ub.edu.pis2017.pis_17.synergy.View.Activities.profile;

import android.app.FragmentManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Email;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.PostConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.RateMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.SystemMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.VacantConfirmMessage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.DoneRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Offer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.OngoingRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien.AvailableAlienOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien.AvailableAlienProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.alien.AvailableAlienRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.own.AvailableOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.own.AvailableOwnProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.availablePost.own.AvailableOwnRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.alien.DoneAlienOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.alien.DoneAlienProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.alien.DoneAlienRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.own.DoneOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.own.DoneOwnProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.donePost.own.DoneOwnRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.alien.OngoingAlienOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.alien.OngoingAlienProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.alien.OngoingAlienRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnOfferActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnProjectActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.post.ongoingPost.own.OngoingOwnRequestActivity;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.PostConfirmMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.RateMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost.StartAvailableOfferWithParticipantRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost.StartAvailablePostWithParticipantRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.Runnables.StartAvailablePost.StartAvailableRequestWithParticipantRunnable;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.SystemMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.UserMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Dialogs.VacantConfirmMessageDialogFragment;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Lists.MessageAdapters.MessageListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.UserMessage;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ParticipantAdapters.ParticipantListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.MessageListViewPage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableOffer;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableRequest;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;

import static ub.edu.pis2017.pis_17.synergy.Presenter.Presenter.getInstance;

/**
 * Created by manuellechasanchez on 24/03/2018.
 */


public class InboxActivity extends AppCompatActivity implements ProgressBarToggleable {

    private Toolbar toolbar;

    private ProgressBar progressBar;

    private ListView messageListView;
    private MessageListViewAdapter participantsListAdapter;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_layout);

        progressBar = (ProgressBar) findViewById(R.id.inbox_pgrsbar);
        messageListView = findViewById(R.id.inbox_lstvw_messages);

        participantsListAdapter = new MessageListViewAdapter(this);
        messageListView.setAdapter(participantsListAdapter);
        messageListView.setEmptyView(findViewById(R.id.emptyElement));

        FutureBox<User> futureUser = Presenter.getInstance().getCurrentUser();

        futureUser.whenFull(u -> {
            currentUser = u;
            loadMessages();
        });

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Message m = participantsListAdapter.getItem(position);

                // Before doing anything else, mark the message as read.
                Presenter.getInstance().markMessageAsRead(m).whenDone(() -> view.findViewById(R.id.messageListItem_readIndicator).setActivated(false));
                if(m.getActionDone() != 2) {
                    if(m instanceof PostConfirmMessage){
                        PostConfirmMessageDialogFragment dialogFragment = new PostConfirmMessageDialogFragment();

                        dialogFragment.setActivityClass(getActivityClass(m.getContext(), ((PostConfirmMessage) m).getContext().getAdmin(), currentUser));
                        assert m != null;
                        dialogFragment.setPostConfirmMessage((PostConfirmMessage) m);
                        dialogFragment.setProgressBarToggleable(InboxActivity.this);
                        Post post = m.getContext();
                        StartAvailablePostWithParticipantRunnable startPostRunnable = null;
                        if(post instanceof AvailableOffer) {
                            startPostRunnable = new StartAvailableOfferWithParticipantRunnable((AvailableOffer) post, InboxActivity.this, InboxActivity.this, dialogFragment, (PostConfirmMessage) m);
                        }
                        else if(post instanceof AvailableRequest) {
                            startPostRunnable = new StartAvailableRequestWithParticipantRunnable((AvailableRequest) post, InboxActivity.this, InboxActivity.this, dialogFragment, (PostConfirmMessage) m);
                        }
                        else {
                            //WTF
                            return;
                        }
                        dialogFragment.setStartAvailablePostWithParticipantRunnable(startPostRunnable);

                        FragmentManager fm = getFragmentManager();
                        dialogFragment.show(fm, "Dialog");
                    }else if(m instanceof VacantConfirmMessage){
                        VacantConfirmMessageDialogFragment dialogFragment = new VacantConfirmMessageDialogFragment();

                        dialogFragment.setVacantConfirmMessage((VacantConfirmMessage) m);
                        dialogFragment.setProgressBarToggleable(InboxActivity.this);
                        dialogFragment.setActivityClass(getActivityClass(m.getContext(), ((VacantConfirmMessage) m).getContext().getAdmin(), currentUser));
                        dialogFragment.setProject((AvailableProject)((VacantConfirmMessage) m).getContext());

                        FragmentManager fm = getFragmentManager();
                        dialogFragment.show(fm, "Dialog");
                    }else if(m instanceof SystemMessage){
                        SystemMessageDialogFragment dialogFragment = new SystemMessageDialogFragment();

                        dialogFragment.setSystemMessage((SystemMessage) m);
                        dialogFragment.setActivityClass(getActivityClass(m.getContext(), ((SystemMessage) m).getContext().getAdmin(), currentUser));

                        FragmentManager fm = getFragmentManager();
                        dialogFragment.show(fm, "Dialog");
                    }else if(m instanceof RateMessage){
                        RateMessageDialogFragment dialogFragment = new RateMessageDialogFragment();

                        dialogFragment.setRateMessage((RateMessage) m);
                        dialogFragment.setPostContext(((RateMessage) m).getContext());
                        dialogFragment.setProgressBarToggleable(InboxActivity.this);
                        dialogFragment.setActivityClass(getActivityClass(m.getContext(), ((RateMessage) m).getContext().getAdmin(), currentUser));
                        dialogFragment.setToUser(((RateMessage) m).getTo());

                        FragmentManager fm = getFragmentManager();
                        dialogFragment.show(fm, "Dialog");
                    }else if(m instanceof UserMessage){
                        UserMessageDialogFragment dialogFragment = new UserMessageDialogFragment();

                        dialogFragment.setUserMessage((UserMessage) m);
                        dialogFragment.setProgressBarToggleable(InboxActivity.this);
                        dialogFragment.setActivityClass(getActivityClass(m.getContext(), ((UserMessage) m).getContext().getAdmin(), currentUser));

                        FragmentManager fm = getFragmentManager();
                        dialogFragment.show(fm, "Dialog");
                    }

                }

            }
        });

        initToolBar();

    }

    public void loadMessages() {
        Presenter.getInstance().getUserInbox(currentUser).whenFull(inbox -> {
            inbox.forEach(m -> participantsListAdapter.add(m));
            participantsListAdapter.notifyDataSetChanged();
            Log.d("MESSAGES LOADED", String.valueOf(inbox.size()));
        });
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.inbox_layout_toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setToLoading() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void setToFree() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Nullable
    private Class getActivityClass(Post post, User admin, User watcher) {
        if(post instanceof AvailableOffer) {
            if(admin.getUid() == watcher.getUid()) {
                return AvailableOwnOfferActivity.class;
            }
            else {
                return AvailableAlienOfferActivity.class;
            }
        }
        else if(post instanceof AvailableRequest) {
            if(admin.getUid() == watcher.getUid()) {
                return AvailableOwnRequestActivity.class;
            }
            else {
                return AvailableAlienRequestActivity.class;
            }
        }
        else if(post instanceof AvailableProject) {
            if(admin.getUid() == watcher.getUid()) {
                return AvailableOwnProjectActivity.class;
            }
            else {
                return AvailableAlienProjectActivity.class;
            }
        }
        else if(post instanceof OngoingOffer) {
            if(admin.getUid() == watcher.getUid()) {
                return OngoingOwnOfferActivity.class;
            }
            else {
                return OngoingAlienOfferActivity.class;
            }
        }
        else if(post instanceof OngoingRequest) {
            if(admin.getUid() == watcher.getUid()) {
                return OngoingOwnRequestActivity.class;
            }
            else {
                return OngoingAlienRequestActivity.class;
            }
        }
        else if(post instanceof OngoingProject) {
            if(admin.getUid() == watcher.getUid()) {
                return OngoingOwnProjectActivity.class;
            }
            else {
                return OngoingAlienProjectActivity.class;
            }
        }
        else if(post instanceof DoneOffer) {
            if(admin.getUid() == watcher.getUid()) {
                return DoneOwnOfferActivity.class;
            }
            else {
                return DoneAlienOfferActivity.class;
            }
        }
        else if(post instanceof DoneRequest) {
            if(admin.getUid() == watcher.getUid()) {
                return DoneOwnRequestActivity.class;
            }
            else {
                return DoneAlienRequestActivity.class;
            }
        }
        else if(post instanceof DoneProject) {
            if(admin.getUid() == watcher.getUid()) {
                return DoneOwnProjectActivity.class;
            }
            else {
                return DoneAlienProjectActivity.class;
            }
        }
        return null;
    }

}

