package team.stephen.sunshine.web.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author stephen
 * @date //
 */
@ApiModel(description = "修改基本信息用DTO")
public class UpdateUserDto extends SignInUserDto {
    @ApiModelProperty(value = "用户的userId")
    private Integer userId;

    UpdateUserDto() {
        super();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
