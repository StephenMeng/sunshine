package team.stephen.sunshine.service.front.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.stephen.sunshine.dao.front.ChannelDao;
import team.stephen.sunshine.model.front.Channel;
import team.stephen.sunshine.service.front.ChannelService;

/**
 * @author Stephen
 * @date 2018/05/21 23:23
 */
@Service
public class ChannelServiceImpl implements ChannelService {
    @Autowired
    private ChannelDao channelDao;

    @Override
    public Channel selectByChannelId(int id) {
        return channelDao.selectByPrimaryKey(id);
    }

    @Override
    public Page<Channel> select(Channel condition, int pageNum, int pageSize) {
        if (condition == null) {
            return null;
        }
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Channel>) channelDao.select(condition);
    }

    @Override
    public int addChannel(Channel channel) {
        return channelDao.insert(channel);
    }

    @Override
    public int updateSelective(Channel channel) {
        return channelDao.updateByPrimaryKeySelective(channel);
    }

    @Override
    public int delete(int channelId) {
        Channel channel = new Channel();
        channel.setChannelId(channelId);
        channel.setDeleted(true);
        return updateSelective(channel);
    }

    @Override
    public int restore(int channelId) {
        Channel channel = new Channel();
        channel.setChannelId(channelId);
        channel.setDeleted(false);
        return updateSelective(channel);
    }

    @Override
    public Channel selectOne(Channel condition) {
        return channelDao.selectOne(condition);
    }
}
