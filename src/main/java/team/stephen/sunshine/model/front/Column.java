package team.stephen.sunshine.model.front;


import io.swagger.models.auth.In;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Stephen
 * @date 2018/05/21 23:16
 */
@Entity
@Table(name = "sunshine_column")
public class Column {
    @Id
    @javax.persistence.Column(name = "column_id")
    private Integer columnId;
    @javax.persistence.Column(name = "channel_id")
    private Integer channelId;
    @javax.persistence.Column(name = "column_uri")
    private String columnUri;
    @javax.persistence.Column(name = "column_name_cn")
    private String columnNameCn;
    @javax.persistence.Column(name = "column_name_en")
    private String columnNameEn;
    @javax.persistence.Column(name = "deleted")
    private Boolean deleted;

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public String getColumnUri() {
        return columnUri;
    }

    public void setColumnUri(String columnUri) {
        this.columnUri = columnUri;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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
