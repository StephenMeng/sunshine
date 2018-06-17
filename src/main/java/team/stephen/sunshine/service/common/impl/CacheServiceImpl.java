package team.stephen.sunshine.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.constant.JedisConst;
import team.stephen.sunshine.web.dto.user.UserDto;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.CacheService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.element.StringUtils;

@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UserService userService;
    @Autowired
    private DtoTransformService dtoTransformService;
    @Override
    public UserDto findUserDtoByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        String idKey = String.format(JedisConst.USER_INFO_ID, userId);
        String userStr = jedisService.get(idKey);
        if (!StringUtils.isNull(userStr)) {
            return JSONObject.parseObject(userStr, UserDto.class);
        }
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return null;
        }

        UserDto userDto = dtoTransformService.userModelToDto(user);
        addOrUpdateUserCache(userDto);
        return userDto;

    }

    @Override
    public UserDto findUserDtoByUserNo(String userNo) {
        if (StringUtils.isNull(userNo)) {
            return null;
        }
        String key = String.format(JedisConst.USER_INFO_NO, userNo);
        String userStr = jedisService.get(key);
        if (!StringUtils.isNull(userStr)) {
            return JSONObject.parseObject(userStr, UserDto.class);
        }
        User user = userService.getUserByUserNo(userNo);
        if (user == null) {
            return null;
        }
        UserDto userDto = dtoTransformService.userModelToDto(user);
        addOrUpdateUserCache(userDto);
        return userDto;
    }

    @Override
    public boolean addOrUpdateUserCache(UserDto userDto) {
        if (userDto == null || userDto.getUserId() == null) {
            return false;
        }
        String idKey = String.format(JedisConst.USER_INFO_ID, userDto.getUserId());
        String noKey = String.format(JedisConst.USER_INFO_NO, userDto.getUserNo());
        jedisService.set(idKey, JSONObject.toJSONString(userDto));
        jedisService.set(noKey, JSONObject.toJSONString(userDto));
        return true;
    }

    @Override
    public int removeCache(String userNo) {
        UserDto userDto=findUserDtoByUserNo(userNo);
        if(userDto!=null) {
            String noKey = String.format(JedisConst.USER_INFO_NO, userDto.getUserNo());
            jedisService.remove(noKey);
            return 1;
        }
        return 0;
    }
}
