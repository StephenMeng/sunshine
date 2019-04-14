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

    public String getZzmc() {
        return zzmc;
    }

    public void setZzmc(String zzmc) {
        this.zzmc = zzmc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getDcbj() {
        return dcbj;
    }

    public void setDcbj(String dcbj) {
        this.dcbj = dcbj;
    }

    public String getFwxs() {
        return fwxs;
    }

    public void setFwxs(String fwxs) {
        this.fwxs = fwxs;
    }

    public String getJglb1() {
        return jglb1;
    }

    public void setJglb1(String jglb1) {
        this.jglb1 = jglb1;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getPyxm() {
        return pyxm;
    }

    public void setPyxm(String pyxm) {
        this.pyxm = pyxm;
    }

    public String getSftt() {
        return sftt;
    }

    public void setSftt(String sftt) {
        this.sftt = sftt;
    }

    public String getSxdm() {
        return sxdm;
    }

    public void setSxdm(String sxdm) {
        this.sxdm = sxdm;
    }

    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    public String getYzbm() {
        return yzbm;
    }

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    public String getZzdq() {
        return zzdq;
    }

    public void setZzdq(String zzdq) {
        this.zzdq = zzdq;
    }

    public String getZzpm() {
        return zzpm;
    }

    public void setZzpm(String zzpm) {
        this.zzpm = zzpm;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getYjdm() {
        return yjdm;
    }

    public void setYjdm(String yjdm) {
        this.yjdm = yjdm;
    }

    public String getQklb() {
        return qklb;
    }

    public void setQklb(String qklb) {
        this.qklb = qklb;
    }

    public String getXkfl2() {
        return xkfl2;
    }

    public void setXkfl2(String xkfl2) {
        this.xkfl2 = xkfl2;
    }

    public String getXkfl1() {
        return xkfl1;
    }

    public void setXkfl1(String xkfl1) {
        this.xkfl1 = xkfl1;
    }

    public String getNian() {
        return nian;
    }

    public void setNian(String nian) {
        this.nian = nian;
    }

    @Override
    public String toString() {
        return "CssciAuthor{" +
                "id='" + id + '\'' +
                ", sno='" + sno + '\'' +
                ", bmmc='" + bmmc + '\'' +
                ", dcbj='" + dcbj + '\'' +
                ", fwxs='" + fwxs + '\'' +
                ", jglb1='" + jglb1 + '\'' +
                ", jgmc='" + jgmc + '\'' +
                ", pyxm='" + pyxm + '\'' +
                ", sftt='" + sftt + '\'' +
                ", sxdm='" + sxdm + '\'' +
                ", txdz='" + txdz + '\'' +
                ", yzbm='" + yzbm + '\'' +
                ", zzdq='" + zzdq + '\'' +
                ", zzpm='" + zzpm + '\'' +
                ", yjdm='" + yjdm + '\'' +
                ", qklb='" + qklb + '\'' +
                ", xkfl2='" + xkfl2 + '\'' +
                ", xkfl1='" + xkfl1 + '\'' +
                ", nian='" + nian + '\'' +
                '}';
    }
}
