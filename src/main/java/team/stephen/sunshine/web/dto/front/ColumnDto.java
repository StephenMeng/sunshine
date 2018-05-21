package team.stephen.sunshine.web.dto.front;

/**
 * @author Stephen
 * @date 2018/05/21 23:50
 */
public class ColumnDto {
    private Integer columnId;
    private Integer channelId;
    private String columnNameCn;
    private String columnNameEn;

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

}
