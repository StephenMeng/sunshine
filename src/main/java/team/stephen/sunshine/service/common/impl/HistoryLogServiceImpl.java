package team.stephen.sunshine.service.common.impl;


import org.springframework.stereotype.Service;
import team.stephen.sunshine.model.common.HistoryLog;
import team.stephen.sunshine.service.common.HistoryLogService;

/**
 * 日志service
 * @author stephen
 * @date 2018/5/26
 */
@Service
public class HistoryLogServiceImpl implements HistoryLogService {
    @Override
    public int addArticleLog(HistoryLog log) {
        return 0;
    }

    @Override
    public int addBlogLog(HistoryLog log) {
        return 0;
    }
}
