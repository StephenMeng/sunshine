package other.weibo.model;

import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import other.weibo.BaseWeiboCrawlTest;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.service.other.parse.Parser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchParser;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboUserDetailParser;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;

import java.io.*;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author Stephen
 * @date 2019/04/06 10:23
 */
public class WeiboCrawlParseTest extends BaseWeiboCrawlTest {
    private static String searchResultHtml;
    private static String userDetailHtml;
    private static final String ENCODE = "utf-8";

    @BeforeClass
    public static void initHtml() {
        try {
            InputStream in = Resources.getResource("./data/weibo.html").openStream();
            searchResultHtml = IOUtils.toString(in, ENCODE);
            in = Resources.getResource("./data/user.html").openStream();
            userDetailHtml = IOUtils.toString(in, ENCODE);
        } catch (IOException e) {
            LogRecord.error(e);
        }
    }

    @Test
    public void testCrawlSimpleHtml() {
        try {
            HttpResponse response = HttpUtils.httpGet(searchParam.getUrl(), searchParam.getHeaders());
            String html = IOUtils.toString(response.getEntity().getContent(), searchParam.getEncode());
            String p = Resources.getResource("./data/weibo.html").getPath();
            Writer writer = new FileWriter(new File(p));
            IOUtils.write(html, writer);
            assert html.length() > 100000;

        } catch (IOException e) {
            LogRecord.error(e);
        }
    }

    @Test
    public void testCrawlUserDetail() {
        try {
            HttpResponse response = HttpUtils.httpGet(userDetailParam.getUrl(), userDetailParam.getHeaders());
            String html = IOUtils.toString(response.getEntity().getContent(), userDetailParam.getEncode());
            String p = Resources.getResource("./data/user.html").getPath();
            Writer writer = new FileWriter(new File(p));
            IOUtils.write(html, writer);
            assert html.length() > 100000;

        } catch (IOException | CrawlException e) {
            LogRecord.error(e);
        }
    }

    @Test
    public void testParseSearchResult() {
        WeiboSearchParser parser = new WeiboSearchParser();
        List list = parser.parse(searchResultHtml);
        LogRecord.print(list);
        assert list.size() > 0;
    }

    @Test
    public void testParseUserDetail() {
        Parser parser = new WeiboUserDetailParser();
        List list = null;
        try {
            list = parser.parse(userDetailHtml);
        } catch (CrawlException e) {
            e.printStackTrace();
        }
        LogRecord.print(list);
        assert list.size() > 0;
    }
}
