package team.stephen.sunshine.web.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.web.dto.front.StandardChannelDto;

/**
 * @author stephen
 * @date 2018/5/22
 */
@ApiModel(description = "频道实体类")
public class BaseChannelDto {
    @ApiModelProperty(value = "频道URI")
    private String channelUri;
    @ApiModelProperty(value = "频道中文名称")
    private String channelNameCn;
    @ApiModelProperty(value = "频道英文名称")
    private String channelNameEn;
    @ApiModelProperty(value = "是否有子栏目")
    private Boolean hasColumn;

    public BaseChannelDto() {

    }

    public BaseChannelDto(StandardChannelDto source) {
        setChannelNameCn(source.getChannelNameCn());
        setChannelNameEn(source.getChannelNameEn());
        setChannelUri(source.getChannelUri());
        setHasColumn(source.getHasColumn());
    }

    public String getChannelUri() {
        return channelUri;
    }

    public void setChannelUri(String channelUri) {
        this.channelUri = channelUri;
    }

    public String getChannelNameCn() {
        return channelNameCn;
    }

    public void setChannelNameCn(String channelNameCn) {
        this.channelNameCn = channelNameCn;
    }

    public String getChannelNameEn() {
        return channelNameEn;
    }

    public void setChannelNameEn(String channelNameEn) {
        this.channelNameEn = channelNameEn;
    }

    public Boolean getHasColumn() {
        return hasColumn;
    }

    public void setHasColumn(Boolean hasColumn) {
        this.hasColumn = hasColumn;
    }
}
