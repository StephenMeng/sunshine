package team.stephen.sunshine.model.other;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * cssci的期刊信息
 *
 * @author stephen
 * @date 2018/5/26
 */
@Entity
@Table(name = "cssci_journal")
public class CssciJournal {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "address")
    private String address;
    @Column(name = "company")
    private String company;
    @Column(name = "creat_pubdate")
    private String creatPubdate;
    @Column(name = "email")
    private String email;
    @Column(name = "gg")
    private String gg;
    @Column(name = "hx")
    private String hx;
    @Column(name = "issn")
    private String issn;
    @Column(name = "kb")
    private String kb;
    @Column(name = "lan")
    private String lan;
    @Column(name = "old_name")
    private String oldName;
    @Column(name = "post")
    private String post;
    @Column(name = "press")
    private String press;
    @Column(name = "qkType")
    private String qkType;
    @Column(name = "qk_img")
    private String qkImg;
    @Column(name = "tag")
    private String tag;
    @Column(name = "tel")
    private String tel;
    @Column(name = "title_c")
    private String titleC;
    @Column(name = "title_e")
    private String titleE;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCreatPubdate() {
        return creatPubdate;
    }

    public void setCreatPubdate(String creatPubdate) {
        this.creatPubdate = creatPubdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getHx() {
        return hx;
    }

    public void setHx(String hx) {
        this.hx = hx;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getKb() {
        return kb;
    }

    public void setKb(String kb) {
        this.kb = kb;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getQkType() {
        return qkType;
    }

    public void setQkType(String qkType) {
        this.qkType = qkType;
    }

    public String getQkImg() {
        return qkImg;
    }

    public void setQkImg(String qkImg) {
        this.qkImg = qkImg;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTitleC() {
        return titleC;
    }

    public void setTitleC(String titleC) {
        this.titleC = titleC;
    }

    public String getTitleE() {
        return titleE;
    }

    public void setTitleE(String titleE) {
        this.titleE = titleE;
    }

    @Override
    public String toString() {
        return "CssciJournal{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", company='" + company + '\'' +
                ", creatPubdate='" + creatPubdate + '\'' +
                ", email='" + email + '\'' +
                ", gg='" + gg + '\'' +
                ", hx='" + hx + '\'' +
                ", issn='" + issn + '\'' +
                ", kb='" + kb + '\'' +
                ", lan='" + lan + '\'' +
                ", oldName='" + oldName + '\'' +
                ", post='" + post + '\'' +
                ", press='" + press + '\'' +
                ", qkType='" + qkType + '\'' +
                ", qkImg='" + qkImg + '\'' +
                ", tag='" + tag + '\'' +
                ", tel='" + tel + '\'' +
                ", titleC='" + titleC + '\'' +
                ", titleE='" + titleE + '\'' +
                '}';
    }
}
