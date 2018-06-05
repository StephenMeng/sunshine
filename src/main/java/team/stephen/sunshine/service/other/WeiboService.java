package team.stephen.sunshine.service.other;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.other.Weibo;
import team.stephen.sunshine.model.other.WeiboComment;
import team.stephen.sunshine.model.other.WeiboUserConfig;

import java.io.IOException;
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
     * 爬微博
     *
     * @param config  博主的基本信息
     * @param headers headers
     */
    void crawlWeibo(WeiboUserConfig config, Map<String, String> headers);

    /**
     * 爬取搜索页信息
     *
     * @param url     搜索页
     * @param headers header
     * @return 列表
     */
    List<Weibo> crawlWeiboSearchPage(String url, Map<String, String> headers);

    /**
     * 爬取搜索页信息
     *
     * @param url     搜索页
     * @param headers header
     * @return 列表
     */
    int crawlWeiboSearchPageSize(String url, Map<String, String> headers) throws IOException;

    /**
     * 爬用户主页的第一节数据
     *
     * @param url
     * @param headers
     * @return
     */
    List<Weibo> crawlWeiboFirstPage(String url, Map<String, String> headers);

    /**
     * 爬用户主页的第二三节数据
     *
     * @param url
     * @param headers
     * @return
     */
    List<Weibo> crawlWeiboPageBar(String url, Map<String, String> headers);

    /**
     * 爬评论
     *
     * @param mid     微博的id
     * @param page    页码
     * @param headers headers
     * @return 列表
     */
    List<WeiboComment> crawlWeiboComment(String mid, int page, Map<String, String> headers);

    /**
     * 主键查找
     *
     * @param mid
     * @return
     */
    Weibo selectByPrimary(String mid);

    int updateSelective(Weibo tu);

    /**
     * 获取正文、去除表情
     *
     * @param weibo   weibo
     * @param headers
     */
    void completeExtraInfo(Map<String, String> headers, Weibo weibo);

    int updateSelective(WeiboUserConfig config);
}
