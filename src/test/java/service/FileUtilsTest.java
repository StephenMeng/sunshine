package service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.util.common.FileUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.helper.FtpClientFactory;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class FileUtilsTest {

    @Test
    public void testAdd() {
        String ftpHost = "192.168.1.103";
        int ftpPort = 21;
        int ftpClientoolSize = 2;
        int maxPoolSiz = 8;
        String ftpUserName = "Stephen_FTP";
        String ftpPassword = "016611";

        FtpClientFactory.init(ftpHost, ftpPort, ftpClientoolSize, maxPoolSiz, ftpUserName, ftpPassword);
        for (int i = 0; i < 20; i++) {
            final int n = i;
            new Thread(() -> {
                try {
                    final InputStream inputStream = new FileInputStream(new File("C:\\Users\\Stephen\\Desktop\\毕业论文\\参考文献\\phd.pdf"));
                    FileUtils.uploadFtpFile("/", "phd" + n + ".pdf", inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 20; i++) {
            final int n = i;
            new Thread(() -> {
                try {
                    final InputStream inputStream = new FileInputStream(new File("C:\\Users\\Stephen\\Desktop\\毕业论文\\参考文献\\phd.pdf"));
                    FileUtils.uploadFtpFile("/", "phd" + n + ".pdf", inputStream);
                    Thread.sleep(2000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @Test
    public void testList() {
        ConcurrentLinkedQueue<String> strings = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 4; i++) {
            strings.add("str" + i);
        }
        LogRecord.print(strings);
        for (int i = 0; i < 4; i++) {
            if (i / 2 == 0) {
            }
        }
        LogRecord.print(strings);
    }
}
