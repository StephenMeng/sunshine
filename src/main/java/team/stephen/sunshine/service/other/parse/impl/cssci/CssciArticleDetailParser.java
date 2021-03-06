package team.stephen.sunshine.service.other.parse.impl.cssci;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import team.stephen.sunshine.model.other.bean.cssci.CssciAuthor;
import team.stephen.sunshine.model.other.bean.cssci.CssciPaper;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stephen on 2017/10/29.
 */
public class CssciArticleDetailParser implements Parser {
    @Override
    public List parse(String html) {
        List<CssciPaper> papers = new ArrayList<>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(html);
            JSONArray paperJArray = jsonObject.getJSONArray("contents");
            JSONObject object = paperJArray.getJSONObject(0);
            CssciPaper paper = JSONObject.toJavaObject(object, CssciPaper.class);
            if (paper != null) {
                normalize(paper);
                papers.add(paper);
            }
        } catch (Exception e) {
            LogRecord.error(e);
        }

        return papers;
    }

    private void normalize(CssciPaper cssciPaper) {
        cssciPaper.setAuthors(normalizeItem(cssciPaper.getAuthors()));
        cssciPaper.setByc(normalizeItem(cssciPaper.getByc()));
        cssciPaper.setAuthorsAddress(normalizeItem(cssciPaper.getAuthorsAddress()));
        cssciPaper.setAuthorsJg(normalizeItem(cssciPaper.getAuthorsJg()));
    }

    private String normalizeItem(String item) {
        String res = item.replaceAll("aaa", ";").replaceAll(";;;", ";").replaceAll(";;", ";");
        if (res.startsWith(";")) {
            return res.substring(1);
        }
        return res;
    }
}
