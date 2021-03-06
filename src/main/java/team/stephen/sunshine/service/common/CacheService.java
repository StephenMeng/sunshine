package team.stephen.sunshine.service.common;

import team.stephen.sunshine.web.dto.user.UserDto;

public interface CacheService {
    UserDto findUserDtoByUserId(Integer userId);

    UserDto findUserDtoByUserNo(String userNo);

    boolean addOrUpdateUserCache(UserDto userDto);

    int removeCache(String userNo);
}
