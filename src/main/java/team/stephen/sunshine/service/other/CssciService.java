package team.stephen.sunshine.service.other;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.other.bean.Pagination;
import team.stephen.sunshine.model.other.bean.cssci.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author stephen
 * @date 2018/5/26
 */
public interface CssciService {
    int addJournal(CssciJournal journal);

    int addPaper(CssciPaper paper);

    int addPaperAuthorRelation(CssciPaperAuthorRel rel);

    int updatePaperSelective(CssciPaper cssciPaper);

    int addAuthor(CssciAuthor author);

    int addCitation(CssciCitation citation);

    int crawlOnePaper(String sno, Map<String, String> headers) throws IOException;

    Page<CssciPaper> selectPaper(CssciPaper paper, Pagination pagination);

    Page<String> selectToCrawl(Pagination pagination);
}
