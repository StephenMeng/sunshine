package team.stephen.sunshine.model.other.bean.cssci;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author stephen
 * @date 2018/5/26
 */
@Entity
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getAuthorsAddress() {
        return authorsAddress;
    }

    public void setAuthorsAddress(String authorsAddress) {
        this.authorsAddress = authorsAddress;
    }

    public String getAuthorsJg() {
        return authorsJg;
    }

    public void setAuthorsJg(String authorsJg) {
        this.authorsJg = authorsJg;
    }

    public String getBlpm() {
        return blpm;
    }

    public void setBlpm(String blpm) {
        this.blpm = blpm;
    }

    public String getByc() {
        return byc;
    }

    public void setByc(String byc) {
        this.byc = byc;
    }

    public String getDcbj() {
        return dcbj;
    }

    public void setDcbj(String dcbj) {
        this.dcbj = dcbj;
    }

    public String getJjlb() {
        return jjlb;
    }

    public void setJjlb(String jjlb) {
        this.jjlb = jjlb;
    }

    public String getJuan() {
        return juan;
    }

    public void setJuan(String juan) {
        this.juan = juan;
    }

    public String getLrymc() {
        return lrymc;
    }

    public void setLrymc(String lrymc) {
        this.lrymc = lrymc;
    }

    public String getLypm() {
        return lypm;
    }

    public void setLypm(String lypm) {
        this.lypm = lypm;
    }

    public String getLypmp() {
        return lypmp;
    }

    public void setLypmp(String lypmp) {
        this.lypmp = lypmp;
    }

    public String getNian() {
        return nian;
    }

    public void setNian(String nian) {
        this.nian = nian;
    }

    public String getQi() {
        return qi;
    }

    public void setQi(String qi) {
        this.qi = qi;
    }

    public String getQkdm() {
        return qkdm;
    }

    public void setQkdm(String qkdm) {
        this.qkdm = qkdm;
    }

    public String getQkmc() {
        return qkmc;
    }

    public void setQkmc(String qkmc) {
        this.qkmc = qkmc;
    }

    public String getQkno() {
        return qkno;
    }

    public void setQkno(String qkno) {
        this.qkno = qkno;
    }

    public String getSkdm() {
        return skdm;
    }

    public void setSkdm(String skdm) {
        this.skdm = skdm;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getWzlx() {
        return wzlx;
    }

    public void setWzlx(String wzlx) {
        this.wzlx = wzlx;
    }

    public String getWzlxZ() {
        return wzlxZ;
    }

    public void setWzlxZ(String wzlxZ) {
        this.wzlxZ = wzlxZ;
    }

    public String getXkdm1() {
        return xkdm1;
    }

    public void setXkdm1(String xkdm1) {
        this.xkdm1 = xkdm1;
    }

    public String getXkdm2() {
        return xkdm2;
    }

    public void setXkdm2(String xkdm2) {
        this.xkdm2 = xkdm2;
    }

    public String getXkfl1() {
        return xkfl1;
    }

    public void setXkfl1(String xkfl1) {
        this.xkfl1 = xkfl1;
    }

    public String getXkfl2() {
        return xkfl2;
    }

    public void setXkfl2(String xkfl2) {
        this.xkfl2 = xkfl2;
    }

    public String getXmlb() {
        return xmlb;
    }

    public void setXmlb(String xmlb) {
        this.xmlb = xmlb;
    }

    public String getYcflag() {
        return ycflag;
    }

    public void setYcflag(String ycflag) {
        this.ycflag = ycflag;
    }

    public String getYjdm() {
        return yjdm;
    }

    public void setYjdm(String yjdm) {
        this.yjdm = yjdm;
    }

    public String getYm() {
        return ym;
    }

    public void setYm(String ym) {
        this.ym = ym;
    }

    public String getYwsl() {
        return ywsl;
    }

    public void setYwsl(String ywsl) {
        this.ywsl = ywsl;
    }

    @Override
    public String toString() {
        return "CssciPaper{" +
                "sno='" + sno + '\'' +
                ", id='" + id + '\'' +
                ", authors='" + authors + '\'' +
                ", authorsAddress='" + authorsAddress + '\'' +
                ", authorsJg='" + authorsJg + '\'' +
                ", blpm='" + blpm + '\'' +
                ", byc='" + byc + '\'' +
                ", dcbj='" + dcbj + '\'' +
                ", jjlb='" + jjlb + '\'' +
                ", juan='" + juan + '\'' +
                ", lrymc='" + lrymc + '\'' +
                ", lypm='" + lypm + '\'' +
                ", lypmp='" + lypmp + '\'' +
                ", nian='" + nian + '\'' +
                ", qi='" + qi + '\'' +
                ", qkdm='" + qkdm + '\'' +
                ", qkmc='" + qkmc + '\'' +
                ", qkno='" + qkno + '\'' +
                ", skdm='" + skdm + '\'' +
                ", wzlx='" + wzlx + '\'' +
                ", wzlxZ='" + wzlxZ + '\'' +
                ", xkdm1='" + xkdm1 + '\'' +
                ", xkdm2='" + xkdm2 + '\'' +
                ", xkfl1='" + xkfl1 + '\'' +
                ", xkfl2='" + xkfl2 + '\'' +
                ", xmlb='" + xmlb + '\'' +
                ", ycflag='" + ycflag + '\'' +
                ", yjdm='" + yjdm + '\'' +
                ", ym='" + ym + '\'' +
                ", ywsl='" + ywsl + '\'' +
                '}';
    }
}
