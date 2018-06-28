package team.stephen.sunshine.web.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.model.common.Attachment;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;

import java.util.List;

/**
 * @author stephen
 * @date 2018/5/22
 */
@ApiModel(description = "附件 输入实体类")
public class AttachmentDto {
    @ApiModelProperty(value = "附件名")
    private String attachName;
    @ApiModelProperty(value = "附件uri")
    private String attachUri;

    public AttachmentDto() {
    }

    public AttachmentDto(String attachUri, String attachName) {
        setAttachName(attachName);
        setAttachUri(attachUri);
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachUri() {
        return attachUri;
    }

    public void setAttachUri(String attachUri) {
        this.attachUri = attachUri;
    }
}
