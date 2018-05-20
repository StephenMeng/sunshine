package team.stephen.sunshine.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sunshine_role_permission_relation")
public class RolePermissionRelation {

    @Id
    @Column(name = "user_role")
    private String userRole;
    @Id
    @Column(name = "permission")
    private Integer permission;


    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }
}
