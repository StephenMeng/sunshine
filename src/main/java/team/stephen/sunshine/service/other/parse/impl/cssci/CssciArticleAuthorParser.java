package team.stephen.sunshine.service.other.parse.impl.cssci;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import team.stephen.sunshine.model.other.bean.cssci.CssciAuthor;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.Collections;
import java.util.List;

/**
 * Created by stephen on 2017/10/29.
 */
public class CssciArticleAuthorParser implements Parser<CssciAuthor> {
    @Override
    public List<CssciAuthor> parse(String html) {
        List<CssciAuthor> authorList = Collections.emptyList();
        JSONObject jsonObject = JSONObject.parseObject(html);

        JSONArray authorJArray = jsonObject.getJSONArray("author");
        if (authorJArray != null) {
            try {
                authorList = authorJArray.toJavaList(CssciAuthor.class);
            } catch (Exception e) {
                LogRecord.error(e);
            }
        }
        return authorList;
    }
}
