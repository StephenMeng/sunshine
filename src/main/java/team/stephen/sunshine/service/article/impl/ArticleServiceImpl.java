package team.stephen.sunshine.service.article.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.article.ArticleDao;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.util.element.StringUtils;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;

    @Override
    public Article getArticleById(Long articleId) {
        if (articleId == null) {
            return null;
        }
        return articleDao.selectByPrimaryKey(articleId);
    }

    @Override
    public Article getArticleByLinkId(String linkId) {
        if (StringUtils.isNull(linkId)) {
            return null;
        }
        Article condition = new Article();
        condition.setArticleLinkId(linkId);
        return articleDao.selectOne(condition);
    }

    @Override
    public int addArticle(Article article) throws Exception {
        return articleDao.insert(article);
    }

    @Override
    public int updateSelective(Article article) {
        return articleDao.updateByPrimaryKeySelective(article);
    }
}
