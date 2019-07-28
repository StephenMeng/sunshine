package team.stephen.sunshine.service.other.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.bean.CrawlParam;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;

import java.io.IOException;
import java.util.function.Function;

/**
 * @author Stephen
 * @date 2019/04/06 15:08
 */
public class CrawlUtils {
    /**
     * 根据入参，获取html结果
     *
     * @param param
     * @return
     * @throws CrawlException
     */
    public static String getHttpResult(CrawlParam param) throws CrawlException {
        try {
            HttpResponse response = HttpUtils.httpGet(param.getUrl(), param.getHeaders());
            return IOUtils.toString(response.getEntity().getContent(), param.getEncode());
        } catch (IOException e) {
            throw new CrawlException(e);
        }

    }

    /**
     * 根据入参，获取html结果
     *
     * @param param
     * @return
     * @throws CrawlException
     */
    public static Object getHttpResultWithFunction(CrawlParam param, Function<String, Object> f) throws CrawlException {
        try {
            String html = HttpUtils.okrHttpGet(param.getUrl(), param.getHeaders());
            return f.apply(html);
        } catch (IOException e) {
            throw new CrawlException(e);
        }
    }
}
