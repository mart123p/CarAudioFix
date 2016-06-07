package ca.martinpouliot.caraudio.caraudiofix;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;

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


        // Load the preferences from an XML resource
        Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
        CharSequence[] entries = new CharSequence[devices.size()];
        CharSequence[] entryValues = new CharSequence[devices.size()];
        int i = 0;
        for (BluetoothDevice device : devices) {
            entries[i] = device.getName();
            entryValues[i] = device.getAddress();
            i++;
        }

        addPreferencesFromResource(R.xml.settings_test);
        final MultiSelectListPreference list = (MultiSelectListPreference) findPreference("pref_bluetooth_list");
        list.setEntries(entries);
        list.setEntryValues(entryValues);
    }
}