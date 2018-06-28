package team.stephen.sunshine.controller.personal;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.stephen.sunshine.constant.enu.DegreeEnum;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.model.user.UserEducation;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.PageUtil;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.web.dto.user.UserDto;
import team.stephen.sunshine.web.dto.user.UserEducationDto;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("personal")
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

    @ApiOperation(value = "新增用户教育经历", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "education/add", method = RequestMethod.POST)
    public Response addUserEducation(@RequestBody UserEducationDto educationDto) {
        UserEducation userEducation = new UserEducation();
        BeanUtils.copyProperties(educationDto, userEducation);
        userEducation.setUserId(getUser().getUserId());
        userEducation.setDegree(DegreeEnum.getCodeByCn(educationDto.getDegree()));
        userService.addUserEducation(userEducation);
        return Response.success(true);
    }

    @ApiOperation(value = "更新用户教育经历", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "education/update", method = RequestMethod.POST)
    public Response updateUserEducation(@RequestBody UserEducationDto educationDto) {
        UserEducation userEducation = new UserEducation();
        userEducation.setUserId(getUser().getUserId());
        BeanUtils.copyProperties(educationDto, userEducation);
        userService.updateUserEducationSelective(userEducation);
        return Response.success(true);
    }

    @ApiOperation(value = "获取用户教育经历", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "education/list", method = RequestMethod.GET)
    public Response selectLit(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<UserEducation> educationPage = userService.getUserEducation(getUser().getUserId(), pageNum, pageSize);
        Page<UserEducationDto> userEducationDtoPage = PageUtil.transformPage(educationPage, education -> {
            UserEducationDto dto = new UserEducationDto();
            BeanUtils.copyProperties(education, dto);
            dto.setDegree(DegreeEnum.getCnByCode(education.getDegree()));
            return dto;
        });
        return Response.success(new PageInfo<>(userEducationDtoPage));
    }

    @ApiOperation(value = "刪除用户教育经历", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "eid", value = "eid", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "education/delete", method = RequestMethod.POST)
    public Response add(@RequestParam("eid") Integer eid) {
        return Response.success(userService.deleteUserEducation(eid));
    }
}
