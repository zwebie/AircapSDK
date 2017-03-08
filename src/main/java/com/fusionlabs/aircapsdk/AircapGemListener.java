package com.fusionlabs.aircapsdk;

import android.util.Log;

import com.gemsense.gemsdk.Gem;
import com.gemsense.gemsdk.GemAbstractListener;

/**
 * Created by zwebie on 2/22/17.
 */

public class AircapGemListener extends GemAbstractListener {

    private static final String TAG = AircapGemListener.class.getSimpleName();

    @Override
    public void onStateChanged(int state) {
        Log.d(TAG, "onStateChanged");

        switch (state) {
            case Gem.STATE_CONNECTED:
                Log.d(TAG, "Connected to a gem");
                break;
            case Gem.STATE_DISCONNECTED:
                Log.d(TAG, "Gem was disconnected");
                break;
        }
    }

    @Override
    public void onErrorOccurred(int errCode) {
        Log.d(TAG, "onErrorOccurred");
        if (errCode == Gem.ERR_CONNECTING_TIMEOUT) {
            Log.d(TAG, "error connecting timeout");
        }
    }

}
