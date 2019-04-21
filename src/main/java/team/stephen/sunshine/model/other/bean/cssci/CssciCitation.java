package team.stephen.sunshine.model.other.bean.cssci;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author stephen
 * @date 2018/5/26
 */
@Data
@Entity
@Table(name = "cssci_citation")
public class CssciCitation {
    @Id
    @Column(name = "id")
    private String id;
    @Id
    @Column(name = "sno")
    private String sno;
    @Column(name = "by_tag")
    private String byTag;
    @Column(name = "bynd")
    private String bynd;
    @Column(name = "dcbj")
    private String dcbj;
    @Column(name = "jcxh")
    private String jcxh;
    @Column(name = "qk_tag")
    private String qkTag;
    @Column(name = "qkdm")
    private String qkdm;
    @Column(name = "qkno")
    private String qkno;

    @Column(name = "wzdm")
    private String wzdm;
    @Column(name = "yw_id")
    private String ywId;
    @Column(name = "ywcbd")
    private String ywcbd;
    @Column(name = "ywcbs")
    private String ywcbs;
    @Column(name = "ywcc")
    private String ywcc;
    @Column(name = "ywlx")
    private String ywlx;
    @Column(name = "ywnd")
    private String ywnd;
    @Column(name = "ywno")
    private String ywno;
    @Column(name = "ywpm")
    private String ywpm;
    @Column(name = "ywpm_p")
    private String ywpmP;
    @Column(name = "ywqk")
    private String ywqk;
    @Column(name = "ywqk_id")
    private String ywqkId;
    @Column(name = "ywxj")
    private String ywxj;
    @Column(name = "ywym")
    private String ywym;
    @Column(name = "ywyz")
    private String ywyz;
    @Column(name = "ywzz")
    private String ywzz;
    @Column(name = "ywzz_p")
    private String ywzzP;
    @Column(name = "yylb")
    private String yylb;

}
