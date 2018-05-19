package team.stephen.sunshine.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.constant.JedisConst;
import team.stephen.sunshine.dto.UserDto;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.CacheService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.StringUtils;
import team.stephen.sunshine.util.jedis.JedisAdapter;

@Service
public class CacheServiceImpl implements CacheService {
    @Autowired
    private JedisAdapter jedisAdapter;
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
        String userStr = jedisAdapter.get(idKey);
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
        String userStr = jedisAdapter.get(key);
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
        jedisAdapter.set(idKey, JSONObject.toJSONString(userDto));
        jedisAdapter.set(noKey, JSONObject.toJSONString(userDto));
        return true;
    }
}
