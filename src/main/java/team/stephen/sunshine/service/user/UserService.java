package team.stephen.sunshine.service.user;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.user.User;

public interface UserService {

    int updateSelective(User user);

    User getUserByUserId(Integer userId);

    User getUserByUserNo(String userNo);

    Page<User> select(User condition, Integer pageNum, Integer pageSize);

    int insert(User user);
}
