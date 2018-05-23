package team.stephen.sunshine.util.helper;

import org.apache.commons.net.ftp.FTP;
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
    private String ftpHost;
    private int ftpPort;
    private int ftpClientPoolSize;
    private int ftpClientMaxPoolSize;
    private String ftpUserName;
    private String ftpPassword;
    private AtomicInteger userd = new AtomicInteger(0);
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
                helper.setFtpClientFactory(ftpClientFactory);
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
        return helper;
    }

    /**
     * 获取client，并且在此过程中删除不活跃的client
     */
    public static FtpClientHelper getFtpClient() {
        //没有加锁，可能会创建多于maxNumPoolSize的线程数。。不过影响不大
        if (ftpClients.size() == 0 && getInstance().getFtpClientMaxPoolSize() > getInstance().userd.get()) {
            try {
                FtpClientHelper helper = getDefaultFtpClientHelper(
                        getInstance().ftpHost, getInstance().ftpPort,
                        getInstance().ftpUserName,
                        getInstance().ftpPassword, getInstance());
                //临时创建的client设置超时时间为10分钟
                helper.setKeepAliveTimeOut(600);
                //此时无需添加client池，等回收时会自动添加。
                return helper;
            } catch (Exception e) {
                LogRecod.error(e.getStackTrace());
            }
        }
        FtpClientHelper ftp = ftpClients.poll();
        if (ftp == null) {
            return null;
        }
        int reply = ftp.getReplyCode();
        //判断活跃性
        if (!FTPReply.isPositiveCompletion(reply)) {
            try {
                ftp.disconnect();
                //失活的要删除，不然占堆内存空间
                getInstance().remove(ftp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //递归，做法并不推荐。
            ftp = getFtpClient();
        }
        return ftp;
    }

    void recycle(FtpClientHelper helper) {
        ftpClients.offer(helper);
    }

    public void remove(FtpClientHelper helper) {
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
