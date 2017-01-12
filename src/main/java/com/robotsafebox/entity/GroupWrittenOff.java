package com.robotsafebox.entity;

import java.io.Serializable;
import java.util.Date;

public class GroupWrittenOff implements Serializable {
    private Long id;

    private Long groupId;

    private String groupName;

    private Date groupCreateTime;

    private String groupAddress;

    private String addressX;

    private String addressY;

    private Date createTime;

    private Date updateTime;

    private Date writtenOffTime;

    private Long writtenOffUserId;

    private String groupMemberUserIds;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getGroupCreateTime() {
        return groupCreateTime;
    }

    public void setGroupCreateTime(Date groupCreateTime) {
        this.groupCreateTime = groupCreateTime;
    }

    public String getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(String groupAddress) {
        this.groupAddress = groupAddress;
    }

    public String getAddressX() {
        return addressX;
    }

    public void setAddressX(String addressX) {
        this.addressX = addressX;
    }

    public String getAddressY() {
        return addressY;
    }

    public void setAddressY(String addressY) {
        this.addressY = addressY;
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

    public Long getWrittenOffUserId() {
        return writtenOffUserId;
    }

    public void setWrittenOffUserId(Long writtenOffUserId) {
        this.writtenOffUserId = writtenOffUserId;
    }

    public String getGroupMemberUserIds() {
        return groupMemberUserIds;
    }

    public void setGroupMemberUserIds(String groupMemberUserIds) {
        this.groupMemberUserIds = groupMemberUserIds;
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
        GroupWrittenOff other = (GroupWrittenOff) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupId() == null ? other.getGroupId() == null : this.getGroupId().equals(other.getGroupId()))
            && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
            && (this.getGroupCreateTime() == null ? other.getGroupCreateTime() == null : this.getGroupCreateTime().equals(other.getGroupCreateTime()))
            && (this.getGroupAddress() == null ? other.getGroupAddress() == null : this.getGroupAddress().equals(other.getGroupAddress()))
            && (this.getAddressX() == null ? other.getAddressX() == null : this.getAddressX().equals(other.getAddressX()))
            && (this.getAddressY() == null ? other.getAddressY() == null : this.getAddressY().equals(other.getAddressY()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getWrittenOffTime() == null ? other.getWrittenOffTime() == null : this.getWrittenOffTime().equals(other.getWrittenOffTime()))
            && (this.getWrittenOffUserId() == null ? other.getWrittenOffUserId() == null : this.getWrittenOffUserId().equals(other.getWrittenOffUserId()))
            && (this.getGroupMemberUserIds() == null ? other.getGroupMemberUserIds() == null : this.getGroupMemberUserIds().equals(other.getGroupMemberUserIds()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupId() == null) ? 0 : getGroupId().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getGroupCreateTime() == null) ? 0 : getGroupCreateTime().hashCode());
        result = prime * result + ((getGroupAddress() == null) ? 0 : getGroupAddress().hashCode());
        result = prime * result + ((getAddressX() == null) ? 0 : getAddressX().hashCode());
        result = prime * result + ((getAddressY() == null) ? 0 : getAddressY().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getWrittenOffTime() == null) ? 0 : getWrittenOffTime().hashCode());
        result = prime * result + ((getWrittenOffUserId() == null) ? 0 : getWrittenOffUserId().hashCode());
        result = prime * result + ((getGroupMemberUserIds() == null) ? 0 : getGroupMemberUserIds().hashCode());
        return result;
    }
}