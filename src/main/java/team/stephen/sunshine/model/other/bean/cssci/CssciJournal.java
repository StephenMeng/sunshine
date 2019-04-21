package team.stephen.sunshine.model.other.bean.cssci;

import lombok.Data;

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
@Data
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
}
