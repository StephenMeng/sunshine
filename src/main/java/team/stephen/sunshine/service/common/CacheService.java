package team.stephen.sunshine.service.common;

import team.stephen.sunshine.dto.UserDto;
import team.stephen.sunshine.model.user.User;

public interface CacheService {
    UserDto findUserDtoByUserId(Integer userId);

    UserDto findUserDtoByUserNo(String userNo);

    boolean addOrUpdateUserCache(UserDto userDto);

}
