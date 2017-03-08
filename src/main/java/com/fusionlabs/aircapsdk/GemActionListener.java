package com.fusionlabs.aircapsdk;

import android.graphics.Point;

/**
 * Created by zwebie on 2/22/17.
 */

public interface GemActionListener {
    void onGemAction(GemAction action);
    void onGemMove(Float acceleration, Point point);
    void onGemConnected();
}
