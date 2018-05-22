package team.stephen.sunshine.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.model.front.Column;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.front.ChannelService;
import team.stephen.sunshine.service.front.ColumnService;
import team.stephen.sunshine.util.common.PageUtil;
import team.stephen.sunshine.util.common.ParamCheck;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.element.StringUtils;
import team.stephen.sunshine.web.dto.front.AdminChannelDto;
import team.stephen.sunshine.web.dto.front.AdminColumnDto;
import team.stephen.sunshine.web.dto.front.StandardChannelDto;
import team.stephen.sunshine.web.dto.front.StandardColumnDto;

import static team.stephen.sunshine.util.element.StringUtils.EMPTY;

/**
 * front的频道、栏目管理
 *
 * @author stephen
 * @date 2018/5/22
 */
@RequestMapping("admin/front")
@RestController
public class FrontManageController extends BaseController {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ColumnService columnService;
    @Autowired
    private DtoTransformService dtoTransformService;

    @ApiOperation(value = "获取频道列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hasColumn", value = "是否有子栏目，默认是", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "deleted", value = "是否是回收站内的，默认否", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "channel", method = RequestMethod.GET)
    public Response getChannel(
            @RequestParam(value = "hasColumn", defaultValue = "true") Boolean hasColumn,
            @RequestParam(value = "deleted", defaultValue = "false") Boolean deleted,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Channel condition = new Channel();
        condition.setDeleted(deleted);
        condition.setHasColumn(hasColumn);

        Page<Channel> channelPage = channelService.select(condition, pageNum, pageSize);
        Page<StandardChannelDto> channelDtoPage = PageUtil.transformPage(channelPage, dtoTransformService::channelModelToDto);
        return Response.success(new PageInfo(channelDtoPage));
    }

