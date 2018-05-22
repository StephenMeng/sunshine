package team.stephen.sunshine.util.helper;

/**
 * 文件辅助工具类
 *
 * @author stephen
 * @date 2018/5/22
 */
public class FileHelper {
    private String fileName;
    private String fileType;
    private Long fileLength;
    private String filePath;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
