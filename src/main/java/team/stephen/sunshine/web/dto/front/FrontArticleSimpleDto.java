package team.stephen.sunshine.web.dto.front;

import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;

/**
 * @author stephen
 * @date 2018/5/22
 */
public class FrontArticleSimpleDto extends BaseArticleDto {
    public  FrontArticleSimpleDto(Article article) {
        super(article);
    }
}
