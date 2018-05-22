package team.stephen.sunshine.web.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * base user dto
 *
 * @author stephen
 * @date 2018/5/22
 */
@ApiModel(description = "baseUserDto实体类")
public class BaseUserDto {
    @ApiModelProperty(value = "用户学号")
    private String userNo;
    @ApiModelProperty(value = "用户名")
    private String userName;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
