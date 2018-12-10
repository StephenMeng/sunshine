package team.stephen.sunshine.controller.other;

import com.google.common.collect.Lists;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import team.stephen.sunshine.constant.enu.Topic;
import team.stephen.sunshine.controller.BaseController;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.model.user.User;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.other.CrawlErrorService;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.service.user.UserService;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.common.Response;
import team.stephen.sunshine.web.dto.user.SignInUserDto;
import team.stephen.sunshine.web.dto.user.UserDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author stephen
 * @date 2018/5/21
 */
@RestController
@RequestMapping("other/weibo")
public class WeiboController extends BaseController {
    @Autowired
    private WeiboService weiboService;
    private static String cookie =
            "SINAGLOBAL=8490149224106.765.1520337385948; login_sid_t=56bb5ab6d316649f4b928e94e59f64e1; cross_origin_proto=SSL; YF-Ugrow-G0=169004153682ef91866609488943c77f; YF-V5-G0=9717632f62066ddd544bf04f733ad50a; WBStorage=5548c0baa42e6f3d|undefined; wb_view_log=1366*7681; _s_tentry=www.baidu.com; UOR=www.hankcs.com,widget.weibo.com,www.baidu.com; crossidccode=CODE-yf-1FzY4j-29rLT2-K6GkdX8xOjvyWRVb909c9; Apache=3171886070859.895.1530534996889; ULV=1530534997868:6:1:1:3171886070859.895.1530534996889:1523867738876; ALF=1562071003; SSOLoginState=1530535004; SUB=_2A252PmwMDeRhGeNJ7FYT8SnIyDiIHXVVStrErDV8PUNbmtANLULmkW9NS7N__gfN1QGdBQ8aa_pDc-QTT00lBZtZ; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5KzhUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt; SUHB=0rrrzJ_fhY_jmQ; wvr=6; YF-Page-G0=70942dbd611eb265972add7bc1c85888; wb_view_log_5774217434=1366*7681";
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Cookie", cookie);
    }

    private int corePoolSize = 20;
    private int maximumPoolSize = 40;
    private int keeAliveTime = 30;


    @ApiOperation(value = "查询微博用户信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public Response crawlUser(int pageNum, int pageSize) {
//        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keeAliveTime,
//                TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new DefaultThreadFactory(Topic.EMAIL.getName()));
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<WeiboUserConfig> userConfigs = weiboService.selectUserConfig(null, pageNum, pageSize);
        List<String> ouIds = userConfigs.stream().filter(conf -> conf.getName() == null).map(WeiboUserConfig::getOid).collect(Collectors.toList());
        ouIds= Lists.reverse(ouIds);
        LogRecord.print(ouIds.size());
        for (String ouid : ouIds) {
            executor.execute(() -> {
                String url = "https://weibo.com/u/" + ouid + "?refer_flag=1001030201_";
                LogRecord.print(url);
                WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
                if (config.getName() != null) {
                    weiboService.updateSelective(config);
                }else {
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
//            break;
        }
        return Response.success(ouIds.size());
    }
    @ApiOperation(value = "查询微博用户信息", httpMethod = "GET", response = Response.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "数据量", required = true, dataType = "int", paramType = "query")})
    @RequestMapping(value = "user-uri", method = RequestMethod.GET)
    public Response crawlUserByUril(int pageNum, int pageSize) {
//        ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keeAliveTime,
//                TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new DefaultThreadFactory(Topic.EMAIL.getName()));
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<WeiboUserConfig> userConfigs = weiboService.selectUserConfig(null, pageNum, pageSize);
        List<String> uris = userConfigs.stream().filter(conf -> conf.getName() == null).map(WeiboUserConfig::getUri).collect(Collectors.toList());
        LogRecord.print(uris.size());
        for (String uri : uris) {
            executor.execute(() -> {
                String url = "https://weibo.com"+uri;
                LogRecord.print(url);
                WeiboUserConfig config = weiboService.crawlUserConfig(url, headers);
                config.setUri(uri);
                if (config.getName() != null) {
                    weiboService.updateSelective(config);
                }else {
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
//            break;
        }
        return Response.success(uris.size());
    }
}
