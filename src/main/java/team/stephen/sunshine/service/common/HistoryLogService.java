package team.stephen.sunshine.service.common;

import team.stephen.sunshine.model.common.HistoryLog;

/**
 * @author stephen
 * @date 2018/5/26
 */
public interface HistoryLogService {
    int addArticleLog(HistoryLog log);

    int addBlogLog(HistoryLog log);
}
