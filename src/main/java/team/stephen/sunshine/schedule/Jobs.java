package team.stephen.sunshine.schedule;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class Jobs {

    @Scheduled(cron = "0 1 0 0 0 0")
    public void test() {

    }

}
