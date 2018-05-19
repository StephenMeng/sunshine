package team.stephen.sunshine.dao.article;


import org.apache.ibatis.annotations.Mapper;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.util.BaseDao;

/**
 * @author stephen
 * @date 2018/5/19
 */
@Mapper
public interface ArticleDao extends BaseDao<Article> {
}
