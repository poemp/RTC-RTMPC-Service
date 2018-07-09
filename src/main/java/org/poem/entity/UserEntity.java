package org.poem.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "user")
public class UserEntity implements Serializable {


    @Id
    @Column(name = "ip")
    private String ip;

    @Basic
    @Column(name = "random_name")

    private String randomName;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getRandomName() {
        return randomName;
    }

    public void setRandomName(String randomName) {
        this.randomName = randomName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;
        if (randomName != null ? !randomName.equals(that.randomName) : that.randomName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + (randomName != null ? randomName.hashCode() : 0);
        return result;
    }
}
