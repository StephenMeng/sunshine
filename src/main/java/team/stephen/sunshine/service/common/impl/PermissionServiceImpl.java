package team.stephen.sunshine.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.permission.UserRoleRelationDao;
import team.stephen.sunshine.dto.UserDto;
import team.stephen.sunshine.model.permission.UserRoleRelation;
import team.stephen.sunshine.service.common.CacheService;
import team.stephen.sunshine.service.common.PermissionService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private UserRoleRelationDao userRoleRelationDao;
    @Autowired
    private CacheService cacheService;

    @Override
    public List<String> getUserRoles(Integer userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        UserRoleRelation condition = new UserRoleRelation();
        condition.setUserId(userId);
        List<UserRoleRelation> relations = userRoleRelationDao.select(condition);
        return relations.stream().map(UserRoleRelation::getUserRole).collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRoles(String userNo) {
        UserDto userDto = cacheService.findUserDtoByUserNo(userNo);
        if (userDto == null) {
            return Collections.emptyList();
        }
        return getUserRoles(userDto.getUserId());
    }
}
