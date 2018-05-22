package team.stephen.sunshine.web.dto.front;

import team.stephen.sunshine.web.dto.base.BaseColumnDto;

/**
 * @author Stephen
 * @date 2018/05/21 23:50
 */
public class FrontColumnDto extends BaseColumnDto {
    private FrontChannelDto channel;

    public FrontColumnDto() {
    }

    public FrontColumnDto(StandardColumnDto source) {
        super(source);
    }

    public FrontChannelDto getChannel() {
        return channel;
    }

    public void setChannel(FrontChannelDto channel) {
        this.channel = channel;
    }
}
