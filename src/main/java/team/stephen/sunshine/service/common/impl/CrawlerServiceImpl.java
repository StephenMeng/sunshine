package team.stephen.sunshine.service.common.impl;

import org.springframework.stereotype.Service;
import team.stephen.sunshine.model.crawler.Parser;
import team.stephen.sunshine.service.common.CrawlerService;
import team.stephen.sunshine.util.HttpUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Override
    public List get(String url, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpGet(url);
        return parser.parse(html);
    }

    @Override
    public List get(String url, Map<String, String> header, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpGet(url, header);
        return parser.parse(html);
    }

    @Override
    public List post(String url, Map<String, String> postBody, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpPost(url, postBody);
        return parser.parse(html);
    }

    @Override
    public List post(String url, Map<String, String> header, Map<String, String> postBody, Parser parser) throws IOException {
        String html = HttpUtils.okrHttpPost(url, header, postBody);
        return parser.parse(html);
    }
}
