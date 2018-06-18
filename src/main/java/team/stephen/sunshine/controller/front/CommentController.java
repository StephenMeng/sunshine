package team.stephen.sunshine.controller.front;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.common.Comment;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.CommentService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.service.front.ColumnService;
import team.stephen.sunshine.util.common.PageUtil;
import team.stephen.sunshine.util.common.ParamCheck;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.web.dto.comment.CommentDto;
import team.stephen.sunshine.web.dto.comment.CommentInputDto;
import team.stephen.sunshine.web.dto.user.UserDto;

/**
 * 展示页面相关的方法
 *
 * @author stephen
 * @date 2018/5/22
 */
@RestController
@RequestMapping("comment")
public class CommentController extends BaseController{
    @Autowired
    private CommentService commentService;
    @Autowired
    private ColumnService columnService;
    @Autowired
    private DtoTransformService dtoTransformService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SolrService solrService;


    @ApiOperation(value = "新增评论", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Response addColumn(@RequestBody CommentInputDto commentInputDto) {
        UserDto currentUser = getUser();
        ParamCheck check = ParamCheck.right();
        if (check.error()) {
            return Response.error(check);
        }
        Article article=articleService.selectArticleByLinkId(commentInputDto.getArticleLinkId());
        if(article==null){
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "查无此文章", "查无此文章");
        }
        Comment comment = Comment.getNewDefaultInstance();
        comment.setCommentContent(commentInputDto.getCommentContent());
        comment.setCommentUserId(currentUser.getUserId());
        comment.setCommentOnBinId(article.getArticleId());
        try {
            commentService.addCommentOnArticle(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }

        return Response.success(true);
    }
    @ApiOperation(value = "获取评论列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleLinkId", value = "文章linkId", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Response getArticle(
            @RequestParam(value = "articleLinkId", required = true) String articleLinkId,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Article article=articleService.selectArticleByLinkId(articleLinkId);
        if(article==null){
            return Response.error(ResultEnum.CLIENT_ERROR.getCode(), "查无此文章", "查无此文章");
        }
        Page<Comment> commentPage=commentService.selectCommentOnArticle(article.getArticleId(),pageNum,pageSize);
        Page<CommentDto>commentDtoPage= PageUtil.transformPage(commentPage,commentService::modelToDto);
        return Response.success(new PageInfo(commentDtoPage));
    }

}
