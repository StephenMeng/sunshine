package team.stephen.sunshine.service.other.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.other.CrawlErrorDao;
import team.stephen.sunshine.model.other.CrawlError;
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
        return crawlErrorDao.select(condition);
    }

    @Override
    public int completed(int id) {
        CrawlError condition = new CrawlError();
        condition.setId(id);
        condition.setDeleted(true);
        return crawlErrorDao.updateByPrimaryKeySelective(condition);
    }
}
