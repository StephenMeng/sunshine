package team.stephen.sunshine.service.article.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.stephen.sunshine.dao.sunshine.article.ArticleDao;
import team.stephen.sunshine.dao.sunshine.common.ArticleAttachRelationDao;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.common.ArticleAttachRelation;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.common.AttachmentDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author stephen
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleAttachRelationDao articleAttachRelationDao;

    @Override
    public Article selectArticleById(Long articleId) {
        if (articleId == null) {
            return null;
        }
        return articleDao.selectByPrimaryKey(articleId);
    }

    @Override
    public Article selectArticleByLinkId(String linkId) {
        if (StringUtils.isNull(linkId)) {
            return null;
        }
        Article condition = new Article();
        condition.setArticleLinkId(linkId);
        return articleDao.selectOne(condition);
    }

    @Override
    public int addArticle(Article article) {
        return articleDao.insert(article);
    }

    @Override
    public int updateSelective(Article article) {
        return articleDao.updateByPrimaryKeySelective(article);
    }

    @Override
    public Page<Article> select(Article articleCondition, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Article>) articleDao.select(articleCondition);
    }

    @Override
    public Article selectOne(Article articleCondition) {
        return articleDao.selectOne(articleCondition);
    }

    @Override
    public List<AttachmentDto> getAttachments(Long articleId) {
        List<ArticleAttachRelation> relations = getArticleAttachRelations(articleId);
        if (relations == null) {
            return Collections.EMPTY_LIST;
        }
        return relations.stream().map(attach->new AttachmentDto(attach.getAttachUri(),attach.getAttachName())).collect(Collectors.toList());
    }

    private List<ArticleAttachRelation> getArticleAttachRelations(Long articleId) {
        ArticleAttachRelation condition = new ArticleAttachRelation();
        condition.setArticleId(articleId);
        return articleAttachRelationDao.select(condition);
    }

    @Override
    @Transactional(rollbackFor =Exception.class )
    public int addAttachments(Long articleId, List<AttachmentDto> uris) {
        if (articleId == null) {
            return 0;
        }
        if (uris == null || uris.size() == 0) {
            return 0;
        }
        List<ArticleAttachRelation> relations = getArticleAttachRelations(articleId);
        if(relations!=null&&relations.size()>0){
            for (ArticleAttachRelation relation : relations) {
                articleAttachRelationDao.deleteByPrimaryKey(relation);
            }
        }
        int count = 0;
        for (AttachmentDto att : uris) {
            ArticleAttachRelation relation = new ArticleAttachRelation();
            relation.setArticleId(articleId);
            relation.setAttachUri(att.getAttachUri());
            relation.setAttachName(att.getAttachName());
            try {
                articleAttachRelationDao.insert(relation);
                count++;
            } catch (Exception e) {
                LogRecord.print(e.getLocalizedMessage());
            }
        }
        return count;
    }
}
