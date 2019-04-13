package team.stephen.sunshine.service.other.parse.impl.weibo;

import com.google.common.collect.Lists;
import io.swagger.models.auth.In;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.service.other.parse.BaseParser;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author stephen
 * @date 2017/10/29
 */
public class WeiboSearchPageNumParserImpl extends BaseParser {
    @Override
    public List parse(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.select("div[class=m-page]").first();
        String result = parseItem(element, e -> String.valueOf(e.select("li").size()));
        Integer count = 1;
        if (StringUtils.isNotNull(result)) {
            count = Integer.parseInt(result);
        }
        return Lists.newArrayList(count);
    }
}
