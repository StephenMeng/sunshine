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
@Table(name = "cssci_paper_author_rel")
public class CssciPaperAuthorRel {
    @Id
    @Column(name = "author_id")
    private String authorId;
    @Id
    @Column(name = "sno")
    private String sno;
}
