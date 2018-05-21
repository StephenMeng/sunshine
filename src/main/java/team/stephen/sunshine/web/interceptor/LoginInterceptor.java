package team.stephen.sunshine.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by stephen on 2017/7/15.
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        UserDto userDto = (UserDto) httpServletRequest.getSession().getAttribute(LoginConstant.USER);
//        if (userDto == null) {
////            httpServletRequest.getRequestDispatcher("index").
//            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/toLogin");
//            return false;
//
//        }
//        User user = new User();
//        user.setUserId(new Random().nextInt(100));
//        user.setUserName("stephen");
//        Holder.setUser(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
