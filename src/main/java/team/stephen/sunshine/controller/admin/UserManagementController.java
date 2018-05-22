package team.stephen.sunshine.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.CacheService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.common.*;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.user.SignInUserDto;
import team.stephen.sunshine.web.dto.user.UpdateUserDto;
import team.stephen.sunshine.web.dto.user.UserDto;

/**
 * 用户管理
 *
 * @author stephen
 * @date 2018/5/22
 */
@Api(description = "管理员管理系统用户")
@RequestMapping("admin/user")
@RestController
public class UserManagementController {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private DtoTransformService dtoTransformService;


    @ApiOperation(value = "新增成员", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Response add(SignInUserDto signInUserDto) {
        ParamCheck check = checkSignParam(signInUserDto);
        if (check.error()) {
            return Response.error(check);
        }
        User user = parseUserDto(signInUserDto);
        //密码加密
        user.setPassword(SecurityUtil.getEncodedPassword(user.getUserNo(), user.getUserNo()));
        try {
            userService.insert(user);
        } catch (Exception e) {
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    private ParamCheck checkSignParam(SignInUserDto signInUserDto) {
        if (StringUtils.isBlank(signInUserDto.getUserNo())) {
            return ParamCheck.error("学号/工号不能为空", "the teacher/student's number can not be null");
        }
        if (StringUtils.isBlank(signInUserDto.getUserName())) {
            return ParamCheck.error("姓名不能为空", "the name can not be null");
        }
        if (StringUtils.isBlank(signInUserDto.getTitle())) {
            return ParamCheck.error("学位/职称不能为空", "the title can not be null");
        }
        if (StringUtils.isBlank(signInUserDto.getEmail())) {
            return ParamCheck.error("邮箱不能为空", "the email can not be null");
        }
        return ParamCheck.right();
    }

    @ApiOperation(value = "更新成员", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response update(UpdateUserDto updateUserDto) {
        ParamCheck check = checkUpdateParam(updateUserDto);
        if (check.error()) {
            return Response.error(check);
        }
        User user = parseUserDto(updateUserDto);
        try {
            userService.updateSelective(user);
        } catch (Exception e) {
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    private ParamCheck checkUpdateParam(UpdateUserDto updateUserDto) {
        if (updateUserDto.getUserId() == null) {
            return ParamCheck.error("userId不能为空", "the teacher/student's number can not be null");
        }
        return ParamCheck.right();
    }

    private User parseUserDto(SignInUserDto userDto) {
        User user = new User();
        dtoTransformService.copyProperties(user, userDto);
        //统一大写
        if (StringUtils.isNotNull(user.getUserNo())) {
            user.setUserNo(user.getUserNo().toUpperCase());
        }
        return user;
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public Response select(String userNo) {
        return Response.success(cacheService.findUserDtoByUserNo(userNo));
    }

    @ApiOperation(value = "获取成员列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public Response selectLit(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        User condition = new User();
        Page<User> userPage = userService.select(condition, pageNum, pageSize);
        Page<UserDto> userDtoPage = PageUtil.transformPage(userPage, dtoTransformService::userModelToDto);
        return Response.success(new PageInfo<>(userDtoPage));
    }
}
