package team.stephen.sunshine.web.dto.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import team.stephen.sunshine.web.dto.base.BaseColumnDto;

/**
 * @author Stephen
 * @date 2018/05/21 23:50
 */
@ApiModel(description = "栏目实体类")
public class AdminColumnDto extends BaseColumnDto {
    @ApiModelProperty(value = "栏目id")
    private Integer columnId;
    @ApiModelProperty(value = "channel dto")
    private AdminChannelDto channel;


    public AdminColumnDto() {

    }

    public AdminColumnDto(StandardColumnDto source) {
        super(source);
        setColumnId(source.getColumnId());
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public AdminChannelDto getChannel() {
        return channel;
    }

    public void setChannel(AdminChannelDto channel) {
        this.channel = channel;
    }
}