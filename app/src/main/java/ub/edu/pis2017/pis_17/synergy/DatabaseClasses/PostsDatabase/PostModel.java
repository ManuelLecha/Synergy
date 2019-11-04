package ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostsDatabase;

import android.location.Location;

import ub.edu.pis2017.pis_17.synergy.DatabaseClasses.PostType;
import ub.edu.pis2017.pis_17.synergy.LocationHelper;

public abstract class PostModel {

    protected String id;
    protected String adminUid;
    protected String location;
    protected String title;
    protected String description;
    protected PostType postType;

    public PostModel(String id, String adminUid, Location location, String title, String description, PostType postType) {
        this.id = id;
        this.adminUid = adminUid;

        this.location = LocationHelper.dump(location);
        this.title = title;
        this.description = description;
        this.postType = postType;
    }

    public PostModel(){

    }
    /**
     * Returns the unique id of the post
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Return the admin of the post
     * @return
     */
    public String getAdminUid() {
        return this.adminUid;
    }

    /**
     * Returns the location of the post
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the title of the post
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    public String getDescription() {return description;}

    public PostType getPostType() {
        return postType;
    }
}
