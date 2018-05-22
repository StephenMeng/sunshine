package team.stephen.sunshine.web.dto.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import team.stephen.sunshine.web.dto.front.StandardColumnDto;

/**
 * @author Stephen
 * @date 2018/05/21 23:50
 */
@ApiModel(description = "栏目实体类")
public class BaseColumnDto {
    @ApiModelProperty(value = "栏目URI")
    private String columnUri;
    @ApiModelProperty(value = "栏目中文名称")
    private String columnNameCn;
    @ApiModelProperty(value = "栏目英文名称")
    private String columnNameEn;

    public BaseColumnDto() {

    }

    public BaseColumnDto(StandardColumnDto source) {
        setColumnNameCn(source.getColumnNameCn());
        setColumnNameEn(source.getColumnNameEn());
        setColumnUri(source.getColumnUri());
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

}
