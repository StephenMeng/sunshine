package team.stephen.sunshine.controller;

import org.apache.juli.logging.Log;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import team.stephen.sunshine.service.UserService;
import team.stephen.sunshine.util.LogRecod;
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

    @RequestMapping("tologin")
    @ResponseBody
    public Response tologin() {
        return Response.success("tologin");
    }

    @RequestMapping("login")
    @ResponseBody
    public Response login(@RequestParam(value = "userName", required = false) String userName,
                          @RequestParam(value = "password", required = false) String password,
                          HttpServletRequest request) {
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            LogRecod.info("对用户[" + userName + "]进行登录验证..验证开始");
            currentUser.login(token);
            LogRecod.info("对用户[" + userName + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            LogRecod.info("对用户[" + userName + "]进行登录验证..验证未通过,未知账户");
            return Response.error(400, "error", "未知账户");
        } catch (IncorrectCredentialsException ice) {
            LogRecod.info("对用户[" + userName + "]进行登录验证..验证未通过,错误的凭证");
            return Response.error(400, "message", "密码不正确");
        } catch (LockedAccountException lae) {
            LogRecod.info("对用户[" + userName + "]进行登录验证..验证未通过,账户已锁定");
            Response.error(400, "message", "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            LogRecod.info("对用户[" + userName + "]进行登录验证..验证未通过,错误次数过多");
            Response.error(400, "message", "用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            LogRecod.info("对用户[" + userName + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            Response.error(400, "message", "用户名或密码不正确");
        }
        //验证是否登录成功
        if (currentUser.isAuthenticated()) {
            LogRecod.info("用户[" + userName + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            return Response.success("login success");
        } else {
            token.clear();
            return Response.error(400, "login failed", "login failed");
        }
    }

    @RequestMapping("logout")
    @ResponseBody
    public Response logout(HttpServletRequest request) {
        //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
        SecurityUtils.getSubject().logout();
        return Response.success("您已安全退出");
    }
}