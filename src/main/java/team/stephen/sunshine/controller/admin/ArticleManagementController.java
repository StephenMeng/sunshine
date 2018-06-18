package team.stephen.sunshine.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Joiner;
import com.hankcs.hanlp.HanLP;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.service.front.ChannelService;
import team.stephen.sunshine.service.front.ColumnService;
import team.stephen.sunshine.util.common.PageUtil;
import team.stephen.sunshine.util.common.ParamCheck;
import team.stephen.sunshine.util.common.RandomIDUtil;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.article.InputArticleDto;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;
import team.stephen.sunshine.web.dto.user.UserDto;

import java.util.Date;

import static team.stephen.sunshine.constant.AricleConst.ARTICLE_LINK_ID_LENGTH;

/**
 * 管理员管理文章、博客
 *
 * @author stephen
 * @date 2018/5/22
 */
@Api(description = "管理员管理文章、博客")
@RequestMapping("admin/article")
@RestController
public class ArticleManagementController extends BaseController {
    @Autowired
    private DtoTransformService dtoTransformService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SolrService solrService;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ColumnService columnService;

    @ApiOperation(value = "获取文章列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelId", value = "频道ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "columnId", value = "栏目ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "private", value = "是否私有", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "deleted", value = "是否是回收站内的，默认否", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Response getArticle(
            @RequestParam(value = "channelId", required = false) Integer channelId,
            @RequestParam(value = "columnId", required = false) Integer columnId,
            @RequestParam(value = "private", defaultValue = "false") Boolean isPrivate,
            @RequestParam(value = "deleted", defaultValue = "false") Boolean deleted,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Article condition = new Article();
        condition.setChannelId(channelId);
        condition.setColumnId(columnId);
        condition.setPrivate(isPrivate);
        condition.setDeleted(deleted);
        Page<Article> articlePage = articleService.select(condition, pageNum, pageSize);
        Page<BaseArticleDto> standardArticleDtoPage = PageUtil.transformPage(articlePage, article -> {
            BaseArticleDto standardArticleDto = new BaseArticleDto();
            dtoTransformService.copyProperties(standardArticleDto, article);
            return standardArticleDto;
        });
        return Response.success(new PageInfo(standardArticleDtoPage));
    }

    @ApiOperation(value = "新增文章", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Response addColumn(@RequestBody InputArticleDto inputArticleDto) {
        UserDto currentUser = getUser();
        //todo 测试
        currentUser = new UserDto();
        currentUser.setUserId(1);
        ParamCheck check = checkAddArticleParam(inputArticleDto);
        if (check.error()) {
            return Response.error(check);
        }
        Article article = Article.getNewDefaultInstance();
        dtoTransformService.copyProperties(article, inputArticleDto);
        if (inputArticleDto.getTags() != null) {
            article.setArticleTag(Joiner.on(";").join(inputArticleDto.getTags()));
        }
        if (StringUtils.isNull(inputArticleDto.getChannel())) {
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "频道不能为空", "频道不能为空");
        }
        Channel channel = channelService.selectByChannelUri(inputArticleDto.getChannel());
        if (channel == null) {
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "查无此频道", "查无此频道");
        }
        article.setChannelId(channel.getChannelId());
        if (StringUtils.isNotNull(inputArticleDto.getColumn())) {
            Column column = columnService.selectBycolumnUri(inputArticleDto.getColumn());
            if (column != null) {
                article.setColumnId(column.getColumnId());
            }else {
                return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "查无此栏目", "查无此栏目");
            }
        }
        article.setDeleted(false);
        article.setArticleAuthorId(currentUser.getUserId());
        article.setArticleCreateDate(new Date());
        article.setArticleUpdateDate(new Date());
        article.setArticleAbstract(String.valueOf(HanLP.extractSummary(article.getArticleContent(), 10)));
        article.setArticleLinkId(RandomIDUtil.randomID(ARTICLE_LINK_ID_LENGTH));
        article.setArticleLink("article/detail/" + article.getArticleLinkId());
        try {
            articleService.addArticle(article);
            article = articleService.selectArticleByLinkId(article.getArticleLinkId());
            solrService.addArticle(article);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }

        return Response.success(true);
    }

    private ParamCheck checkAddArticleParam(InputArticleDto inputArticleDto) {
        if (inputArticleDto.getArticleTitle().isEmpty()) {
            return ParamCheck.error("题名不能为空", "title can not be null");
        }
        if (inputArticleDto.getArticleContent().isEmpty()) {
            return ParamCheck.error("正文内容不能为空", "content can not be null");
        }
        return ParamCheck.right();
    }

    @ApiOperation(value = "更新文章信息", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response updateChannel(InputArticleDto inputArticleDto) {
        UserDto currentUser = getUser();
        ParamCheck check = checkUpdateArticleParam(inputArticleDto);
        if (check.error()) {
            return Response.error(check);
        }
        Article article = articleService.selectArticleByLinkId(inputArticleDto.getArticleLinkId());
        if (article == null) {
            return Response.error(ResultEnum.NO_RESOURCE_FOUND);
        }
        dtoTransformService.copyProperties(article, inputArticleDto);
        article.setDeleted(false);
        article.setArticleAuthorId(currentUser.getUserId());
        article.setArticleUpdateDate(new Date());
        article.setArticleAbstract(String.valueOf(HanLP.extractSummary(article.getArticleContent(), 10)));
        try {
            articleService.updateSelective(article);
            solrService.update(article);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    private ParamCheck checkUpdateArticleParam(InputArticleDto inputArticleDto) {
        if (StringUtils.isBlank(inputArticleDto.getArticleLinkId())) {
            return ParamCheck.error("link ID 不能为空", "linkId can not be null");
        }
        if (inputArticleDto.getArticleTitle().isEmpty()) {
            return ParamCheck.error("题名不能为空", "title can not be null");
        }
        if (inputArticleDto.getArticleContent().isEmpty()) {
            return ParamCheck.error("正文内容不能为空", "content can not be null");
        }
        return ParamCheck.right();
    }

    @ApiOperation(value = "将文章放入回收站", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "linkId", value = "文章 link ID", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "recycle", method = RequestMethod.POST)
    public Response recycleArticle(String linkId) {
        return recycleOrRestoreArticle(linkId, true);
    }
//

    @ApiOperation(value = "将文章从回收站还原", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "linkId", value = "文章 Link ID", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "restore", method = RequestMethod.POST)
    public Response resotoreColumn(String linkId) {
        return recycleOrRestoreArticle(linkId, false);
    }

    private Response recycleOrRestoreArticle(String linkId, boolean recycle) {
        Article article = articleService.selectArticleByLinkId(linkId);
        if (article == null) {
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "could not find this artilce", "查无此文章");
        }
        Article tmp = new Article();
        tmp.setArticleId(article.getArticleId());
        tmp.setDeleted(recycle);
        try {
            articleService.updateSelective(tmp);
            if (recycle) {
                solrService.deleteArticleByArticleId(article.getArticleId());
            } else {
                solrService.addArticle(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }
}
