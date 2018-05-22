package team.stephen.sunshine.web.dto.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.web.dto.base.BaseChannelDto;

/**
 * @author Stephen
 * @date 2018/05/21 23:49
 */
public class FrontChannelDto extends BaseChannelDto {

    public FrontChannelDto() {
    }

    public FrontChannelDto(StandardChannelDto source) {
        super(source);
    }

    public FrontChannelDto(Channel source) {
        setChannelUri(source.getChannelUri());
        setChannelNameEn(source.getChannelNameEn());
        setChannelNameCn(source.getChannelNameCn());
    }
}
