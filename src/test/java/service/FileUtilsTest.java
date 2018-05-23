package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.util.common.FileUtils;
import team.stephen.sunshine.util.helper.FtpClientFactory;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class FileUtilsTest {

    @Test
    public void testAdd() {
        String ftpHost = "192.168.2.117";
        int ftpPort = 21;
        int ftpClientoolSize = 10;
        String ftpUserName = "ftp-root";
        String ftpPassword = "016611sai";

        FtpClientFactory.init(ftpHost, ftpPort, ftpClientoolSize, ftpClientMaxPoolSize, ftpUserName, ftpPassword);
        for (int i = 0; i < 20; i++) {
            final int n = i;
            new Thread(() -> {
                try {
                    FileUtils.uploadFtpFile(null, null, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }
}
