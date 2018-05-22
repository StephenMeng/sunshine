package team.stephen.sunshine.controller;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import team.stephen.sunshine.constant.SessionAttr;
import team.stephen.sunshine.service.common.CacheService;
import team.stephen.sunshine.web.dto.user.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author stephen
 * @date 2017/7/15
 */
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;

    @Autowired
    private CacheService cacheService;

    @ModelAttribute
    public void setReqAndResp(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
    }

    public UserDto getUser() {
        Object value = session.getAttribute(SessionAttr._USER);
        if (!(value instanceof UserDto)) {
            return null;
        }
        //使用缓存中的最新信息
        return cacheService.findUserDtoByUserId(((UserDto) value).getUserId());
    }
}
