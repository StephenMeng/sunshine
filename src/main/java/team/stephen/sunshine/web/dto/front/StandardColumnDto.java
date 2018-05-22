package team.stephen.sunshine.web.dto.front;

/**
 * 标准columndto
 *
 * @author stephen
 * @date 2018/5/22
 */
public class StandardColumnDto {
    private Integer columnId;
    private Integer channelId;
    private String columnUri;
    private String columnNameCn;
    private String columnNameEn;
    private Boolean deleted;

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getColumnUri() {
        return columnUri;
    }

    public void setColumnUri(String columnUri) {
        this.columnUri = columnUri;
    }

    public String getColumnNameCn() {
        return columnNameCn;
    }

    public void setColumnNameCn(String columnNameCn) {
        this.columnNameCn = columnNameCn;
    }

    public String getColumnNameEn() {
        return columnNameEn;
    }

    public void setColumnNameEn(String columnNameEn) {
        this.columnNameEn = columnNameEn;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
