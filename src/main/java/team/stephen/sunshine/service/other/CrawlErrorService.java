package team.stephen.sunshine.service.other;

import team.stephen.sunshine.model.other.CrawlError;

import java.util.List;

/**
 * @author Stephen
 * @date 2018/06/03 09:47
 */
public interface CrawlErrorService {
    int addError(CrawlError error);

    List<CrawlError> getCrawlError(String site);

    int completed(int id);
}
