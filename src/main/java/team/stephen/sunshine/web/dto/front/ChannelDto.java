package team.stephen.sunshine.web.dto.front;


/**
 * @author Stephen
 * @date 2018/05/21 23:49
 */
public class ChannelDto {
    private Integer channelId;
    private String channelNameCn;
    private String channelNameEn;
    private Boolean hasColumn;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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
