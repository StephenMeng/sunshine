package team.stephen.sunshine.controller.article;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.common.ArticleAttachRelation;
import team.stephen.sunshine.model.common.Attachment;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.CacheService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.util.common.PageUtil;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.article.SimpleArticleDto;
import team.stephen.sunshine.web.dto.article.StandardArticleDto;
import team.stephen.sunshine.web.dto.common.AttachmentDto;

import java.util.List;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("article")
public class ArticleController extends BaseController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private DtoTransformService dtoTransformService;
    @Autowired
    private CacheService cacheService;

    @ApiOperation(value = "获取我喜欢的文章列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "like", method = RequestMethod.GET)
    public Response getArticle(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "4") Integer pageSize) {
        Article condition = new Article();
        Page<Article> articlePage = articleService.select(condition, pageNum, pageSize);
        Page<SimpleArticleDto> standardArticleDtoPage = PageUtil.transformPage(articlePage, article -> {
            SimpleArticleDto simpleArticleDto = new SimpleArticleDto();
            dtoTransformService.copyProperties(simpleArticleDto, article);
            simpleArticleDto.setArticleAuthor(cacheService.findUserDtoByUserId(article.getArticleAuthorId()));
            return simpleArticleDto;
        });
        return Response.success(new PageInfo(standardArticleDtoPage));
    }

    @ApiOperation(value = "获取文章详情", httpMethod = "GET", response = Response.class)
    @ApiImplicitParam(name = "articleLinkId", value = "文章linkId", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public Response getArticle(
            @RequestParam(name = "articleLinkId") String articleLinkId) {
        Article article = articleService.selectArticleByLinkId(articleLinkId);
        StandardArticleDto standardArticleDto = dtoTransformService.articleModelToDto(article);
        List<AttachmentDto> attaches = articleService.getAttachments(article.getArticleId());
        standardArticleDto.setAttachments(attaches);
        return Response.success(standardArticleDto);
    }
}
