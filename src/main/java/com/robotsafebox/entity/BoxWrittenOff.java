package com.robotsafebox.entity;

import java.io.Serializable;
import java.util.Date;

public class BoxWrittenOff implements Serializable {
    private Long id;

    private Long boxId;

    private String ichId;

    private String ibeaconId;

    private String wifiId;

    private String wifiPassword;

    private String boxName;

    private Long groupId;

    private Date createTime;

    private Date updateTime;

    private Date writtenOffTime;

    private String boxUserIds;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBoxId() {
        return boxId;
    }

    public void setBoxId(Long boxId) {
        this.boxId = boxId;
    }

    public String getIchId() {
        return ichId;
    }

    public void setIchId(String ichId) {
        this.ichId = ichId;
    }

    public String getIbeaconId() {
        return ibeaconId;
    }

    public void setIbeaconId(String ibeaconId) {
        this.ibeaconId = ibeaconId;
    }

    public String getWifiId() {
        return wifiId;
    }

    public void setWifiId(String wifiId) {
        this.wifiId = wifiId;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getWrittenOffTime() {
        return writtenOffTime;
    }

    public void setWrittenOffTime(Date writtenOffTime) {
        this.writtenOffTime = writtenOffTime;
    }

    public String getBoxUserIds() {
        return boxUserIds;
    }

    public void setBoxUserIds(String boxUserIds) {
        this.boxUserIds = boxUserIds;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BoxWrittenOff other = (BoxWrittenOff) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBoxId() == null ? other.getBoxId() == null : this.getBoxId().equals(other.getBoxId()))
            && (this.getIchId() == null ? other.getIchId() == null : this.getIchId().equals(other.getIchId()))
            && (this.getIbeaconId() == null ? other.getIbeaconId() == null : this.getIbeaconId().equals(other.getIbeaconId()))
            && (this.getWifiId() == null ? other.getWifiId() == null : this.getWifiId().equals(other.getWifiId()))
            && (this.getWifiPassword() == null ? other.getWifiPassword() == null : this.getWifiPassword().equals(other.getWifiPassword()))
            && (this.getBoxName() == null ? other.getBoxName() == null : this.getBoxName().equals(other.getBoxName()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getWrittenOffTime() == null ? other.getWrittenOffTime() == null : this.getWrittenOffTime().equals(other.getWrittenOffTime()))
            && (this.getBoxUserIds() == null ? other.getBoxUserIds() == null : this.getBoxUserIds().equals(other.getBoxUserIds()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBoxId() == null) ? 0 : getBoxId().hashCode());
        result = prime * result + ((getIchId() == null) ? 0 : getIchId().hashCode());
        result = prime * result + ((getIbeaconId() == null) ? 0 : getIbeaconId().hashCode());
        result = prime * result + ((getWifiId() == null) ? 0 : getWifiId().hashCode());
        result = prime * result + ((getWifiPassword() == null) ? 0 : getWifiPassword().hashCode());
        result = prime * result + ((getBoxName() == null) ? 0 : getBoxName().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getWrittenOffTime() == null) ? 0 : getWrittenOffTime().hashCode());
        result = prime * result + ((getBoxUserIds() == null) ? 0 : getBoxUserIds().hashCode());
        return result;
    }
}