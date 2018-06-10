package team.stephen.sunshine;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author stephen
 * @date 2017/7/14
 */
@EnableSwagger2
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class})
@EnableScheduling
@ServletComponentScan
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    static {
        try {
            // 初始化log4j
            String log4jPath = Application.class.getClassLoader().getResource("").getPath() + "log4j.properties";
            logger.info("初始化Log4j......");
            PropertyConfigurator.configure(log4jPath);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(Application.class, args);
    }
}