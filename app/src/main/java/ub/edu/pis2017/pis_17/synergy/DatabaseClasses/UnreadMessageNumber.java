package ub.edu.pis2017.pis_17.synergy.DatabaseClasses;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import ub.edu.pis2017.pis_17.synergy.Presenter.MyApplication;
import ub.edu.pis2017.pis_17.synergy.View.Activities.main.MainActivity;
import ub.edu.pis2017.pis_17.synergy.View.Activities.profile.UserProfileActivity;

public class UnreadMessageNumber {
    private static final UnreadMessageNumber ourInstance = new UnreadMessageNumber();
    private boolean DONE = false;


    public static UnreadMessageNumber getInstance() {
        return ourInstance;
    }

    private int unreadNumber;

    public UnreadMessageNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }

    public UnreadMessageNumber() {
        if(!DONE){
            this.unreadNumber = 0;
            DONE = true;
        }
    }

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {

        if(unreadNumber < this.unreadNumber & DONE){

            Vibrator v = (Vibrator) MyApplication.getContext().getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
            }else{
                v.vibrate(500);
            }
        }

        this.unreadNumber = unreadNumber;

        if(MainActivity.isActive){
            MainActivity.setNotificationText();
        }else if(UserProfileActivity.isActive){
            UserProfileActivity.setNotificationText();
        }
    }




}
