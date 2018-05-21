package team.stephen.sunshine.web.schedule;


import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务
 *
 * @author stephen
 */
public class Jobs {

    @Scheduled(cron = "0 1 0 0 0 0")
    public void test() {

    }

}
