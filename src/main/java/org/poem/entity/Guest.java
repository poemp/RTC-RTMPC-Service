package org.poem.entity;

import java.io.Serializable;

public class Guest implements Serializable {
    private UserEntity userEntity;
    private long accessTime;

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public long getAccessTime() {
        return accessTime;
    }
}
