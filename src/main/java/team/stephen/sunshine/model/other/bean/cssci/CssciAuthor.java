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
@Table(name = "cssci_author")
public class CssciAuthor {
    @Id
    @Column(name = "id")
    private String id;
    @Id
    @Column(name = "sno")
    private String sno;
    @Column(name = "bmmc")
    private String bmmc;
    @Column(name = "dcbj")
    private String dcbj;
    @Column(name = "fwxs")
    private String fwxs;
    @Column(name = "jglb1")
    private String jglb1;
    @Column(name = "jgmc")
    private String jgmc;
    @Column(name = "pyxm")
    private String pyxm;
    @Column(name = "sftt")
    private String sftt;
    @Column(name = "sxdm")
    private String sxdm;
    @Column(name = "txdz")
    private String txdz;
    @Column(name = "yzbm")
    private String yzbm;
    @Column(name = "zzdq")
    private String zzdq;
    @Column(name = "zzpm")
    private String zzpm;
    @Column(name = "yjdm")
    private String yjdm;
    @Column(name = "qklb")
    private String qklb;
    @Column(name = "xkfl2")
    private String xkfl2;

    @Column(name = "xkfl1")
    private String xkfl1;
    @Column(name = "nian")
    private String nian;
    @Column(name = "zzmc")
    private String zzmc;
}
