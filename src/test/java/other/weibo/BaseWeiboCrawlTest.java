package other.weibo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import team.stephen.sunshine.exception.CrawlException;
import team.stephen.sunshine.model.other.WeiboUserConfig;
import team.stephen.sunshine.model.other.bean.weibo.*;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.DateUtils;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


/**
 * @author Stephen
 * @date 2019/04/06 12:19
 */
public class BaseWeiboCrawlTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    protected WeiboSearchParamBuilder searchParamBuilder;
    protected WeiboSearchParam searchParam;
    protected UserDetailParam userDetailParam;

    private WeiboCrawlResource resource = mock(WeiboCrawlResource.class);
    private String cookie = "SINAGLOBAL=7256170564393.524.1525878160642; UOR=,,login.sina.com.cn; login_sid_t=c263a2a189cc63fe607c4809b3c21dd7; cross_origin_proto=SSL; _s_tentry=passport.weibo.com; Apache=5262196885634.101.1554518640955; ULV=1554518640960:4:1:1:5262196885634.101.1554518640955:1552921740938; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhHG.930Gf5fOgXqM.y_wER5JpX5o275NHD95Q0e02ceK-fSh5fWs4Dqcj.i--Xi-ihiKyWi--4iKnEi-8Wi--Ri-2pi-zEi--fi-zEiK.7; SSOLoginState=1554518664; ALF=1586054671; SCF=ApWJpYkIBCSLvQa6VugVvlZ6e-DWM2_b7Y4Eih38-j3ovgQSMhdc4kJF4SkWiXvJO642AMfE-_ndM2-ha5bVnpE.; SUB=_2A25xrGLBDeRhGeVN6lQQ9SvIyzmIHXVS2NMJrDV8PUNbmtBeLWzbkW9NTEdLmXsyN_JP1bgIJs24FUg2w9XO0GYX; SUHB=0auA-lTtooKawK; wvr=6; webim_unReadCount=%7B%22time%22%3A1554518679177%2C%22dm_pub_total%22%3A3%2C%22chat_group_pc%22%3A0%2C%22allcountNum%22%3A36%2C%22msgbox%22%3A0%7D; WBStorage=201904061059|undefined";


    @Before
    public void init() throws CrawlException {
        doReturn(cookie).when(resource).getCookie();
        searchParamBuilder = new WeiboSearchParamBuilder(resource);
        searchParamBuilder.setKeyword("china").setStartDate("2017-07-11").setEndDate("2017-07-20").setType(AdvanceType.HOT);
        searchParamBuilder.setEndoce("utf-8");
        searchParamBuilder.setPage(1);
        searchParam = searchParamBuilder.build();


        LogRecord.print(searchParam.getUrl());
        userDetailParam = new UserDetailParam(resource);
        userDetailParam.setOid("3788285987");
    }

    @After
    public void after() {

    }
}
