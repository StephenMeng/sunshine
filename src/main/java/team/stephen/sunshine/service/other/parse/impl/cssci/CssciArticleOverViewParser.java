package team.stephen.sunshine.service.other.parse.impl.cssci;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import team.stephen.sunshine.model.other.CssciPaper;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by stephen on 2017/10/29.
 */
public class CssciArticleOverViewParser implements Parser {
    @Override
    public List parse(String html) {
        List<CssciPaper> articleList = Collections.emptyList();
        JSONObject jsonObject = JSONObject.parseObject(html);
        JSONArray jsonArray = jsonObject.getJSONArray("contents");
        if (jsonArray != null) {
            articleList = jsonArray.toJavaList(CssciPaper.class);
            articleList.forEach(this::normalize);
        }
        return articleList;
    }

    private void normalize(CssciPaper cssciPaper) {
        cssciPaper.setAuthors(normalizeItem(cssciPaper.getAuthors()));
        cssciPaper.setByc(normalizeItem(cssciPaper.getByc()));
    }

    private String normalizeItem(String item) {
        String res = item.replaceAll("aaa", ";").replaceAll(";;;", ";").replaceAll(";;", ";");
        if (res.startsWith(";")) {
            return res.substring(1);
        }
        return res;
    }
}
