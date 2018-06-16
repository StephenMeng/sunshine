package team.stephen.sunshine.web.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;

import java.util.List;

/**
 * @author stephen
 * @date 2018/5/22
 */
@ApiModel(description = "附件 输入实体类")
public class AttachmentDto extends BaseArticleDto {
    @ApiModelProperty(value = "附件名")
    private String attachName;
    @ApiModelProperty(value = "附件uri")
    private Integer attachUri;
}
