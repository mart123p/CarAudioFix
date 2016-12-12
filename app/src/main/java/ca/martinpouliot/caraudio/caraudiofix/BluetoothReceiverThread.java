package ca.martinpouliot.caraudio.caraudiofix;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.util.Set;

/**
 * Created by martin on 12/11/16.
 */
public class BluetoothReceiverThread extends Thread {
    Context context;
    Set<String> wifi;
    Set<String> bluetoothAddress;
    int timeToWait;
    boolean autoPlay;
    boolean nextTrack;
    boolean maxVolume;
    BluetoothDevice device;

    BluetoothReceiverThread(final Context context,Set<String> wifi,Set<String> bluetoothAddress,int timeToWait,boolean autoPlay,boolean nextTrack,boolean maxVolume,BluetoothDevice device){
        this.context =context;
        this.wifi =wifi;
        this.bluetoothAddress = bluetoothAddress;
        this.timeToWait = timeToWait;
        this.autoPlay = autoPlay;
        this.nextTrack = nextTrack;
        this.maxVolume = maxVolume;
        this.device = device;

    }

    public void run(){
        try{
            if (bluetoothAddress != null && bluetoothAddress.contains(device.getAddress())) {

                if(wifi != null){
                    //The user is using the wifi feature we must check if it is still connected to
                    //the network during the period specified by the user.
                    long startTime = System.currentTimeMillis()/1000;
                    long finishTime = startTime + timeToWait;
                    if(wifi.contains(getCurrentSsid(context)) && !getCurrentSsid(context).equals("")){

                        //The user is currently at his house
                        boolean userHasMoved = false;
                        while((System.currentTimeMillis()/1000) < finishTime){
                            Thread.sleep(200);
                            if(!wifi.contains(getCurrentSsid(context)) || getCurrentSsid(context).equals("")){
                                userHasMoved = true;
                                break;
                            }
                        }

                        if(!userHasMoved){
                            //We won't start the music since the user is not moving
                            return;
                        }

                    }
                }



                Intent player;
                if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 )  {
                    // use the [MediaStore.INTENT_ACTION_MUSIC_PLAYER] intent
                    player = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                    player.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                }
                else  {
                    // use the [Intent.CATEGORY_APP_MUSIC] intent
                    player =Intent.makeMainSelectorActivity(Intent.ACTION_MAIN,
                            Intent.CATEGORY_APP_MUSIC);
                    player.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//Min SDK 15
                }

                context.startActivity(player);
                //We wait 5s to start google play music
                Thread.sleep(5000);



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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private String getCurrentSsid(Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    String ssid = wifiInfo.getSSID();
                    return ssid.substring(1,ssid.length()-1);
                }
            }
        }
        return "";

    }
}
