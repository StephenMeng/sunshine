package team.stephen.sunshine.util.handler;

import team.stephen.sunshine.model.other.WeiboUserConfig;

/**
 * @author Stephen
 * @date 2018/06/04 19:28
 */
public class UrlHandler {
    private static String WeiboBaseUrl = "https://weibo.com";

    public static String getWeiboFirstPageUrl(WeiboUserConfig config, int page) {
        return WeiboBaseUrl + config.getUri() + "?pids=" + config.getPids() + "&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&ajaxpagelet=1&ajaxpagelet_v6=1";
    }

    public static String getWeiboPageBarUrl(WeiboUserConfig config, int page, int barNum) {
        return WeiboBaseUrl + "/p/aj/v6/mblog/mbloglist?ajwvr=6&domain="
                + config.getDomain() + "&is_search=0&visible=0&is_all=1&is_tag=0&profile_ftype=1&" +
                "page=" + page + "&pagebar=" + barNum + "&pl_name="
                + config.getPids() + "&id=" + config.getPageId() + "&script_uri="
                + config.getUri() + "&feed_type=0&pre_page=" + page + "&domain_op=" + config.getDomain();
    }
}
