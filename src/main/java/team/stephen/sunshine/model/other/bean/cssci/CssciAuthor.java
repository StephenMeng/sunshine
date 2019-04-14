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
}
