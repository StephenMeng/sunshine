package team.stephen.sunshine.web.schedule;


import org.springframework.scheduling.annotation.Scheduled;

public class Jobs {

    @Scheduled(cron = "0 1 0 0 0 0")
    public void test() {

    }

}
