package com.it.hgad.logisticsmanager.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */
public class DeviceData implements Serializable {

    private String deviceName;
    private String deviceType;
    private String devicePic;
    private List<DeviceDetail> deviceDetails;

    public DeviceData(String deviceName, String deviceType, String devicePic, List<DeviceDetail> deviceDetails) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.devicePic = devicePic;
        this.deviceDetails = deviceDetails;
    }

    public List<DeviceDetail> getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(List<DeviceDetail> deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public String getDevicePic() {
        return devicePic;
    }

    public void setDevicePic(String devicePic) {
        this.devicePic = devicePic;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


}
