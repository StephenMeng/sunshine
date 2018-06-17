package team.stephen.sunshine.controller.personal;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.common.PageUtil;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.web.dto.base.BaseArticleDto;
import team.stephen.sunshine.web.dto.user.SignInUserDto;
import team.stephen.sunshine.web.dto.user.UserDto;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("personal/article")
public class ArticleController extends BaseController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private DtoTransformService dtoTransformService;

    @ApiOperation(value = "获取我喜欢的文章列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "like", method = RequestMethod.GET)
    public Response getArticle(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Article condition = new Article();
        Page<Article> articlePage = articleService.select(condition, pageNum, pageSize);
        Page<BaseArticleDto> standardArticleDtoPage = PageUtil.transformPage(articlePage, article -> {
            BaseArticleDto standardArticleDto = new BaseArticleDto();
            dtoTransformService.copyProperties(standardArticleDto, article);
            return standardArticleDto;
        });
        return Response.success(new PageInfo(standardArticleDtoPage));
    }

}
