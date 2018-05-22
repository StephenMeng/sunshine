package team.stephen.sunshine.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.web.dto.front.FrontArticleDetailDto;

/**
 * 管理员管理文章、博客
 *
 * @author stephen
 * @date 2018/5/22
 */
@RequestMapping("admin/article")
@RestController
public class ArticleController {
    @RequestMapping(value = "publish", method = RequestMethod.GET)
    public Response publish(FrontArticleDetailDto detailDto) {
        return Response.success(true);
    }
}
