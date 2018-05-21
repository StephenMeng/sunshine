package team.stephen.sunshine.util.common;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;

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
}
