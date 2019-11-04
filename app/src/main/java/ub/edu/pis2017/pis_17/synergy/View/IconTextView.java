package ub.edu.pis2017.pis_17.synergy.View;

/**
 * From https://android.jlelse.eu/font-awesome-a-better-way-to-display-symbols-and-icons-in-android-d694e5ee621f
 * Created by kanales on 24/03/2018.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;

public class IconTextView extends android.support.v7.widget.AppCompatTextView {
    protected static Typeface faTypeface = null;
    private Context context;

    public IconTextView(Context context) {
        super(context);
        this.context = context;
        createView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createView();
    }

    private void createView(){
        setGravity(Gravity.CENTER);

        // avoid memory leak
        if (faTypeface == null) {
            faTypeface = Typeface.createFromAsset(context.getAssets(), "font/fa5solid900.otf");
        }

        setTypeface(faTypeface);
    }
}