    @ApiOperation(value = "新增频道", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "channel/add", method = RequestMethod.POST)
    public Response addChannel(AdminChannelDto adminChannelDto) {

        ParamCheck check = checkAddChannelParam(adminChannelDto);
        if (check.error()) {
            return Response.error(check);
        }
        adminChannelDto.setChannelId(null);
        Channel channel = new Channel();
        dtoTransformService.copyProperties(channel, adminChannelDto);
        channel.setDeleted(false);
        try

        {
            channelService.addChannel(channel);
        } catch (
                Exception e)

        {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "更新频道信息", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "channel/update", method = RequestMethod.POST)
    public Response updateChannel(AdminChannelDto adminChannelDto) {
        ParamCheck check = checkUpdateParam(adminChannelDto);
        if (check.error()) {
            return Response.error(check);
        }
        Channel channel = new Channel();
        dtoTransformService.copyProperties(channel, adminChannelDto);
        try {
            channelService.updateSelective(channel);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "将频道放入回收站", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "channelId", value = "频道ID", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "channel/recycle", method = RequestMethod.POST)
    public Response recycleChannel(Integer channelId) {
        return recycleOrRestoreChannel(channelId, true);
    }


    @ApiOperation(value = "将频道从回收站还原", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "channelId", value = "频道ID", required = true, dataType = "string", paramType = "query")
    @RequestMapping(value = "channel/restore", method = RequestMethod.POST)
    public Response restoreChannel(Integer channelId) {
        return recycleOrRestoreChannel(channelId, false);
    }

    private Response recycleOrRestoreChannel(Integer channelId, boolean recycle) {
        ParamCheck check = checkRecycleParam(channelId);
        if (check.error()) {
            return Response.error(check);
        }
        Channel channel = new Channel();
        channel.setChannelId(channelId);
        channel.setDeleted(recycle);
        try {
            channelService.updateSelective(channel);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    private ParamCheck checkRecycleParam(Integer channelId) {
        if (channelId == null || channelId < 0) {
            return ParamCheck.error("参数错误", "参数错误");
        }
        return ParamCheck.right();
    }

    private ParamCheck checkUpdateParam(AdminChannelDto adminChannelDto) {
        if (adminChannelDto == null) {
            return ParamCheck.error("参数为空", "参数为空");
        }
        if (adminChannelDto.getChannelId() == null) {
            return ParamCheck.error("频道ID为空", "频道ID为空");
        }
        if (EMPTY.equals(adminChannelDto.getChannelUri())) {
            return ParamCheck.error("频道uri为空", "频道uri为空");
        }
        if (EMPTY.equals(adminChannelDto.getChannelNameCn())) {
            return ParamCheck.error("频道中文名称为空", "频道中文名称为空");
        }
        if (EMPTY.equals(adminChannelDto.getChannelNameEn())) {
            return ParamCheck.error("频道英文文名称为空", "频道英文文名称为空");
        }
        return ParamCheck.right();
    }

    private ParamCheck checkAddChannelParam(AdminChannelDto adminChannelDto) {
        if (adminChannelDto == null) {
            return ParamCheck.error("参数为空", "null param");
        }
        if (StringUtils.isBlank(adminChannelDto.getChannelUri())) {
            return ParamCheck.error("频道uri为空", "null param");
        }
        if (StringUtils.isBlank(adminChannelDto.getChannelNameCn())) {
            return ParamCheck.error("频道中文名称为空", "null param");
        }
        if (StringUtils.isBlank(adminChannelDto.getChannelNameEn())) {
            return ParamCheck.error("频道英文名称为空", "null param");
        }
        if (adminChannelDto.getHasColumn() == null) {
            return ParamCheck.error("频道是否有子栏目为空", "null param");
        }
        return ParamCheck.right();
    }

    @ApiOperation(value = "获取栏目列表", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "channelId", value = "父频道ID", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "deleted", value = "是否是回收站内的，默认否", dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "column", method = RequestMethod.GET)
    public Response getColumn(
            @RequestParam(value = "channelId", defaultValue = "true") Integer channelId,
            @RequestParam(value = "deleted", defaultValue = "false") Boolean deleted,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Column condition = new Column();
        condition.setDeleted(deleted);
        condition.setChannelId(channelId);
        PageHelper.startPage(pageNum, pageSize);
        Page<Column> channelPage = columnService.select(condition, pageNum, pageSize);
        Page<StandardColumnDto> channelDtoPage = PageUtil.transformPage(channelPage, dtoTransformService::columnModelToDto);
        return Response.success(new PageInfo(channelDtoPage));
    }

    @ApiOperation(value = "新增栏目", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "column/add", method = RequestMethod.POST)
    public Response addColumn(AdminColumnDto adminColumnDto) {
        ParamCheck check = checkAddColumnParam(adminColumnDto);
        if (check.error()) {
            return Response.error(check);
        }
        adminColumnDto.setColumnId(null);
        Column column = new Column();
        dtoTransformService.copyProperties(column, adminColumnDto);
        column.setChannelId(adminColumnDto.getChannel().getChannelId());
        column.setDeleted(false);
        try {
            columnService.addcolumn(column);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "更新栏目信息", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "column/update", method = RequestMethod.POST)
    public Response updateChannel(AdminColumnDto adminColumnDto) {
        ParamCheck check = checkUpdateColumnParam(adminColumnDto);
        if (check.error()) {
            return Response.error(check);
        }
        Column column = new Column();
        dtoTransformService.copyProperties(column, adminColumnDto);
        try {
            columnService.updateSelective(column);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    @ApiOperation(value = "将栏目放入回收站", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "columnId", value = "栏目ID", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "column/recycle", method = RequestMethod.POST)
    public Response recycleColumn(Integer columnId) {
        return recycleOrRestoreColumn(columnId, true);
    }


    @ApiOperation(value = "将栏目从回收站还原", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "columnId", value = "栏目ID", required = true, dataType = "int", paramType = "query")
    @RequestMapping(value = "column/restore", method = RequestMethod.POST)
    public Response resotoreColumn(Integer columnId) {
        return recycleOrRestoreColumn(columnId, false);
    }

    private Response recycleOrRestoreColumn(Integer columnId, boolean recycle) {
        ParamCheck check = checkRecycleParam(columnId);
        if (check.error()) {
            return Response.error(check);
        }
        Column column = new Column();
        column.setColumnId(columnId);
        column.setDeleted(recycle);
        try {
            columnService.updateSelective(column);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG.getCode(), e.getMessage(), e.getMessage());
        }
        return Response.success(true);
    }

    private ParamCheck checkAddColumnParam(AdminColumnDto adminColumnDto) {
        if (adminColumnDto == null) {
            return ParamCheck.error("参数为空", "null param");
        }
        if (adminColumnDto.getChannel() == null || adminColumnDto.getChannel().getChannelId() == null) {
            return ParamCheck.error("频道ID为空", "null param");
        }
        if (channelService.selectByChannelId(adminColumnDto.getChannel().getChannelId()) == null) {
            return ParamCheck.error("频道ID不存在", "null param");
        }
        if (StringUtils.isBlank(adminColumnDto.getColumnUri())) {
            return ParamCheck.error("栏目uri为空", "null param");
        }
        if (StringUtils.isBlank(adminColumnDto.getColumnNameCn())) {
            return ParamCheck.error("栏目中文名称为空", "null 栏目中文名称为空");
        }
        if (StringUtils.isBlank(adminColumnDto.getColumnNameEn())) {
            return ParamCheck.error("栏目英文名称为空", "栏目英文名称为空 param");
        }
        return ParamCheck.right();
    }

    private ParamCheck checkUpdateColumnParam(AdminColumnDto adminColumnDto) {
        if (adminColumnDto == null) {
            return ParamCheck.error("参数为空", "null param");
        }
        if (adminColumnDto.getColumnId() == null) {
            return ParamCheck.error("栏目ID为空", "栏目ID为空");
        }
        if (adminColumnDto.getChannel() != null && adminColumnDto.getChannel().getChannelId() != null) {
            if (channelService.selectByChannelId(adminColumnDto.getChannel().getChannelId()) == null) {
                return ParamCheck.error("频道ID不存在", "null param");
            }
        }
        return ParamCheck.right();
    }
}
