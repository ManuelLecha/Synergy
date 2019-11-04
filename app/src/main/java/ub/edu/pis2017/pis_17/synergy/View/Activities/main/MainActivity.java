package ub.edu.pis2017.pis_17.synergy.View.Activities.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Debug;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.UnreadMessageNumber;
import ub.edu.pis2017.pis_17.synergy.FutureBox;
import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.AvailableProject;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.Model.User;
import ub.edu.pis2017.pis_17.synergy.Presenter.Presenter;
import ub.edu.pis2017.pis_17.synergy.View.Activities.ProgressBarToggleable;
import ub.edu.pis2017.pis_17.synergy.View.Hashtags.HashtagShower;
import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters.OfferListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters.ProjectListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Lists.PostAdapters.RequestListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.OfferListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.PostListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ActivityFragmentPagerAdapter;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.ProjectListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages.RequestListViewPage;
import ub.edu.pis2017.pis_17.synergy.R;

import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.UserProfileActivity;

public class MainActivity extends AppCompatActivity implements LocationListener {
    protected static Typeface montserrat;
    private static final int REQUEST_LOCATION = 0;
    public static final int OFFERS = 0, REQUESTS = 1, PROJECTS = 2;
    private static final int PROXIMITY = 0, RATING = 1; // ya sé que es una practica horrible pero no se como hacerlo mejor
    public static final String SELECT_TAB = "selectTab";

    private ViewPager viewPager;
    private ActivityFragmentPagerAdapter pagerAdapter;
    private PostListViewPage currentPostPage;

    private ListViewAdapter offerListAdapter;
    private ListViewAdapter requestListAdapter;
    private ListViewAdapter projectListAdapter;

    private static Location lastKnownLocation;

    private EditText searchEdtxt;

    private SortingState state;
    private PostListViewPage offerFragment;
    private PostListViewPage requestFragment;
    private PostListViewPage projectFragment;
    private int currentPosition;
    public static TextView notification;

    private List<Hashtag> currentHashtags;

    public static boolean isActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d("DEBUG", "Creating Main Activity...");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        notification = findViewById(R.id.main_activity_notification_badget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_layout_tlb);
        TextView title = findViewById(R.id.title);
        title.setTypeface(getMontserrat());

        //Prepare Toolbar
        setSupportActionBar(toolbar);

        initPager();

