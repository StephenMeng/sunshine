package team.stephen.sunshine.web.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author Stephen
 * @date 2018/05/26 14:54
 */
@ApiModel(description = "用户教育信息")
public class UserEducationDto {
    @ApiModelProperty(value = "eid")
    private Integer eId;
    @ApiModelProperty(value = "user_id")
    private Integer userId;
    @ApiModelProperty(value = "degree")
    private String degree;
    @ApiModelProperty(value = "major")
    private String major;
    @ApiModelProperty(value = "school")
    private String school;
    @ApiModelProperty(value = "note")
    private String note;
    @ApiModelProperty(value = "start_date")
    private Date startDate;
    @ApiModelProperty(value = "end_date")
    private Date endDate;

    public Integer geteId() {
        return eId;
    }

    public void seteId(Integer eId) {
        this.eId = eId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
