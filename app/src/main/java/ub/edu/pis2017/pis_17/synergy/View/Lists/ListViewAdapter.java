package ub.edu.pis2017.pis_17.synergy.View.Lists;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.SortingState;

public abstract class ListViewAdapter<Item> extends BaseAdapter {
    protected class ItemListViewHolder {
        public int viewNum;
    }

    @Override
    public boolean isEmpty() {
        Log.i("Adapter","Is empty? " + listItems.isEmpty());
        return listItems.isEmpty();
    }

    protected Activity mActivity;
    protected List<Item> listItems;

    public ListViewAdapter(Activity mActivity) {
        super();
        this.mActivity = mActivity;
        this.listItems = new ArrayList<>(); // TODO: 07/05/2018 Possible required dependency injection?
    }

    @Override
    public int getCount() {return listItems.size();}

    @Override
    public Item getItem(int i) {return listItems.get(i);}

    @Override
    public long getItemId(int i) {return i;}

    public void add(Item item) {
        this.listItems.add(item);
    }

    public void addAll(List<Item> listItems) {
        for(int i = 0; i < listItems.size(); i++) {
            this.listItems.add(listItems.get(i));
        }
    }

    public void removeLast() {
        int size = listItems.size();
        if(size != 0) {
            listItems.remove(size-1);
        }

    }

    public void clear() {listItems.clear();}

    @Override
    public View getView(int uiPosition, View itemView, ViewGroup viewGroup) {
        Item item = listItems.get(uiPosition);
        return setHolder(itemView, item, uiPosition);
        /*
        if (itemView == null || ((ItemListViewHolder)itemView.getTag()).viewNum != uiPosition) {
            return setHolder(itemView, item, uiPosition);
        }
        return itemView;
        */
    }

    public List<Item> getList(){
        return listItems;
    }

    //Method that must be overriden to programatically set layouts of specific listItems (following listViewHolder pattern)
    protected abstract View setHolder(View itemView, Item item, int uiPosition);
}
