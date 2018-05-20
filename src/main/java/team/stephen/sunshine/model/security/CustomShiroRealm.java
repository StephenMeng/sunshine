package team.stephen.sunshine.model.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.PermissionService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.LogRecod;

import java.util.List;

public class CustomShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        LogRecod.print("授权");
        LogRecod.print(principals.getPrimaryPrincipal());
        String userNo = (String) principals.getPrimaryPrincipal();
        List<String> roles = permissionService.getUserRoles(userNo);
        authorizationInfo.addRoles(roles);
        return authorizationInfo;
    }

    /**
     * 主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        LogRecod.print("验证");
        //获取用户的输入的账号.
        String userNo = (String) token.getPrincipal();
//        System.out.println(token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userService.getUserByUserNo(userNo);
        if (user == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(user.getUserNo(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getUserNo()), getName());
    }

}