        //Prepare floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_layout_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPostPage.createPost();
            }
        });

        initProfileButton();

        this.state = new DefaultSortingState();

        //// LOCATIONS
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initLocation();
        initSortingSpinner();

        currentHashtags = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
        Log.d("DEBUG", "Starting Main Activity...");
        String tag = getIntent().getStringExtra(HashtagShower.SEARCH_HASHTAG);
        currentPosition = getIntent().getIntExtra("PAGE_POSITION", 0);
        viewPager.setCurrentItem(currentPosition);
        currentPostPage = (PostListViewPage) pagerAdapter.getItem(currentPosition);
        this.currentHashtags.clear();
        if (tag != null) {
            this.currentHashtags.add(new Hashtag(tag));
        }
        switch (currentPosition) {
            case OFFERS:
                Log.d("DEBUG", "Initially refreshing Offers...");
                refreshOfferList();
                break;
            case REQUESTS:
                Log.d("DEBUG", "Initially refreshing Requests...");
                refreshRequestList();
                break;
            case PROJECTS:
                Log.d("DEBUG", "Initially refreshing Projects...");
                refreshProjectList();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }


    public static void setNotificationText(){
        int x = UnreadMessageNumber.getInstance().getUnreadNumber();
        if (x > 0) {
            notification.setVisibility(View.VISIBLE);
            notification.setText(Integer.toString(x));
        } else {
            notification.setVisibility(View.GONE);
        }
    }

    private void initProfileButton() {
        //Prepare profile button
        ImageView profileButton = findViewById(R.id.main_layout_profile_imgbtn);

        FutureBox<User> fu = Presenter.getInstance().getCurrentUser();

        fu.whenFull(u -> {
            Presenter.getInstance().setUserImageToContext(u, MainActivity.this, profileButton);
            setNotificationText();
        });

        //TODO añadir imagen del user en el profileButton
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(i);
            }
        });

    }

    private void initPager() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_layout_tablay);
        //Prepare PagerAdapter with listViewFragments
        viewPager = (ViewPager) findViewById(R.id.main_layout_viewpgr);

        //Create the pagerAdapter
        pagerAdapter = new ActivityFragmentPagerAdapter(getSupportFragmentManager());

        //Create the fragments for the pagerAdapter
        offerFragment = new OfferListViewPage();
        requestFragment = new RequestListViewPage();
        projectFragment = new ProjectListViewPage();

        offerFragment.setOnRefreshListener(() -> refreshOfferList());
        requestFragment.setOnRefreshListener(() -> refreshRequestList());
        projectFragment.setOnRefreshListener(() -> refreshProjectList());
        //Create the listAdapters for each of the pagerAdapter fragments
        offerListAdapter = new OfferListViewAdapter(this);
        requestListAdapter = new RequestListViewAdapter(this);
        projectListAdapter = new ProjectListViewAdapter(this);

        //Set these listAdapters to the pagerAdapter fragments
        offerFragment.setAdapter(offerListAdapter);
        requestFragment.setAdapter(requestListAdapter);
        projectFragment.setAdapter(projectListAdapter);

        //Set the parent activity to the fragments
        offerFragment.setParentActivity(this);
        requestFragment.setParentActivity(this);
        projectFragment.setParentActivity(this);

        //Add these fragments to the pagerAdapter
        pagerAdapter.addFragment(offerFragment, "Fragment Offer");
        pagerAdapter.addFragment(requestFragment, "Fragment Request");
        pagerAdapter.addFragment(projectFragment, "Fragment Project");

        //Set the pagerAdapter to the viewPager
        viewPager.setAdapter(pagerAdapter);

        //Set the initial current page
        currentPostPage = (PostListViewPage) offerFragment;



        //Set Component Listeners
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                currentPostPage = (PostListViewPage) pagerAdapter.getItem(position);
                switch (position) {
                    case OFFERS:
                        refreshOfferList();
                        return;
                    case REQUESTS:
                        refreshRequestList();
                        return;
                    case PROJECTS:
                        refreshProjectList();
                        return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        SearchView sv = (SearchView) (menu.getItem(0).getActionView());
        //Menu m = menu.getItem(1).getSubMenu();
        searchEdtxt = (EditText) sv.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEdtxt.setTextColor(getResources().getColor((R.color.primaryTextColor)));
        searchEdtxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.i("SEARCH","serach1");
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("SEARCH","serach2");
                    currentHashtags = Hashtag.parseString(v.getText().toString());
                    searchEdtxt.setText(Hashtag.printHashtags(currentHashtags));
                    Log.d("DEBUG", String.valueOf(currentHashtags.size()));
                    switch (currentPosition) {
                        case OFFERS:
                            offerFragment.setRefreshing(true);
                            refreshOfferList();
                            break;
                        case REQUESTS:
                            requestFragment.setRefreshing(true);
                            refreshRequestList();
                            break;
                        case PROJECTS:
                            projectFragment.setRefreshing(true);
                            refreshProjectList();
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    public void initSortingSpinner() {
        Spinner s = findViewById(R.id.mainActivity_sortingSpinner);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (lastKnownLocation == null) {
                    state = new DefaultSortingState();
                    return;
                }
                switch (i) {
                    case PROXIMITY:
                        Log.i("Sorting", "proximity");
                        state = new ProximitySortingState(lastKnownLocation);
                        break;
                    case RATING:
                        Log.i("Sorting", "score");
                        state = new ScoreSortingState();
                        break;
                    default:
                        Log.e("Sorting", "Uknown index " + i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (lastKnownLocation == null) {
                    state = new DefaultSortingState();
                    return;
                }
                state = new ProximitySortingState(lastKnownLocation);
            }
        });
        state = new DefaultSortingState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_main:
                Log.d("SEARCHSELECTED", "SEARCHSELECTED");
                searchEdtxt.setText(Hashtag.printHashtags(currentHashtags));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        /*Do nothing to prevent going back to login activity*/
        //TODO destroy all activities before this one so that this method won't need to exist
    }

    /******************************************************
     * PRIVATE METHODS
     ******************************************************/

    private void refreshOfferList() {
        if (lastKnownLocation == null) {
            setOffersList(new LinkedList<>());
            // try again sweety
            return;
        }
        offerFragment.setRefreshing(true);

        Presenter p = Presenter.getInstance();
        float radius = Float.POSITIVE_INFINITY;

        FutureBox<User> currentUserBox = p.getCurrentUser();
        FutureBox<List<Post>> postBox;

        if(currentHashtags.isEmpty()) {
            postBox = currentUserBox.flatMap(user -> p.readAvailableOffersInARadius(radius, this.lastKnownLocation, user));
        } else {
            Hashtag h = currentHashtags.get(0);
            postBox = currentUserBox.flatMap(user -> p.readAvailableOffersFilteredByHashtag(h, radius, this.lastKnownLocation, user));
        }

        postBox.whenFull(list -> {
            setOffersList(list);
            offerFragment.setRefreshing(false);
        }).onCancellation(r -> {
            offerFragment.setRefreshing(false);
        });
    }

    private void refreshRequestList() {
        if (lastKnownLocation == null) {
            setRequestsList(new LinkedList<>());
            // try again sweety
            return;
        }
        requestFragment.setRefreshing(true);
        Presenter p = Presenter.getInstance();
        float radius = Float.POSITIVE_INFINITY;
        FutureBox<User> currentUserBox = p.getCurrentUser();
        FutureBox<List<Post>> postBox;

        if(currentHashtags.isEmpty()) {
            postBox = currentUserBox.flatMap(user -> p.readAvailableRequestsInARadius(radius, this.lastKnownLocation, user));
        }
        else {
            Hashtag h = currentHashtags.get(0);
            postBox = currentUserBox.flatMap(user -> p.readAvailableRequestsFilteredByHashtag(h, radius, this.lastKnownLocation, user));
        }
        postBox.whenFull(list -> {
            setRequestsList(list);
            requestFragment.setRefreshing(false);
        }).onCancellation(r -> {
            requestFragment.setRefreshing(false);
        });
    }

    private void refreshProjectList() {
        if (lastKnownLocation == null) {
            setProjectsList(new LinkedList<>());
            // try again sweety
            return;
        }
        projectFragment.setRefreshing(true);

        Presenter p = Presenter.getInstance();
        float radius = Float.POSITIVE_INFINITY;
        FutureBox<User> currentUserBox = p.getCurrentUser();
        FutureBox<List<Post>> postBox;

        if(currentHashtags.isEmpty()) {
            postBox = currentUserBox.flatMap(user -> p.readAvailableProjectsInARadius(radius, this.lastKnownLocation, user));
        }
        else {
            Hashtag h = currentHashtags.get(0);
            postBox = currentUserBox.flatMap(user -> p.readAvailableProjectsFilteredByHashtag(h, radius, this.lastKnownLocation, user));
        }
        postBox.whenFull(list -> {
            setProjectsList(list);
            projectFragment.setRefreshing(false);
        }).onCancellation(e -> projectFragment.setRefreshing(false));

    }

    public void setOffersList(List<Post> offers) {
        Log.d("Search", "Setting offers: " + offers.toString());
        offerFragment.setRefreshing(false);
        offerListAdapter.clear();
        state.sort(offers);
        offerListAdapter.addAll(offers);
        offerListAdapter.notifyDataSetChanged();
        offerFragment.invalidateViews();
    }

    public void setRequestsList(List<Post> requests) {
        Log.i("Sorting",state.getClass().toString());
        requestFragment.setRefreshing(false);
        requestListAdapter.clear();
        state.sort(requests);
        requestListAdapter.addAll(requests);
        requestListAdapter.notifyDataSetChanged();
        requestFragment.invalidateViews();
    }

    public void setProjectsList(List<Post> projects) {
        projectFragment.setRefreshing(false);
        projectListAdapter.clear();
        state.sort(projects);
        projectListAdapter.addAll(projects);
        projectListAdapter.notifyDataSetChanged();
        projectFragment.invalidateViews();
    }

    /*private Location getLocation() {
        // TODO: 12/05/2018 poner esto real
        Location loc = new Location("");
        loc.setAltitude(41.387152);
        loc.setLatitude(2.163691);
        return loc;
    }*/

    private void initLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("LOCATION","No permissions");
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    public static Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null) return;

        Log.i("Location",location.toString());
        lastKnownLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    protected Typeface getMontserrat() {
        if (montserrat == null) {
            montserrat = Typeface.createFromAsset(getAssets(),"font/Montserrat-Medium.ttf");
        }

        return montserrat;
    }
}
