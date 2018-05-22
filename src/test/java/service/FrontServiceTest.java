package service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.service.front.ChannelService;
import team.stephen.sunshine.service.front.ColumnService;

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
        PageHelper.startPage(1, 0);
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
        r = channelService.restore(1);
        assert r == 1;
    }
}
