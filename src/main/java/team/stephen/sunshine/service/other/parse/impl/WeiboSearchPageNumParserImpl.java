package team.stephen.sunshine.service.other.parse.impl;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by stephen on 2017/10/29.
 */
public class WeiboSearchPageNumParserImpl implements Parser {
    @Override
    public List parse(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.select("div[class=m-page]").first();
        Integer result;
        try {
            result = element.select("li").size();
        } catch (Exception e) {
            result = 1;
            LogRecord.error("could not get record page num,or may be one");
        }
        return Lists.newArrayList(result);
    }
}
