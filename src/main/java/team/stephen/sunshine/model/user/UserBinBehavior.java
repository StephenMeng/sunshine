package team.stephen.sunshine.model.user;

import io.swagger.models.auth.In;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Stephen
 * @date 2018/05/26 14:54
 */
@Entity
@Table(name = "sunshine_user_bin_behavior")
public class UserBinBehavior {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Id
    @Column(name = "bin_id")
    private Integer binId;
    @Id
    @Column(name = "type")
    private Integer type;
    @Column(name = "create_date")
    private Date createData;
    @Column(name = "ext")
    private String ext;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBinId() {
        return binId;
    }

    public void setBinId(Integer binId) {
        this.binId = binId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateData() {
        return createData;
    }

    public void setCreateData(Date createData) {
        this.createData = createData;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
