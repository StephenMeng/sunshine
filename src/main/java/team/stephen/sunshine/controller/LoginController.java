package team.stephen.sunshine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import team.stephen.sunshine.service.UserService;
import team.stephen.sunshine.util.Response;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("toLogin")
    public ModelAndView toLogin(String userName, String password, HttpServletRequest request) {
        return new ModelAndView("login");
    }

    @RequestMapping("login")
    @ResponseBody
    public Response login(@RequestParam("userName") String inputUserName,
                          @RequestParam("password") String inputPassword,
                          HttpServletRequest request) {
        return Response.success(true);
    }

    @RequestMapping("logout")
    @ResponseBody
    public Response logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return null;
    }
}
