package team.stephen.sunshine.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hankcs.hanlp.HanLP;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
import team.stephen.sunshine.web.dto.front.AdminChannelDto;
import team.stephen.sunshine.web.dto.front.AdminColumnDto;
import team.stephen.sunshine.web.dto.front.StandardChannelDto;
import team.stephen.sunshine.web.dto.front.StandardColumnDto;
import team.stephen.sunshine.web.dto.user.UserDto;

import java.util.Date;

import static team.stephen.sunshine.constant.AricleConst.ARTICLE_LINK_ID_LENGTH;
import static team.stephen.sunshine.util.element.StringUtils.EMPTY;

/**
 * front的频道、栏目管理
 *
 * @author stephen
 * @date 2018/5/22
 */
@RequestMapping("admin/front")
@RestController
public class FrontManageController extends BaseController {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ColumnService columnService;
    @Autowired
    private DtoTransformService dtoTransformService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SolrService solrService;

    @ApiOperation(value = "获取频道列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hasColumn", value = "是否有子栏目，默认是", required = false, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "deleted", value = "是否是回收站内的，默认否", required = false, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "channel", method = RequestMethod.GET)
    public Response getChannel(
            @RequestParam(value = "hasColumn", defaultValue = "true") Boolean hasColumn,
            @RequestParam(value = "deleted", defaultValue = "false") Boolean deleted,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Channel condition = new Channel();
        condition.setDeleted(deleted);
        condition.setHasColumn(hasColumn);

        Page<Channel> channelPage = channelService.select(condition, pageNum, pageSize);
        Page<StandardChannelDto> channelDtoPage = PageUtil.transformPage(channelPage, dtoTransformService::channelModelToDto);
        return Response.success(new PageInfo(channelDtoPage));
    }

