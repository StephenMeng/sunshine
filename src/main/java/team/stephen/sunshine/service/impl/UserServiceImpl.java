package team.stephen.sunshine.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.UserDao;
import team.stephen.sunshine.model.User;
import team.stephen.sunshine.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User findByUserNo(String userNo) {
        if (StringUtils.isBlank(userNo)) {
            return null;
        }
        User condition = new User();
        condition.setUserNo(userNo);
        return userDao.selectOne(condition);
    }
}
