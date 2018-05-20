package team.stephen.sunshine.service.crawler;


import team.stephen.sunshine.model.crawler.Parser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CrawlerService {
    List get(String url, Parser parser) throws IOException;

    List get(String url, Map<String, String> header, Parser parser) throws IOException;

    List post(String url, Map<String, String> postBody, Parser parser) throws IOException;

    List post(String url, Map<String, String> header, Map<String, String> postBody, Parser parser) throws IOException;
}
