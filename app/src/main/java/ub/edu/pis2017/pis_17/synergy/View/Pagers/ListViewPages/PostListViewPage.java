package ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPage;
import ub.edu.pis2017.pis_17.synergy.Model.Posts.Post;
import ub.edu.pis2017.pis_17.synergy.View.Showers.PostShower;

public abstract class PostListViewPage extends ListViewPage<Post> {

    @Override
    protected void setView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post post = adapter.getItem(i);
                PostShower ps = new PostShower(parentActivity);
                ps.show(post);
            }
        });
    }

    public abstract void createPost();


}
