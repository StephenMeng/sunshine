package team.stephen.sunshine.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sunshine_user_role_relation")
public class UserRoleRelation {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    @Id
    @Column(name = "user_role")
    private String userRole;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}
