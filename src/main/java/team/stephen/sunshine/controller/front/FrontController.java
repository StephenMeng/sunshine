package team.stephen.sunshine.controller.front;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.exception.NullParamException;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.service.front.ChannelService;
import team.stephen.sunshine.service.front.ColumnService;
import team.stephen.sunshine.util.common.PageUtil;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.article.StandardArticleDto;
import team.stephen.sunshine.web.dto.condition.ArticleSearchCondition;
import team.stephen.sunshine.web.dto.front.*;

import java.io.IOException;

/**
 * 展示页面相关的方法
 *
 * @author stephen
 * @date 2018/5/22
 */
@RestController
@RequestMapping("front")
public class FrontController {
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
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "channel", method = RequestMethod.GET)
    public Response getChannel(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                               @RequestParam(name = "pageSize", defaultValue = "0") Integer pageSize) {
        Channel condition = new Channel();
        condition.setDeleted(false);
        PageHelper.startPage(pageNum, pageSize);
        Page<Channel> channelPage = channelService.select(condition, pageNum, pageSize);
        Page<StandardChannelDto> dtoPage = PageUtil.transformPage(channelPage, dtoTransformService::channelModelToDto);
        Page<FrontChannelDto> frontChannelDtos = PageUtil.transformPage(dtoPage, FrontChannelDto::new);
        return Response.success(new PageInfo<>(frontChannelDtos));
    }

    @ApiOperation(value = "获取栏目列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channel", value = "频道uri", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "column", method = RequestMethod.GET)
    public Response getColumn(@RequestParam("channel") String channelUri,
                              @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "0") Integer pageSize) {
        if (StringUtils.isBlank(channelUri)) {
            return Response.error(ResultEnum.NULL_PARAMETER);
        }
        Channel channelCondition = new Channel();
        channelCondition.setChannelUri(channelUri);
        channelCondition.setDeleted(false);
        Channel exist;
        try {
            exist = channelService.selectOne(channelCondition);
        } catch (Exception e) {
            return Response.error(ResultEnum.SERVER_WRONG);
        }
        if (exist == null) {
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "could not find this channel", "查无此频道");
        }
        Column columnCondition = new Column();
        columnCondition.setChannelId(exist.getChannelId());
        columnCondition.setDeleted(false);
        Page<Column> channelPage = columnService.select(columnCondition, pageNum, pageSize);
        Page<StandardColumnDto> standardColumnDtoPage = PageUtil.transformPage(channelPage, dtoTransformService::columnModelToDto);
        Page<FrontColumnDto> dtoPage = PageUtil.transformPage(standardColumnDtoPage, FrontColumnDto::new);
        dtoPage.forEach(dto ->
                dto.setChannel(new FrontChannelDto(exist)));
        return Response.success(new PageInfo<>(dtoPage));
    }

    @ApiOperation(value = "获取文章列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelUri", value = "频道uri", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "columnUri", value = "栏目uri", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量，默认10", required = true, dataType = "int", paramType = "query")})
    @RequestMapping("/{channelUri}/{columnUri}")
    public Response articleList(@PathVariable("channelUri") String channelUri,
                                @PathVariable("columnUri") String columnUri,
                                @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        if (StringUtils.isBlank(channelUri) || StringUtils.isBlank(columnUri)) {
            return Response.error(ResultEnum.NULL_PARAMETER);
        }
        Channel channelCondition = new Channel();
        channelCondition.setChannelUri(channelUri);
        channelCondition.setDeleted(false);
        Channel existChannel;
        try {
            existChannel = channelService.selectOne(channelCondition);
        } catch (Exception e) {
            return Response.error(ResultEnum.SERVER_WRONG);
        }
        if (existChannel == null) {
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "could not find this channel", "查无此频道");
        }

        Column columnCondition = new Column();
        columnCondition.setChannelId(existChannel.getChannelId());
        columnCondition.setDeleted(false);
        Column existColumn;
        try {
            existColumn = columnService.selectOne(columnCondition);
        } catch (Exception e) {
            return Response.error(ResultEnum.SERVER_WRONG);
        }
        if (existColumn == null) {
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "could not find this column", "查无此栏目");
        }
        Article articleCondition = new Article();
        articleCondition.setChannelId(existChannel.getChannelId());
        articleCondition.setColumnId(existColumn.getColumnId());

        PageHelper.startPage(pageNum, pageSize);
        Page<Article> articlePage = articleService.select(articleCondition);
        Page<StandardArticleDto> standardArticleDtoPage = PageUtil.transformPage(articlePage, dtoTransformService::articleModelToDto);
        return Response.success(new PageInfo<>(standardArticleDtoPage));
    }

    @ApiOperation(value = "获取文章详情", httpMethod = "GET", response = Response.class)
    @ApiImplicitParam(name = "linkId", value = "文章的linkId", required = true, dataType = "string", paramType = "path")
    @RequestMapping("/{linkId}")
    public Response article(@PathVariable("linkId") String linkId) {
        Article articleCondition = new Article();
        articleCondition.setArticleLinkId(linkId);
        Article article = articleService.selectOne(articleCondition);
        StandardArticleDto dto = dtoTransformService.articleModelToDto(article);
        return Response.success(dto);
    }

    @ApiOperation(value = "搜索文章", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "搜索关键词", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量，默认10", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Response searchArticle(@RequestParam("q") String q,
                                  @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        ArticleSearchCondition condition = new ArticleSearchCondition();
        condition.setArticleTag(q);
        condition.setDeleted(false);
        condition.setPrivate(false);
        condition.setPageNum(pageNum);
        condition.setPageSize(pageSize);

        try {
            PageInfo<Article> pageInfo = solrService.queryArticle(condition);
            PageInfo<FrontArticleSimpleDto> simpleDtoPageInfo = PageUtil.transformPageInfo(pageInfo, FrontArticleSimpleDto::new);
            return Response.success(simpleDtoPageInfo);
        } catch (IOException | SolrServerException | NullParamException e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG);
        }
    }
}
