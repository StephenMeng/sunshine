package team.stephen.sunshine.service.user;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.model.user.UserEducation;

public interface UserService {

    int updateSelective(User user);

    User getUserByUserId(Integer userId);

    User getUserByUserNo(String userNo);

    Page<User> select(User condition, Integer pageNum, Integer pageSize);

    int insert(User user);

    int deleteUser(String userNo);

    Page<UserEducation> getUserEducation(Integer userId, Integer pageNum, Integer pageSize);

    int addUserEducation(UserEducation education);

    int updateUserEducationSelective(UserEducation education);

    int deleteUserEducation(Integer eid);

}
