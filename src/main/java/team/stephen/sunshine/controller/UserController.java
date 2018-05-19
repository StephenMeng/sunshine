package team.stephen.sunshine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.util.Response;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {
    @RequestMapping("info")
    public Response info(){
        return Response.success("info");
    }
    @RequestMapping("ok")
    public Response userCheck(){
        return Response.success("ok");
    }
}
