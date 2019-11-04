package ub.edu.pis2017.pis_17.synergy.View.Pagers;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ub.edu.pis2017.pis_17.synergy.View.Lists.ListViewAdapter;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.SortingState;

public abstract class ListViewPage<Item> extends  Fragment {

    protected Activity parentActivity;
    protected ListViewAdapter<Item> adapter;
    protected ListView listView;
    protected View emptyView;
    protected SwipeRefreshLayout.OnRefreshListener listener;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected boolean refreshHolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_fragment_layout,container,false);
        listView = view.findViewById(R.id.swipe_fragment_layout_lstvw);

        Log.d("DEBUG", "Creating ListView");
        swipeRefreshLayout = view.findViewById(R.id.listViewFragment_refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(listener);
        swipeRefreshLayout.setRefreshing(refreshHolder);
        SwipeRefreshLayout rlay = view.findViewById(R.id.emptyRefreshLayout);
        rlay.setOnRefreshListener(listener);

        listView.setAdapter(adapter);
        listView.setEmptyView(rlay);
        setView();
        return view;
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        this.listener = listener;
    }

    public void setRefreshing(boolean flag) {
        if(this.swipeRefreshLayout != null) {
            this.swipeRefreshLayout.setRefreshing(flag);
        }
        else {
            this.refreshHolder = flag;
        }
    }

    public void setParentActivity(Activity parentActivity) {this.parentActivity = parentActivity;}
    public void setAdapter(ListViewAdapter<Item> adapter) {
        this.adapter = adapter;
    }

    public void invalidateViews() {
        if(this.listView != null) {this.listView.invalidateViews();}
    }

    //Method that must be overriden to programatically set the view of the page
    protected abstract void setView();
}
