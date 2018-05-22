package team.stephen.sunshine.web.dto.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.web.dto.base.BaseChannelDto;

/**
 * @author Stephen
 * @date 2018/05/21 23:49
 */
@ApiModel(description = "频道实体类")
public class AdminChannelDto extends BaseChannelDto {
    @ApiModelProperty(value = "频道ID")
    private Integer channelId;

    public AdminChannelDto() {
    }

    public AdminChannelDto(StandardChannelDto source) {
        super(source);
        setChannelId(source.getChannelId());
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }
}
