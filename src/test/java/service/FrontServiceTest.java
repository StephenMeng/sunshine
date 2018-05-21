package service;


import com.github.pagehelper.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.article.Article;
import team.stephen.sunshine.model.book.Book;
import team.stephen.sunshine.model.crawler.impl.CSDNParser;
import team.stephen.sunshine.model.crawler.impl.DouBanBookParser;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.service.article.ArticleService;
import team.stephen.sunshine.service.book.BookService;
import team.stephen.sunshine.service.common.CrawlerService;
import team.stephen.sunshine.service.common.DtoTransformService;
import team.stephen.sunshine.service.common.SolrService;
import team.stephen.sunshine.service.front.ChannelService;
import team.stephen.sunshine.service.front.ColumnService;
import team.stephen.sunshine.web.dto.book.BookDto;

import java.io.IOException;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class FrontServiceTest {
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ColumnService columnService;

    @Test
    public void testChannelAdd() {
        Channel channel = new Channel();
        channel.setChannelNameCn("科研项目");
        channel.setChannelNameEn("PROJECT");
        channel.setHasColumn(true);
        channel.setDeleted(false);
        channelService.addChannel(channel);
    }

    @Test
    public void testChannelSelect() {
        Channel channel = new Channel();
        channel.setChannelId(1);
        Page<Channel> channelPage = channelService.select(channel, 1, 0);
        assert channelPage.size() == 1;
    }

    @Test
    public void testChannelUpdate() {
        Channel channel = new Channel();
        channel.setChannelId(1);
        channel.setChannelNameCn("首页");
        int r = channelService.updateSelective(channel);
        assert r == 1;
    }

    @Test
    public void testChannelDelete() {
        int r = channelService.delete(1);
        r=channelService.restore(1);
        assert r == 1;
    }
}
