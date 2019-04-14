package team.stephen.sunshine.service.other.parse.impl.cssci;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import team.stephen.sunshine.model.other.bean.Pagination;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;

import java.util.List;

/**
 * Created by stephen on 2017/10/29.
 */
public class CssciPageParser implements Parser {
    @Override
    public List parse(String html) {
        Pagination res = new Pagination();
        try {
            JSONObject jsonObject = JSONObject.parseObject(html);
            String totalStr = jsonObject.getString("totalfound");
            res.setTotal(Integer.parseInt(totalStr));
        } catch (Exception e) {
            LogRecord.error(e);
        }
        return Lists.newArrayList(res);
    }

}
