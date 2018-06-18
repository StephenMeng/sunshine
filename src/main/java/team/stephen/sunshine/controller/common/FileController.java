package team.stephen.sunshine.controller.common;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.stephen.sunshine.constant.enu.ResultEnum;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.common.FileUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.QRCodeUtils;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.util.helper.FtpClientFactory;
import team.stephen.sunshine.web.dto.user.UserDto;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static team.stephen.sunshine.conf.GloableConfig.AVATAR_URI;
import static team.stephen.sunshine.conf.GloableConfig.PICTURE_URI;
import static team.stephen.sunshine.conf.GloableConfig.production;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("file")
public class FileController extends BaseController{
    @Autowired
    private UserService userService;
    @ApiOperation(value = "上传文件", httpMethod = "POST", response = Response.class)
    @PostMapping(value = "upload", consumes = "multipart/*", headers = "content-type=multipart/formdata")
    public Response update(@ApiParam(value = "上传的文件", required = true) MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            FileUtils.upload(file.getInputStream(), AVATAR_URI + "/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.success(true);
    }
    @ApiOperation(value = "上传头像", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "upload/avatar")
    public Response updateAvatar(@RequestParam("file")  MultipartFile file) {
        UserDto userDto=getUser();
        //todo 测试
        String fileName = file.getOriginalFilename();
        String uriBasePath="/img/avatar/";
        String uriPath=uriBasePath+fileName;
        try {
            File rootDir=ResourceUtils.getFile("classpath:static"+uriBasePath);
            String filePath=rootDir+ fileName;
            FileUtils.upload(file.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(ResultEnum.SERVER_WRONG);
        }
        User user=new User();
        user.setUserId(userDto.getUserId());
        user.setAvatarUrl(uriPath);
        userService.updateSelective(user);
        return Response.success(uriPath);
    }
    @ApiOperation(value = "上传图片", httpMethod = "POST", response = Response.class)
    @RequestMapping(value = "upload/picture")
    public Response updatePicture(@RequestParam("file")  MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String filePath=null;
        try {
            File rootDir=ResourceUtils.getFile("classpath:static/img/picture");
            filePath=rootDir+ "/" + fileName;
            FileUtils.upload(file.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.success("/img/picture/"+fileName);
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
