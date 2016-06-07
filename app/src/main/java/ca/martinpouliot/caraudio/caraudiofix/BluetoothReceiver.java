package ca.martinpouliot.caraudio.caraudiofix;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.provider.Settings;

import java.util.Set;

/**
 * Created by martin on 06/06/16.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> bluetoothAddress = sharedPreferences.getStringSet("pref_bluetooth_list",null);
        boolean autoPlay = sharedPreferences.getBoolean("pref_autoPlay",true);
        boolean nextTrack = sharedPreferences.getBoolean("pref_nextTrack",true);
        boolean maxVolume = sharedPreferences.getBoolean("pref_maxVolume", false);


        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        if (bluetoothAddress != null && bluetoothAddress.contains(device.getAddress())) {
            if(autoPlay) {
                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "togglepause");
                context.sendBroadcast(i);
            }
            if(nextTrack) {
                Intent i2 = new Intent("com.android.music.musicservicecommand");
                i2.putExtra("command", "next");
                context.sendBroadcast(i2);
            }
            if(maxVolume){
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(
                        AudioManager.STREAM_MUSIC,
                        am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                        0);
            }
        }
    }
}
