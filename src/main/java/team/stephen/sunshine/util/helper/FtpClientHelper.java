package team.stephen.sunshine.util.helper;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * ftpClient的帮助类
 *
 * @author stephen
 * @date 2018/5/23
 */
public class FtpClientHelper {
    private FTPClient ftpClient;
    private FtpClientFactory ftpClientFactory;

    public FtpClientHelper() {
        ftpClient = new FTPClient();
    }

    public void setFtpClientFactory(FtpClientFactory ftpClientFactory) {
        this.ftpClientFactory = ftpClientFactory;
    }

    public synchronized void returnToPool() {
        ftpClientFactory.recycle(this);
    }

    public int getReplyCode() {
        return ftpClient.getReplyCode();
    }

    public void changeWorkingDirectory(String s) throws IOException {
        ftpClient.changeWorkingDirectory(s);
    }

    public void setFileType(int binaryFileType) throws IOException {
        ftpClient.setFileType(binaryFileType);
    }

    public void disconnect() throws IOException {
        ftpClient.disconnect();
    }

    public void storeFile(String s, InputStream inputStream) throws IOException {
        ftpClient.storeFile(s, inputStream);
    }

    public void connect(String s, int i) throws IOException {
        ftpClient.connect(s, i);
    }

    public void login(String s, String p) throws IOException {
        ftpClient.login(s, p);
    }

    public void remove() {
        ftpClientFactory.remove(this);
    }

    public FTPFile[] listFiles() throws IOException {
        return ftpClient.listFiles();
    }

    public void retrieveFile(String name, OutputStream os) throws IOException {
        ftpClient.retrieveFile(name, os);
    }

    public void setKeepAliveTimeOut(int time) {
        ftpClient.setControlKeepAliveTimeout(time);
    }
}
