package team.stephen.sunshine.service.front;

import com.github.pagehelper.Page;
import team.stephen.sunshine.model.front.Channel;

/**
 * @author Stephen
 * @date 2018/05/21 23:22
 */
public interface ChannelService {
    Channel selectByChannelId(int id);

    Page<Channel> select(Channel condition, Integer pageNum, Integer pageSize);

    int addChannel(Channel channel);

    int updateSelective(Channel channel);

    int delete(int channelId);

    int restore(int channelId);

    Channel selectOne(Channel condition);

    Channel selectByChannelUri(String channel);
}
