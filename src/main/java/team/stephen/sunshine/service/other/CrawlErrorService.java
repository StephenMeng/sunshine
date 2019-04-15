package team.stephen.sunshine.service.other;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.bean.Pagination;

import java.util.List;

/**
 * @author Stephen
 * @date 2018/06/03 09:47
 */
public interface CrawlErrorService {
    int addError(CrawlError error);

    List<CrawlError> getCrawlError(String site);

    int completed(int id);

    int selectCount(CrawlError error);

    Page<CrawlError> selectError(CrawlError condition, Pagination pagination);
}
