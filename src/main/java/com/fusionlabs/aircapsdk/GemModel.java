package com.fusionlabs.aircapsdk;

/**
 * Created by zwebie on 2/22/17.
 */

public class GemModel {
    String address;
    int rssi;

    public GemModel(String address, int rssi) {
        this.address = address;
        this.rssi = rssi;
    }
}
