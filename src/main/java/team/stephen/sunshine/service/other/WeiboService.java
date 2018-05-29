package team.stephen.sunshine.service.other;

import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboComment;

/**
 * @author Stephen
 * @date 2018/05/30 02:33
 */
public interface WeiboService {
    int addWeibo(Weibo weibo);
    int addWeiboComment(WeiboComment weiboComment);
}
