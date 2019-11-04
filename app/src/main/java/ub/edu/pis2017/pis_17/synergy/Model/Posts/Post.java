package ub.edu.pis2017.pis_17.synergy.Model.Posts;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import ub.edu.pis2017.pis_17.synergy.Model.Labelable;
import ub.edu.pis2017.pis_17.synergy.Model.User;

/**
 * Created by manuellechasanchez on 10/04/2018.
 */

public abstract class Post implements Labelable {
    private String id;
    private User admin;
    private Location location;
    private String title;
    private String description;

    public Post(String id, @NonNull User admin, Location location, String title, String description) {
        this.id = id;
        this.admin = admin;
        this.location = location;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", admin='" + admin.toString() +
                '}';
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
    public User getAdmin() {
        return this.admin;
    }

    /**
     * Tests wether a certain user has created a post
     * @param u
     * @return
     */
    public boolean isAdmin(User u) {
        return u.getUid().equals(this.admin);
    }

    /**
     * Returns the location of the post
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the title of the post
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the post
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param id
     * @return
     */
    public Post setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @param admin
     * @return
     */
    public Post setAdmin(User admin) {
        this.admin = admin;
        return this;
    }

    /**
     * @param location
     * @return
     */
    public Post setLocation(Location location) {
        this.location = location;
        return this;
    }

    /**
     * @param title
     * @return
     */
    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @param description
     * @return
     */
    public Post setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return id != null ? id.equals(post.id) : post.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
