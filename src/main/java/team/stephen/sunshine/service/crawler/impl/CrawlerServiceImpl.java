package team.stephen.sunshine.service.crawler.impl;

import org.springframework.stereotype.Service;
import team.stephen.sunshine.model.crawler.Parser;
import team.stephen.sunshine.service.crawler.CrawlerService;
import team.stephen.sunshine.util.HttpUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Override
    public List<Object> get(String url, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpGet(url);
        return null;
    }

    @Override
    public List get(String url, Map<String, String> header, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpGet(url, header);
        return parser.parse(html);
    }

    @Override
    public List<Object> post(String url, Map<String, String> postBody, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpPost(url, postBody);
        return null;
    }

    @Override
    public List<Object> post(String url, Map<String, String> header, Map<String, String> postBody, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpPost(url, header, postBody);

        return null;
    }
}
