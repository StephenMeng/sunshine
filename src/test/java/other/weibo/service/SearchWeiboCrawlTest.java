package other.weibo.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.Test;
import other.weibo.BaseWeiboCrawlTest;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchPageNumParserImpl;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchParser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboUserDetailParser;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;

import java.io.IOException;
import java.util.List;

/**
 * @author Stephen
 * @date 2019/04/06 12:18
 */
public class SearchWeiboCrawlTest extends BaseWeiboCrawlTest {
    @Test
    public void testCrawlSimpleHtml() {
        try {
            HttpResponse response = HttpUtils.httpGet(searchParam.getUrl(), searchParam.getHeaders());
            String html = IOUtils.toString(response.getEntity().getContent(), searchParam.getEncode());
            assert html.length() > 100000;
        } catch (IOException e) {
            LogRecord.error(e);
        }
    }

    @Test
    public void testCrawlSimpleHtmlResult() {
        try {
            searchParamBuilder.setPage(2);
            searchParam = searchParamBuilder.build();
            HttpResponse response = HttpUtils.httpGet(searchParam.getUrl(), searchParam.getHeaders());
            String html = IOUtils.toString(response.getEntity().getContent(), searchParam.getEncode());
            Parser parser = new WeiboSearchParser();
            List<Weibo> result = parser.parse(html);
            LogRecord.print(result.size());
            LogRecord.print(result);

        } catch (IOException | CrawlException e) {
            LogRecord.error(e);
        }
    }

    @Test
    public void testCrawlUserDetail() {
        try {
            HttpResponse response = HttpUtils.httpGet(userDetailParam.getUrl(), userDetailParam.getHeaders());
            String html = IOUtils.toString(response.getEntity().getContent(), userDetailParam.getEncode());
            Parser parser = new WeiboSearchParser();
            List<Weibo> result = parser.parse(html);
            LogRecord.print(result.size());
            LogRecord.print(result);

        } catch (IOException | CrawlException e) {
            LogRecord.error(e);
        }
    }

    @Test
    public void testCrawlSimpleHtmlPageNum() {
        try {
            HttpResponse response = HttpUtils.httpGet(searchParam.getUrl(), searchParam.getHeaders());
            String html = IOUtils.toString(response.getEntity().getContent(), searchParam.getEncode());
            Parser parser = new WeiboSearchPageNumParserImpl();
            List<Integer> result = parser.parse(html);
            assert result.get(0) == 14;
        } catch (IOException | CrawlException e) {
            LogRecord.error(e);
        }
    }

    @Test
    public void testCrawlUserInfo() {
        try {
            HttpResponse response = HttpUtils.httpGet(userDetailParam.getUrl(), userDetailParam.getHeaders());
            String html = IOUtils.toString(response.getEntity().getContent(), userDetailParam.getEncode());
            Parser parser = new WeiboUserDetailParser();
            List<WeiboUserConfig> result = parser.parse(html);
            assert result.get(0).getOid().equals(userDetailParam.getOid());
        } catch (IOException | CrawlException e) {
            LogRecord.error(e);
        }
    }
}
