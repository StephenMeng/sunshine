package team.stephen.sunshine.service.other.parse.impl.weibo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import team.stephen.sunshine.model.other.bean.weibo.WeiboUserConfig;
import team.stephen.sunshine.model.other.bean.weibo.UserDetailParam;
import team.stephen.sunshine.service.other.parse.BaseParser;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author stephen
 * @date 2017/10/29
 */
public class WeiboUserDetailParser extends BaseParser {
    private static final Pattern PID_PATTER = Pattern.compile("Pl_Official_MyProfileFeed__(.*?)\"");
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("domain'\\]='(.*?)'");
    private static final Pattern OID_PATTERN = Pattern.compile("'oid'\\]='(.*?)'");
    private static final Pattern PAGE_ID_PATTERN = Pattern.compile("'page_id'\\]='(.*?)'");
    private static final Pattern TITLE_PATTERN = Pattern.compile("'title_value'\\]='(.*?)'");
    private static final Pattern STATISTIC_PATTERN = Pattern.compile("\\{\"ns(.*?)>关注<(.*?)>粉丝<(.*?)>微博<(.*?)}");
    private static final Pattern SEX_PATTER = Pattern.compile("\"ns\":\"pl.nav.index(.*?)}");
    private static final Pattern CORE_USER_PATTER = Pattern.compile("\"domid\":\"Pl_Core_UserInfo__6(.*?)}");

    @Override
    public List parse(String html) {
        //获取用户的uri
        WeiboUserConfig config = new WeiboUserConfig();
        config.setPids(parsePatternItem(html, PID_PATTER).replace("\\\"", ""));
        config.setDomain(parsePatternItem(html, DOMAIN_PATTERN).replace("domain']='", "").replace("'", ""));
        config.setOid(parsePatternItem(html, OID_PATTERN).replace("oid']='", "").replace("'", ""));
        config.setPageId(parsePatternItem(html, PAGE_ID_PATTERN).replace("page_id']='", "").replace("'", ""));
        config.setName(parsePatternItem(html, TITLE_PATTERN).replace("title_value']='", "").replace("'", ""));
        config.setUri(UserDetailParam.PREFIX + config.getOid());

        Matcher statisticMatcher = STATISTIC_PATTERN.matcher(html);
        if (statisticMatcher.find()) {
            String json = statisticMatcher.group();
            String resultHtml = getHtmlFromJson(json, "html");
            Document document = Jsoup.parse(resultHtml);

            Elements elements = document.select("strong");
            String follow = parseItemFromList(elements, 0, Element::text);
            String fans = parseItemFromList(elements, 1, Element::text);
            String weiboNum = parseItemFromList(elements, 2, Element::text);

            config.setWeiboNum(StringUtils.isNumeric(weiboNum) ? Integer.parseInt(weiboNum) : null);
            config.setFansNum(StringUtils.isNumeric(fans) ? Integer.parseInt(fans) : null);
            config.setFollowNum(StringUtils.isNumeric(follow) ? Integer.parseInt(follow) : null);
        }
        Matcher sexMatcher = SEX_PATTER.matcher(html);
        if (sexMatcher.find()) {
            String json = sexMatcher.group();
            json = "{" + json;
            Document document = Jsoup.parse(getHtmlFromJson(json, "html"));
            Elements sexs = document.select("span[class=S_txt1 t_link]");
            config.setSex(parseItemFromList(sexs, 0, e -> {
                String sexStr = e.text();
                if (sexStr.contains("她")) {
                    return "女";
                } else {
                    return "男";
                }
            }));

        }
        Matcher coreUserMatcher = CORE_USER_PATTER.matcher(html);
        if (coreUserMatcher.find()) {
            try {
                String json = coreUserMatcher.group();
                json = "{" + json;
                Document document = Jsoup.parse(getHtmlFromJson(json, "html"));

                Elements lvEs = document.select("span[class=icon_group S_line1 W_fl]");
                config.setlv(parseItemFromList(lvEs, 0, Element::text));

                Elements desEs = document.select("p[class=info]");
                config.setDescription(parseItemFromList(desEs, 0, Element::text));

                Element detailE = document.select("div[class=WB_innerwrap]").first();
                Elements lisE = detailE.select("li");
                for (Element liE : lisE) {
                    String item = liE.text();
                    if (liE.html().contains("cd_place")) {
                        config.setPlace(item.replace("2 ", ""));
                    }
                    if (liE.html().contains("ficon_constellation ")) {
                        config.setBirthday(item.replace("ö ", ""));
                    }
                    if (liE.html().contains("ficon_pinfo ")) {
                        config.setPinfo(StringUtils.filterEmoji(item.replace("Ü ", "")));
                    }
                    if (liE.html().contains("ficon_link")) {
                        config.setLink(item.replace("5 ", "") + ":" + liE.select("a").first().attr("href"));
                    }
                    if (liE.html().contains("pinfo_icon_baidu")) {
                        config.setBaike(item);
                    }
                    if (liE.html().contains("ficon_bag")) {
                        config.setCareer(item.replace("3 ", ""));
                    }
                    if (liE.html().contains("ficon_cd_coupon")) {
                        config.setTag(item.replace("T ", ""));
                    }
                }
            } catch (Exception e) {
                LogRecord.error(e);
            }
        }

        if (StringUtils.isNull(config.getName())) {
            return Collections.emptyList();
        }
        return Lists.newArrayList(config);
    }

    private String parsePatternItem(String html, Pattern pattern) {
        Matcher pidMatcher = pattern.matcher(html);
        if (pidMatcher.find()) {
            return pidMatcher.group(0);
        }
        return EMPTY;
    }

    private String getHtmlFromJson(String html, String data) {
        JSONObject jsonObject = JSONObject.parseObject(html);
        return jsonObject.getString(data);
    }

}
