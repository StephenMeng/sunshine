package team.stephen.sunshine.web.dto.front;

import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;
import team.stephen.sunshine.web.dto.user.UserDto;

/**
 * @author stephen
 * @date 2018/5/22
 */
public class FrontArticleSimpleDto extends BaseArticleDto {
    protected UserDto articleAuthor;

    public UserDto getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(UserDto articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public FrontArticleSimpleDto(Article article) {
        super(article);
    }
}
