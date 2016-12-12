package ca.martinpouliot.caraudio.caraudiofix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class MainActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getStringSet("pref_bluetooth_list",null) == null){
            addSlide(AppIntroFragment.newInstance("Let's get you started!", "This app will fix your bluetooth car issues.", R.drawable.car_present, getResources().getColor(R.color.presentSlide1)));
            addSlide(AppIntroFragment.newInstance("Settings", "1. Select your bluetooth devices you wish to use with this apps.", R.drawable.bluetooth, getResources().getColor(R.color.presentSlide2)));
            addSlide(AppIntroFragment.newInstance("Your done!", "2. You will be redirected to settings.", R.drawable.done, getResources().getColor(R.color.presentSlide3)));
            // OPTIONAL METHODS
            // Override bar/separator color.
            setProgressButtonEnabled(true);


            //It is a first launch
        }else{
            launchSettings();
        }
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        launchSettings();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        launchSettings();
    }

    private void launchSettings(){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
        finish();
    }

}
