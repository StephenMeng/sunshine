package team.stephen.sunshine.web.dto.front;

/**
 * 标准channeldto
 *
 * @author stephen
 * @date 2018/5/22
 */
public class StandardChannelDto {
    private Integer channelId;
    private String channelUri;
    private String channelNameCn;
    private String channelNameEn;
    private Boolean hasColumn;
    private Boolean deleted;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
