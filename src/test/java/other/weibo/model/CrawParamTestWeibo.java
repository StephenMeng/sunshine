package other.weibo.model;

import org.junit.Test;
import other.weibo.BaseWeiboCrawlTest;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.bean.weibo.AdvanceType;
import team.stephen.sunshine.util.common.LogRecord;

import java.text.ParseException;

/**
 * @author Stephen
 * @date 2019/04/06 10:23
 */
public class CrawParamTestWeibo extends BaseWeiboCrawlTest {


    @Test
    public void testInitCrawParamDate() throws ParseException, CrawlException {
        searchParam = searchParamBuilder.build();
        assert searchParam.getUrl().contains("timescope=custom:2017-07-11:2017-07-20");
    }

    @Test
    public void testInitCrawParamEmptyKeywordError() throws ParseException, CrawlException {
        searchParamBuilder.setKeyword(null);
        searchParam = searchParamBuilder.build();
        thrown.expect(CrawlException.class);
        thrown.expectMessage("key word is empty!");
        searchParam.getUrl();
    }

    @Test
    public void testInitCrawParamEmptyKeywordChina() throws ParseException, CrawlException {
        searchParam = searchParamBuilder.build();
        assert searchParam.getUrl().contains("q=china");
    }

    @Test
    public void testInitCrawParamSearchTypeHot() throws ParseException, CrawlException {
        searchParamBuilder.setType(AdvanceType.HOT);
        searchParam = searchParamBuilder.build();
        assert searchParam.getUrl().contains("xsort=hot");
    }

    @Test
    public void testInitCrawParamSearchTypeAll() throws ParseException, CrawlException {
        searchParamBuilder.setType(AdvanceType.ALL);
        searchParam = searchParamBuilder.build();

        assert searchParam.getUrl().contains("typeall=1");
    }

    @Test
    public void testInitCrawParamSearchTypeNull() throws ParseException, CrawlException {
        searchParamBuilder.setType(null);
        searchParam = searchParamBuilder.build();

        assert searchParam.getUrl().contains("typeall=1");
    }

    @Test
    public void testInitCrawParamGetStandardUrl() throws ParseException, CrawlException {
        searchParam = searchParamBuilder.build();
        LogRecord.print(searchParam.getUrl());
        assert searchParam.getUrl().equals("http://s.weibo.com/weibo?q=china&timescope=custom:2017-07-11:2017-07-20&xsort=hot&suball=1");
    }

    @Test
    public void testInitCrawParamBuilderClone() throws ParseException, CrawlException {
        searchParam = searchParamBuilder.build();
        String url1 = searchParam.getUrl();
        searchParamBuilder.setStartDate("2017-01-01");
        String url2 = searchParam.getUrl();
        assert url1.equals(url2);
    }

    @Test
    public void testInitCrawParamHeaders() throws ParseException {
        searchParam = searchParamBuilder.build();
        LogRecord.print(searchParam.getHeaders());
        assert searchParam.getHeaders().containsKey("Cookie");
    }

    @Test
    public void testInitUserDetailParam() throws ParseException, CrawlException {
        LogRecord.print(userDetailParam.getUrl());
        assert userDetailParam.getUrl().equals("https://weibo.com/u/3788285987");
    }
}
