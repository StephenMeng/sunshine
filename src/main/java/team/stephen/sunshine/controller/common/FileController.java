package team.stephen.sunshine.controller.common;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.stephen.sunshine.util.common.FileUtils;
import team.stephen.sunshine.util.common.QRCodeUtils;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.helper.FtpClientFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static team.stephen.sunshine.conf.GloableConfig.AVATAR_URI;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("file")
public class FileController {
    @ApiOperation(value = "上传文件", httpMethod = "POST", response = Response.class)
    @PostMapping(name = "upload", consumes = "multipart/*", headers = "content-type=multipart/formdata")
    public Response update(@ApiParam(value = "上传的文件", required = true) MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            FileUtils.upload(file.getInputStream(), AVATAR_URI + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.success(true);
    }

    @ApiOperation(value = "下载文件", httpMethod = "POST", response = Response.class)
    @ApiImplicitParam(name = "path", value = "下载路径", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public Response download(String path, HttpServletResponse response) {
        FileUtils.download(response, path);
        return Response.success(true);
    }

    @ApiOperation(value = "生成二维码", httpMethod = "GET", response = Response.class)
    @ApiImplicitParam(name = "text", value = "文本信息", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "qr", method = RequestMethod.GET)
    public Response qrCode(String text, HttpServletResponse response) {
        text = "http://im.nju.edu.cn";
        QRCodeUtils.create(text, response);
        return Response.success(true);
    }

    @ApiOperation(value = "ftp下载", httpMethod = "GET", response = Response.class)
    @ApiImplicitParam(name = "filename", value = "文件名", required = true, dataType = "String", paramType = "query")
    @RequestMapping(value = "ftp/download", method = RequestMethod.GET)
    public Response ftpDownload(String filename, HttpServletResponse response) {
        try {
            FileUtils.downloadFtpFile("/img", filename + ".doc", response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.success(true);
    }
}
