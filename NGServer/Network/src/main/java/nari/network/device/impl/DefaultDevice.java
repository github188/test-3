package nari.network.device.impl;

import nari.network.device.*;

/**
 * Created by Birderyu on 2017/5/18.
 */
public class DefaultDevice
    implements Device {

    /**
     * 设备子类型
     */
    private int modelId = 0;

    /**
     * 设备ID
     */
    private int oid = 0;

    /**
     * 电压等级
     */
    private int voltageLevel = 0;

    private boolean inBigFeeder = false;

    private BigFeeder bigFeeder = null;

    public DefaultDevice(int modelId, int oid, int voltageLevel) {
        this.modelId = modelId;
        this.oid = oid;
        this.voltageLevel = voltageLevel;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Override
    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    @Override
    public int getVoltageLevel() {
        return voltageLevel;
    }

    public void setVoltageLevel(int voltageLevel) {
        this.voltageLevel = voltageLevel;
    }

    @Override
    public boolean isTopoDevice() {
        return false;
    }

    @Override
    public boolean isStation() {
        return false;
    }

    @Override
    public boolean isInStation() {
        return false;
    }

    @Override
    public boolean isInBigFeeder() {
        return inBigFeeder;
    }

    @Override
    public void setInBigFeeder(boolean inBigFeeder) {
        this.inBigFeeder = inBigFeeder;
    }

    @Override
    public BigFeeder getBigFeeder() {
        return bigFeeder;
    }

    @Override
    public void setBigFeeder(BigFeeder bigFeeder) {
        this.bigFeeder = bigFeeder;
    }

    @Override
    public TopoDevice asTopoDevice() {
        if (!isTopoDevice()) {
            return null;
        }
        return (TopoDevice) this;
    }

    @Override
    public Station asStation() {
        if (!isStation()) {
            return null;
        }
        return (Station) this;
    }

    @Override
    public InStationDevice asInStation() {
        if (!isInStation()) {
            return null;
        }
        return (InStationDevice) this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + modelId;
        result = prime * result + oid;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Device)) {
            return false;
        }
        Device other = (Device) obj;
        if (modelId != other.getModelId())
            return false;
        if (oid != other.getOid())
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "设备 [modelId=" + modelId + ", oid=" + oid + "]";
    }
}
