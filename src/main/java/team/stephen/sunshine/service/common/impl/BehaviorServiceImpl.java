package team.stephen.sunshine.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.user.UserBinBehaviorDao;
import team.stephen.sunshine.service.common.BehaviorService;

/**
 * @author Stephen
 * @date 2018/05/26 15:28
 */
@Service
public class BehaviorServiceImpl implements BehaviorService {
    @Autowired
    private UserBinBehaviorDao userBinBehaviorDao;

}
