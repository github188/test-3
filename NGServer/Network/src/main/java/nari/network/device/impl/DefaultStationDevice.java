package nari.network.device.impl;

import nari.network.device.Station;

/**
 * Created by Birderyu on 2017/5/18.
 */
public class DefaultStationDevice
    extends DefaultDevice implements Station {

    public DefaultStationDevice(int modelId, int oid, int voltageLevel) {
        super(modelId, oid, voltageLevel);
    }

    @Override
    public boolean isStation() {
        return true;
    }
}
