package cn.gson.oasys.model.entity.attendce;

import javax.persistence.*;
import java.math.BigInteger;

/**
 * @author 86156
 * 统计类
 */
@Entity
public class Census {
    @Column(name = "num")
    private BigInteger num;

    @Column(name = "dept_id")
    private String deptId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String realName;

    @Column(name = "dept_name")
    private String deptName;

    public BigInteger getNum() {
        return num;
    }

    public void setNum(BigInteger num) {
        this.num = num;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "Census{" +
                "num=" + num +
                ", deptId='" + deptId + '\'' +
                ", userId='" + userId + '\'' +
                ", realName='" + realName + '\'' +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
