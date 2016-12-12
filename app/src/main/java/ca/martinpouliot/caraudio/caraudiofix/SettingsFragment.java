package ca.martinpouliot.caraudio.caraudiofix;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.util.Log;

import java.util.List;
import java.util.Set;

/**
 * Created by martin on 05/06/16.
 */
public class SettingsFragment extends PreferenceFragment {
    BluetoothAdapter btAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        addPreferencesFromResource(R.xml.settings_test);
        WifiManager vm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiConfigs = vm.getConfiguredNetworks();
        if(wifiConfigs == null){
            //Wifi is not enabled on the device, we can ask for the user to enable wifi.
            final MultiSelectListPreference wifiList = (MultiSelectListPreference) findPreference("pref_wifi_list");
            wifiList.setEnabled(false);


            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            builder.setTitle("Car Audio Fix");
            builder.setMessage("The wifi needs to be turned on, in order for this app to access wifi data.");
            builder.setPositiveButton("Enable wifi",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                final DialogInterface dialogInterface,
                                final int i) {

                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
            builder.setNegativeButton("Forget about wifi",null);
            builder.create().show();
        }else{
            CharSequence[] wifiEntries = new CharSequence[wifiConfigs.size()];
            for(int i = 0; i < wifiConfigs.size(); i++){
                wifiEntries[i] = wifiConfigs.get(i).SSID;
                wifiEntries[i] = wifiEntries[i].toString().substring(1,wifiEntries[i].toString().length()-1);
            }
            final MultiSelectListPreference wifiList = (MultiSelectListPreference) findPreference("pref_wifi_list");
            wifiList.setEnabled(true);
            wifiList.setEntries(wifiEntries);
            wifiList.setEntryValues(wifiEntries);
        }


        if(!btAdapter.isEnabled()){
            final MultiSelectListPreference list = (MultiSelectListPreference) findPreference("pref_bluetooth_list");
            list.setEnabled(false);

            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            builder.setTitle("Car Audio Fix");
            builder.setMessage("The bluetooth needs to be turned on, in order for this app to access bluetooth data.");
            builder.setPositiveButton("Enable bluetooth",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                final DialogInterface dialogInterface,
                                final int i) {

                            startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                        }
                    });
            builder.setNegativeButton("Forget about bluetooth",null);
            builder.create().show();
        }else{
            Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
            CharSequence[] BluetoothEntries = new CharSequence[devices.size()];
            CharSequence[] BluetoothEntryValues = new CharSequence[devices.size()];
            int i = 0;
            for (BluetoothDevice device : devices) {
                BluetoothEntries[i] = device.getName();
                BluetoothEntryValues[i] = device.getAddress();
                i++;
            }
            final MultiSelectListPreference list = (MultiSelectListPreference) findPreference("pref_bluetooth_list");
            list.setEnabled(true);
            list.setEntries(BluetoothEntries);
            list.setEntryValues(BluetoothEntryValues);

        }

    }
    @Override
    public void onResume(){
        super.onResume();
        //addPreferencesFromResource(R.xml.settings_test);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        WifiManager vm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> wifiConfigs = vm.getConfiguredNetworks();
        if(wifiConfigs != null){

            CharSequence[] wifiEntries = new CharSequence[wifiConfigs.size()];
            for(int i = 0; i < wifiConfigs.size(); i++){
                wifiEntries[i] = wifiConfigs.get(i).SSID;
                wifiEntries[i] = wifiEntries[i].toString().substring(1,wifiEntries[i].toString().length()-1);
            }
            final MultiSelectListPreference wifiList = (MultiSelectListPreference) findPreference("pref_wifi_list");
            wifiList.setEnabled(true);
            wifiList.setEntries(wifiEntries);
            wifiList.setEntryValues(wifiEntries);

        }else{
            final MultiSelectListPreference wifiList = (MultiSelectListPreference) findPreference("pref_wifi_list");
            wifiList.setEnabled(false);
        }
        if(btAdapter.isEnabled()){
            Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
            CharSequence[] BluetoothEntries = new CharSequence[devices.size()];
            CharSequence[] BluetoothEntryValues = new CharSequence[devices.size()];
            int i = 0;
            for (BluetoothDevice device : devices) {
                BluetoothEntries[i] = device.getName();
                BluetoothEntryValues[i] = device.getAddress();
                i++;
            }
            final MultiSelectListPreference list = (MultiSelectListPreference) findPreference("pref_bluetooth_list");
            list.setEnabled(true);
            list.setEntries(BluetoothEntries);
            list.setEntryValues(BluetoothEntryValues);
        }else{
            final MultiSelectListPreference list = (MultiSelectListPreference) findPreference("pref_bluetooth_list");
            list.setEnabled(false);
        }


    }
}