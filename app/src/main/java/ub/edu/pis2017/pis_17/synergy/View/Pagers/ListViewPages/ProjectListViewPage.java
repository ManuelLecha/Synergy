package ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages;

import android.content.Intent;

import ub.edu.pis2017.pis_17.synergy.View.Activities.post.newPost.NewProjectActivity;

public class ProjectListViewPage extends PostListViewPage {

    @Override
    public void createPost() {
        startActivity(new Intent(parentActivity, NewProjectActivity.class));
    }

}
