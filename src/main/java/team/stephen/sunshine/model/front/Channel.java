package team.stephen.sunshine.model.front;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Stephen
 * @date 2018/05/21 23:16
 */
@Entity
@Table(name = "sunshine_channel")
public class Channel {
    @Id
    @Column(name = "channel_id")
    private Integer channelId;
    @Column(name = "channel_uri")
    private String channelUri;
    @Column(name = "channel_name_cn")
    private String channelNameCn;
    @Column(name = "channel_name_en")
    private String channelNameEn;
    @Column(name = "has_column")
    private Boolean hasColumn;
    @Column(name = "deleted")
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
