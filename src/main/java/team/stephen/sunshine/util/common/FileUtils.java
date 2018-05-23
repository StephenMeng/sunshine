package team.stephen.sunshine.util.common;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
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

        // set to binary type
        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);

        // Copy the stream to the response's output stream.
        try {
            InputStream myStream = new FileInputStream(filePath);
            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connect(int i) throws IOException {
        FtpClientHelper ftp = FtpClientFactory.getFtpClient();
        if (ftp == null) {
            LogRecod.print("null ftp");
            return;
        }
        //链接远程服务
        //返回登录结果状态
        int reply = ftp.getReplyCode();
        //修改上传文件路径
        ftp.changeWorkingDirectory("C:\\Users\\ftp-root\\ftp-upload");
        //修改文件类型
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return;
        }
        //获取上传文件的输入流
        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\DELL\\Desktop\\spark2.txt"));
        //把文件推到服务器上
        ftp.storeFile("spark" + i + ".txt", fileInputStream);
        //回收
        ftp.returnToPool();
        LogRecod.print("上传成功");
    }
}
