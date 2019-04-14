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
@Table(name = "cssci_paper_author_rel")
public class CssciPaperAuthorRel {
    @Id
    @Column(name = "author_id")
    private String authorId;
    @Id
    @Column(name = "sno")
    private String sno;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    @Override
    public String toString() {
        return "CssciPaperAuthorRel{" +
                "authorId='" + authorId + '\'' +
                ", sno='" + sno + '\'' +
                '}';
    }
}
