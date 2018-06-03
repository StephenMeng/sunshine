package team.stephen.sunshine.service.other;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboComment;
import team.stephen.sunshine.model.other.WeiboUserConfig;

import java.util.List;
import java.util.Map;

/**
 * @author Stephen
 * @date 2018/05/30 02:33
 */
public interface WeiboService {
    /**
     * 新增微博
     *
     * @param weibo 微博
     * @return 新增的数量
     */
    int addWeibo(Weibo weibo);

    /**
     * 新增评论
     *
     * @param weiboComment 评论
     * @return 新增的数量
     */
    int addWeiboComment(WeiboComment weiboComment);

    /**
     * 新增微博config
     *
     * @param weiboUserConfig config
     * @return 新增的数量
     */
    int addWeiboUserConfig(WeiboUserConfig weiboUserConfig);

    WeiboUserConfig selectUserConfig(String userUri);

    /**
     * 搜索微博
     *
     * @param condition 条件
     * @param pageNum   页码
     * @param pageSize  页数
     * @return
     */
    Page<Weibo> selectWeibo(Weibo condition, Integer pageNum, Integer pageSize);

    /**
     * 爬用户的配置信息
     *
     * @param userUrl 用户地址
     * @param headers headers
     * @return 配置信息
     */
    WeiboUserConfig crawlUserConfig(String userUrl, Map<String, String> headers);

    /**
     * 爬微博
     *
     * @param config  博主的基本信息
     * @param page    页码
     * @param headers headers
     * @return 列表
     */
    List<Weibo> crawlWeibo(WeiboUserConfig config, int page, Map<String, String> headers);

    /**
     * 爬评论
     *
     * @param mid     微博的id
     * @param page    页码
     * @param headers headers
     * @return 列表
     */
    List<WeiboComment> crawlWeiboComment(String mid, int page, Map<String, String> headers);

}
