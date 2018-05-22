package team.stephen.sunshine.model.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 附件实体类
 *
 * @author stephen
 * @date 2018/5/22
 */
@Entity
@Table(name = "sunshine_attachment")
public class Attachment {
    @Id
    @Column(name = "att_id")
    private Integer attId;
    @Column(name = "att_type")
    private String attType;
    @Column(name = "uploader")
    private Integer uploader;
    @Column(name = "owner_id")
    private Integer ownerId;
    @Column(name = "owner_type")
    private Integer ownerType;
    @Column(name = "att_name")
    private String attName;
    @Column(name = "att_uri")
    private String attUri;
    @Column(name = "att_length")
    private Long attLength;
    @Column(name = "upload_date")
    private Date uploadDate;
    @Column(name = "update_date")
    private Date updateDate;

    public Integer getAttId() {
        return attId;
    }

    public void setAttId(Integer attId) {
        this.attId = attId;
    }

    public String getAttType() {
        return attType;
    }

    public void setAttType(String attType) {
        this.attType = attType;
    }

    public Integer getUploader() {
        return uploader;
    }

    public void setUploader(Integer uploader) {
        this.uploader = uploader;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    public String getAttName() {
        return attName;
    }

    public void setAttName(String attName) {
        this.attName = attName;
    }

    public String getAttUri() {
        return attUri;
    }

    public void setAttUri(String attUri) {
        this.attUri = attUri;
    }

    public Long getAttLength() {
        return attLength;
    }

    public void setAttLength(Long attLength) {
        this.attLength = attLength;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
