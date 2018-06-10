package team.stephen.sunshine.controller.other;

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
    private static String cookie ="YF-V5-G0=b1e3c8e8ad37eca95b65a6759b3fc219; _s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; YF-Ugrow-G0=ea90f703b7694b74b62d38420b5273df; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; YF-Page-G0=00acf392ca0910c1098d285f7eb74a11; WBtopGlobal_register_version=cd58c0d338fe446e; SSOLoginState=1528313282; SUB=_2AkMsRywaf8NxqwJRmP8cz2jlaohwzQvEieKaG93BJRMxHRl-yT9jqmoQtRB6B8cC9RX4nU5FbPeZ2pNB1jbLql3U4zV3; SUBP=0033WrSXqPxfM72-Ws9jqgMF55529P9D9W5oAnXq5fc4mqqBjqOdeSSI";
//            "_s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SWBSSL=usrmdinst_7; SWB=usrmdinst_15; SSOLoginState=1528313282; wvr=6; ULOGIN_IMG=15283371533974; SCF=ApWJpYkIBCSLvQa6VugVvlZ6e-DWM2_b7Y4Eih38-j3oXRVFZSzz1MfSljY7KuTkx-p5ct8dpYgVMqFRW4toQFM.; SUB=_2A252Hv4TDeRhGeNJ7FYT8SnIyDiIHXVVamjbrDV8PUJbmtAKLUfWkW9NS7N__luXtJ0hCdsXY69Zd4DS8WDzddZJ; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5K-hUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt; SUHB=06ZVb8IQ0Wz4ON; ALF=1560003009; UOR=,,graph.qq.com; WBStorage=5548c0baa42e6f3d|undefined";
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
        }
        return Response.success(ouIds.size());
    }

}
