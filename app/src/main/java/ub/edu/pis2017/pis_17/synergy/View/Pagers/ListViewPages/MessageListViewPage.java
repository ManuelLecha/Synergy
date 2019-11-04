package ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPages;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;

import ub.edu.pis2017.pis_17.synergy.Model.Messages.Message;
import ub.edu.pis2017.pis_17.synergy.View.Pagers.ListViewPage;
import ub.edu.pis2017.pis_17.synergy.View.Showers.MessageShower;

public class MessageListViewPage extends ListViewPage<Message> {


    @Override
    protected void setView() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message msg = adapter.getItem(i);
                MessageShower ms = new MessageShower(parentActivity);
                ms.show(msg);
            }
        });
    }
}