    @ApiOperation(value = "新增频道", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "channel/add", method = RequestMethod.POST)
    public Response addChannel(AdminChannelDto adminChannelDto) {

        ParamCheck check = checkAddChannelParam(adminChannelDto);
        if (check.error()) {
            return Response.error(check);
        }
        adminChannelDto.setChannelId(null);
        Channel channel = new Channel();
        dtoTransformService.copyProperties(channel, adminChannelDto);
        channel.setDeleted(false);
        try

        {
            channelService.addChannel(channel);
        } catch (
                Exception e)

        {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "更新频道信息", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "channel/update", method = RequestMethod.POST)
    public Response updateChannel(AdminChannelDto adminChannelDto) {
        ParamCheck check = checkUpdateParam(adminChannelDto);
        if (check.error()) {
            return Response.error(check);
        }
        Channel channel = new Channel();
        dtoTransformService.copyProperties(channel, adminChannelDto);
        try {
            channelService.updateSelective(channel);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "将频道放入回收站", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "channelId", value = "频道ID", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "channel/recycle", method = RequestMethod.POST)
    public Response recycleChannel(Integer channelId) {
        return recycleOrRestoreChannel(channelId, true);
    }


    @ApiOperation(value = "将频道从回收站还原", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "channelId", value = "频道ID", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "channel/restore", method = RequestMethod.POST)
    public Response restoreChannel(Integer channelId) {
        return recycleOrRestoreChannel(channelId, false);
    }

    private Response recycleOrRestoreChannel(Integer channelId, boolean recycle) {
        ParamCheck check = checkRecycleParam(channelId);
        if (check.error()) {
            return Response.error(check);
        }
        Channel channel = new Channel();
        channel.setChannelId(channelId);
        channel.setDeleted(recycle);
        try {
            channelService.updateSelective(channel);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    private ParamCheck checkRecycleParam(Integer channelId) {
        if (channelId == null || channelId < 0) {
            return ParamCheck.error("参数错误", "参数错误");
        }
        return ParamCheck.right();
    }

    private ParamCheck checkUpdateParam(AdminChannelDto adminChannelDto) {
        if (adminChannelDto == null) {
            return ParamCheck.error("参数为空", "参数为空");
        }
        if (adminChannelDto.getChannelId() == null) {
            return ParamCheck.error("频道ID为空", "频道ID为空");
        }
        if (EMPTY.equals(adminChannelDto.getChannelUri())) {
            return ParamCheck.error("频道uri为空", "频道uri为空");
        }
        if (EMPTY.equals(adminChannelDto.getChannelNameCn())) {
            return ParamCheck.error("频道中文名称为空", "频道中文名称为空");
        }
        if (EMPTY.equals(adminChannelDto.getChannelNameEn())) {
            return ParamCheck.error("频道英文文名称为空", "频道英文文名称为空");
        }
        return ParamCheck.right();
    }

    private ParamCheck checkAddChannelParam(AdminChannelDto adminChannelDto) {
        if (adminChannelDto == null) {
            return ParamCheck.error("参数为空", "null param");
        }
        if (StringUtils.isBlank(adminChannelDto.getChannelUri())) {
            return ParamCheck.error("频道uri为空", "null param");
        }
        if (StringUtils.isBlank(adminChannelDto.getChannelNameCn())) {
            return ParamCheck.error("频道中文名称为空", "null param");
        }
        if (StringUtils.isBlank(adminChannelDto.getChannelNameEn())) {
            return ParamCheck.error("频道英文名称为空", "null param");
        }
        if (adminChannelDto.getHasColumn() == null) {
            return ParamCheck.error("频道是否有子栏目为空", "null param");
        }
        return ParamCheck.right();
    }

    @ApiOperation(value = "获取栏目列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelId", value = "父频道ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "deleted", value = "是否是回收站内的，默认否", required = false, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "column", method = RequestMethod.GET)
    public Response getColumn(
            @RequestParam(value = "channelId", defaultValue = "true") Integer channelId,
            @RequestParam(value = "deleted", defaultValue = "false") Boolean deleted,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Column condition = new Column();
        condition.setDeleted(deleted);
        condition.setChannelId(channelId);
        PageHelper.startPage(pageNum, pageSize);
        Page<Column> channelPage = columnService.select(condition, pageNum, pageSize);
        Page<StandardColumnDto> channelDtoPage = PageUtil.transformPage(channelPage, dtoTransformService::columnModelToDto);
        return Response.success(new PageInfo(channelDtoPage));
    }

    @ApiOperation(value = "新增栏目", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "column/add", method = RequestMethod.POST)
    public Response addColumn(AdminColumnDto adminColumnDto) {
        ParamCheck check = checkAddColumnParam(adminColumnDto);
        if (check.error()) {
            return Response.error(check);
        }
        adminColumnDto.setColumnId(null);
        Column column = new Column();
        dtoTransformService.copyProperties(column, adminColumnDto);
        column.setChannelId(adminColumnDto.getChannel().getChannelId());
        column.setDeleted(false);
        try {
            columnService.addcolumn(column);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "更新栏目信息", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "column/update", method = RequestMethod.POST)
    public Response updateChannel(AdminColumnDto adminColumnDto) {
        ParamCheck check = checkUpdateColumnParam(adminColumnDto);
        if (check.error()) {
            return Response.error(check);
        }
        Column column = new Column();
        dtoTransformService.copyProperties(column, adminColumnDto);
        try {
            columnService.updateSelective(column);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "将栏目放入回收站", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "columnId", value = "栏目ID", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "column/recycle", method = RequestMethod.POST)
    public Response recycleColumn(Integer columnId) {
        return recycleOrRestoreColumn(columnId, true);
    }


    @ApiOperation(value = "将栏目从回收站还原", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "columnId", value = "栏目ID", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "column/restore", method = RequestMethod.POST)
    public Response resotoreColumn(Integer columnId) {
        return recycleOrRestoreColumn(columnId, false);
    }

    private Response recycleOrRestoreColumn(Integer columnId, boolean recycle) {
        ParamCheck check = checkRecycleParam(columnId);
        if (check.error()) {
            return Response.error(check);
        }
        Column column = new Column();
        column.setColumnId(columnId);
        column.setDeleted(recycle);
        try {
            columnService.updateSelective(column);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    private ParamCheck checkAddColumnParam(AdminColumnDto adminColumnDto) {
        if (adminColumnDto == null) {
            return ParamCheck.error("参数为空", "null param");
        }
        if (adminColumnDto.getChannel() == null || adminColumnDto.getChannel().getChannelId() == null) {
            return ParamCheck.error("频道ID为空", "null param");
        }
        if (channelService.selectByChannelId(adminColumnDto.getChannel().getChannelId()) == null) {
            return ParamCheck.error("频道ID不存在", "null param");
        }
        if (StringUtils.isBlank(adminColumnDto.getColumnUri())) {
            return ParamCheck.error("栏目uri为空", "null param");
        }
        if (StringUtils.isBlank(adminColumnDto.getColumnNameCn())) {
            return ParamCheck.error("栏目中文名称为空", "null 栏目中文名称为空");
        }
        if (StringUtils.isBlank(adminColumnDto.getColumnNameEn())) {
            return ParamCheck.error("栏目英文名称为空", "栏目英文名称为空 param");
        }
        return ParamCheck.right();
    }

    private ParamCheck checkUpdateColumnParam(AdminColumnDto adminColumnDto) {
        if (adminColumnDto == null) {
            return ParamCheck.error("参数为空", "null param");
        }
        if (adminColumnDto.getColumnId() == null) {
            return ParamCheck.error("栏目ID为空", "栏目ID为空");
        }
        if (adminColumnDto.getChannel() != null && adminColumnDto.getChannel().getChannelId() != null) {
            if (channelService.selectByChannelId(adminColumnDto.getChannel().getChannelId()) == null) {
                return ParamCheck.error("频道ID不存在", "null param");
            }
        }
        return ParamCheck.right();
    }

    @ApiOperation(value = "获取文章列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelId", value = "频道ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "columnId", value = "栏目ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "private", value = "是否私有", required = false, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "deleted", value = "是否是回收站内的，默认否", required = false, dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "article", method = RequestMethod.GET)
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
    @RequestMapping(value = "article/add", method = RequestMethod.POST)
    public Response addColumn(InputArticleDto inputArticleDto) {
        UserDto currentUser = getUser();
        ParamCheck check = checkAddArticleParam(inputArticleDto);
        if (check.error()) {
            return Response.error(check);
        }
        Article article = Article.getNewDefaultInstance();
        dtoTransformService.copyProperties(article, inputArticleDto);
        article.setDeleted(false);
        article.setArticleAuthor(currentUser.getUserId());
        article.setArticleCreateDate(new Date());
        article.setArticleUpdateDate(new Date());
        article.setArticleAbstract(String.valueOf(HanLP.extractSummary(article.getArticleContent(), 10)));
        article.setArticleLinkId(RandomIDUtil.randomID(ARTICLE_LINK_ID_LENGTH));
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

    //
    @ApiOperation(value = "更新文章信息", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "article/update", method = RequestMethod.POST)
    public Response updateChannel(InputArticleDto inputArticleDto) {
        UserDto currentUser = getUser();
        ParamCheck check = checkUpdateArticleParam(inputArticleDto);
        if (check.error()) {
            return Response.error(check);
        }
        Article article = articleService.selectArticleByLinkId(inputArticleDto.getArticleLinkId());
        if(article==null){
            return Response.error(ResultEnum.NO_RESOURCE_FOUND);
        }
        dtoTransformService.copyProperties(article, inputArticleDto);
        article.setDeleted(false);
        article.setArticleAuthor(currentUser.getUserId());
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
    @RequestMapping(value = "article/recycle", method = RequestMethod.POST)
    public Response recycleArticle(String linkId) {
        return recycleOrRestoreArticle(linkId, true);
    }
//

    @ApiOperation(value = "将文章从回收站还原", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "linkId", value = "文章 Link ID", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "article/restore", method = RequestMethod.POST)
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
