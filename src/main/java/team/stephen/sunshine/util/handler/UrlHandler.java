package team.stephen.sunshine.util.handler;

import team.stephen.sunshine.model.other.bean.weibo.WeiboUserConfig;
import team.stephen.sunshine.util.element.StringUtils;

/**
 * @author Stephen
 * @date 2018/06/04 19:28
 */
public class UrlHandler {
    private static String WeiboBaseUrl = "https://weibo.com";

    public static String getWeiboFirstPageUrl(WeiboUserConfig config, int page) {
        return WeiboBaseUrl + config.getUri() + "?pids=" + config.getPids() + "&is_search="+(StringUtils.isNull(config.getKeyword())?"0":"1")+"&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&ajaxpagelet=1&ajaxpagelet_v6=1"+ (StringUtils.isNull(config.getKeyword())?"":("&key_word="+config.getKeyword()));
    }

    public static String getWeiboPageBarUrl(WeiboUserConfig config, int page, int barNum) {
        return WeiboBaseUrl + "/p/aj/v6/mblog/mbloglist?ajwvr=6&domain="
                + config.getDomain() + "&is_search="+(StringUtils.isNull(config.getKeyword())?"0":"1")+"&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&pagebar=" + barNum + "&pl_name="
                + config.getPids() + "&id=" + config.getPageId() + "&script_uri="
                + config.getUri() + "&feed_type=0&pre_page=" + page + "&domain_op=" + config.getDomain()
                + (StringUtils.isNull(config.getKeyword())?"":("&key_word="+config.getKeyword()))
                ;
    }
}
