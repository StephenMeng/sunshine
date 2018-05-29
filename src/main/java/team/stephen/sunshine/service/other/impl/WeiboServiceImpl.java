package team.stephen.sunshine.service.other.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.other.WeiboCommentDao;
import team.stephen.sunshine.dao.other.WeiboDao;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboComment;
import team.stephen.sunshine.service.other.WeiboService;

/**
 * @author Stephen
 * @date 2018/05/30 02:33
 */
@Service
public class WeiboServiceImpl implements WeiboService{
    @Autowired
    private WeiboDao weiboDao;
    @Autowired
    private WeiboCommentDao weiboCommentDao;
    @Override
    public int addWeibo(Weibo weibo) {
        return weiboDao.insert(weibo);
    }

    @Override
    public int addWeiboComment(WeiboComment weiboComment) {
        return weiboCommentDao.insert(weiboComment);
    }
}
