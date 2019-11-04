package ub.edu.pis2017.pis_17.synergy.View.Activities.profile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;


import java.util.List;

import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Project;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Request;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.DefaultSortingState;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.ProximitySortingState;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.ScoreSortingState;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters.OfferListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters.ProjectListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters.RequestListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ActivityFragmentPagerAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.OfferListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.PostListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.ProjectListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.RequestListViewPage;

/**
 * Created by manuellechasanchez on 25/03/2018.
 */

public class HistoryActivity extends AppCompatActivity{
    public static final int OFFERS = 0, REQUESTS = 1, PROJECTS = 2;
    public static final int AVAILABLE = 0, ONGOING = 1, DONE = 2;

    private Toolbar toolbar;

    public int currentPosition;
    private int currentType;
    private PostListViewPage offerFragment;
    private PostListViewPage requestFragment;
    private PostListViewPage projectFragment;
    private ListViewAdapter offerListAdapter;
    private ListViewAdapter requestListAdapter;
    private ListViewAdapter projectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_layout);

        //
        initToolBar();

        //Prepare Toolbar
        setSupportActionBar(toolbar);

        //Set Component Listeners
        initPager();

        //You forgot this
        initTypeSpinner();

        this.currentType = AVAILABLE;

    }

    private void initPager() {
        ViewPager viewPager = findViewById(R.id.history_viewpgr);
        TabLayout tabLayout = findViewById(R.id.history_tablay);

        ActivityFragmentPagerAdapter pagerAdapter = new ActivityFragmentPagerAdapter(getSupportFragmentManager());
        //Create the fragments for the pagerAdapter
        offerFragment = new OfferListViewPage();
        requestFragment = new RequestListViewPage();
        projectFragment = new ProjectListViewPage();

        //Set the parent activity to the fragments
        offerFragment.setParentActivity(this);
        requestFragment.setParentActivity(this);
        projectFragment.setParentActivity(this);

        offerFragment.setOnRefreshListener(() -> refreshOfferList());
        requestFragment.setOnRefreshListener(() -> refreshRequestList());
        projectFragment.setOnRefreshListener(() -> refreshProjectList());
        //Create the listAdapters for each of the pagerAdapter fragments
        //Create the listAdapters for each of the pagerAdapter fragments
        offerListAdapter = new OfferListViewAdapter(this);
        requestListAdapter = new RequestListViewAdapter(this);
        projectListAdapter = new ProjectListViewAdapter(this);

        //Set these listAdapters to the pagerAdapter fragments
        offerFragment.setAdapter(offerListAdapter);
        requestFragment.setAdapter(requestListAdapter);
        projectFragment.setAdapter(projectListAdapter);

        //Add these fragments to the pagerAdapter
        pagerAdapter.addFragment(offerFragment, "Fragment Offer");
        pagerAdapter.addFragment(requestFragment, "Fragment Request");
        pagerAdapter.addFragment(projectFragment, "Fragment Project");

        //Set the pagerAdapter to the viewPager
        viewPager.setAdapter(pagerAdapter);

        //Set the initial current page
        PostListViewPage currentPostPage = (PostListViewPage) offerFragment;
        this.currentPosition = OFFERS;

        //Set Component Listeners
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                refreshList();
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        // load things
        refreshList();
    }

    private void refreshList() {
        switch (this.currentPosition) {
            case OFFERS:
                refreshOfferList();
                break;
            case REQUESTS:
                refreshRequestList();
                break;
            case PROJECTS:
                refreshRequestList();
                break;
            default:
                throw new UnknownError();
        }
    }

    private void refreshProjectList() {
        Presenter p = Presenter.getInstance();
        FutureBox<User> fu = p.getCurrentUser();
        FutureBox<List<Post>> fl = FutureBox.empty();
        switch (this.currentType) {
            case AVAILABLE:
                fl = fu.flatMap(p::readAdministratingProjects);
                break;
            case ONGOING:
                fl = fu.flatMap(p::readOngoingProjects);
                break;
            case DONE:
                fl = fu.flatMap(p::readDoneProjects);
                break;
            default:
                fl.error("Something went terribly wrong");
        }
        fl.whenFull(this::setProjectsList);
    }

    private void refreshRequestList() {
        Presenter p = Presenter.getInstance();
        FutureBox<User> fu = p.getCurrentUser();
        FutureBox<List<Post>> fl = FutureBox.empty();
        switch (this.currentType) {
            case AVAILABLE:
                fl = fu.flatMap(p::readAdministratingRequests);
                break;
            case ONGOING:
                fl = fu.flatMap(p::readOngoingRequests);
                break;
            case DONE:
                fl = fu.flatMap(p::readDoneRequests);
                break;
            default:
                fl.error("Something went terribly wrong");
        }
        fl.whenFull(this::setRequestsList);
    }

    private void refreshOfferList() {
        Presenter p = Presenter.getInstance();
        FutureBox<User> fu = p.getCurrentUser();
        FutureBox<List<Post>> fl = FutureBox.empty();
        switch (this.currentType) {
            case AVAILABLE:
                fl = fu.flatMap(p::readAdministratingOffers);
                break;
            case ONGOING:
                fl = fu.flatMap(p::readOngoingOffers);
                break;
            case DONE:
                fl = fu.flatMap(p::readDoneOffers);
                break;
            default:
                fl.error("Something went terribly wrong");
        }
        fl.whenFull(list -> {
            setOffersList(list);
            Log.d("LISTLENGTH", String.valueOf(list.size()));
        });
    }

    public void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.history_tlb);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }

    public void initTypeSpinner() {
        Spinner s = findViewById(R.id.mainActivity_sortingSpinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // intentionally redundant switch that helps debugging
                Log.d("PASTTYPE", String.valueOf(i));
                switch (i) {
                    case AVAILABLE:
                        Log.i("History", "Set filter to available");
                        Log.d("TYPE", "AVAILABLE");
                        currentType = AVAILABLE;
                        break;
                    case ONGOING:
                        Log.i("Sorting", "Set filter to ongoing");
                        Log.d("TYPE", "ONGOING");
                        currentType = ONGOING;
                        break;
                    case DONE:
                        Log.i("Sorting", "Set filter to done");
                        Log.d("TYPE", "DONE");
                        currentType = DONE;
                        break;
                    default:
                        Log.e("Sorting", "Uknown index " + i);
                }

                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
        currentType = AVAILABLE; // Always start at available
    }

    // Handle action bar item clicks here.
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings_history:
                SubMenu subMenu = item.getSubMenu();
                switch(subMenu.getItem().getItemId()) {
                    case R.id.menu_available_item:
                        this.currentType = AVAILABLE;
                        break;
                    case R.id.menu_ongoing_item:
                        this.currentType = ONGOING;
                        break;
                    case R.id.menu_done_item:
                        this.currentType = DONE;
                        break;
                }
                break;
        }
        refreshList();
        return super.onOptionsItemSelected(item);
    }
    */

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_history_activity, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setOffersList(List<Post> offers) {
        offerFragment.setRefreshing(false);
        offerListAdapter.clear();
        offerListAdapter.addAll(offers);
        offerListAdapter.notifyDataSetChanged();
    }

    public void setRequestsList(List<Post> requests) {
        requestFragment.setRefreshing(false);
        requestListAdapter.clear();
        requestListAdapter.addAll(requests);
        requestListAdapter.notifyDataSetChanged();
    }

    public void setProjectsList(List<Post> projects) {
        projectFragment.setRefreshing(false);
        projectListAdapter.clear();
        projectListAdapter.addAll(projects);
        projectListAdapter.notifyDataSetChanged();
    }

}
