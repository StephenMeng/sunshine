package team.stephen.sunshine.service.other.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.other.CrawlErrorDao;
import team.stephen.sunshine.model.other.CrawlError;
import team.stephen.sunshine.model.other.bean.Pagination;
import team.stephen.sunshine.service.other.CrawlErrorService;

import java.util.Date;
import java.util.List;

/**
 * @author Stephen
 * @date 2018/06/03 09:48
 */
@Service
public class CrawErrorServiceImpl implements CrawlErrorService {
    @Autowired
    private CrawlErrorDao crawlErrorDao;

    @Override
    public int addError(CrawlError error) {
        error.setCreateDate(new Date());
        return crawlErrorDao.insert(error);
    }

    @Override
    public List<CrawlError> getCrawlError(String site) {
        CrawlError condition = new CrawlError();
        condition.setSite(site);
        condition.setDeleted(false);
        return crawlErrorDao.select(condition);
    }

    @Override
    public int completed(int id) {
        CrawlError condition = new CrawlError();
        condition.setId(id);
        condition.setDeleted(true);
        return crawlErrorDao.updateByPrimaryKeySelective(condition);
    }

    @Override
    public int selectCount(CrawlError error) {
        return 0;
    }

    @Override
    public Page<CrawlError> selectError(CrawlError condition, Pagination pagination) {
        PageHelper.startPage(pagination.getPageIndex(), pagination.getPageSize());
        return (Page<CrawlError>) crawlErrorDao.select(condition);
    }
}
