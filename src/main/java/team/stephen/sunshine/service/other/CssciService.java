package team.stephen.sunshine.service.other;

import team.stephen.sunshine.model.other.CssciAuthor;
import team.stephen.sunshine.model.other.CssciCitation;
import team.stephen.sunshine.model.other.CssciJournal;
import team.stephen.sunshine.model.other.CssciPaper;

import java.io.IOException;
import java.util.Map;

/**
 * @author stephen
 * @date 2018/5/26
 */
public interface CssciService {
    int addJournal(CssciJournal journal);

    int addPaper(CssciPaper paper);

    int updatePaperSelective(CssciPaper cssciPaper);

    int addAuthor(CssciAuthor author);

    int addCitation(CssciCitation citation);

    int crawlOnePaper(String sno, Map<String, String> headers) throws IOException;
}
