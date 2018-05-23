package team.stephen.sunshine.util.helper;

import team.stephen.sunshine.util.common.LogRecod;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ftpClient的工厂类，采用线程池
 *
 * @author stephen
 * @date 2018/5/23
 */
public class FtpClientFactory {
    private static ConcurrentLinkedQueue<FtpClientHelper> ftpClients = new ConcurrentLinkedQueue<>();
    private static FtpClientFactory ftpClientFactory = new FtpClientFactory();

    private FtpClientFactory() {
    }

    public static FtpClientFactory getInstance() {
        return ftpClientFactory;
    }

    public static FtpClientHelper getFtpClient() {
        return ftpClients.poll();
    }

    public static void init(String ftpHost, int ftpPort, int poolSize, String ftpUserName, String ftpPassword) {
        for (int i = 0; i < poolSize; i++) {
            try {
                FtpClientHelper helper = new FtpClientHelper();
                helper.connect(ftpHost, ftpPort);
                helper.login(ftpUserName, ftpPassword);
                ftpClients.add(helper);
            } catch (Exception e) {
                LogRecod.error(e.getStackTrace());
            }
        }
        ftpClients.forEach(f -> f.setFtpClientFactory(getInstance()));
        LogRecod.info("ftp client factory 共成功初始化：" + ftpClients.size() + "个 client");
    }

    public void recycle(FtpClientHelper helper) {
        LogRecod.print("回收");
        ftpClients.offer(helper);
    }

    public void remove(FtpClientHelper helper) {
        ftpClientFactory.remove(helper);
    }
}
