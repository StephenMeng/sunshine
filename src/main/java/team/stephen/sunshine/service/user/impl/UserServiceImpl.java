package team.stephen.sunshine.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.sunshine.user.UserDao;
import team.stephen.sunshine.dao.sunshine.user.UserEducationDao;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.model.user.UserEducation;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.element.StringUtils;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserEducationDao userEducationDao;
    @Override
    public User getUserByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        User condition = new User();
        condition.setUserId(userId);
        return userDao.selectOne(condition);
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
    public Page<User> select(User condition, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return (Page<User>) userDao.select(condition);
    }

    @Override
    public int insert(User user) {
        return userDao.insert(user);
    }

    @Override
    public int deleteUser(String userNo) {
        User user = new User();
        user.setUserNo(userNo);
        return userDao.delete(user);
    }

    @Override
    public Page<UserEducation> getUserEducation(Integer userId, Integer pageNum,Integer pageSize) {
        UserEducation condition=new UserEducation();
        condition.setUserId(userId);
        PageHelper.startPage(pageNum,pageSize);
        return (Page<UserEducation>) userEducationDao.select(condition);
    }

    @Override
    public int addUserEducation(UserEducation education) {
        return userEducationDao.insert(education);
    }

    @Override
    public int updateUserEducationSelective(UserEducation education) {
        return userEducationDao.updateByPrimaryKeySelective(education);
    }

    @Override
    public int deleteUserEducation(Integer eid) {
        return userEducationDao.deleteByPrimaryKey(eid);
    }

    @Override
    public int updateSelective(User user) {
        return userDao.updateByPrimaryKeySelective(user);
    }


}
