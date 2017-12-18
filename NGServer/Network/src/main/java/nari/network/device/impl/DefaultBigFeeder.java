package nari.network.device.impl;

import nari.network.device.BigFeeder;
import nari.network.device.Station;

/**
 * Created by Birderyu on 2017/6/16.
 */
public class DefaultBigFeeder
    extends DefaultDevice implements BigFeeder {

    private int outletSwitchType;
    private int outletSwitchId;
    private Station startStation;


    public DefaultBigFeeder(int modelId, int oid, int voltageLevel,
                            int outletSwitchType, int outletSwitchId,
                            Station startStation) {
        super(modelId, oid, voltageLevel);
        this.outletSwitchType = outletSwitchType;
        this.outletSwitchId = outletSwitchId;
        this.startStation = startStation;
    }

    @Override
    public int getOutletSwitchType() {
        return outletSwitchType;
    }

    @Override
    public int getOutletSwitchId() {
        return outletSwitchId;
    }

    @Override
    public Station getStartStation() {
        return startStation;
    }
}
