package team.stephen.sunshine.service.other.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.other.CssciAuthorDao;
import team.stephen.sunshine.dao.other.CssciCitationDao;
import team.stephen.sunshine.dao.other.CssciJournalDao;
import team.stephen.sunshine.dao.other.CssciPaperDao;
import team.stephen.sunshine.model.other.bean.cssci.CssciAuthor;
import team.stephen.sunshine.model.other.bean.cssci.CssciCitation;
import team.stephen.sunshine.model.other.bean.cssci.CssciJournal;
import team.stephen.sunshine.model.other.bean.cssci.CssciPaper;
import team.stephen.sunshine.service.other.CssciService;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author stephen
 * @date 2018/5/26
 */
@Service
public class CssciServiceImpl implements CssciService {
    @Autowired
    private CssciJournalDao cssciJournalDao;
    @Autowired
    private CssciPaperDao cssciPaperDao;
    @Autowired
    private CssciAuthorDao cssciAuthorDao;
    @Autowired
    private CssciCitationDao cssciCitationDao;

    @Override
    public int addJournal(CssciJournal journal) {
        return cssciJournalDao.insert(journal);
    }

    @Override
    public int addPaper(CssciPaper paper) {
        try {
            return cssciPaperDao.insert(paper);
        } catch (Exception e) {
            LogRecord.error(e);
            return -1;
        }
    }

    @Override
    public int updatePaperSelective(CssciPaper cssciPaper) {
        return cssciPaperDao.updateByPrimaryKeySelective(cssciPaper);
    }

    @Override
    public int addAuthor(CssciAuthor author) {
        return cssciAuthorDao.insert(author);
    }

    @Override
    public int addCitation(CssciCitation citation) {
        return cssciCitationDao.insert(citation);
    }

    @Override
    public int crawlOnePaper(String sno, Map<String, String> headers) throws IOException {
        String url = "http://cssci.nju.edu.cn/control/controllers.php?control=search&action=source_id&id=" + sno;
        String html = HttpUtils.okrHttpGet(url, headers);
        JSONObject jsonObject = JSONObject.parseObject(html);

        JSONArray authorJArray = jsonObject.getJSONArray("author");
        List<CssciAuthor> authorList = null;
        if (authorJArray != null) {
            authorList = authorJArray.toJavaList(CssciAuthor.class);
            authorList.forEach(author -> {
                try {
                    addAuthor(author);
                } catch (Exception e) {
                    LogRecord.error(e);
                }
            });
        }
        JSONArray citationJArray = jsonObject.getJSONArray("catation");
        if (citationJArray != null) {
            List<CssciCitation> citationList = citationJArray.toJavaList(CssciCitation.class);
            citationList.forEach(citation -> {
                try {
                    addCitation(citation);
                } catch (Exception e) {
                    LogRecord.error(e);
                }
            });
        }
        JSONArray paperJArray = jsonObject.getJSONArray("contents");
        try {
            JSONObject object = paperJArray.getJSONObject(0);
            CssciPaper paper = JSONObject.toJavaObject(object, CssciPaper.class);
            if (authorList != null) {
                List<String> aids = authorList.stream().map(CssciAuthor::getId).collect(Collectors.toList());
                String authorIds = Joiner.on(";").join(aids);
                paper.setAuthorsId(authorIds);
            }
            updatePaperSelective(paper);
        } catch (Exception e) {
            LogRecord.error(e);
        }
        return 0;
    }
}
