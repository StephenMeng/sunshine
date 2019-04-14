package team.stephen.sunshine.service.other.parse.impl.weibo;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import team.stephen.sunshine.service.other.parse.BaseParser;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.List;

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
