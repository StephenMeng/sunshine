package team.stephen.sunshine.util.common;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import team.stephen.sunshine.util.helper.FtpClientFactory;
import team.stephen.sunshine.util.helper.FtpClientHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static void upload(InputStream inputStream, String filePath) throws IOException {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            Files.createParentDirs(targetFile);
        }
        Files.write(IOUtils.toByteArray(inputStream), targetFile);
    }

    public static void download(HttpServletResponse response, String filePath) {
        // Get your file stream from wherever.
        File downloadFile = new File(filePath);
        //下载文件，一定要指定response的header，否则默认的文件名为download，没有后缀
        makeUpResponseHeader(downloadFile.getName(), response);
        response.setContentLength((int) downloadFile.length());
        // 将输入流输出到response
        try {
            InputStream myStream = new FileInputStream(filePath);
            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void uploadFtpFile(String pathName, String fileName, InputStream inputStream) throws IOException {
        FtpClientHelper ftp = FtpClientFactory.getFtpClient();
        if (ftp == null) {
            LogRecord.print("null ftp");
            return;
        }
        //链接远程服务
        //返回登录结果状态
        int reply = ftp.getReplyCode();
        //修改上传文件路径
        ftp.changeWorkingDirectory(pathName);
        //修改文件类型
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return;
        }
        //获取上传文件的输入流
        //把文件推到服务器上
        ftp.storeFile(fileName, inputStream);
        //回收
        ftp.returnToPool();
    }

    public static boolean downloadFtpFile(String pathname, String filename, HttpServletResponse response) throws IOException {

        boolean flag = false;
        FtpClientHelper ftp = FtpClientFactory.getFtpClient();
        if (ftp == null) {
            throw new IOException("ftp client is not enough");
        }
        try {
            System.out.println("开始下载文件");
            //下载文件，一定要指定response的header，否则默认的文件名为download，没有后缀
            makeUpResponseHeader(filename, response);
            //切换FTP目录
            ftp.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftp.listFiles();
            for (FTPFile file : ftpFiles) {
                if (filename.equalsIgnoreCase(file.getName())) {
                    LogRecord.print(filename);
                    ftp.retrieveFile(filename, response.getOutputStream());
                }
            }
            flag = true;
            System.out.println("下载文件成功");
        } catch (Exception e) {
            System.out.println("下载文件失败");
            e.printStackTrace();
        } finally {
            ftp.returnToPool();
        }
        return flag;
    }

    private static void makeUpResponseHeader(String filename, HttpServletResponse response) {
        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                filename);
        response.setHeader(headerKey, headerValue);
    }
}
