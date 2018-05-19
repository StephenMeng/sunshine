package team.stephen.sunshine.service.user;

import team.stephen.sunshine.model.user.User;

public interface UserService {

    int updateSelective(User user);

    User getUserByUserId(Integer userId);

    User getUserByUserNo(String userNo);
}
