package ca.martinpouliot.caraudio.caraudiofix;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import java.util.Set;

/**
 * Created by martin on 06/06/16.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        Set<String> wifi = sharedPreferences.getStringSet("pref_wifi_list",null);
        Set<String> bluetoothAddress = sharedPreferences.getStringSet("pref_bluetooth_list",null);
        int timeToWait = Integer.valueOf(sharedPreferences.getString("pref_wifi_wait_time","300"));
        boolean autoPlay = sharedPreferences.getBoolean("pref_autoPlay",true);
        boolean nextTrack = sharedPreferences.getBoolean("pref_nextTrack",true);
        boolean maxVolume = sharedPreferences.getBoolean("pref_maxVolume", false);
        Log.i("BluetoothReceiver","Bluetooth connection detected");
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        BluetoothReceiverThread th = new BluetoothReceiverThread(context,wifi,bluetoothAddress,timeToWait,autoPlay,nextTrack,maxVolume,device);
        th.start();
    }

}
