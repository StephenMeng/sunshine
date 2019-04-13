package team.stephen.sunshine.service.other.parse;

import team.stephen.sunshine.exception.CrawlException;

import java.util.List;

/**
 * Created by stephen on 2017/10/22.
 */
public interface Parser<T> {
    public List<T> parse(String html) throws CrawlException;
}
