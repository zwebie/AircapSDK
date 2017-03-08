package com.fusionlabs.aircapsdk;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.gemsense.common.GemSensorsData;
import com.gemsense.common.TapData;
import com.gemsense.gemsdk.Gem;
import com.gemsense.gemsdk.GemManager;
import com.gemsense.gemsdk.GemScanListener;
import com.gemsense.gemsdk.GemScanner;
import com.gemsense.gemsdk.OnSensorsAbstractListener;
import com.gemsense.gemsdk.OnTapListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zwebie on 2/22/17.
 */
public class AircapManager {
    private static final String TAG = AircapManager.class.getSimpleName();

    private static AircapManager instance;
    private List<GemActionListener> listeners;
    private GemScanner scanner;
    private Map<String, GemModel> mGems;
    private Gem gem;

    public static AircapManager getInstance() {
        if (instance == null) {
            instance = new AircapManager();
        }
        return instance;
    }

    private AircapManager() {
        listeners = new ArrayList<>();
        mGems = new HashMap();
        scanForGem();
    }

    //register a listener for gem actions
    //TODO: add an id so user will be able to unregister
    public void registerGemActionListener(GemActionListener listener) {
        listeners.add(listener);
    }

    private void onGemAction(GemAction action) {
        for (GemActionListener listener : listeners) {
            listener.onGemAction(action);
        }
    }

    private void scanForGem() {
        scanner = new GemScanner(new GemScanListener() {
            @Override
            public void onScanFinish() {
                Log.d(TAG, "onScanFinish");
                connectToGem();
            }

            @Override
            public void onDeviceDiscovered(final BluetoothDevice device, int rssi) {
                Log.i(TAG, "Gem found: " + device.getAddress());
                mGems.put(device.getAddress(), new GemModel(device.getAddress(), rssi));
            }
        });

        //or specify this time manually
        scanner.scan(5);
    }

    private void connectToGem() {
        Map.Entry<String, GemModel> entry = mGems.entrySet().iterator().next();
        String key = entry.getKey();
        Log.d(TAG, "connecting to Gem: " + key);
        gem = GemManager.getDefault().getGem(mGems.get(key).address, new AircapGemListener());
        for (GemActionListener listener : listeners) {
            listener.onGemConnected();
        }

        if (gem != null) {
            connectGemSensorListener();
        }
    }

    private void connectGemSensorListener() {
        //Configure motion sensors callback
        gem.setSensorsListener(new OnSensorsAbstractListener() {
            @Override
            public void onSensorsChanged(GemSensorsData data) {
                //data.quaternion: float[4] {w, x, y, z}
//                Log.d(TAG, "data.quaternion: " + data.quaternion[0]);
                //                cubeView.setOrientation(data.quaternion);
                //data.acceleration: float[3] {x, y, z}
//                Log.d(TAG, "data.acceleration: " + data.acceleration[0]);
                //                accelView.setAcceleration(data.acceleration);
            }
        });
    }

    public void bindService(Context context) {
        GemManager.getDefault().bindService(context);
    }

    public void unbindService(Context context) {
        GemManager.getDefault().unbindService(context);
    }

    public void setTapListener(final AircapOnTapListener listener) {
        gem.setTapListener(new OnTapListener() {
            @Override
            public void onTap(TapData tapData) {
                listener.onTap(tapData);
            }
        });
    }
}
