package team.stephen.sunshine.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.user.UserDao;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.element.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        User condition = new User();
        condition.setUserId(userId);
        return userDao.selectByPrimaryKey(condition);
    }

    @Override
    public User getUserByUserNo(String userNo) {
        if (StringUtils.isNull(userNo)) {
            return null;
        }
        User condition = new User();
        condition.setUserNo(userNo);
        return userDao.selectOne(condition);
    }

    @Override
    public int updateSelective(User user) {
        return userDao.updateByPrimaryKeySelective(user);
    }


}
