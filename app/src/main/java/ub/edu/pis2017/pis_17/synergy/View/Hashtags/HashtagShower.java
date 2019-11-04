package ub.edu.pis2017.pis_17.synergy.View.Hashtags;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ub.edu.pis2017.pis_17.synergy.Model.Hashtags.Hashtag;
import ub.edu.pis2017.pis_17.synergy.R;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;

public class HashtagShower {
    public static final String SEARCH_HASHTAG = "searchHashtag";

    private Activity mActivity;
    private ArrayList<LinearLayout> linearLayoutList;
    private ArrayList<Hashtag> hashtagList;
    private int currentPostType;

    public HashtagShower(Activity mActivity, ArrayList<LinearLayout> linearLayoutList) {
        this.mActivity = mActivity;
        this.linearLayoutList = linearLayoutList;
        hashtagList = new ArrayList<>();
    }

    public void addHashtags(List<Hashtag> hashtagList) {
        this.hashtagList.addAll(hashtagList);

        LinearLayout chosenLnlay;
        for(Hashtag hashtag : hashtagList) {
            chosenLnlay = linearLayoutList.get(0);
            for(LinearLayout lnlay : linearLayoutList) {
                int wrapSpec1 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                lnlay.measure(wrapSpec1, wrapSpec1);
                int wrapSpec2 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                chosenLnlay.measure(wrapSpec2, wrapSpec2);
                if(lnlay.getMeasuredWidth() < chosenLnlay.getMeasuredWidth()) {chosenLnlay = lnlay;}
            }
            TextView hashtagView = (TextView) mActivity.getLayoutInflater().inflate(R.layout.label_hashtag, chosenLnlay, false);
            hashtagView.setText("#" + hashtag.getHashtagName());
            hashtagView.setOnClickListener(view -> {
                Intent i = new Intent(mActivity, MainActivity.class);
                i.putExtra(SEARCH_HASHTAG,((TextView)view).getText());
                i.putExtra("PAGE_POSITION", currentPostType);
                mActivity.startActivity(i);
            });
            chosenLnlay.addView(hashtagView);
        }
    }

    public void clearHashtags() {
        hashtagList.clear();

        for(LinearLayout lnlay : linearLayoutList) {
            lnlay.removeAllViews();
        }
    }

    public ArrayList<Hashtag> getHashtagList() {
        return hashtagList;
    }

    public void setCurrentPostType(int currentPostType) {this.currentPostType = currentPostType;}
}
