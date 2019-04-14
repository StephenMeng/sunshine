package team.stephen.sunshine.service.other.parse.impl.cssci;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import team.stephen.sunshine.model.other.bean.cssci.CssciAuthor;
import team.stephen.sunshine.model.other.bean.cssci.CssciCitation;
import team.stephen.sunshine.model.other.bean.cssci.CssciPaper;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.Collections;
import java.util.List;

/**
 * Created by stephen on 2017/10/29.
 */
public class CssciArticleCitationParser implements Parser {
    @Override
    public List parse(String html) {
        List<CssciCitation> citationList = Collections.emptyList();
        JSONObject jsonObject = JSONObject.parseObject(html);

        JSONArray citationJArray = jsonObject.getJSONArray("catation");
        if (citationJArray != null) {
            try {
                citationList = citationJArray.toJavaList(CssciCitation.class);
            } catch (Exception e) {
                LogRecord.error(e);
            }
        }
        return citationList;
    }

}
