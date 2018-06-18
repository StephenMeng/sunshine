package team.stephen.sunshine.web.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.web.dto.base.BaseUserDto;

import java.io.Serializable;

/**
 * @author stephen
 */
@ApiModel(description = "用户实体类")
public class UserDto extends BaseUserDto implements Serializable {
    private static final long serialVersionUID = 1216491852961950902L;
    @ApiModelProperty(value = "用戶ID")
    private Integer userId;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "角色")
    private String userRole;
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}
