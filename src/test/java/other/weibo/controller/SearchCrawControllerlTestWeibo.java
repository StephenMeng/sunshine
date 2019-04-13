package other.weibo.controller;

import org.junit.Test;
import other.weibo.BaseWeiboCrawlTest;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.bean.weibo.WeiboSearchParam;
import team.stephen.sunshine.service.other.parse.impl.weibo.WeiboSearchPageNumParserImpl;
import team.stephen.sunshine.service.other.utils.CrawlUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.DateUtils;
import team.stephen.sunshine.util.element.TimeFormateUtil;

import java.util.Date;

/**
 * @author Stephen
 * @date 2019/04/06 12:18
 */
public class SearchCrawControllerlTestWeibo extends BaseWeiboCrawlTest {
    String startDate = "2016-07-11";
    String endDate = "2017-01-11";

    @Test
    public void testCrawlSimpleHtml() {
        Date sd = TimeFormateUtil.parseStringToDate(startDate);
        Date ed = TimeFormateUtil.parseStringToDate(endDate);
        while (ed.after(sd)) {
            for (int sh = 0; sh <= 24; sh++) {
                try {
                    Integer page = getTotalPage(searchParamBuilder.build());
                    for (int i = 1; i < page; i++) {
                    }
                } catch (CrawlException e) {
                    LogRecord.error(e);
                    //todo 入库
                }
            }
            sd = TimeFormateUtil.parseStringToDate(DateUtils.getNextDayStr(DateUtils.parseDateToString(sd)));
        }
    }

    private Integer getTotalPage(WeiboSearchParam param) throws CrawlException {
        Object pageNum = CrawlUtils.getHttpResultWithFunction(param, (html) -> new WeiboSearchPageNumParserImpl().parse(html));
        return (Integer) pageNum;
    }

}
