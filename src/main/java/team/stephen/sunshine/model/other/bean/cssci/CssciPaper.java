package team.stephen.sunshine.model.other.bean.cssci;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
@Builder
@Table(name = "cssci_paper")
public class CssciPaper {
    @Id
    @Column(name = "sno")
    private String sno;
    @Column(name = "id")
    private String id;
    @Column(name = "authors")
    private String authors;
    @Column(name = "authors_address")
    private String authorsAddress;
    @Column(name = "authors_jg")
    private String authorsJg;
    @Column(name = "blpm")
    private String blpm;
    @Column(name = "byc")
    private String byc;
    @Column(name = "dcbj")
    private String dcbj;
    @Column(name = "jjlb")
    private String jjlb;
    @Column(name = "juan")
    private String juan;
    @Column(name = "lrymc")
    private String lrymc;
    @Column(name = "lypm")
    private String lypm;
    @Column(name = "lypmp")
    private String lypmp;
    @Column(name = "nian")
    private String nian;
    @Column(name = "qi")
    private String qi;
    @Column(name = "qkdm")
    private String qkdm;
    @Column(name = "qkmc")
    private String qkmc;
    @Column(name = "qkno")
    private String qkno;
    @Column(name = "skdm")
    private String skdm;
    @Column(name = "wzlx")
    private String wzlx;
    @Column(name = "wzlx_z")
    private String wzlxZ;
    @Column(name = "xkdm1")
    private String xkdm1;
    @Column(name = "xkdm2")
    private String xkdm2;
    @Column(name = "xkfl1")
    private String xkfl1;
    @Column(name = "xkfl2")
    private String xkfl2;
    @Column(name = "xmlb")
    private String xmlb;
    @Column(name = "ycflag")
    private String ycflag;
    @Column(name = "yjdm")
    private String yjdm;
    @Column(name = "ym")
    private String ym;
    @Column(name = "ywsl")
    private String ywsl;
}
