package team.stephen.sunshine.controller.member;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.web.dto.user.UserDto;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private DtoTransformService dtoTransformService;

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public Response info() {
        return Response.success(getUser());
    }

    @RequestMapping(value = "ok", method = RequestMethod.GET)
    public Response userCheck() {
        return Response.success("ok");
    }

    @ApiOperation(value = "更新用户信息", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response updateUser(UserDto userDto) {
        User user = dtoTransformService.userDtoToModel(userDto);
        user.setPassword(
                new SimpleHash("md5", user.getPassword(),
                        ByteSource.Util.bytes(user.getUserNo()), 2).toString());
        return Response.success(userService.updateSelective(user));
    }
}
