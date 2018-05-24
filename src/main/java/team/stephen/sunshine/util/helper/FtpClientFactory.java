package team.stephen.sunshine.util.helper;

import org.apache.commons.net.ftp.FTPReply;
import team.stephen.sunshine.util.common.LogRecod;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ftpClient的工厂类，采用线程池
 *
 * @author stephen
 * @date 2018/5/23
 */
public class FtpClientFactory {
    private final Object lock = "_lock";
    private String ftpHost;
    private int ftpPort;
    private int ftpClientPoolSize;
    private int ftpClientMaxPoolSize;
    private String ftpUserName;
    private String ftpPassword;
    private AtomicInteger used = new AtomicInteger(0);
    private static ConcurrentLinkedQueue<FtpClientHelper> ftpClients = new ConcurrentLinkedQueue<>();
    /**
     * 饿汉式单例 - -
     */
    private static FtpClientFactory ftpClientFactory = new FtpClientFactory();

    public static FtpClientFactory getInstance() {
        return ftpClientFactory;
    }

    public static void init(String ftpHost, int ftpPort, int poolSize, int ftpClientMaxPoolSize, String ftpUserName, String ftpPassword) {
        FtpClientFactory ftpClientFactory = getInstance();
        ftpClientFactory.setFtpHost(ftpHost);
        ftpClientFactory.setFtpPort(ftpPort);
        ftpClientFactory.setFtpUserName(ftpUserName);
        ftpClientFactory.setFtpPassword(ftpPassword);
        ftpClientFactory.setFtpClientPoolSize(poolSize);
        ftpClientFactory.setFtpClientMaxPoolSize(ftpClientMaxPoolSize);
        for (int i = 0; i < poolSize; i++) {
            try {
                FtpClientHelper helper = getDefaultFtpClientHelper(ftpHost, ftpPort, ftpUserName, ftpPassword, ftpClientFactory);
                ftpClients.add(helper);
            } catch (Exception e) {
                LogRecod.error(e.getStackTrace());
            }
        }
        LogRecod.info("ftp client factory 共成功初始化：" + ftpClients.size() + "个 client");

    }

    private static FtpClientHelper getDefaultFtpClientHelper(String ftpHost, int ftpPort, String ftpUserName, String ftpPassword, FtpClientFactory ftpClientFactory) throws IOException {
        FtpClientHelper helper = new FtpClientHelper();
        helper.connect(ftpHost, ftpPort);
        helper.login(ftpUserName, ftpPassword);
        helper.setFtpClientFactory(ftpClientFactory);

        return helper;
    }

    /**
     * 获取client，并且在此过程中删除不活跃的client
     */
    public static FtpClientHelper getFtpClient() {
        FtpClientHelper ftp = ftpClients.poll();
        if (ftp == null) {
            //todo  client的size 和used 加起来才可以
            if (getInstance().used.get() < getInstance().getFtpClientMaxPoolSize()) {
                synchronized (getInstance().lock) {
                    if (getInstance().used.get() < getInstance().getFtpClientMaxPoolSize()) {
                        try {
                            FtpClientHelper helper = getDefaultFtpClientHelper(
                                    getInstance().ftpHost, getInstance().ftpPort,
                                    getInstance().ftpUserName,
                                    getInstance().ftpPassword, getInstance());
                            //临时创建的client设置超时时间为10分钟
                            helper.setKeepAliveTimeOut(3);
                            //此时无需添加client池，等回收时会自动添加。
                            LogRecod.print("poll size:" + ftpClients.size());
                            LogRecod.print("used :" + getInstance().used.get());
                            LogRecod.print("新增 ftp client 成功");

                            return helper;
                        } catch (Exception e) {
                            LogRecod.error(e.getStackTrace());
                        }
                    }
                }
            }
        }
        //没有加锁，可能会创建多于maxNumPoolSize的线程数。。不过影响不大
        LogRecod.print("get poll size:" + ftpClients.size());
        LogRecod.print("get used :" + getInstance().used.get());
        int reply = ftp.getReplyCode();
        //判断活跃性
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.remove();
            try {
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //递归获取ftp client
            ftp = getFtpClient();
        }
        if (ftp != null) {
            getInstance().used.addAndGet(1);
        }
        return ftp;
    }

    void recycle(FtpClientHelper helper) {
        getInstance().used.decrementAndGet();
        ftpClients.offer(helper);
    }

    public void remove(FtpClientHelper helper) {
        LogRecod.print("删除 FTPClient");
        ftpClientFactory.remove(helper);
    }

    private void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    private void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    private void setFtpClientPoolSize(int ftpClientPoolSize) {
        this.ftpClientPoolSize = ftpClientPoolSize;
    }

    private void setFtpClientMaxPoolSize(int ftpClientMaxPoolSize) {
        this.ftpClientMaxPoolSize = ftpClientMaxPoolSize;
    }

    private void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    private void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public int getFtpClientMaxPoolSize() {
        return ftpClientMaxPoolSize;
    }


}
